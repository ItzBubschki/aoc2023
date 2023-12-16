package com.itzbubschki.aoc2023.day16

import Direction
import DirectionToPositionMap
import get2d
import get2dOptional
import inputToClass
import plus
import println
import readInput

data class Tile(
    val position: Pair<Int, Int>,
    val sign: Char,
    var energized: Boolean = false,
    val previousDirections: MutableList<Direction> = mutableListOf()
)

data class Beam(
    var position: Pair<Int, Int>, var direction: Direction, var canMove: Boolean = true
)

val input = readInput("Day16")
//    readInput("Day16_test")

fun main() {
    val parsed = input.inputToClass { x, y, c -> Tile(x to y, c) }
    part1(parsed).println()
    part2(parsed).println()
}

fun part1(map: List<List<Tile>>): Int {
    return moveBeams(map, Beam(0 to 0, Direction.EAST))
}

fun part2(map: List<List<Tile>>): Int {
    val lastIndex = map.size - 1
    return map.indices.maxOf {
        listOf(
            moveBeams(map, Beam(it to 0, Direction.SOUTH)),
            moveBeams(map, Beam(it to lastIndex, Direction.NORTH)),
            moveBeams(map, Beam(0 to it, Direction.EAST)),
            moveBeams(map, Beam(lastIndex to it, Direction.WEST))
        ).max()
    }
}

fun moveBeams(map: List<List<Tile>>, startingBeam: Beam): Int {
    map.flatten().forEach {
        it.energized = false
        it.previousDirections.clear()
    }
    val beams = mutableListOf(startingBeam)
    map.get2d(startingBeam.position).energized = true
    while (beams.any { it.canMove }) {
        beams.filter { it.canMove }.forEach {
            val tile = map.get2dOptional(it.position)
            when {
                tile == null -> it.canMove = false
                tile.sign == '.' -> moveBeam(it, map)
                tile.sign in "/\\" -> moveAtMirror(tile.sign == '/', it, map)
                tile.sign in "|-" -> potentiallySplitBeam(it, tile.sign == '-', beams, map)
            }
        }
    }
    return map.flatten().count { it.energized }
}

fun moveAtMirror(forwardMirror: Boolean, beam: Beam, map: List<List<Tile>>) {
    beam.direction = when (beam.direction) {
        Direction.NORTH -> if (forwardMirror) Direction.EAST else Direction.WEST
        Direction.EAST -> if (forwardMirror) Direction.NORTH else Direction.SOUTH
        Direction.WEST -> if (forwardMirror) Direction.SOUTH else Direction.NORTH
        Direction.SOUTH -> if (forwardMirror) Direction.WEST else Direction.EAST
    }
    moveBeam(beam, map)
}

fun potentiallySplitBeam(beam: Beam, horizontalLine: Boolean, beams: MutableList<Beam>, map: List<List<Tile>>) {
    when (beam.direction) {
        Direction.EAST, Direction.WEST -> if (horizontalLine) moveBeam(beam, map)
        else {
            beam.direction = Direction.NORTH
            beams.add(Beam(beam.position, Direction.SOUTH))
        }

        Direction.SOUTH, Direction.NORTH -> if (horizontalLine) {
            beam.direction = Direction.EAST
            beams.add(Beam(beam.position, Direction.WEST))
        } else moveBeam(beam, map)
    }
}

fun moveBeam(beam: Beam, tiles: List<List<Tile>>) {
    val tile = tiles.get2dOptional(beam.position)
    if (tile == null || tile.previousDirections.contains(beam.direction)) {
        beam.canMove = false
    } else {
        tile.energized = true
        tile.previousDirections.add(beam.direction)
        beam.position += DirectionToPositionMap[beam.direction]!!
    }
}