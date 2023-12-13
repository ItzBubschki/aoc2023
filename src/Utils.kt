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

fun rotateMatrix(matrix: List<String>): List<String> {
    val rotated = matrix.map { it.reversed() }.toMutableList()
    return rotated[0].indices.map { i -> rotated.map { it[i] } }.map { it.joinToString("") }
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