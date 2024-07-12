package java

import java.util.ArrayList

object Verbose {
    fun tryCount(l: ArrayList<Int>, n: Int): Int {
        if (l.isEmpty()) {
            return Int.MAX_VALUE
        }

        var count = 0

        for (i in 0 until l.size - 1) {
            if (l[i] + 1 != l[i + 1]) {
                count++
            }
        }
        if (l.first() != 0) {
            count++
        }

        if (l.last() != n - 1) {
            count++
        }

        return count
    }
}

