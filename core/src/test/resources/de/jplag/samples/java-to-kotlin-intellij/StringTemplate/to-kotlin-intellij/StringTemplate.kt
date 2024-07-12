class StringTemplate {
    fun test() {
        val param1 = 1
        val param2 = "test"

        val result: String = TODO(
            """
            |Cannot convert element
            |With text:
            |STR."prefix \{param1} infix + \{param2.length()} suffix"
            """.trimMargin()
        )
    }
}