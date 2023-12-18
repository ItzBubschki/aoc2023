package com.itzbubschki.aoc2023.day12

import com.itzbubschki.aoc2023.utils.println
import com.itzbubschki.aoc2023.utils.readInput

val input =
    readInput("Day12")
//    readInput("Day12_test")

private val knownSolutions = hashMapOf<Pair<String, List<Int>>, Long>()

fun main() {
    part1().println()
    part2().println()
}

fun part1(): Long {
    return input.sumOf {
        it.split(" ")
            .let { split -> recursivelyCheckRemainingInput(split.first(), split.last().split(",").map(String::toInt)) }
    }
}

fun part2(): Long {
    return input.sumOf {
        it.split(" ").let { split ->
            recursivelyCheckRemainingInput(
                "${split.first()}?".repeat(5).dropLast(1),
                "${split.last()},".repeat(5).dropLast(1).split(",").map(String::toInt)
            )
        }
    }
}

fun recursivelyCheckRemainingInput(config: String, groups: List<Int>): Long {
    //no more groups but also no more springs left
    if (groups.isEmpty()) return if ("#" in config) 0 else 1
    //reached the end but there are groups remaining
    if (config.isEmpty()) return 0

    return knownSolutions.getOrPut(Pair(config, groups)) {
        var result = 0L
        if (config.first() in ".?")
            result += recursivelyCheckRemainingInput(config.drop(1), groups)
        if (config.first() in "#?" &&
            //are there even enough letters left to satisfy the first group length
            groups.first() <= config.length &&
            //are there enough springs or unknowns to fulfill the first group
            "." !in config.take(groups.first()) &&
            //are there only springs/unknowns left OR is the first token after the current group not a certain spring
            (groups.first() == config.length || config[groups.first()] != '#'))
            result += recursivelyCheckRemainingInput(config.drop(groups.first() + 1), groups.drop(1))
        result
    }
}