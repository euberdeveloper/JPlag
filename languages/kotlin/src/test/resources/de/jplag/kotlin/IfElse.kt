import java.util.Arrays

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException()
    } else {
        println(args.contentToString())
    }
}

