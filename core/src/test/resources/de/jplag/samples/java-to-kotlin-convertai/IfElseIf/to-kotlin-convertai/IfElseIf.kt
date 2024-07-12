import java.util.*

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException()
    } else if (args.size > 1) {
        println(args.contentToString())
    }
}

