import java.util.*

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException()
    }
    if (args.size > 1) {
        println(args.contentToString())
    }
}

