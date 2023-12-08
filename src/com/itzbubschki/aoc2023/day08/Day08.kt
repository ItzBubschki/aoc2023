package com.itzbubschki.aoc2023.day08

import println
import readInput

val input =
    readInput("Day08")
//    readInput("Day08_test")

fun main() {
    part1().println()
}

fun part1(): Int {
    val instructionParts = input.joinToString("\n").split("\n\n")
    val instructions = instructionParts.first()
    val nodeInstructions = instructionParts.last().split("\n")
    var current = "AAA"
    var currentStep = Pair(instructions.first(), 0)
    var steps = 0
    while (current != "ZZZ") {
        val line = nodeInstructions.find { it.startsWith(current) }
        val leftAndRight = getLeftAndRight(line!!)
        current = when (currentStep.first) {
            'L' -> leftAndRight.first()
            else -> leftAndRight.last()
        }
        val nextInstruction = instructions.getOrNull(currentStep.second + 1) ?: instructions[0]
        currentStep =
            Pair(nextInstruction, if (currentStep.second + 1 >= instructions.length) 0 else currentStep.second + 1)
        steps++
    }
    return steps
}

fun getLeftAndRight(line: String): List<String> {
    val parts = line.split(" = ")
    return parts.last().replace(Regex("[\\\\()]"), "").split(",").map { it.trim() }
}
