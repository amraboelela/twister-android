//
//  Device.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 2/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

open class Device constructor(val id: String, var ipAddress: String, var port: Int? = 0) {

    constructor() : this("", "0.0.0.0", 0)
    constructor(id: String) : this(id, "0.0.0.0", 0)

    fun equals(device: Device): Boolean {
        return this.id == device.id
    }
}
