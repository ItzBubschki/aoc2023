package com.itzbubschki.aoc2023.day21

import com.itzbubschki.aoc2023.utils.*
import java.util.*

val input = readInput("Day21")

fun main() {
    part1(listOf(64)).println()

    part2(26_501_365).println()
}

fun part1(maxSteps: List<Int>): Long =
    solve(maxSteps).first()

fun part2(maxSteps: Int): Long {
    val cycleLength = (input.size * 2)
    val cycleOffset = maxSteps % cycleLength
    val initialCycles = 5
    val neededSteps = (2..initialCycles).map { cycleOffset + it * cycleLength }
    val firstNums = solve(neededSteps)

    val restCycles = (maxSteps - cycleOffset) / cycleLength - firstNums.size - 1
    var nums = firstNums.toList()
    for (cycle in 1..restCycles) {
        val newNum = solve21(nums)
        nums = (nums.takeLast(initialCycles) + newNum)
    }

    return nums.last()
}


var height: Int = 0
var width: Int = 0
var rocks: Set<Point> = emptySet()
var startPos: Point = 0 to 0

fun solve21(input: List<Long>): Long =
    recursiveDiff(input).map { it.last() }
        .reversed()
        .fold(0L) { acc, num ->
            num + acc
        }

fun solve(maxSteps: List<Int>): List<Long> {
    height = input.size
    width = input.first().length
    rocks = Grid.of(input, ignoreChars = listOf('.', 'S')).gridMap.keys.toSet()
    val col = input.indexOfFirst{ it.contains("S") }
    startPos = col to input[col].indexOfFirst { it == 'S' }

    return dfs(maxSteps)
}

fun isRock(pos: Point): Boolean {
    val newPos = Point(pos.first.mod(height), pos.second.mod(width))
    return rocks.contains(newPos)
}

fun dfs(relevantSteps: List<Int>): List<Long> {
    val foundPaths: MutableSet<Point> = mutableSetOf()
    val visited = mutableSetOf<Point>()
    val queue = PriorityQueue<IndexedValue<Point>>(compareBy { it.index })
    val maxSteps = relevantSteps.sorted().last()
    val evenOdd = maxSteps % 2
    visited += startPos
    queue += IndexedValue(0, startPos)
    val results = mutableMapOf<Int, Long>()
    while (queue.isNotEmpty()) {
        val pos = queue.poll()
        if (pos.index % 2 == evenOdd) {
            foundPaths += pos.value
        }
        results[pos.index] = foundPaths.size.toLong()
        if (pos.index == maxSteps) {
            continue
        }

        val nextPositions =
            pos.value.neighbours()
                .filterNot { isRock(it) || it == pos.value || visited.contains(it) }
                .map { IndexedValue(pos.index + 1, it) }
        visited += nextPositions.map { it.value }
        queue += nextPositions
    }
    return results.filter { it.key in relevantSteps }.map { it.value }
}