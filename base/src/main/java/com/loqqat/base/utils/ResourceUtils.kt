package com.loqqat.base.utils

import android.content.Context
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser.END_TAG
import org.xmlpull.v1.XmlPullParser.START_TAG


class ResourceUtils {

    companion object {
        fun getHashMapResource(c: Context, hashMapResId: Int): Map<String, String>? {
            var map: MutableMap<String, String>? = null
            val parser = c.resources.getXml(hashMapResId)

            var key: String? = null
            var value: String? = null

            try {
                var eventType = parser.eventType

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        Log.d("utils", "Start document")
                    } else if (eventType == START_TAG) {
                        if (parser.name == "map") {
                            val isLinked = parser.getAttributeBooleanValue(null, "linked", false)

                            map = if (isLinked) LinkedHashMap() else HashMap()
                        } else if (parser.name == "entry") {
                            key = parser.getAttributeValue(null, "key")

                            if (null == key) {
                                parser.close()
                                return null
                            }
                        }
                    } else if (eventType == END_TAG) {
                        if (parser.name == "entry" && map != null && key != null && value != null) {
                            map[key] = value
                            key = null
                            value = null
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (null != key) {
                            value = parser.text
                        }
                    }
                    eventType = parser.next()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
            return map
        }
    }
}