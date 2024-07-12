package java

import java.util.Arrays

fun main(args: Array<String>) {
    if (args == null) {
        throw IllegalArgumentException()
    } else if (args.size > 1) {
        println(args.contentToString())
    } else {
        println(args[0])
    }
}

