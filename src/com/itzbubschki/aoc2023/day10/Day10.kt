package com.itzbubschki.aoc2023.day10

import com.itzbubschki.aoc2023.utils.Direction
import com.itzbubschki.aoc2023.utils.inputToClass
import com.itzbubschki.aoc2023.utils.println
import com.itzbubschki.aoc2023.utils.readInput
import kotlin.math.abs


data class Pipe(
    val position: Pair<Int, Int>, val openings: List<Direction>, val startTile: Boolean, var distance: Int = 0
)

val input = readInput("Day10")
//    readInput("Day10_test")

fun main() {
    val pipes = input.inputToClass { x, y, char -> Pipe(Pair(x, y), getOpeningsForChar(char), char == 'S') }
    val startTile = pipes.flatten().first { it.startTile }
    val tilesInLoop = mutableListOf<Pipe>()
    for (startDirection in Direction.entries) {
        tilesInLoop.clear()
        tilesInLoop.add(startTile)
        var current = getNextPipe(startTile, startDirection, pipes) ?: continue
        var nextDirection = startDirection
        while (!current.startTile) {
            tilesInLoop.add(current)
            if (current.openings.isNotEmpty()) nextDirection = getNextDirection(nextDirection, current.openings)
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
                0, tilesInLoop.lastIndex
            )))
        }
        (loopBorders.indices step 2).sumOf { i ->
            (loopBorders[i]..loopBorders[i + 1]).count { x -> x to y !in positionsInLoop }
        }
    }
}

fun getOpeningsForChar(char: Char): List<Direction> {
    return when (char) {
        '|' -> listOf(Direction.NORTH, Direction.SOUTH)
        '-' -> listOf(Direction.EAST, Direction.WEST)
        'L' -> listOf(Direction.NORTH, Direction.WEST)
        'J' -> listOf(Direction.NORTH, Direction.EAST)
        '7' -> listOf(Direction.EAST, Direction.SOUTH)
        'F' -> listOf(Direction.WEST, Direction.SOUTH)
        else -> emptyList()
    }
}

fun getNextPipe(start: Pipe, direction: Direction, allPipes: List<List<Pipe>>): Pipe? {
    val (x, y) = start.position
    return when (direction) {
        Direction.EAST -> allPipes.flatten().firstOrNull { it.position.first == x - 1 && it.position.second == y }
        Direction.WEST -> allPipes.flatten().firstOrNull { it.position.first == x + 1 && it.position.second == y }
        Direction.NORTH -> allPipes.flatten().firstOrNull { it.position.first == x && it.position.second == y - 1 }
        Direction.SOUTH -> allPipes.flatten().firstOrNull { it.position.first == x && it.position.second == y + 1 }
    }
}

fun getNextDirection(start: Direction, openings: List<Direction>): Direction {
    val banned = start.opposite
    return openings.first { it != banned }
}