//
//  Server.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

import net.twister.foundation.*

class Server() : Device(TwisterEnvironment.serverID) {
    init {
        if (BuildConfig.DEBUG) {
            ipAddress = TwisterEnvironment.serverAddress_dev
            port = TwisterEnvironment.serverPort_dev
        } else {
            ipAddress = TwisterEnvironment.serverAddress
            port = TwisterEnvironment.serverPort
        }
    }

    companion object {
        private var instance: Server? = null

        fun getInstance(): Server {
            if (instance == null) {
                instance = Server()
            }
            return instance!!
        }
    }

}
