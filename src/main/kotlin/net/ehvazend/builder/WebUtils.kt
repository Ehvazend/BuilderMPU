package net.ehvazend.builder

import com.typesafe.config.ConfigObject
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

    data class GameVersionData(val fileType: FileType, val gameVersion: String, val projectFileID: Int) {
        companion object {
            fun fill(configObject: ConfigObject) = GameVersionData(
                when (configObject["FileType"]?.atKey("FileType")?.getString("FileType")) {
                    "alpha" -> FileType.ALPHA
                    "beta" -> FileType.BETA
                    "release" -> FileType.RELEASE

                    else -> throw IllegalArgumentException("Unidentified type of FileType")
                },
                configObject["GameVesion"]?.atKey("GameVersion")?.getString("GameVersion")!!,
                configObject["ProjectFileID"]?.atKey("ProjectFileID")?.getInt("ProjectFileID")!!
            )

        }
    }

    enum class FileType {
        ALPHA,
        BETA,
        RELEASE
    }
}