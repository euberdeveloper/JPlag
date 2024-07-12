package java

import java.util.ArrayList

object Compact {
    fun tryCount(a: ArrayList<Int>, n: Int): Int {
        if (a.isEmpty())
            return Int.MAX_VALUE

        var count = 0

        for (i in 0 until a.size - 1)
            if (a[i] + 1 != a[i + 1])
                count++

        if (a.first() != 0)
            count++

        if (a.last() != n - 1)
            count++

        return count
    }
}

