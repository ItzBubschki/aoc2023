fun main() {
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

fun part1(input: List<String>): Int {
    val newMap = input.map { it2 -> it2.filter { it.isDigit() } }
    return newMap.map { "${it.first()}${it.last()}" }.sumOf { it.toInt() }
}

fun convertWordToNumber(word: String?): Int {
    return when (word) {
        "one" -> 1
        "two" -> 2
        "three" -> 3
        "four" -> 4
        "five" -> 5
        "six" -> 6
        "seven" -> 7
        "eight" -> 8
        "nine" -> 9
        else -> 0
    }
}

fun part2(input: List<String>): Int {
    //replace the words with numbers
    val firstMap = input.map { Regex("""(\d|one|two|three|four|five|six|seven|eight|nine)""").find(it)?.value }
        .map { it?.toIntOrNull() ?: convertWordToNumber(it) }
    val lastMap =
        input.map { Regex("""(\d|eno|owt|eerht|ruof|evif|xis|neves|thgie|enin)""").find(it.reversed())?.value }
            .map { it?.toIntOrNull() ?: convertWordToNumber(it?.reversed()) }

    return firstMap.indices.map{i -> "${firstMap[i]}${lastMap[i]}"}.sumOf { it.toInt() }
}