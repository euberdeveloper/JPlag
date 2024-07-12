package de.jplag.java

import java.io.File
import java.io.FileNotFoundException
import java.util.*

class TryWithResource {
    fun load(path: String?) {
        var other: Scanner? = null // This is just here to keep the tokens similar.
        try {
            Scanner(File(path)).also { other = it }.use { scanner ->  // same for = other =
                while (scanner.hasNext()) {
                    println(scanner.nextLine())
                }
            }
        } catch (exception: FileNotFoundException) {
            exception.printStackTrace()
        } finally {
            if (other != null) { // This as well...
                other!!.close() // This as well...
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            TryWithResource().load("DoesNotExist.txt")
        }
    }
}
