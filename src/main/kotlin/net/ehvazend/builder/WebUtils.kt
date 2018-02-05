package net.ehvazend.builder

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigList
import com.typesafe.config.ConfigObject
import com.typesafe.config.ConfigValue
import net.ehvazend.builder.WebUtils.GameVersionData.Companion.FileBranch.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object WebUtils {
    fun startConnection(address: String) = (URL(address).openConnection() as HttpsURLConnection).run {
        // Data for CurseMeta
        setRequestProperty("User-Agent", "BuilderMPU")

        println("\nSending '$requestMethod' request to URL : $url\nResponse Code : $responseCode\n")

        try {
            return@run BufferedReader(InputStreamReader(inputStream)).readLine()
        } catch (ioException: IOException) {
            return@run ""
        }
    }!!

    fun getMods(value: String) = ArrayList<PrimaryModData>().apply {
        ConfigFactory.parseString(value).root()!!.forEach {
            this += PrimaryModData.fill(it)
        }
    }

    data class PrimaryModData(
        val id: Int,
        val name: String,
        val summary: String,
        val url: URL,
        val authorName: String,
        val popularityScore: Double,
        val gameVersionFiles: ArrayList<GameVersionData>
    ) {
        companion object {
            fun fill(value: Map.Entry<String, ConfigValue>): PrimaryModData {
                val (id, mod) = value
                mod as ConfigObject

                return PrimaryModData(
                    id.toInt(),
                    mod getString ("Name"),
                    mod getString ("Summary"),
                    mod getURL ("WebSiteURL").replaceBeforeLast("/", ""),
                    mod getString ("PrimaryAuthorName"),
                    mod getDouble ("PopularityScore"),
                    ArrayList<GameVersionData>().also { list ->
                        (mod["GameVersionLatestFiles"] as ConfigList).forEach {
                            list += GameVersionData.fill(it as ConfigObject)
                        }
                    }
                )
            }
        }
    }

    data class GameVersionData(val fileType: FileBranch, val gameVersion: String, val projectFileID: Int) {
        companion object {
            fun fill(configObject: ConfigObject) = GameVersionData(
                when (configObject getString ("FileType")) {
                    "alpha" -> ALPHA
                    "beta" -> BETA
                    "release" -> RELEASE

                    else -> throw IllegalArgumentException("Unidentified type of FileBranch")
                },

                //Inside GameVersionLatestFiles, GameVesion is misspelled. This is a Curse typo.
                configObject getString ("GameVesion"),
                configObject getInt ("ProjectFileID")
            )

            enum class FileBranch {
                ALPHA,
                BETA,
                RELEASE
            }
        }
    }

    // Extension for refactor
    private infix fun ConfigObject.getString(value: String) = this[value]?.atKey(value)?.getString(value)!!

    private infix fun ConfigObject.getDouble(value: String) = (this getString (value)).toDouble()
    private infix fun ConfigObject.getInt(value: String) = (this getString (value)).toInt()
    private infix fun ConfigObject.getURL(value: String) = (this getString (value)).toURL()

    private fun String.toURL() = URL(this)
}