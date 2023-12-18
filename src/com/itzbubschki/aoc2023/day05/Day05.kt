package com.itzbubschki.aoc2023.day05

import com.itzbubschki.aoc2023.utils.println
import com.itzbubschki.aoc2023.utils.readInput

data class Range(val sourceStart: Long, val destinationStart: Long, val steps: Long)

val input =
    readInput("Day05")
//    readInput("Day05_test")

fun main() {
    part1().println()
    part2().println()
}

fun part1(): Long {
    val seeds = input.first().substringAfter("seeds: ").split(" ").map { it.toLong() }

    val chunkedList = input.drop(2).joinToString(separator = "\n").split("\n\n").map { it.split("\n") }

    val ranges = calculateChunkedRanges(chunkedList)

    val usedLocations = mutableListOf<Long>()
    var lastUsedIndex: Long
    seeds.map { seed ->
        lastUsedIndex = seed
        ranges.map { rangeGroup ->
            val foundRange =
                rangeGroup.firstOrNull { range -> range.sourceStart <= lastUsedIndex && range.sourceStart + range.steps > lastUsedIndex }
            if (foundRange != null) {
                lastUsedIndex = foundRange.destinationStart + (lastUsedIndex - foundRange.sourceStart)
            }
        }
        usedLocations.add(lastUsedIndex)
    }
    return usedLocations.min()
}

fun part2(): Long {
    val seeds = input.first().substringAfter("seeds: ").split(" ").map { it.toLong() }.chunked(2)

    val chunkedList = input.drop(2).joinToString(separator = "\n").split("\n\n").map { it.split("\n") }

    val ranges = calculateChunkedRanges(chunkedList).reversed()

    for (i in 0..ranges.first().maxOf { range -> range.destinationStart + range.steps }) {
        var lastUsedIndex = i
        ranges.forEach { rangeGroup ->
            val foundRange =
                rangeGroup.firstOrNull { range -> lastUsedIndex >= range.destinationStart && lastUsedIndex < range.destinationStart + range.steps }
            if (foundRange != null) {
                lastUsedIndex = foundRange.sourceStart + (lastUsedIndex - foundRange.destinationStart)
            }
        }
        seeds.firstOrNull { lastUsedIndex >= it.first() && lastUsedIndex < it.first() + it.last() }?.let { return i }
    }
    return -1
}

fun calculateChunkedRanges(chunkedList: List<List<String>>): MutableList<List<Range>> {
    val ranges = mutableListOf<List<Range>>()
    chunkedList.map { chunk ->
        val innerRanges = mutableListOf<Range>()
        chunk.drop(1).map { line ->
            val nums = line.split(" ").map { it.toLong() }
            innerRanges.add(Range(nums[1], nums.first(), nums.last()))
        }
        ranges.add(innerRanges)
    }
    return ranges
}