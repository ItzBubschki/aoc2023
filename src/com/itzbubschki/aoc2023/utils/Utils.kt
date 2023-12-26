package com.itzbubschki.aoc2023.utils

import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

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


tailrec fun recursiveDiff(list: List<Long>, acc: MutableList<List<Long>> = mutableListOf()): List<List<Long>> {
    acc.add(list)
    val nextList = list.zipWithNext { a, b -> b - a }
    return if (nextList.all { it == 0L }) acc else recursiveDiff(nextList, acc)
}