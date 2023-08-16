//
//  SocketOutputStream.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/29/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import android.os.Handler
import android.os.Looper

import java.io.IOException
import java.net.Socket

class SocketOutputStream(s: Socket) : SocketStream(s) {

    override fun open() {
        super.open()
        delegate?.handleStreamEvent(this, StreamEvent.hasSpaceAvailable)
    }

    fun write(buffer: ByteArray) {
        try {
            this.status = StreamStatus.writing
            val os = socket.getOutputStream()
            os.write(buffer)
            Handler(Looper.getMainLooper()).post {
                status = StreamStatus.open
                delegate?.writeCompleted()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            delegate?.handleStreamEvent(this, StreamEvent.errorOccurred)
        }

    }

    @Throws(IOException::class)
    internal fun writeInt(n: Int) {
        val os = socket.getOutputStream()
        os.write(n shr 0 and 0xff)
        os.write(n shr 8 and 0xff)
        os.write(n shr 16 and 0xff)
        os.write(n shr 24 and 0xff)
    }

    fun sendMessage(buffer: ByteArray) {
        status = StreamStatus.writing
        val socketOutputStream = this
        executor.execute {
            try {
                val os = socket.getOutputStream()
                writeInt(buffer.size)
                os.write(buffer)
                Handler(Looper.getMainLooper()).post {
                    status = StreamStatus.open
                    delegate?.writeMessageCompleted()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                delegate?.handleStreamEvent(socketOutputStream, StreamEvent.errorOccurred)
            }
        }
    }

}
