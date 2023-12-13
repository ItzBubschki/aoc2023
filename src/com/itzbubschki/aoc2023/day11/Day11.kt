package com.itzbubschki.aoc2023.day11

import println
import readInput
import rotateMatrix
import kotlin.math.*

val input =
    readInput("Day11")
//    readInput("Day11_test")

typealias Point = Pair<Int, Int>

fun main() {
    calculateSizeWithExpansion(1).println()
    calculateSizeWithExpansion(1_000_000L-1).println()
}

fun getEmptyLines(): List<Int> =
    input.mapIndexedNotNull { index, s -> if (!s.contains("#")) index else null }

fun getEmptyRows(): List<Int> =
    rotateMatrix(input).mapIndexedNotNull { index, s -> if (!s.contains("#")) index else null }
        .map { input.first().length - it - 1 }.reversed()

fun createPoints(): List<Point> {
    val points = mutableListOf<Point>()
    for ((y, line) in input.withIndex()) {
        for ((x, field) in line.withIndex()) {
            if (field == '#') {
                points.add(Point(x, y))
            }
        }
    }
    return points
}

fun calculateTotalDistance(points: List<Point>, emptyRows: List<Int>, emptyLines: List<Int>, expansion: Long): Long {
    var totalDistance = 0L
    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            val (i1, i2) = points[i]
            val (j1, j2) = points[j]

            totalDistance += points[i].calculateDistance(points[j]).toLong()
            totalDistance += (min(i1, j1)..max(i1,j1)).count{emptyRows.contains(it)} * expansion
            totalDistance += (min(i2, j2)..max(i2,j2)).count{emptyLines.contains(it)} * expansion
        }
    }
    return totalDistance
}

fun calculateSizeWithExpansion(expansion: Long): Long {
    val emptyLines = getEmptyLines()
    val emptyRows = getEmptyRows()
    val points = createPoints()
    return calculateTotalDistance(points, emptyRows, emptyLines, expansion)
}

fun Point.calculateDistance(other: Point): Int {
    return abs(this.first - other.first) + abs(this.second - other.second)
}