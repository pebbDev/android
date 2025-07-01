package com.example.infinite_track.data.soucre.local.room

import androidx.room.TypeConverter
import java.nio.ByteBuffer
import java.nio.FloatBuffer

class Converters {
    @TypeConverter
    fun fromByteArray(byteArray: ByteArray?): FloatArray? {
        if (byteArray == null) return null

        val buffer = ByteBuffer.wrap(byteArray)
        val floatArray = FloatArray(byteArray.size / 4)
        val floatBuffer = buffer.asFloatBuffer()
        floatBuffer.get(floatArray)
        return floatArray
    }

    @TypeConverter
    fun toByteArray(floatArray: FloatArray?): ByteArray? {
        if (floatArray == null) return null

        val byteBuffer = ByteBuffer.allocate(floatArray.size * 4)
        val floatBuffer = byteBuffer.asFloatBuffer()
        floatBuffer.put(floatArray)
        return byteBuffer.array()
    }
}
