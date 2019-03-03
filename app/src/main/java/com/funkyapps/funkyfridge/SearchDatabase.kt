package com.funkyapps.funkyfridge

import android.util.Log
import java.io.*
import java.net.*

class SearchDataBase {
    init {
        try {
            // should print out all the nutrition info of the item
            getHTML("https://api.nutritionix.com/v1_1/item?upc=[UPC]&appId=359b6d3a&appKey=0f9a03a179334c56721fcf70cc8600b9") //insert upc into [UPC] to search for item
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        @Throws(Exception::class)
        //Should return a string with the product's information
        fun getHTML(urlToRead: String): String {
            val result = StringBuilder()
            val url = URL(urlToRead)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            val rd = BufferedReader(InputStreamReader(conn.inputStream))
            var line: String? = null;
            while ({ line = rd.readLine(); line }() != null) {
                result.append(line)
            }
            rd.close()
            return result.toString()
        }

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            println(getHTML(args[0])) //prints out string containing all nutrition info

        }
    }
}
