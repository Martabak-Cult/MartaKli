import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

class Nft : CliktCommand() {

    override fun run() = Unit
}


fun main(args: Array<String>) =
    Nft()
        .subcommands(Gen())
        .main(args)

