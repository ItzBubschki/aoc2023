package com.itzbubschki.aoc2023.day18

import com.itzbubschki.aoc2023.utils.Direction
import com.itzbubschki.aoc2023.utils.DirectionToPositionMap
import com.itzbubschki.aoc2023.utils.println
import com.itzbubschki.aoc2023.utils.readInput

val input =
    readInput("Day18")
//    readInput("Day18_test")

fun main() {
    calculateContent(part1()).println()
    calculateContent(part2()).println()
}

fun part1(): List<Pair<Direction, Long>> {
    return input.map {
        Direction.fromChar(it.substringBefore(" ").first()) to
                it.substringBeforeLast(" ").split(" ").last().toLong()
    }
}

fun part2(): List<Pair<Direction, Long>> {
    return input.map {
        val content = it.substringAfterLast(" ").drop(2).dropLast(1)
        Direction.entries[content.last().digitToInt()] to
                "0$content".dropLast(1)
                    .uppercase().toLong(radix = 16)

    }
}

fun calculateContent(instructions: List<Pair<Direction, Long>>): Long {
    var perimeter = 0L
    var x = 0L
    var y = 0L
    val pts = mutableListOf(0L to 0L)
    for ((d, n) in instructions) {
        val (dx, dy) = DirectionToPositionMap[d]!!
        x += dx * n
        y += dy * n
        pts.add(x to y)
        perimeter += n
    }

    val shoelace = pts.zipWithNext { a, b -> a.first * b.second - b.first * a.second }.sum()
    return shoelace / 2 + perimeter / 2 + 1L
}

