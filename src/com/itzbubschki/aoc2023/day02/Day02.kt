package com.itzbubschki.aoc2023.day02

import println
import readInput

fun main() {
    val input =
        readInput("Day02")
//        readInput("Day02_test")
    part1(input).println()
    part2(input).println()
}

fun part1(input: List<String>): Int {
    var sum = 0
    input.mapIndexed { index, line ->
        val values = parseValues(line)

        if (values.none(::isTooMuch)) sum += index + 1
    }
    return sum
}

fun parseValues(line: String): List<List<String>> {
    return line.substringAfter(":")
        .replace(";", ",")
        .split(",")
        .map { it.trim().split(" ") }
}

fun isTooMuch(text: List<String>): Boolean {
    return when (text.last()) {
        "red" -> text.first().toInt() > 12
        "green" -> text.first().toInt() > 13
        "blue" -> text.first().toInt() > 14
        else -> false
    }
}

fun part2(input: List<String>): Int {
    return input.asSequence()
        .map { line ->
            val values = parseValues(line)

            listOf("red", "green", "blue")
                .map { color ->
                    values.filter { it.last() == color }
                        .maxOf { it.first().toInt() }
                }
                .reduce { acc, it -> acc * it }
        }
        .sum()
}

