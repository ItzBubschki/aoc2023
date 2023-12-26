package com.itzbubschki.aoc2023.day23

import com.itzbubschki.aoc2023.utils.*

val input =
    readInput("Day23")
//    readInput("Day23_test")

fun main() {
    val matrix = input.map { it.toCharArray().toList() }
    val start = Point(matrix.first().indexOfFirst { it == '.' }, 0)
    val end = Point(matrix.last().indexOfFirst { it == '.' }, matrix.indices.last())

    val graphA = buildGraph(matrix, start, end) { point, map -> nextPointsA(point, map) }
    longestPathDfs(graphA, start, end).println()
    val graphB = buildGraph(matrix, start, end) { point, map -> nextPointsB(point, map) }
    longestPathDfs(graphB, start, end).println()
}

fun buildGraph(
    map: List<List<Char>>,
    start: Point,
    end: Point,
    getNeighboursFunction: (Point, List<List<Char>>) -> List<Point>
): Map<Point, Map<Point, Int>> {
    val graph = mutableMapOf<Point, MutableMap<Point, Int>>()
    populateIntersection(map, start, end, graph, getNeighboursFunction)
    return graph
}

fun populateIntersection(
    map: List<List<Char>>,
    current: Point,
    mapEnd: Point,
    graph: MutableMap<Point, MutableMap<Point, Int>>,
    getNeighbours: (Point, List<List<Char>>) -> List<Point>
) {
    val currentNeighbours = getNeighbours(current, map)
    val paths = currentNeighbours.mapNotNull {
        searchUntilIntersection(it, current, map, mapEnd, getNeighbours)
    }
    graph.getOrPut(current) { mutableMapOf() }.putAll(paths.toMap())

    paths.forEach { (point, _) ->
        if (point !in graph) {
            populateIntersection(map, point, mapEnd, graph, getNeighbours)
        }
    }
}

fun nextPointsA(point: Point, map: List<List<Char>>): List<Point> {
    return if (point.second == -1) {
        listOf(point + Point(0, 1))
    } else when (map[point]) {
        '.' -> point.neighbours()
        '<' -> listOf(Point(point.first - 1, point.second))
        '>' -> listOf(Point(point.first + 1, point.second))
        '^' -> listOf(Point(point.first, point.second - 1))
        'v' -> listOf(Point(point.first, point.second + 1))
        else -> {
            println("Unexpected path char ${map[point]}")
            emptyList()
        }
    }.filter { it in map && map[it] != '#' }
}

fun nextPointsB(point: Point, map: List<List<Char>>): List<Point> {
    return if (point.second == -1) {
        listOf(point + Point(0, 1))
    } else {
        point.neighbours()
    }.filter { it in map && map[it] != '#' }
}

fun searchUntilIntersection(
    first: Point,
    previous: Point,
    map: List<List<Char>>,
    mapEnd: Point,
    getNeighbours: (Point, List<List<Char>>) -> List<Point>
): Pair<Point, Int>? {
    val currentPath = mutableSetOf(previous)
    var current = first
    var neighbours = getNeighbours(current, map).filter { it !in currentPath }
    while (neighbours.size == 1) {
        currentPath.add(current)
        current = neighbours.first()
        neighbours = getNeighbours(current, map).filter { it !in currentPath }
    }

    return if (neighbours.isNotEmpty() || current == mapEnd) {
        current to currentPath.size
    } else {
        null
    }
}

fun longestPathDfs(
    graph: Map<Point, Map<Point, Int>>,
    current: Point,
    end: Point,
    seen: MutableMap<Point, Boolean> = graph.mapValuesTo(mutableMapOf()) { false }
): Int {
    seen[current] = true
    val answer = if (current == end) {
        0
    } else {
        val max = graph[current]!!.maxOfOrNull { (point, cost) ->
            if (!seen.getValue(point)) {
                longestPathDfs(graph, point, end, seen) + cost
            } else {
                Int.MIN_VALUE
            }
        } ?: Int.MIN_VALUE
        max
    }
    seen[current] = false
    return answer
}