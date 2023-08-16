//
//  TwisterConnection.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/31/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import android.util.Log

//import org.json.JSONObject

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.Arrays

interface TwisterConnectionDelegate {
    fun handshakeDone()
    fun receivedMessage(message: NetworkMessage)
    fun connectionClosed(error: Exception?)
}

class TwisterConnection(hostAddress: String, port: Int) : TCPConnection(hostAddress, port) {

    internal var delegate: TwisterConnectionDelegate? = null
    internal var gotHandshakeVersion = false
    internal var gotHandshakeToken = false

    override fun writeHandshake() {
        Log.d("TwisterConnection", "writeHandshake")
        try {
            var oStream = ByteArrayOutputStream(1)
            oStream.write(1)
            this.write(oStream)

            val handshakeToken = "ctwister"
            oStream = ByteArrayOutputStream(handshakeToken.length)
            oStream.write(handshakeToken.toByteArray())
            this.write(oStream)
            oStream = ByteArrayOutputStream(4)
            oStream.write(0)
            oStream.write(0)
            oStream.write(0)
            oStream.write(0)
            this.write(oStream)
            this.writeHandshakeDone = true
            this.readHandshake()
        } catch (e: IOException) {
            e.printStackTrace()
            this.close(TCPConnectionException(TCPConnectionError.writeError))
        }

    }

    override fun readHandshake() {
        Log.d("TwisterConnection", "readHandshake")
        this.readData(1)
        this.readData(8)
        this.readData(4)
    }

    override fun gotData(data: ByteArrayOutputStream) {
        //NSLog("\(connectionType) - got data: \(data.simpleDescription)")
        val oStream: ByteArrayOutputStream
        try {
            if (!gotHandshakeVersion) {
                oStream = ByteArrayOutputStream(1)
                oStream.write(1)
                if (Arrays.equals(oStream.toByteArray(), data.toByteArray())) {
                    gotHandshakeVersion = true
                } else {
                    this.close(TCPConnectionException(TCPConnectionError.handshakeError))
                }
            } else if (!gotHandshakeToken) {
                val handshakeToken = "stwister"
                oStream = ByteArrayOutputStream(handshakeToken.length)
                oStream.write(handshakeToken.toByteArray())
                if (Arrays.equals(oStream.toByteArray(), data.toByteArray())) {
                    gotHandshakeToken = true
                } else {
                    this.close(TCPConnectionException(TCPConnectionError.handshakeError))
                }
            } else {
                oStream = ByteArrayOutputStream(4)
                oStream.write(0)
                oStream.write(0)
                oStream.write(0)
                oStream.write(0)
                if (Arrays.equals(oStream.toByteArray(), data.toByteArray())) {
                    readHandshakeDone = true
                    if (writeHandshakeDone) {
                        handshakeDone()
                    }
                } else {
                    this.close(TCPConnectionException(TCPConnectionError.handshakeError))
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            this.close(TCPConnectionException(TCPConnectionError.writeError))
        }

    }

    override fun handshakeDone() {
        super.handshakeDone()
        Log.d("TwisterConnection", "handshakeDone")
        //NSLog("\(connectionType) - handshakeDone")
        delegate?.handshakeDone()
    }

    override fun gotMessage(message: ByteArrayOutputStream) {
        NetworkMessage.decode(message)?.let {
            delegate?.receivedMessage(it)
        }
    }

    /*
    override func readString(withLength length: Int) throws -> String {
        guard let inputStream = inputStream else {
            self.close(TCPConnectionError.inputStreamIsNil)
            throw TCPConnectionError.inputStreamIsNil
        }
        let stringPointer = UnsafeMutablePointer<UInt8>.allocate(capacity: length)
        defer {
            stringPointer.deinitialize(count: length)
            stringPointer.deallocate()
        }
        var readCount = 0
        repeat {
            readCount = inputStream.read(stringPointer, maxLength: length)
        } while readCount == -1 && errno == EAGAIN
        if readCount == 0 {
            throw TCPConnectionError.endEncountered
        } else if readCount == length {
            let data = Data(bytes: stringPointer, count: readCount)
            return data.simpleDescription
        } else {
            return ""
        }
    }

    override func writeHandshake() throws {
        //NSLog("writeHandshake")
        var version: UInt8 = 1
        var r = outputStream?.write(&version, maxLength: 1)
        //NSLog("\(connectionType) writeHandshake version: \(version)")
        if r != 1 {
            NSLog("\(connectionType) writeHandshake error writing version")
            return
        }

        var twisterString: String = ""
        if self.outbound {
            twisterString = "ctwister"
        } else {
            twisterString = "stwister"
        }
        //NSLog("\(connectionType) writeHandshake twisterString: \(twisterString)")
        write(string: twisterString)

        var size: UInt32 = 0
        let sizeRawPointer = UnsafeMutableRawPointer(&size)
        let sizePointer = sizeRawPointer.bindMemory(to: UInt8.self, capacity: uint32Size)
        //NSLog("\(connectionType) writeHandshake size: \(size)")
        r = outputStream?.write(sizePointer, maxLength: uint32Size)
        if r != uint32Size {
            NSLog("\(connectionType) writeHandshake error writting size")
            return
        }
        self.writeHandshakeDone = true
        if readHandshakeDone {
            handshakeDone()
        }
    }*/

}
