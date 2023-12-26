package com.itzbubschki.aoc2023.utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.absoluteValue


infix fun IntRange.intersects(other: IntRange): Boolean = first <= other.last && last >= other.first

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16).padStart(32, '0')

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

fun Long.lcm(other: Long) = if (this == 0L || other == 0L) {
    0
} else {
    (this * other).absoluteValue / this.gcd(other)
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

fun <A, B> Map<A, B>.getNotNull(key: A): B {
    return this[key]!!
}

fun <T> List<String>.inputToClass(transform: (Int, Int, Char) -> T): List<List<T>> {
    return this.mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            transform(x, y, c)
        }
    }
}

fun Iterable<Int>.product(): Int = reduce(Int::times)

fun Iterable<Long>.product(): Long = reduce(Long::times)