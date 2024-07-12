class PatternMatchingManual {
    @JvmRecord
    private data class Test(val x: Int)

    fun test() {
        val a: Any = Test(1)
        if (a is Test) {
            val testA = a
        }
    }
}