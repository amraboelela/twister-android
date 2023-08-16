//
//  ServerProxy.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

import android.util.Log

import net.twister.foundation.*
import java.util.Hashtable

class ServerProxy(device: Device, timeout: Long) : Proxy(device, timeout) {

    internal var callbacks = Hashtable<String, NetworkMessageCallback>()

    fun sync(postTime: Long?, direction: NetworkMessageDirection, callback: NetworkMessageCallback) {
        connect { error ->
            //Log.i("ServerProxy", "sync");
            val networkMessage = NetworkMessage(NetworkMessageType.sync.toString())
            if (error != null) {
                Log.d("ServerProxy",
                        "sync client failed to sync. postTime: $postTime, direction: $direction " +
                                "error: $error")
                close(error)
                callback(networkMessage, error)
            } else {
                networkMessage.time = postTime
                networkMessage.direction = direction
                callbacks.put(networkMessage.callbackKey(), callback)
                send(networkMessage)
            }
        }
    }

    override fun send(message: NetworkMessage) {
        //val deviceID = MyDevice.getInstance().id
        message.deviceID =  MyDevice.getInstance().id
        super.send(message)
    }

    /*
    public func translate(text: String, toLanguage: String, callback: @escaping NetworkMessageCallback) {
        dispatchQueue.async {
            print("Client sending translate text request. text: \(text)")
            self.connect() { error in
                var returnError: Error?
                let networkMessage = NetworkMessage(messageType: .translateText)
                defer {
                    if returnError != nil {
                        if let callbackKey = networkMessage.callbackKey {
                            self.callbacks[callbackKey] = nil
                        }
                        callback(networkMessage, returnError)
                    }
                }
                if let error = error {
                    returnError = error
                    return
                }
                networkMessage.text = text
                networkMessage.language = toLanguage
                self.callbacks[networkMessage.callbackKey!] = callback
                do {
                    try self.send(message: networkMessage)
                } catch {
                    returnError = error
                    return
                }
            }
        }
    }

    public func translate(post: [String:Any], toLanguage: String, callback: @escaping NetworkMessageCallback) {
        dispatchQueue.async {
            print("Client sending translate text request. post: \(post)")
            self.connect() { error in
                var returnError: Error?
                let networkMessage = NetworkMessage(messageType: .translatePost)
                defer {
                    if returnError != nil {
                        if let callbackKey = networkMessage.callbackKey {
                            self.callbacks[callbackKey] = nil
                        }
                        callback(networkMessage, returnError)
                    }
                }
                if let error = error {
                    returnError = error
                    return
                }
                networkMessage.username = postAdapter.username(ofPost: post)
                networkMessage.postTime = postAdapter.time(ofPost: post)
                networkMessage.language = toLanguage
                self.callbacks[networkMessage.callbackKey!] = callback
                do {
                    try self.send(message: networkMessage)
                } catch {
                    returnError = error
                    return
                }
            }
        }
    }

    public func newAccount(username: String, password: String, fullname: String, bio: String, location: String, url: String, callback: @escaping NetworkMessageCallback) {
        dispatchQueue.async {
            print("Client sending newAccount. username: \(username), password: \(password)")
            self.connect() { error in
                var returnError: Error?
                let networkMessage = NetworkMessage(messageType: .newAccount)
                defer {
                    if returnError != nil {
                        if let callbackKey = networkMessage.callbackKey {
                            self.callbacks[callbackKey] = nil
                        }
                        callback(networkMessage, returnError)
                    }
                }
                if let error = error {
                    returnError = error
                    return
                }
                networkMessage.username = username
                networkMessage.password = password
                networkMessage.fullname = fullname
                networkMessage.bio = bio
                networkMessage.location = location
                networkMessage.url = url
                self.callbacks[networkMessage.callbackKey!] = callback
                do {
                    try self.send(message: networkMessage)
                } catch {
                    returnError = error
                    return
                }
            }
        }
    }

    public func newPost(message: String, username: String, replyUsername: String? = nil, replyID: String? = nil, callback: @escaping NetworkMessageCallback) {
        dispatchQueue.async {
            print("Client sending newPost. message: \(message)")
            self.connect() { error in
                var returnError: Error?
                let networkMessage = NetworkMessage(messageType: .newPost)
                defer {
                    if returnError != nil {
                        if let callbackKey = networkMessage.callbackKey {
                            self.callbacks[callbackKey] = nil
                        }
                        callback(networkMessage, returnError)
                    }
                }
                if let error = error {
                    returnError = error
                    return
                }
                networkMessage.message = message
                networkMessage.username = username
                networkMessage.replyUsername = replyUsername
                networkMessage.replyID = replyID
                self.callbacks[networkMessage.callbackKey!] = callback
                do {
                    try self.send(message: networkMessage)
                } catch {
                    returnError = error
                    return
                }
            }
        }
    }

    public func updateAccount(username: String, fullname: String, bio: String, location: String, url: String, callback: @escaping NetworkMessageCallback) {
        dispatchQueue.async {
            print("Client sending updateAccount. username: \(username)")
            self.connect() { error in
                var returnError: Error?
                let networkMessage = NetworkMessage(messageType: .updateAccount)
                defer {
                    if returnError != nil {
                        if let callbackKey = networkMessage.callbackKey {
                            self.callbacks[callbackKey] = nil
                        }
                        callback(networkMessage, returnError)
                    }
                }
                if let error = error {
                    returnError = error
                    return
                }
                networkMessage.username = username
                networkMessage.fullname = fullname
                networkMessage.bio = bio
                networkMessage.location = location
                networkMessage.url = url
                self.callbacks[networkMessage.callbackKey!] = callback
                do {
                    try self.send(message: networkMessage)
                } catch {
                    returnError = error
                    return
                }
            }
        }
    }

    public func login(username: String, password: String, privateKey: String, callback: @escaping NetworkMessageCallback) {
        dispatchQueue.async {
            print("Client sending login. username: \(username), password: \(password)")
            self.connect() { error in
                var returnError: Error?
                let networkMessage = NetworkMessage(messageType: .login)
                defer {
                    if returnError != nil {
                        if let callbackKey = networkMessage.callbackKey {
                            self.callbacks[callbackKey] = nil
                        }
                        callback(networkMessage, returnError)
                    }
                }
                if let error = error {
                    returnError = error
                    return
                }
                networkMessage.username = username
                networkMessage.password = password
                networkMessage.privateKey = privateKey
                self.callbacks[networkMessage.callbackKey!] = callback
                do {
                    try self.send(message: networkMessage)
                } catch {
                    returnError = error
                    return
                }
            }
        }
    }

    public func follow(username: String, myUsername: String, callback: @escaping NetworkMessageCallback) {
        dispatchQueue.async {
            print("Client sending follow. username: \(username), myUsername: \(myUsername)")
            self.connect() { error in
                var returnError: Error?
                let networkMessage = NetworkMessage(messageType: .follow)
                defer {
                    if returnError != nil {
                        if let callbackKey = networkMessage.callbackKey {
                            self.callbacks[callbackKey] = nil
                        }
                        callback(networkMessage, returnError)
                    }
                }
                if let error = error {
                    returnError = error
                    return
                }
                networkMessage.username = username
                networkMessage.myUsername = myUsername
                self.callbacks[networkMessage.callbackKey!] = callback
                do {
                    try self.send(message: networkMessage)
                } catch {
                    returnError = error
                    return
                }
            }
        }
    }

    public func unfollow(username: String, myUsername: String, callback: @escaping NetworkMessageCallback) {
        dispatchQueue.async {
            print("Client sending unfollow. username: \(username), myUsername: \(myUsername)")
            self.connect() { error in
                var returnError: Error?
                let networkMessage = NetworkMessage(messageType: .unfollow)
                defer {
                    if returnError != nil {
                        if let callbackKey = networkMessage.callbackKey {
                            self.callbacks[callbackKey] = nil
                        }
                        callback(networkMessage, returnError)
                    }
                }
                if let error = error {
                    returnError = error
                    return
                }
                networkMessage.username = username
                networkMessage.myUsername = myUsername
                self.callbacks[networkMessage.callbackKey!] = callback
                do {
                    try self.send(message: networkMessage)
                } catch {
                    returnError = error
                    return
                }
            }
        }
    }*/

