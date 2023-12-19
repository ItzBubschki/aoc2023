package com.itzbubschki.aoc2023.day19

import com.itzbubschki.aoc2023.utils.getNotNull
import com.itzbubschki.aoc2023.utils.println
import com.itzbubschki.aoc2023.utils.readInput
import kotlin.math.max
import kotlin.math.min

data class Node(val condition: String, val accepted: Node?, val rejected: Node?)
typealias Range = Pair<Long, Long>

val accepted = Node("A", null, null)
val rejected = Node("R", null, null)

val input =
    readInput("Day19")
//    readInput("Day19_test")

fun main() {
    val parts = input.joinToString("\n").split("\n\n")
    part1(parts.first().split("\n"), parts.last().split("\n")).println()
    part2(parts.first().split("\n")).println()
}

fun part1(rules: List<String>, parts: List<String>): Int {
    val parsedRules = rules.associate { it.substringBefore("{") to it.substringAfter("{").dropLast(1).split(",") }
    return parts.filter { checkPart(parsedRules, it) }
        .fold(0) { acc, part -> acc + Regex("[0-9]+").findAll(part).sumOf { it.value.toInt() } }
}

fun part2(rules: List<String>): Long {
    val parsedRules = rules.associate { it.substringBefore("{") to it.substringAfter("{").dropLast(1).split(",") }
    val firstRule = parsedRules.getNotNull("in")
    val firstResult = firstRule.first().substringAfter(":")
    val root = Node(
        firstRule.first(),
        buildTree(parsedRules.getNotNull(firstResult), parsedRules),
        buildTree(firstRule.drop(1), parsedRules)
    )
    return walkTree(root)
}

fun buildTree(conditions: List<String>, rules: Map<String, List<String>>): Node {
    return if (conditions.first().contains(":")) {
        Node(
            conditions.first(),
            getNext(conditions.first().substringAfter(":"), rules),
            buildTree(conditions.drop(1), rules)
        )
    } else {
        getNext(conditions.first(), rules)
    }
}

fun walkTree(
    root: Node,
    x: Range = 0L to 4000,
    m: Range = 0L to 4000,
    a: Range = 0L to 4000,
    s: Range = 0L to 4000
): Long {
    if (root == rejected) return 0
    if (root == accepted) return (x.second - x.first).orZero() * (m.second - m.first).orZero() * (a.second - a.first).orZero() * (s.second - s.first).orZero()
    val value = Regex("[0-9]+").find(root.condition)!!.value.toLong()
    val sign = root.condition[1]
    return when (root.condition.first()) {
        'x' -> walkTree(root.accepted!!, adaptRange(x, value, sign), m, a, s) +
                walkTree(root.rejected!!, adaptRange(x, value, sign, true), m, a, s)

        'm' -> walkTree(root.accepted!!, x, adaptRange(m, value, sign), a, s) +
                walkTree(root.rejected!!, x, adaptRange(m, value, sign, true), a, s)

        'a' -> walkTree(root.accepted!!, x, m, adaptRange(a, value, sign), s) +
                walkTree(root.rejected!!, x, m, adaptRange(a, value, sign, true), s)

        else -> walkTree(root.accepted!!, x, m, a, adaptRange(s, value, sign)) +
                walkTree(root.rejected!!, x, m, a, adaptRange(s, value, sign, true))
    }
}

fun getNext(next: String, rules: Map<String, List<String>>): Node {
    return when (next) {
        "A" -> accepted
        "R" -> rejected
        else -> buildTree(rules.getNotNull(next), rules)
    }
}

fun adaptRange(range: Range, value: Long, sign: Char, rejected: Boolean = false): Range {
    return when (sign) {
        '<' -> {
            if (rejected) max(range.first, value - 1) to range.second
            else range.first to min(range.second, value - 1)
        }

        else -> {
            if (rejected) range.first to min(range.second, value)
            else max(range.first, value) to range.second
        }
    }

}

fun checkPart(parsedRules: Map<String, List<String>>, part: String): Boolean {
    val parsedPart = part.drop(1).dropLast(1).split(",")
        .associate { it.substringBefore("=").first() to it.substringAfter("=").toInt() }
    var current = parsedRules.getNotNull("in")
    while (true) {
        for (it in current) {
            if (it == "A" || it == "R") return it == "A"
            if (it.contains(":")) {
                val partSize = parsedPart.getNotNull(it.first())
                val match = Regex("[0-9]+").find(it)!!.value.toInt()
                if (it[1] == '>' && partSize > match || it[1] == '<' && partSize < match) {
                    val nextRule = it.substringAfter(":")
                    if (nextRule == "A" || nextRule == "R") return nextRule == "A"
                    current = parsedRules.getNotNull(nextRule)
                    break
                }
            } else {
                current = parsedRules.getNotNull(it)
                break
            }
        }
    }
}

fun Long.orZero(): Long {
    return this.takeIf { it > 0 } ?: 0
}