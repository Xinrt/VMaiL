package com.teamsixers.vmail.utils

import android.util.Log
import com.iflytek.cloud.RecognizerResult
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*

/**
 * Json parser for IFLYTEK ASR Json result of recognition result.
 */
object JsonParser {

    /**
     * Get recognition result string for json.
     * @since 1.0
     *
     * @author Mingyan(Cyril) Wang
     */
    fun getResultString(mIatResults: HashMap<Int, String>, recognizerResult: RecognizerResult) {
        val json = recognizerResult.resultString
        val str = parseIatResult(json)
        Log.d("json", json)

        str.trim()

        var sn: String? = null
        // get sn field from json result
        try {
            val resultJson = JSONObject(json)
            sn = resultJson.optString("sn")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (sn != null) {
            mIatResults[sn.toInt()] = str
        }

        val resultBuffer = StringBuffer()
        for (key in mIatResults.keys) {
            resultBuffer.append(mIatResults[key])
        }
    }

    /**
     * Json parser for IFLYTEK ASR Json result of recognition.
     *
     * @since 1.0
     *
     * @author Xinran Tang
     */
    fun parseIatResult(json: String?): String {
        val ret = StringBuffer()
        try {
            val tokener = JSONTokener(json)
            val joResult = JSONObject(tokener)
            val words = joResult.getJSONArray("ws")
            for (i in 0 until words.length()) {
                val items = words.getJSONObject(i).getJSONArray("cw")
                val obj = items.getJSONObject(0)
                ret.append(obj.getString("w"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ret.toString()
    }

}