//
//  MyDevice.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/30/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

class MyDevice : Device("99C46915A18B4908BD1555D427149FCC", "127.0.0.1") {

    fun description(): String {
        return "<MyDevice ID: " + this.id + ">"
    }

    companion object {
        private var instance: MyDevice? = null

        fun getInstance(): MyDevice {
            if (instance == null) {
                instance = MyDevice()
            }
            return instance!!
        }
    }

}
