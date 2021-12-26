package com.teamsixers.vmail.utils

import android.util.Log
import java.util.*

/**
 * StringUtils singleton class provides functionality for
 * processing string.
 */
object StringUtils {
    /**
     * Process raw email address and return formatted email address.
     * caller should check the raw email address whether contains `at`(@).
     *
     * @sample "123456atqqdotcom" will return "123456@qq.com"
     *
     * @since 1.0
     *
     * @author Mingyan(Cyril) Wang
     */
    fun processRawEmailAddress(str: String): String {
        val strWithoutSpace = str.replace("\\s".toRegex(), "")
        val strAllLowerCase = strWithoutSpace.toLowerCase(Locale.UK)
        // find final index of "at"
        val atString = "at"
        val lastIndexOfAt = strAllLowerCase.lastIndexOf(atString)
        var stringBehindAt = ""
        var trueStringBehindAt = ""
        val resultStringBuilder = StringBuilder()
        if (lastIndexOfAt != -1) {
            stringBehindAt = strAllLowerCase.substring(lastIndexOfAt + atString.length, strAllLowerCase.length)
            trueStringBehindAt = stringBehindAt.replace("dot", ".", true)
            if (lastIndexOfAt != 0) {
                resultStringBuilder.append(strAllLowerCase.substring(0, lastIndexOfAt))
            }
            resultStringBuilder
                    .append("@")
                    .append(trueStringBehindAt)
                    .toString()
            Log.d("StringUtils", "processRawEmailAddress: $strAllLowerCase")

        } else {
            resultStringBuilder.append(strAllLowerCase)
        }
        return resultStringBuilder.toString()
    }


    /**
     * Get string after target string in original string.
     *
     * @sample 9474112 == getStringAfter("My account is 9474112", "account is ")
     *
     * @since 1.0
     *
     * @author Mingyan Wang
     *
     * @return if targetStr is not found in originString, return empty string.
     * If find, return the string after targetStr.
     */
    fun getStringAfter(originString: String, targetStr: String): String {
        val indexAfterTargetString: Int = originString.indexOf(targetStr) + targetStr.length
        var resultString = ""
        if (indexAfterTargetString > targetStr.length) { // text.indexOf != -1
            resultString = originString.substring(indexAfterTargetString, originString.length)
        }
        return resultString
    }
    /**
     * Get string before target string in original string.
     *
     * @sample My account is == getStringBefore("My account is 9474112", "9474112 ")
     *
     * @since 1.0
     *
     * @author Jiake Hao
     *
     * @return if targetStr is not found in originString, return empty string.
     * If find, return the string before targetStr.
     */
    fun getStringBefore(originString: String, targetStr: String): String {
        val indexBeforeTargetString: Int = originString.indexOf(targetStr)
        var resultString = ""
        if (originString.length > targetStr.length) { // text.indexOf != -1
            resultString = originString.substring(0, indexBeforeTargetString)
        }
        return resultString
    }


    /**
     * Convert English numbers in sentences into Arabic numbers
     *
     * @sample 1 2 3 == changeENumToANum("one two three")
     *
     * @since 1.0
     *
     * @author Jiake Hao
     *
     * @return If cannot find target string, return original string
     * If find, return the string after converting.
     */
    fun changeENumToANum(originString: String): String {
        return originString.replace("zero", "0")
                .replace("one", "1")
                .replace("two", "2")
                .replace("three", "3")
                .replace("four", "4")
                .replace("five", "5")
                .replace("six", "6")
                .replace("seven", "7")
                .replace("eight", "8")
                .replace("nine", "9")
            .replace("One", "1")
            .replace("Two", "2")
            .replace("Three", "3")
            .replace("Four", "4")
            .replace("Five", "5")
            .replace("Six", "6")
            .replace("Eight", "8")
            .replace("Nine", "9")
    }
}