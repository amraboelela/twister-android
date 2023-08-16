//
//  TCPConnection.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/28/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import android.util.Log

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.Socket
import java.util.concurrent.Executor
import java.util.concurrent.Executors

typealias ErrorCallback = (Exception?) -> Unit

enum class TCPConnectionError {
    handshakeError, inputStreamEnded, inputStreamError, inputStreamIsNil, outputStreamIsNil,
    outputStreamEnded, outputStreamError, connectionTimeout, tryAgainReading, writeError,
    readError, endEncountered, connectionIsNotOpen
}

class TCPConnectionException(error: String) : IOException(error) {
    constructor(error: TCPConnectionError) : this(error.toString())
}

open class TCPConnection(hostAddress: String, port: Int) : SocketStreamDelegate {
    internal var readHandshakeDone = false
    internal var writeHandshakeDone = false

    var hostAddress = ""
    var status: StreamStatus
    var handshakeIsDone = false
    internal var executor: Executor

    internal var port = 0
    internal var inputStream: SocketInputStream? = null
    internal var outputStream: SocketOutputStream? = null
    internal var socket = Socket()

    init {
        status = StreamStatus.notOpen
        this.hostAddress = hostAddress
        this.port = port
        executor = Executors.newSingleThreadExecutor()
    }

    open fun writeHandshake() {}

    open fun readHandshake() {}

    fun write(data: ByteArrayOutputStream) {
        executor.execute {
            if (outputStream == null) {
                close(TCPConnectionException(TCPConnectionError.outputStreamIsNil))
                return@execute
            }
            outputStream?.write(data.toByteArray())
        }
    }

    fun readData(size: Int) {
        executor.execute {
            if (inputStream == null) {
                close(TCPConnectionException(TCPConnectionError.inputStreamIsNil))
                return@execute
            }
            inputStream?.read(size)
        }
    }

    internal open fun handshakeDone() {}

    fun description(): String {
        return "<" + this.javaClass.getName() + " hostAddress: " + hostAddress + "; port: " + port + "; inputStream: " + inputStream + "; outputStream: " + outputStream + "; writeHandshakeDone: " + writeHandshakeDone + ">"
    }

    internal fun sendMessage(data: ByteArrayOutputStream) {
        executor.execute {
            if (outputStream == null) {
                close(TCPConnectionException(TCPConnectionError.outputStreamIsNil))
                return@execute
            }
            //EncryptedByteArrayOutputStream.printHexEncodedString(data.toByteArray(), "sendMessage data.toByteArray()");
            //Log.d("TCPConnection", "sendMessage data.toByteArray(): " + EncryptedByteArrayOutputStream.hexEncodedString(data.toByteArray()) + "");
            outputStream?.sendMessage(data.toByteArray())
            readMessage(false)
        }
    }

    internal fun readMessage(checkIfOpen: Boolean) {
        executor.execute{
            if (inputStream == null) {
                close(TCPConnectionException(TCPConnectionError.inputStreamIsNil))
                return@execute
            }
            if (checkIfOpen) {
                if (inputStream?.status == StreamStatus.open) {
                    inputStream?.readMessage()
                }
            } else {
                inputStream?.readMessage()
            }
        }
    }

    fun close(error: Exception) {
        status = StreamStatus.closed
        executor.execute {
            try {
                inputStream?.close()
                outputStream?.close()
                inputStream = null
                outputStream = null
                readHandshakeDone = false
                writeHandshakeDone = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun handleStreamEvent(stream: SocketStream, eventCode: StreamEvent) {
        //Log.d("TCPConnection", "handleStreamEvent stream: " + stream + " eventCode: " + eventCode);
        executor.execute {
            if (stream === inputStream) {
                when (eventCode) {
                    StreamEvent.openCompleted -> {
                    }
                    StreamEvent.hasBytesAvailable -> Log.d("TCPConnection", "streamHandleEvent Input stream hasBytesAvailable")
                    StreamEvent.endEncountered -> {
                        Log.d("TCPConnection", "streamHandleEvent Input stream endEncountered")
                        close(TCPConnectionException(TCPConnectionError.inputStreamEnded))
                    }
                    StreamEvent.errorOccurred -> {
                        Log.d("streamHandleEvent", "Input stream errorOccurred")
                        close(TCPConnectionException(TCPConnectionError.inputStreamError))
                    }
                    else -> Log.d("streamHandleEvent", "Input stream eventCode: " + eventCode)
                }//NSLog("\(outbound ? "client" : "server") Input stream openCompleted")
            } else if (stream === outputStream) {
                when (eventCode) {
                    StreamEvent.openCompleted -> {
                    }
                    StreamEvent.hasSpaceAvailable -> if (!writeHandshakeDone) {
                        writeHandshake()
                    }
                    StreamEvent.endEncountered -> {
                        Log.d("TCPConnection", "streamHandleEvent Output stream endEncountered")
                        close(TCPConnectionException(TCPConnectionError.outputStreamEnded))
                    }
                    StreamEvent.errorOccurred -> {
                        Log.d("TCPConnection", "streamHandleEvent Output stream errorOccurred")
                        close(TCPConnectionException(TCPConnectionError.outputStreamError))
                    }
                    else -> Log.d("TCPConnection", "streamHandleEvent Output stream eventCode: " + eventCode)
                }
            }
        }
    }

    override fun writeCompleted() {}

    override fun writeMessageCompleted() {}

    override fun gotData(data: ByteArrayOutputStream) {}

    override fun gotMessage(data: ByteArrayOutputStream) {}

    // MARK: - Public functions

    fun connect(callback: ErrorCallback) {
        status = StreamStatus.open
        val tcpConnection = this
        executor.execute {
            try {
                Log.d("TCPConnection", "connect")
                //create a socket to make the connection with the server
                val socket = Socket(hostAddress, port)
                if (!socket.isConnected) {
                    socket.connect(socket.remoteSocketAddress)
                }
                outputStream = SocketOutputStream(socket)
                inputStream = SocketInputStream(socket)
                outputStream?.delegate = tcpConnection
                inputStream?.delegate = tcpConnection
                outputStream?.open()
                inputStream?.open()
            } catch (e: Exception) {
                e.printStackTrace()
                callback(e)
            }
        }
        //new ConnectAsyncTask(this, callback).executeOnExecutor(executor);
    }
}
