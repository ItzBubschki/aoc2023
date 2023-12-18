package com.itzbubschki.aoc2023.day06

import com.itzbubschki.aoc2023.utils.println
import com.itzbubschki.aoc2023.utils.readInput

val input =
    readInput("Day06")
//    readInput("Day06_test")

fun main() {
    part1().println()
    part2().println()
}

fun part1(): Int {
    val map = input.map {
        it.substringAfter(": ").split(" ").filter { string -> string.isNotBlank() }.map { num -> num.toInt() }
    }
    val winCount = mutableListOf<Int>()
    map.first().forEachIndexed { index, value ->
        run {
            var winners = 0
            val distance = map.last()[index]
            repeat(value) { i ->
                if ((value - i) * i > distance) {
                    winners++
                }
            }
            winCount.add(winners)
        }
    }
    return winCount.reduce { acc, i -> acc * i }
}

fun part2(): Int {
    val nums = input.map { it.substringAfter(": ").replace(" ", "").toLong() }
    var wins = 0
    repeat(nums.first().toInt()) { i ->
        if ((nums.first() - i) * i > nums.last()) {
            wins++
        }
    }
    return wins
}