package com.itzbubschki.aoc2023.day17

import com.itzbubschki.aoc2023.utils.*

data class PointInDirection(val point: Point, val direction: Direction, val line: Int) {
    fun neighbours(): List<PointInDirection> {
        return buildList {
            if (line < 3) {
                add(PointInDirection(point + direction.pointPositiveDown, direction, line + 1))
            }
            add(PointInDirection(point + direction.right.pointPositiveDown, direction.right, 1))
            add(PointInDirection(point + direction.left.pointPositiveDown, direction.left, 1))
        }
    }

    fun ultraNeighbours(): List<PointInDirection> {
        return buildList {
            if (line < 10) {
                add(PointInDirection(point + direction.pointPositiveDown, direction, line + 1))
            }
            //line == 0 caters for the starting state where we can go any direction
            if (line >= 4 || line == 0) {
                add(PointInDirection(point + direction.right.pointPositiveDown, direction.right, 1))
                add(PointInDirection(point + direction.left.pointPositiveDown, direction.left, 1))
            }
        }
    }
}

val input =
    readInput("Day17")

//    readInput("Day17_test")
fun main() {
    val matrix = input.map { line -> line.map { it.digitToInt() } }
    part1(matrix).println()
    part2(matrix).println()
}

fun part1(matrix: List<List<Int>>): Int {
    val start = PointInDirection(Point(0, 0), Direction.EAST, 0)
    val end = Point(matrix[0].lastIndex, matrix.lastIndex)
    val path = findShortestPathByPredicate(
        start,
        { (p, _) -> p == end },
        { it.neighbours().filterNot { p -> matrix.get2dOptional(p.point) == null } },
        { _, (point) -> matrix[point] })
    return path.getScore()
}

fun part2(matrix: List<List<Int>>): Int {
    val start = PointInDirection(Point(0, 0), Direction.EAST, 0)
    val end = Point(matrix[0].lastIndex, matrix.lastIndex)
    val path = findShortestPathByPredicate(
        start,
        { (p, _, line) -> p == end && line >= 4 },
        { it.ultraNeighbours().filterNot { p -> matrix.get2dOptional(p.point) == null } },
        { _, (point) -> matrix[point] }
    )
    return path.getScore()
}