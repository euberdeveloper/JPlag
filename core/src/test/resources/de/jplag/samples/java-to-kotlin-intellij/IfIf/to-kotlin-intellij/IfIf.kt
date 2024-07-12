internal class {
    object IfElse1 {
        @JvmStatic
        fun main(args: Array<String>) {
            requireNotNull(args)
            if (args.size > 1) {
                println(args.contentToString())
            }
        }
    }
}
