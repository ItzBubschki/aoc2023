import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.absoluteValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

tailrec fun Long.gcd(other: Long): Long {
    return if (this == 0L || other == 0L) {
        this + other
    } else {
        val bigger = maxOf(this.absoluteValue, other.absoluteValue)
        val smaller = minOf(this.absoluteValue, other.absoluteValue)
        (bigger % smaller).gcd(smaller)
    }
}

fun Long.lcm(other: Long) =
    if (this == 0L || other == 0L) {
        0
    } else {
        (this * other).absoluteValue / this.gcd(other)
    }

fun rotateMatrixCounterCw(matrix: List<String>): List<String> {
    val rotated = matrix.map { it.reversed() }.toMutableList()
    return rotated[0].indices.map { i -> rotated.map { it[i] } }.map { it.joinToString("") }
}

fun rotateMatrixCw(matrix: List<String>): List<String> {
    val transposed = matrix[0].indices.map { i -> matrix.map { it[i] } }
    return transposed.map { it.joinToString("").reversed() }
}

fun chunkThroughEmptyLines(input: List<String>): List<List<String>> {
    return input.joinToString("\n").split("\n\n").map { it.split("\n") }
}

fun Int.mirroredIndex(mirror: Int): Int {
    return mirror - this + mirror + 1
}

fun Int.calculateColumnIndex(rows: Int, rotated: Boolean = false): Int {
    return if (rotated) (rows - this) % rows else this
}

fun String.getDifferenceCount(other: String): Int {
    require(this.length == other.length) { "Input strings must have the same length" }
    return this.zip(other).count { it.first != it.second }
}

fun String.replaceAt(index: Int, replacement: Char): String {
    if (index in indices) {
        val stringBuilder = StringBuilder(this)
        stringBuilder.setCharAt(index, replacement)
        return stringBuilder.toString()
    }
    return this
}

enum class Direction {
    EAST,
    WEST,
    NORTH,
    SOUTH
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + other.first, this.second + other.second)
}

val DirectionToPositionMap = mapOf(
    Direction.NORTH to (0 to -1),
    Direction.EAST to (1 to 0),
    Direction.SOUTH to (0 to 1),
    Direction.WEST to (-1 to 0)
)

fun <A> List<List<A>>.get2d(x: Int, y: Int): A {
    return this[y][x]
}

fun <A> List<List<A>>.get2dOptional(x: Int, y: Int): A? {
    return this.getOrNull(y)?.getOrNull(x)
}

fun <A> List<List<A>>.get2d(position: Pair<Int, Int>): A {
    return this[position.second][position.first]
}

fun <A> List<List<A>>.get2dOptional(position: Pair<Int, Int>): A? {
    return this.getOrNull(position.second)?.getOrNull(position.first)
}

fun <T> List<String>.inputToClass(transform: (Int, Int, Char) -> T): List<List<T>> {
    return this.mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            transform(x, y, c)
        }
    }
}