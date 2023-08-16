//
//  Proxy.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/28/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import android.util.Log
import java.io.IOException

enum class ProxyError {
    deviceIsNil
}

class ProxyException(error: String) : IOException(error) {
    constructor(error: ProxyError) : this(error.toString())
}

interface ProxyDelegate {
    fun handshakeDone(proxy: Proxy)
    fun connectionClosed(proxy: Proxy, error: Exception?)
}

open class Proxy(var device: Device, timeout: Long) : TwisterConnectionDelegate {
    var connection: TwisterConnection
    var delegate: ProxyDelegate? = null
    var connectionCallback: ErrorCallback? = null

    var timeout: Long = 30
    internal var networkMessage: NetworkMessage? = null

    init {
        this.timeout = timeout
        connection = TwisterConnection(device.ipAddress, device.port ?: 0)
        connection.delegate = this
    }

    /*
    Device _device;
    public TwisterConnection connection;
    public weak var delegate: ProxyDelegate?
    public var connectionCallback: ErrorCallback?

    public var timeout: TimeInterval = 30.0
    var networkMessage: NetworkMessage?
    public var dispatchQueue: DispatchQueue!

    public required init() {
    }

    public convenience init(device: Device, timeout: TimeInterval) {
        self.init()
        self.device = device
        self.dispatchQueue = DispatchQueue(label: device.id)
        self.timeout = timeout
        connection = TwisterConnection(address: device.ipAddress, port: device.port)
        connection?.delegate = self
    }

    public convenience init(socket: CFSocketNativeHandle) {
        self.init()
        self.connection = TwisterConnection(socket: socket)
        self.connection?.delegate = self
        self.dispatchQueue = DispatchQueue(label: "\(socket)")
    }

    // MARK: - Accessors

    public var hashValue: Int {
        return self.ID.hashValue
    }*/

    // MARK: - Network calls

    open fun send(message: NetworkMessage) {
        //val subMessageDictionary = messageDictionary.substring(0, Math.min(messageDictionary.length, 200))
        //Log.d("Proxy",  MyDate.now() + " sending message: " + subMessageDictionary);
        message.encode(device)?.let {
            connection.sendMessage(it)
        }
    }

    override fun handshakeDone() {
        Log.d("Proxy", "handshakeDone")
        callConnectionCallback(null)
        delegate?.handshakeDone(this)
    }

    override fun receivedMessage(message: NetworkMessage) {
        //Log.d("Proxy", "receivedMessage message: " + message.toString().substring(0, Math.min(message.toString().count(), 300)));
    }

    override fun connectionClosed(error: Exception?) {
        Log.d("Proxy", "connectionClosed")
        //NSLog("\(connection?.connectionType ?? .unknown) connectionClosed")
        //callConnectionCallback(error: error)
        //self.delegate?.connectionClosed(proxy: self, error: error)
    }

    fun connect(callback: ErrorCallback) {
        if (this.connection.status == StreamStatus.open) {
            callback(null)
            return
        }
        this.connectionCallback = callback
        connection.connect { error ->
            if (error != null) {
                callConnectionCallback(error)
            }
        }
    }

    fun close(error: Exception) {
        connection.close(error)
    }

    fun callConnectionCallback(error: Exception?) {
        connectionCallback?.let { it(error) }
        this.connectionCallback = null
    }
}
