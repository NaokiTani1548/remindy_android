package com.example.remindy.util

import java.security.SecureRandom
import java.time.Instant

fun nowUtc(): String = Instant.now().toString()

/** RFC 4122 UUIDv7: Unix timestamp (ms) + random bits。クライアント採番用。 */
object UuidV7 {
    private val rng = SecureRandom()

    fun generate(): String {
        val now = System.currentTimeMillis()
        val bytes = ByteArray(16)
        rng.nextBytes(bytes)

        // Bits 0-47: unix_ts_ms
        bytes[0] = (now shr 40).toByte()
        bytes[1] = (now shr 32).toByte()
        bytes[2] = (now shr 24).toByte()
        bytes[3] = (now shr 16).toByte()
        bytes[4] = (now shr 8).toByte()
        bytes[5] = now.toByte()

        // Bits 48-51: version = 7
        bytes[6] = ((bytes[6].toInt() and 0x0F) or 0x70).toByte()

        // Bits 64-65: variant = 10
        bytes[8] = ((bytes[8].toInt() and 0x3F) or 0x80).toByte()

        return "%02x%02x%02x%02x-%02x%02x-%02x%02x-%02x%02x-%02x%02x%02x%02x%02x%02x".format(
            bytes[0], bytes[1], bytes[2], bytes[3],
            bytes[4], bytes[5],
            bytes[6], bytes[7],
            bytes[8], bytes[9],
            bytes[10], bytes[11], bytes[12], bytes[13], bytes[14], bytes[15],
        )
    }
}
