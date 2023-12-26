package com.itzbubschki.aoc2023.utils

import kotlin.math.abs

typealias Point = Pair<Int, Int>

operator fun Point.plus(other: Point): Point {
    return Pair(this.first + other.first, this.second + other.second)
}

fun <A> List<List<A>>.get2d(position: Point): A {
    return this[position.second][position.first]
}

fun <A> List<List<A>>.get2dOptional(position: Point): A? {
    return this.getOrNull(position.second)?.getOrNull(position.first)
}

fun Point.calculateDistance(other: Point): Int {
    return abs(this.first - other.first) + abs(this.second - other.second)
}

fun Point.addWithWrap(other: Point, size: Pair<Int, Int>): Point {
    val unwrapped = this + other
    return Math.floorMod(unwrapped.first, size.first) to Math.floorMod(unwrapped.second, size.second)
}

fun Point.neighbours(): List<Point> {
    return Direction.entries.map {
        DirectionToPositionMap.getNotNull(it) + this
    }
}

operator fun <E> List<List<E>>.contains(point: Point): Boolean =
    this.isNotEmpty() && point.second in this.indices && point.first in this.first().indices