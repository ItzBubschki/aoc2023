package com.itzbubschki.aoc2023.utils

import kotlin.math.absoluteValue

data class Vector3(val x: Int, val y: Int, var z: Int) {
    operator fun plus(other: Vector3) = Vector3(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vector3) = Vector3(x - other.x, y - other.y, z - other.z)
    operator fun times(other: Vector3) = Vector3(x * other.x, y * other.y, z * other.z)
    operator fun times(other: Int) = Vector3(x * other, y * other, z * other)
    operator fun div(other: Int) = Vector3(x / other, y / other, z / other)
    operator fun div(other: Vector3) = Vector3(x / other.x, y / other.y, z / other.z)
    operator fun rem(other: Int) = Vector3(x % other, y % other, z % other)
    operator fun rem(other: Vector3) = Vector3(x % other.x, y % other.y, z % other.z)
    operator fun unaryMinus() = Vector3(-x, -y, -z)
    operator fun compareTo(other: Vector3) = compareValuesBy(this, other, Vector3::x, Vector3::y, Vector3::z)
    fun abs() = Vector3(x.absoluteValue, y.absoluteValue, z.absoluteValue)
    fun sum() = x + y + z
    fun product() = x * y * z
    fun manhattanDistance(other: Vector3) = (this - other).abs().sum()
    fun neighbours() = listOf(
        this + Vector3(1, 0, 0),
        this + Vector3(-1, 0, 0),
        this + Vector3(0, 1, 0),
        this + Vector3(0, -1, 0),
        this + Vector3(0, 0, 1),
        this + Vector3(0, 0, -1)
    )
}