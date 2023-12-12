package com.itzbubschki.aoc2023.day12

import println
import readInput

val input =
    readInput("Day12")
//    readInput("Day12_test")

val knownSolutionsMap = mutableMapOf<String, Int>()

fun main() {
//    part1().println()
    part2().println()
}

fun part1(): Int {
    return input.sumOf { recursivelyCheckRemainingInput(it) }
}

fun recursivelyCheckRemainingInput(line: String, startAfter: Int = 0): Int {
    var options = 0
    for ((index, ch) in line.drop(startAfter).withIndex()) {
        when (ch) {
            ' ' -> break
            '?' -> {
                val firstOption = line.replaceAt(index + startAfter, '.')
                val secondOption = line.replaceAt(index + startAfter, '#')
                options += getSolutionsForOption(firstOption, index, startAfter)
                options += getSolutionsForOption(secondOption, index, startAfter)
                break
            }

            else -> {}
        }
    }
    if (line.contains('?')) {
        return options
    } else if (checkIfLineIsValid(line)) {
        return options + 1
    }
    return 0
}

private fun getSolutionsForOption(newLine: String, index: Int, startAfter: Int): Int {
    if (knownSolutionsMap[newLine] != null) {
        return knownSolutionsMap[newLine]!!
    } else if (!checkIfLineIsStillPossible(newLine)) {
        knownSolutionsMap[newLine] = 0
    } else {
        val solutions = recursivelyCheckRemainingInput(newLine, index + startAfter)
        knownSolutionsMap[newLine] = solutions
        return solutions
    }
    return 0
}

fun checkIfLineIsValid(line: String): Boolean {
    val pattern = Regex("#+")
    val matches = pattern.findAll(line)
    val groups = matches.map { it.value }.toList()
    val controlSizes = line.substringAfter(" ").split(",").map { it.toInt() }
    return groups.size == controlSizes.size && groups.withIndex()
        .all { (index, group) -> group.length == controlSizes[index] }
}

fun checkIfLineIsStillPossible(line: String): Boolean {
    val split = line.split(" ")
    val controlSizes = split.last().split(",").map { it.toInt() }
    val toControl = split.first().substringBefore("?").substringBeforeLast(".", "")
    val pattern = Regex("#+")
    val matches = pattern.findAll(toControl)
    val groups = matches.map { it.value }.toList()
    return groups.size <= controlSizes.size && groups.withIndex().all { (index, group) -> group.length == controlSizes[index] }
}

fun part2(): Int {
    val newInput = input.map {
        val split = it.split(" ")
        val newBeginning = mutableListOf<String>()
        val newEnd = mutableListOf<String>()
        repeat(5) {
            newBeginning.add(split.first())
            newEnd.add(split.last())
        }
        newBeginning.joinToString("?") + " " + newEnd.joinToString(",")
    }
    return newInput.sumOf { recursivelyCheckRemainingInput(it) }
}

fun String.replaceAt(index: Int, replacement: Char): String {
    val charArray: CharArray = this.toCharArray()
    charArray[index] = replacement
    return String(charArray)
}