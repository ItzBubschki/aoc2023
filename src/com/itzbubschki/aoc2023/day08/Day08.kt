package com.itzbubschki.aoc2023.day08

import lcm
import println
import readInput

val input =
    readInput("Day08")
//    readInput("Day08_test")

fun main() {
    part1().println()
    part2().println()
}

fun part1(): Long {
    val (instructions, nodeInstructions) = extractInstruction()

    return processInstructions(instructions, nodeInstructions, "AAA").second
}

fun part2(): Long {
    val (instructions, nodeInstructions) = extractInstruction()

    val starterNodes =
        nodeInstructions.filter { it.substringBefore(" =").endsWith("A") }.map { it.substringBefore(" =") }
    val stepsToStart = mutableListOf<Long>()

    starterNodes.forEach { node ->
        val result = processInstructions(instructions, nodeInstructions, node)
        stepsToStart.add(result.second)
    }

    return stepsToStart.reduce { acc, it -> acc.lcm(it) }
}

fun processInstructions(instructions: String, nodeInstructions: List<String>, current: String): Pair<String, Long> {
    var currentNode = current
    var currentInstruction = Pair(instructions.first(), 0)
    var steps = 0L
    while (!currentNode.endsWith("Z")) {
        nodeInstructions.find { it.startsWith(currentNode) }.let { line ->
            val leftAndRight =
                getLeftAndRight(line ?: throw IllegalArgumentException("No line found starting with $currentNode"))
            currentNode = if (currentInstruction.first == 'L') {
                leftAndRight.first()
            } else {
                leftAndRight.last()
            }
            val nextInstruction = instructions.getOrNull(currentInstruction.second + 1) ?: instructions[0]
            currentInstruction = Pair(
                nextInstruction,
                if (currentInstruction.second + 1 >= instructions.length) 0 else currentInstruction.second + 1
            )
            steps++
        }
    }
    return Pair(currentNode, steps)
}

fun extractInstruction(): Pair<String, List<String>> {
    val instructionParts = input.joinToString("\n").split("\n\n")
    val instructions = instructionParts.first()
    val nodeInstructions = instructionParts.last().split("\n")
    return Pair(instructions, nodeInstructions)
}

fun getLeftAndRight(line: String): List<String> {
    val parts = line.split(" = ")
    return parts.last().replace(Regex("[\\\\()]"), "").split(",").map { it.trim() }
}