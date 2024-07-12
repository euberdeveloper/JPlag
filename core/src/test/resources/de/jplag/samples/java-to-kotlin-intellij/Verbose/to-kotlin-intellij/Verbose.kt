package java

object Verbose {
    fun tryCount(l: ArrayList<Int>, n: Int): Int {
        if (l.size == 0) {
            return Int.MAX_VALUE
        }

        var count = 0

        var i = 0
        while (i + 1 < l.size) {
            if (l[i] + 1 != l[i + 1]) {
                count++
            }
            i++
        }
        if (l[0] != 0) {
            count++
        }

        if (l[l.size - 1] != n - 1) {
            count++
        }

        return count
    }
}