    fun getUser(username: String, callback: NetworkMessageCallback) {
        val networkMessage = NetworkMessage(NetworkMessageType.getUser.toString())
        networkMessage.user = User(username)
        val callbackKey = networkMessage.callbackKey()
        if (callbacks.get(callbackKey) != null) {
            callback(networkMessage, TwisterException(TwisterError.alreadySent))
            return
        }
        connect { error ->
            if (error != null) {
                Log.d("ServerProxy", "getUser client failed to getUser. username: $username error: $error")
                close(error)
                callback(networkMessage, error)
            } else {
                callbacks.put(callbackKey, callback)
                send(networkMessage)
            }
        }
    }

    /*
    public func getPost(forUsername username: String, postID: String, callback: @escaping NetworkMessageCallback) {
        dispatchQueue.async {
            print("Client sending getPost. postID: \(postID), username: \(username)")
            self.connect() { error in
                var returnError: Error?
                let networkMessage = NetworkMessage(messageType: .getPost)
                defer {
                    if returnError != nil {
                        if let callbackKey = networkMessage.callbackKey {
                            self.callbacks[callbackKey] = nil
                        }
                        callback(networkMessage, returnError)
                    }
                }
                if let error = error {
                    returnError = error
                    return
                }
                networkMessage.username = username
                networkMessage.postID = postID
                self.callbacks[networkMessage.callbackKey!] = callback
                do {
                    try self.send(message: networkMessage)
                } catch {
                    returnError = error
                    return
                }
            }
        }
    }
    */

    override fun receivedMessage(networkMessage: NetworkMessage) {
        super.receivedMessage(networkMessage)
        callCallback(networkMessage, null)
    }

    internal fun callCallback(networkMessage: NetworkMessage, error: Exception?) {
        val callbackKey = networkMessage.callbackKey()
        /*callbacks.get(callbackKey)?.let { callback ->
            this.callbacks.remove(callback)
            error?.let {
                callback(networkMessage, it)
            } ?: networkMessage.error?.let {
                callback(networkMessage, TwisterException(it))
            } ?: run {
                callback(networkMessage, null)
            }
        }*/
    }

    companion object {
        private var instance: ServerProxy? = null

        fun getInstance(): ServerProxy {
            if (instance == null) {
                instance = ServerProxy(Server.getInstance(), 3)
            }
            return instance!!
        }
    }
}
