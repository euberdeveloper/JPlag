package de.jplag.java

import java.io.File
import java.io.FileNotFoundException
import java.util.Scanner

class Try {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Try().load("DoesNotExist.txt")
        }
    }

    fun load(path: String) {
        var scanner: Scanner? = null
        try {
            val other: Scanner // This is just here to keep the tokens similar.
            scanner = Scanner(File(path))
            while (scanner.hasNext()) {
                println(scanner.nextLine())
            }
        } catch (exception: FileNotFoundException) {
            exception.printStackTrace()
        } finally {
            scanner?.close()
        }
    }
}

