package com.itzbubschki.aoc2023.day24

import com.itzbubschki.aoc2023.utils.*
import kotlin.math.sign
import kotlin.math.round
import kotlin.properties.Delegates

val input = readInput("Day24")

lateinit var stones: List<Hailstone>
val lowerBound = 200000000000000.0
val upperBound = 400000000000000.0
var minX by Delegates.notNull<Double>()
var minY by Delegates.notNull<Double>()
var maxX by Delegates.notNull<Double>()
var maxY by Delegates.notNull<Double>()
fun main() {
    stones = input.map { Hailstone.of(it) }
    minX = stones.minOf { it.x }
    minY = stones.minOf { it.y }
    maxX = stones.maxOf { it.x }
    maxY = stones.maxOf { it.y }

    part1(testArea = false).println()
    part2().println()
}

fun part1(testArea: Boolean = true): Int =
    solve(testArea)

fun part2(): Long {
    val a = input.map { s -> s.split("@").map { vs -> vs.split(",").map { it.trim().toLong() } } }
    val tm = (0..2).firstNotNullOf { k ->
        a.indices.firstNotNullOfOrNull { i ->
            a.indices.map { j ->
                if (a[j][1][k] == a[i][1][k]) (if (a[j][0][k] == a[i][0][k]) 0L else -1L) else {
                    val tn = a[j][0][k] - a[i][0][k]
                    val td = a[i][1][k] - a[j][1][k]
                    if (tn % td == 0L) tn / td else -1L
                }
            }.takeIf { tm -> tm.all { it >= 0 } }
        }
    }
    val (i, j) = tm.withIndex().filter { it.value > 0 }.map { it.index }
    fun p(i: Int, k: Int, t: Long) = a[i][0][k] + a[i][1][k] * t
    return (0..2).sumOf { k -> p(i, k, tm[i]) - (p(i, k, tm[i]) - p(j, k, tm[j])) / (tm[i] - tm[j]) * tm[i] }
}

private fun inArea(p: Point?, testArea: Boolean) =
    if (testArea) inXY(p) else inBounds(p)

private fun inBounds(p: Point?): Boolean {
    if (p == null) return false
    val pxr = round(p.x)
    val pyr = round(p.y)
    return (pxr >= lowerBound) && pxr <= upperBound && pyr >= lowerBound && pyr <= upperBound
}

private fun inXY(p: Point?): Boolean {
    if (p == null) return false
    val pxr = round(p.x)
    val pyr = round(p.y)
    return (pxr >= minX) && pxr <= maxX && pyr >= minY && pyr <= maxY
}

fun solve(testArea: Boolean = true): Int {
    val x = stones.flatMapIndexed { idx, a ->
        stones.drop(idx + 1).map { b ->
            findIntersectionXY(a, b)
        }
    }.filter { it.third != null }
    val sol = x.filter { inArea(it.third, testArea) }
    return sol.size
}

fun findIntersectionXY(stoneA: Hailstone, stoneB: Hailstone): Triple<Hailstone, Hailstone, Point?> {
    // Berechnung der Steigungen
    val ma = stoneA.vy / stoneA.vx
    val mb = stoneB.vy / stoneB.vx

    // Berechnung der y-Achsenabschnitte
    val ba = stoneA.y - ma * stoneA.x
    val bb = stoneB.y - mb * stoneB.x

    // Überprüfen, ob die Geraden parallel sind (gleiche Steigung)
    if (ma == mb) {
        return Triple(stoneA, stoneB, null) // Kein Schnittpunkt, da parallel oder identisch
    }

    // Berechnung des Schnittpunkts
    val x = (bb - ba) / (ma - mb)
    val y = ma * x + ba

    val sAx = sign(x - stoneA.x) == sign(stoneA.vx)
    val sAy = sign(y - stoneA.y) == sign(stoneA.vy)
    val sBx = sign(x - stoneB.x) == sign(stoneB.vx)
    val sBy = sign(y - stoneB.y) == sign(stoneB.vy)
    if (!(sAx && sAy && sBx && sBy)) {
        return Triple(stoneA, stoneB, null) // Treffen in der Vergangenheit
    }

    return Triple(stoneA, stoneB, Point(x, y))
}

data class Point(val x: Double, val y: Double)

data class Hailstone(val x: Double, val y: Double, val z: Double, val vx: Double, val vy: Double, val vz: Double) {
    companion object {
        fun of(input: String): Hailstone {
            val (coord, velo) = input.split(" @ ")
            val (x, y, z) = coord.split(',').map(String::trim).map(String::toDouble)
            val (vx, vy, vz) = velo.split(',').map(String::trim).map(String::toDouble)
            return Hailstone(x, y, z, vx, vy, vz)
        }
    }
}