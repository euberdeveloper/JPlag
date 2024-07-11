package java

object Compact {
    fun tryCount(a: ArrayList<Int>, n: Int): Int {
        if (a.isEmpty()) return Int.MAX_VALUE

        var count = 0

        var i = 0
        while (i + 1 < a.size) {
            if (a[i] + 1 != a[i + 1]) count++
            i++
        }

        if (a[0] != 0) count++

        if (a[a.size - 1] != n - 1) count++

        return count
    }
}
