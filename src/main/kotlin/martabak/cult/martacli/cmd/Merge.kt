package martabak.cult.martacli.cmd

import Config
import Metadata
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintStream

class Merge : CliktCommand() {

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private var config: Config

    private val fromPath: String = System.getProperty("user.dir")

    init {
        config = moshi.adapter(Config::class.java)
            .fromJson(BufferedReader(FileReader("${fromPath}/config.json")).readText())!!
    }

    val dirs by argument().file(mustExist = true).multiple()
    private val output by option(help = "output").default("./merged")

    override fun run() {

        var newId = 1
        dirs.forEach { dir ->
            echo("Merging ${dir.path}")
            val pngFiles = dir.list().filter { !it.startsWith(".") }.filter { it.endsWith(".jpg") }
            echo("Files to merge : ${pngFiles.size}")

            pngFiles.forEach { image ->
                File("${dir.path}/${image}").copyTo(File("${fromPath}/${output}/${newId}.jpg"))
                val f = image.substringBefore('.')
                val metadata = moshi.adapter(Metadata::class.java)
                    .fromJson(BufferedReader(FileReader("${fromPath}/${dir.path}/${f}.json")).readText())
                val updatedMetadata =
                    metadata?.copy(name = "${config.metadata.namePrefix}$newId", tokenId = newId)
                val json = moshi.adapter(Metadata::class.java).toJson(updatedMetadata)
                PrintStream("${fromPath}/${output}/${newId}.json").use { ps -> ps.println(json) }
                newId++
            }
        }
    }
}