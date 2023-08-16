//
//  SocketStream.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/31/18.
//  Copyright © 2018 Amr Aboelela. All rights reserved.
//

package net.twister.foundation

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.Socket
import java.util.concurrent.Executor
import java.util.concurrent.Executors

enum class SocketStreamError {
    failedToConnect
}

class SocketStreamException(error: String) : IOException(error) {
    constructor(error: SocketStreamError) : this(error.toString())
}

enum class StreamEvent {
    openCompleted, hasBytesAvailable, hasSpaceAvailable, errorOccurred, endEncountered
}

enum class StreamStatus {
    notOpen, opening, open, reading, writing, atEnd, closed, error
}

interface SocketStreamDelegate {
    fun handleStreamEvent(stream: SocketStream, eventCode: StreamEvent)
    fun writeCompleted()
    fun writeMessageCompleted()
    fun gotData(data: ByteArrayOutputStream)
    fun gotMessage(data: ByteArrayOutputStream)
}

open class SocketStream(internal var socket: Socket) : Any() {
    var delegate: SocketStreamDelegate? = null
    internal var executor: Executor
    internal var status: StreamStatus

    init {
        executor = Executors.newSingleThreadExecutor()
        status = StreamStatus.opening
    }

    open fun open() {
        this.status = StreamStatus.open
        delegate?.handleStreamEvent(this, StreamEvent.openCompleted)
    }

    fun close() {
        try {
            status = StreamStatus.closed
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        internal var uint32Size = 4
    }

}

