package com.itzbubschki.aoc2023.day04

import println
import readInput
import kotlin.math.pow

val input =
    readInput("Day04")
//    readInput("Day04_test")

data class OwnChoices(val nums: List<String>, var copies: Int)

fun main() {
    part1().println()
    part2().println()
}

fun part1(): Int {
    val parts = input.map { line ->
        line.substringAfter(":").split("|").map { segment ->
            segment.split(" ").filter { word -> word.isNotEmpty() }
        }
    }

    val counts = parts.map { part ->
        part.last().count { word -> part.first().contains(word) }
    }

    return counts.sumOf { count -> 2.0.pow(count - 1).toInt() }
}

fun part2(): Int {
    val parts = input.map {
        val list =
            it.substringAfter(":").split("|").map { parts -> parts.split(" ").filter { it2 -> it2.isNotEmpty() } }
        Pair(list.first(), OwnChoices(list.last(), 1))
    }
    parts.mapIndexed { index, it ->
        val score = it.second.nums.count { it2 -> it.first.contains(it2) }
        repeat(it.second.copies) { repeat(score) { i -> parts[index + i + 1].second.copies++ } }
    }

    return parts.sumOf { it.second.copies }
}