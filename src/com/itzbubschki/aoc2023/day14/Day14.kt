package com.itzbubschki.aoc2023.day14

import println
import readInput
import replaceAt
import rotateMatrixCw


private val knownSolutions = hashMapOf<String, String>()

fun main() {
    val input = readInput("Day14")

    part1(input).println()
    part2(input).println()
}

fun part1(input: List<String>): Int {
    return tiltPlatform(input).withIndex()
        .sumOf { (index, line) -> line.count { s -> s == 'O' } * (input.size - index) }
}

fun part2(input: List<String>): Int {
    var result = input
    repeat(4000) {
        result = rotateMatrixCw(tiltPlatform(result))
    }
    return result.withIndex()
        .sumOf { (index, line) -> line.count { s -> s == 'O' } * (input.size - index) }
}

fun tiltPlatform(platformData: List<String>): List<String> {
    val newColumns = mutableListOf<String>()
    for (x in platformData.first().indices) {
        val column = platformData.map { it[x] }.joinToString("")
        val newColumn = knownSolutions.getOrPut(column) {
            var updatedColumn = column.replace('O', '.')
            for (y in platformData.indices) {
                if (column[y] == 'O') {
                    val index = getStopInColumn(y, updatedColumn)
                    updatedColumn = updatedColumn.replaceAt(index, 'O')
                }
            }
            updatedColumn
        }
        newColumns.add(newColumn)
    }
    return transpose(newColumns)
}

fun getStopInColumn(start: Int, column: String): Int {
    var lastFree = start
    for (i in start - 1 downTo 0) {
        when (column[i]) {
            '#' -> return i + 1
            '.' -> lastFree = i
            'O' -> return lastFree
        }
    }
    return lastFree
}

fun transpose(columns: List<String>): List<String> {
    val transposed = List(columns[0].length) { col ->
        columns.joinToString("") { row -> row[col].toString() }
    }

    return transposed
}