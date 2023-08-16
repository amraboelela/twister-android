//
//  SocketInputStream.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/29/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import android.os.Looper
import android.util.Log

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.Socket
import android.os.Handler

class SocketInputStream(s: Socket) : SocketStream(s) {

    fun read(size: Int) {
        try {
            this.status = StreamStatus.reading
            val inputStream = socket.getInputStream()
            var readCount = 0
            val buffer = ByteArray(size)
            do {
                readCount = inputStream.read(buffer, 0, size)
            } while (readCount == -1)
            val dataReceived = ByteArrayOutputStream(readCount)
            dataReceived.write(buffer, 0, readCount)
            Handler(Looper.getMainLooper()).post {
                delegate?.gotData(dataReceived)
                status = StreamStatus.open
            }
        } catch (e: Exception) {
            e.printStackTrace()
            delegate?.handleStreamEvent(this, StreamEvent.errorOccurred)
        }

    }

    fun readMessage() {
        this.status = StreamStatus.reading
        val socketInputStream = this
        executor.execute {
            try {
                //Log.d("SocketInputStream", "readMessage");
                val inputStream = socket.getInputStream()

                val readBytes = ByteArray(uint32Size)
                var readCount = 0
                readCount = inputStream.read(readBytes)
                var bytesToRead = 0
                if (readCount == 0) {
                    throw TCPConnectionException(TCPConnectionError.endEncountered)
                } else if (readCount == uint32Size) {
                    bytesToRead = getUInt32(readBytes)
                }
                //Log.d("SocketInputStream", "readMessage bytesToRead: " + bytesToRead);
                val buffer = ByteArray(bytesToRead)
                readCount = socketRead(inputStream, buffer, bytesToRead)
                if (check(readCount, bytesToRead) == true) {
                    val dataReceived = ByteArrayOutputStream(readCount)
                    dataReceived.write(buffer, 0, readCount)
                    Handler(Looper.getMainLooper()).post {
                        delegate?.gotMessage(dataReceived)
                        status = StreamStatus.open
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                delegate?.handleStreamEvent(socketInputStream, StreamEvent.errorOccurred)
            }
        }
    }

    internal fun check(count: Int, expectedCount: Int): Boolean {
        if (count > 0 && count == expectedCount) {
            return true
        } else {
            Log.d("SocketInputStream", "check count: $count, expectedCount: $expectedCount")
            this.close()
            delegate?.handleStreamEvent(this,
                    if (count == 0) StreamEvent.endEncountered else StreamEvent.errorOccurred)
        }
        return false
    }

    @Throws(Exception::class)
    internal fun socketRead(inputStream: InputStream, buffer: ByteArray, length: Int): Int {
        var offset = 0
        var readCount = 0
        do {
            offset += readCount
            do {
                readCount = inputStream.read(buffer, offset, length - offset)
            } while (readCount == -1)
            if (readCount == 0) {
                return 0
            }
        } while (offset + readCount < length)
        return offset + readCount
    }

    fun getUInt32(bytes: ByteArray): Int {
        return bytes[0].toInt() and 0xFF or (bytes[1].toInt() and 0xFF shl 8) or
                (bytes[2].toInt() and 0xFF shl 16) or (bytes[3].toInt() and 0xFF shl 24)
    }

    companion object {
        var uint32Size = 4
    }

}
