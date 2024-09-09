package com.example.part2.newsapp.model

import org.json.JSONObject
import org.json.XML

object XmlToJsonConverter {
    fun convertXmlToJson(xml: String): JSONObject? {
        return try {
            XML.toJSONObject(xml)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}