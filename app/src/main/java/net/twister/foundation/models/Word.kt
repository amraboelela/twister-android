//
//  UserPost.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 12/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

data class Word(
        @Transient var key: String? = "",
        val postKey: String
) {
    companion object {
        fun wordsFrom(text: String): ArrayList<String> {
            var result = setOf<String>()
            val words = text.toLowerCase().split("\\s+".toRegex()).map { word ->
                word.replace("""^[,\.]|[,\.]$""".toRegex(), "").replace("-", "")
            }
            for (word in words) {
                if (word.length > 2) {
                    result = result.plus(word)
                }
            }
            return ArrayList(result)
        }
    }
}
