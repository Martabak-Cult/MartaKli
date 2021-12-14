import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.*
import java.net.URLDecoder
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.random.Random

class Gen : CliktCommand(help = "Generate your NFT") {

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private var config: Config

    private val fromPath: String = System.getProperty("user.dir")

    init {
        config = moshi.adapter(Config::class.java)
            .fromJson(BufferedReader(FileReader("${fromPath}/config.json")).readText())!!
    }

    // generation output directory
    private val output by option(help = "output").default("./output")
    // amount to be generated
    private val amount by option(help = "amount").int().default(1)

    override fun run() {
        echo("➡️  Generating assets, please chill \uD83C\uDF7A \uD83D\uDEAC")

        createOutputDirectory()

        val layersFiles = config.layers.map { Triple(it.key, it.value, assetsList("${fromPath}/${it.value}")) }

        for (i in 1..amount) {
            echo("\uD83D\uDDBC️  Assets ${i}/$amount ...")
            generateImage(layersFiles, i)
        }
        echo("✅ Generation done")
    }

    private fun generateImage(layersFiles: List<Triple<String, String, List<String>>>, i: Int) {
        val attributes = arrayListOf<Attribute>()
        val c = BufferedImage(config.resolution.width, config.resolution.height, BufferedImage.TYPE_INT_RGB)
        val g: Graphics = c.graphics
        layersFiles.forEach { layer ->
            val random = Random.nextInt(layer.third.size)
            val image = ImageIO.read(File(URLDecoder.decode("${fromPath}/${layer.second}/${layer.third[random]}")))
            g.drawImage(image, 0, 0, config.resolution.width, config.resolution.height, null)
            attributes.add(Attribute(layer.first, layer.third[random].replace(".png", "")))
        }
        try {
            ImageIO.write(c, "jpg", File("${fromPath}/${output}/${i}.jpg"))
        } catch (e: IOException) {
            echo(e.printStackTrace())
        }
        generateMetadata(i, attributes)
    }

    private fun generateMetadata(i: Int, attributes: List<Attribute>) {
        val metadata = Metadataa("${config.metadata.namePrefix}${i}", config.metadata.description, "", i, attributes)
        val json = moshi.adapter(Metadataa::class.java).toJson(metadata)
        PrintStream("${fromPath}/${output}/${i}.json").use { ps -> ps.println(json) }
    }

    private fun createOutputDirectory() {
        Files.createDirectories(Paths.get("${fromPath}/${output}"))
    }

    private fun assetsList(path: String): List<String> {
        return File(path).list()!!.filter { !it.startsWith(".") }
    }
}