//
//  EncryptedByteArrayOutputStream.kt
//  TwisterFoundation
//
//  Created by Amr Aboelela on 12/3/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.foundation

import android.util.Log

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Random

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

enum class EncryptedByteArrayOutputStreamError {
    terminationStatus, unicodeDecodingError, invalidEnvironmentVariable
}

class EncryptedByteArrayOutputStreamException(error: String) : IOException(error) {
    constructor(error: EncryptedByteArrayOutputStreamError) : this(error.toString())
}

class EncryptedByteArrayOutputStream : ByteArrayOutputStream() {

    @Throws(Exception::class)
    fun encryptedWithSaltUsing(key: String): EncryptedByteArrayOutputStream {
        val ivByte = Random().nextInt().toByte()
        //Log.d("OutputStream", "key: " + key);
        //this.printHexEncodedString(this.toByteArray(), "encryptedWithSaltUsing original data");
        val encryptedData = this.data(true, key, ivByte)
        val result = EncryptedByteArrayOutputStream()
        result.write(ivByte.toInt())
        result.write(encryptedData.toByteArray())
        return result
    }

    @Throws(Exception::class)
    fun decryptedWithSaltUsing(key: String): EncryptedByteArrayOutputStream {
        val bytes = this.toByteArray()
        //EncryptedByteArrayOutputStream.printHexEncodedString(bytes, "decryptedWithSaltUsing bytes");
        val ivByte = bytes[0]
        val encryptedData = EncryptedByteArrayOutputStream()
        encryptedData.write(bytes, 1, bytes.size - 1)
        return encryptedData.data(false, key, ivByte)
    }

    @Throws(Exception::class)
    internal fun data(encrypt: Boolean, key: String, ivByte: Byte): EncryptedByteArrayOutputStream {
        // convert key to bytes
        val keyBytes = key.toByteArray()
        // Use the first 32 bytes (or even less if key is shorter)
        val keyBytes32 = ByteArray(32)
        System.arraycopy(keyBytes, 0, keyBytes32, 0, Math.min(keyBytes.size, 32))

        // convert plain text to bytes
        val plainBytes = this.toByteArray()

        // setup cipher
        val skeySpec = SecretKeySpec(keyBytes32, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val iv = ByteArray(16) // initialization vector with all 0
        iv[0] = ivByte
        //EncryptedByteArrayOutputStream.printHexEncodedString(keyBytes32, "keyBytes32");
        //EncryptedByteArrayOutputStream.printHexEncodedString(iv, "iv");
        var mode = Cipher.ENCRYPT_MODE
        if (!encrypt) {
            mode = Cipher.DECRYPT_MODE
        }
        cipher.init(mode, skeySpec, IvParameterSpec(iv))
        // encrypt
        val result = EncryptedByteArrayOutputStream()
        result.write(cipher.doFinal(plainBytes))
        return result
    }

    companion object {

        fun printHexEncodedString(bytes: ByteArray, tag: String) {
            val hexString = hexEncodedString(bytes)
            Log.d("OutputStream", tag + ": " + hexString.substring(0, Math.min(hexString.length, 300)) + " count: " + bytes.size)
        }

        fun hexEncodedString(bytes: ByteArray): String {

            val sb = StringBuilder()
            for (b in bytes) {
                sb.append(String.format("%02X", b))
            }
            return sb.toString()
        }
    }
}
