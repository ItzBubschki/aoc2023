package com.itzbubschki.aoc2023.day10

import println
import readInput
import kotlin.math.abs

enum class Opening {
    EAST,
    WEST,
    NORTH,
    SOUTH
}

data class Pipe(
    val position: Pair<Int, Int>,
    val openings: List<Opening>,
    val startTile: Boolean,
    var distance: Int = 0
)

val input =
    readInput("Day10")
//    readInput("Day10_test")

fun main() {
    val pipes = input.mapIndexed { y, line ->
        line.mapIndexed { x, char -> Pipe(Pair(x, y), getOpeningsForChar(char), char == 'S') }
    }
    val startTile = pipes.flatten().first { it.startTile }
    val tilesInLoop = mutableListOf<Pipe>()
    for (startDirection in Opening.entries) {
        tilesInLoop.clear()
        tilesInLoop.add(startTile)
        var current = getNextPipe(startTile, startDirection, pipes) ?: continue
        var nextDirection = startDirection
        while (!current.startTile) {
            tilesInLoop.add(current)
            if (current.openings.isNotEmpty())
                nextDirection = getNextDirection(nextDirection, current.openings)
            current = getNextPipe(current, nextDirection, pipes) ?: break
        }
        if (current.startTile) break
    }

    part1(tilesInLoop).println()
    part2(tilesInLoop, pipes).println()
}

fun part1(tilesInLoop: List<Pipe>): Int {
    return tilesInLoop.size / 2
}

fun part2(tilesInLoop: List<Pipe>, pipes: List<List<Pipe>>): Int {
    val positions = pipes.map { it.map { pipe -> pipe.position } }
    val positionsInLoop = tilesInLoop.map { it.position }
    return (0 until positions.size - 1).sumOf { y ->
        val loopBorders = positions[y].indices.filter { x ->
            val i1 = positionsInLoop.indexOf(x to y)
            val i2 = positionsInLoop.indexOf(x to y + 1)
            (i1 != -1 && i2 != -1 && (abs(i1 - i2) == 1 || i1 in listOf(0, tilesInLoop.lastIndex) && i2 in listOf(
                0,
                tilesInLoop.lastIndex
            )))
        }
        (loopBorders.indices step 2).sumOf { i ->
            (loopBorders[i]..loopBorders[i + 1]).count { x -> x to y !in positionsInLoop }
        }
    }
}

fun getOpeningsForChar(char: Char): List<Opening> {
    return when (char) {
        '|' -> listOf(Opening.NORTH, Opening.SOUTH)
        '-' -> listOf(Opening.EAST, Opening.WEST)
        'L' -> listOf(Opening.NORTH, Opening.WEST)
        'J' -> listOf(Opening.NORTH, Opening.EAST)
        '7' -> listOf(Opening.EAST, Opening.SOUTH)
        'F' -> listOf(Opening.WEST, Opening.SOUTH)
        else -> emptyList()
    }
}

fun getNextPipe(start: Pipe, direction: Opening, allPipes: List<List<Pipe>>): Pipe? {
    val (x, y) = start.position
    return when (direction) {
        Opening.EAST -> allPipes.flatten().firstOrNull { it.position.first == x - 1 && it.position.second == y }
        Opening.WEST -> allPipes.flatten().firstOrNull { it.position.first == x + 1 && it.position.second == y }
        Opening.NORTH -> allPipes.flatten().firstOrNull { it.position.first == x && it.position.second == y - 1 }
        Opening.SOUTH -> allPipes.flatten().firstOrNull { it.position.first == x && it.position.second == y + 1 }
    }
}

fun getNextDirection(start: Opening, openings: List<Opening>): Opening {
    val banned = getOppositeDirection(start)
    return openings.first { it != banned }
}

private fun getOppositeDirection(start: Opening): Opening {
    return when (start) {
        Opening.EAST -> Opening.WEST
        Opening.WEST -> Opening.EAST
        Opening.NORTH -> Opening.SOUTH
        Opening.SOUTH -> Opening.NORTH
    }
}