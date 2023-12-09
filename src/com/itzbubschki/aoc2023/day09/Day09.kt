package com.itzbubschki.aoc2023.day09

import println
import readInput

val input =
    readInput("Day09")
//    readInput("Day09_test")

fun main() {
    getExtensionSum(false).println()
    getExtensionSum(true).println()
}

fun getExtensionSum(previous: Boolean): Int {
    val extensions = mutableListOf<Int>()
    input.forEach { extensions.add(buildPyramidAndGetExtension(it, previous)) }
    return extensions.sum()
}

fun buildPyramidAndGetExtension(line: String, previous: Boolean = false): Int {
    val lines = mutableListOf<MutableList<Int>>()
    lines.add(line.split(" ").map { it.toInt() }.toMutableList())
    while (!lines.last().all { it == 0 }) {
        val newLine = mutableListOf<Int>()
        for (i in 0 until lines.last().size - 1) {
            val diff = lines.last()[i + 1] - lines.last()[i]
            newLine.add(diff)
        }
        lines.add(newLine)
    }
    return if (previous) getBeginningForPyramid(lines.reversed()) else getExtensionForPyramid(lines.reversed())
}

fun getExtensionForPyramid(pyramid: List<MutableList<Int>>): Int {
    for (i in 0 until pyramid.size - 1) {
        val nextLine = pyramid[i + 1]
        nextLine.add(pyramid[i].last() + nextLine.last())
    }
    return pyramid.last().last()
}

fun getBeginningForPyramid(pyramid: List<MutableList<Int>>): Int {
    val reversed = pyramid.map { it.reversed().toMutableList() }
    for (i in 0 until reversed.size - 1) {
        val nextLine = reversed[i + 1]
        nextLine.add(nextLine.last() - reversed[i].last())
    }
    return reversed.last().last()
}