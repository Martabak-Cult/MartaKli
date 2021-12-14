import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import martabak.cult.martacli.cmd.Gen
import martabak.cult.martacli.cmd.Merge

class Nft : CliktCommand() {

    override fun run() = Unit
}



fun main(args: Array<String>) =
    Nft()
        .subcommands(Gen(), Merge())
        .main(args)

