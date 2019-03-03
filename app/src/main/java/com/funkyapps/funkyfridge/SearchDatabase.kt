package com.funkyapps.funkyfridge

import org.json.JSONException
import org.json.JSONObject

class Search {
    protected lateinit var json: JSONObject

    init {
        try {
            json =
                JSONObject("https://api.nutritionix.com/v1_1/item?upc=[UPC]&appId=359b6d3a&appKey=0f9a03a179334c56721fcf70cc8600b9")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
}
