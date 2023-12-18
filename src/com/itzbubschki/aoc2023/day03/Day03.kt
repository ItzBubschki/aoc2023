package com.itzbubschki.aoc2023.day03

import com.itzbubschki.aoc2023.utils.println
import com.itzbubschki.aoc2023.utils.readInput

val offsets = listOf(-1, 0, 1)

fun main() {
    val input = readInput("Day03")
    //readInput("Day03_test")
    val matrix = input.map { it.toCharArray() }

    (part1(matrix) == 540131).println()
    (part2(matrix) == 86879020).println()
}

fun part1(matrix: List<CharArray>): Int {
    val indices = mutableListOf<Pair<Int, Int>>()
    matrix.mapIndexed { index, row ->
        row.mapIndexed { innerIndex, element ->
            if (element.isDigit() && isNeighbourSymbol(matrix, innerIndex, index)) indices.add(
                Pair(innerIndex, index)
            )
        }
    }

    return indices.filterNot { areSequentialPairs(it, indices) }.sumOf { getFullNum(matrix, it) }
}

fun part2(matrix: List<CharArray>): Int {
    val numberCoordinates = matrix.withIndex().flatMap { (index, row) ->
        row.withIndex().mapNotNull { (innerIndex, field) ->
            if (field == '*') getNeighbourNumbers(matrix, innerIndex, index) else null
        }
    }

    return numberCoordinates
        .map { coordinate -> coordinate.map { number -> getFullNum(matrix, number) } }
        .sumOf { group ->
            group.drop(1).fold(group.first()) { ratio, num -> ratio * num }
        }
}

fun areSequentialPairs(pair: Pair<Int, Int>, pairsList: List<Pair<Int, Int>>) =
    pairsList.any { it.second == pair.second && it.first == pair.first + 1 }

fun getNeighbourNumbers(matrix: List<CharArray>, x: Int, y: Int): List<Pair<Int, Int>>? {
    val nums = offsets.flatMap { i ->
        offsets.mapNotNull { j ->
            if (matrix.getOrNull(y + i)?.getOrNull(x + j)?.isDigit() == true) Pair(x + j, y + i)
            else null
        }
    }

    return nums.filterNot { areSequentialPairs(it, nums) }.takeIf { it.size >= 2 }
}

fun isNeighbourSymbol(matrix: List<CharArray>, x: Int, y: Int): Boolean {
    return offsets.any { i ->
        offsets.any { j ->
            val neighbourCell = matrix.getOrNull(y + i)?.getOrNull(x + j)
            neighbourCell?.let { !it.isDigit() && it != '.' } ?: false
        }
    }
}

fun getFullNum(matrix: List<CharArray>, coords: Pair<Int, Int>): Int {
    val (x, y) = coords
    val row = matrix[y]

    val rightDigits = row.drop(x).takeWhile { it.isDigit() }
    val leftDigits = row.take(x).reversed().takeWhile { it.isDigit() }.reversed()

    return (leftDigits + rightDigits).joinToString("").toInt()
}
