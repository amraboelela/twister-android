//
//  MyString.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

object MyString {

    fun formattedPhone(phoneNumber: String): String {
        var formattedPhone = phoneNumber
        if (phoneNumber.length == 11) {
            val firstChar = phoneNumber[0]
            if (firstChar == '1') {
                formattedPhone = "(" + phoneNumber.substring(1, 4) + ") " + phoneNumber.substring(4, 7) + "-" + phoneNumber.substring(7, 11)
            }
        }
        return formattedPhone
    }

}