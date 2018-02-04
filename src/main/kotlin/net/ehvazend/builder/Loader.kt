package net.ehvazend.builder

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigObject
import net.ehvazend.builder.data.Mod
import net.ehvazend.builder.data.Module
import net.ehvazend.builder.data.Pack
import java.io.File
import java.net.URI
import java.net.URL

object Loader {
    fun getPacks(address: URI): ArrayList<Pack> {
        val config = ConfigFactory.parseFile(File(address))

        fun getMod(value: ConfigObject): Mod {
            return value.toConfig().run {
                val name = getString("name")
                val summary = getString("summary")
                val url = getString("url")
                val file = getInt("file")
                Mod(name, summary, URL(url), file)
            }
        }

        fun getModule(value: ConfigObject): Module {
            val module = value.toConfig()
            val name = module.getString("name")
            val mods = ArrayList<Mod>().apply {
                module.getObjectList("mods").forEach { this += getMod(it) }
            }
            return Module(name, mods)
        }

        fun getPack(value: ConfigObject): Pack {
            val pack = value.toConfig()
            val name = pack.getString("name")
            val modules = ArrayList<Module>().apply {
                pack.getObjectList("modules").forEach { this += getModule(it) }
            }
            return Pack(name, modules)
        }

        fun getPacks(value: Config): ArrayList<Pack> {
            return ArrayList<Pack>().apply {
                value.getObjectList("packs").forEach { this += getPack(it) }
            }
        }

        return getPacks(config)
    }
}
