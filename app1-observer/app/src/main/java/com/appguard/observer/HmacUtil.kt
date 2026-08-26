package com.appguard.observer

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HmacUtil {

    fun sign(data: String, secret: String): String {
        val mac = Mac.getInstance("HmacSHA256")
        val keySpec = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
        mac.init(keySpec)
        val hashBytes = mac.doFinal(data.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}