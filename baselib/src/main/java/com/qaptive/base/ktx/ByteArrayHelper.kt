package com.qaptive.base.ktx

object ByteArrayHelper {
    fun ByteArray.byteArrayToHexStringReversed():String
    {
        var i: Int
        var j = size-1
        var `in`: Int
        val hex = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
        var out = ""

        while (j >= 0) {
            `in` = get(j).toInt() and 0xff
            i = `in` shr 4 and 0x0f
            out += hex[i]
            i = `in` and 0x0f
            out += hex[i]
            --j
        }
        return out
    }

    fun ByteArray.byteArrayToHexString():String
    {
        var i: Int
        var j = 0
        var `in`: Int
        val hex = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
        var out = ""

        while (j < size) {
            `in` = get(j).toInt() and 0xff
            i = `in` shr 4 and 0x0f
            out += hex[i]
            i = `in` and 0x0f
            out += hex[i]
            ++j
        }
        return out
    }
}