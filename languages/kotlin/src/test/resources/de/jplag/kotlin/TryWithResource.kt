package de.jplag.java

import java.io.File
import java.io.FileNotFoundException
import java.util.Scanner

class TryWithResource {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            TryWithResource().load("DoesNotExist.txt")
        }
    }

    fun load(path: String) {
        var other: Scanner? = null // This is just here to keep the tokens similar.
        try {
            other = Scanner(File(path))
            while (other.hasNext()) {
                println(other.nextLine())
            }
        } catch (exception: FileNotFoundException) {
            exception.printStackTrace()
        } finally {
            other?.close() // This as well...
        }
    }
}

