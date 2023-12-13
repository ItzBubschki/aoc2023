package com.itzbubschki.aoc2023.day13

import calculateColumnIndex
import chunkThroughEmptyLines
import getDifferenceCount
import mirroredIndex
import println
import readInput
import rotateMatrix

val input =
//    readInput("Day13_test")
    readInput("Day13")

fun main() {
    val chunked = chunkThroughEmptyLines(input)
    part1(chunked).println()
    part2(chunked).println()
}

fun part1(chunks: List<List<String>>) =
    chunks.sumOf { findMirrorInRows(rotateMatrix(it), true) ?: (100 * findMirrorInRows(it)!!) }

fun part2(chunks: List<List<String>>) =
    chunks.sumOf { findMirrorsWithSmudges(rotateMatrix(it), true) ?: (100 * findMirrorsWithSmudges(it)!!) }

fun findMirrorInRows(matrix: List<String>, rotated: Boolean = false): Int? {
    return matrix.withIndex().windowed(2).indexOfFirst { (line, nextLine) ->
        line.value == nextLine.value && allMirrored(line.index, matrix)
    }.let {
        if (it == -1) return null
        (it + 1).calculateColumnIndex(matrix.size, rotated)
    }
}

fun allMirrored(index: Int, matrix: List<String>) = (0 until index).all {
    val mirror = it.mirroredIndex(index)
    mirror >= matrix.size || matrix[it] == matrix[mirror]
}

fun findMirrorsWithSmudges(matrix: List<String>, rotated: Boolean = false): Int? {
    return matrix.withIndex().windowed(2).indexOfFirst { (line, nextLine) ->
        val comparison = line.value.getDifferenceCount(nextLine.value)
        if (comparison == 0) {
            var smudged = false
            (0 until line.index).all { i ->
                val mirror = i.mirroredIndex(line.index)
                when {
                    mirror >= matrix.size -> true
                    matrix[i].getDifferenceCount(matrix[mirror]) == 0 -> true
                    matrix[i].getDifferenceCount(matrix[mirror]) == 1 -> !smudged.also { smudged = true }
                    else -> false
                }
            } && smudged
        } else
            comparison == 1 && allMirrored(line.index, matrix)
    }.let {
        if (it == -1) return null
        (it + 1).calculateColumnIndex(matrix.size, rotated)
    }
}