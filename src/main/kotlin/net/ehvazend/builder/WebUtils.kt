package net.ehvazend.builder

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object WebUtils {
    private fun startConnection(address: String) = (URL(address).openConnection() as HttpsURLConnection).run {
        // Data for CurseMeta
        setRequestProperty("User-Agent", "BuilderMPU")

        println("\nSending '$requestMethod' request to URL : $url\nResponse Code : $responseCode\n")

        try {
            return@run BufferedReader(InputStreamReader(inputStream)).readLine()
        } catch (ioException: IOException) {
            return@run ""
        }
    }!!
}