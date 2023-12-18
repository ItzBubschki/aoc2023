package com.itzbubschki.aoc2023.day15

import com.itzbubschki.aoc2023.utils.println
import com.itzbubschki.aoc2023.utils.readInput
import java.util.LinkedList

data class Lens(val label: String, var focalLength: Int)

val input =
    readInput("Day15")
//    readInput("Day15_test")

fun main() {
    part1().println()
    part2().println()
}

fun part1(): Int {
    return input.first().split(",").sumOf(::hashAlgorithm)
}

fun part2(): Long {
    val instructions = input.first().split(",")
    val boxes = List(256) { LinkedList<Lens>() }
    instructions.map { executeInstruction(it, boxes) }
    return boxes.withIndex().sumOf {(index, it) ->
        it.withIndex().fold(0L) { acc, (i, lens) ->
            acc + ((1L + index.toLong()) * (1L + i.toLong()) * lens.focalLength.toLong())
        }
    }
}

fun executeInstruction(instruction: String, boxes: List<LinkedList<Lens>>) {
    if (instruction.contains("-")) {
        val label = instruction.substringBefore("-")
        val hash = hashAlgorithm(label)
        boxes[hash].removeAll { it.label == label }
    } else if (instruction.contains("=")) {
        val split = instruction.split("=")
        val label = split.first()
        val length = split.last().toInt()
        val hash = hashAlgorithm(label)
        boxes[hash].firstOrNull { it.label == label }?.let { it.focalLength = length }
            ?: boxes[hash].addLast(Lens(label, length))
    }
}

fun hashAlgorithm(word: String): Int {
    return word.toCharArray().fold(0) { acc, c ->
        ((acc + c.code) * 17) % 256
    }
}