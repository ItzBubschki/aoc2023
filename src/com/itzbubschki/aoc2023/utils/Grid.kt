package com.itzbubschki.aoc2023.utils

enum class ORIENTATION {
    NORMAL,
    FLIPPED
}

data class TileInstructions(
    val rotate: Direction = Direction.NORTH,
    val orient: ORIENTATION = ORIENTATION.NORMAL
)

data class GridCellContainer(val value: Boolean, val original: Point = Point(0, 0))

class Grid(var gridMap: Map<Point, GridCellContainer>, val offset: Point = Point(0, 0)) {
    val rowMin = if (gridMap.isEmpty()) 0 else gridMap.keys.minOf { it.first }
    val rowMax = if (gridMap.isEmpty()) 0 else gridMap.keys.maxOf { it.first }
    val columnMin = if (gridMap.isEmpty()) 0 else gridMap.keys.minOf { it.second }
    val columnMax = if (gridMap.isEmpty()) 0 else gridMap.keys.maxOf { it.second }

    val columnRangesForRows by lazy {
        val rowRange = gridMap.keys.minOf { it.first }..gridMap.keys.maxOf { it.first }
        rowRange.associateWith { row ->
            gridMap.keys.filter { it.first == row }.minOf { it.second }..gridMap.keys.filter { it.first == row }
                .maxOf { it.second }
        }
    }

    val rowRangesForColumns by lazy {
        val colRange = gridMap.keys.minOf { it.second }..gridMap.keys.maxOf { it.second }
        colRange.associateWith { col ->
            gridMap.keys.filter { it.second == col }.minOf { it.first }..gridMap.keys.filter { it.second == col }
                .maxOf { it.first }
        }
    }

    fun rotate(instructions: TileInstructions): Grid {
        val rowProg = IntProgression.fromClosedRange(rowMin, rowMax, 1)
        val columnProg = IntProgression.fromClosedRange(columnMin, columnMax, 1)
        var newOffset = offset

        val result = mutableMapOf<Point, GridCellContainer>()
        val transFunc: (Int, Int) -> Point
        when (instructions.orient) {
            ORIENTATION.NORMAL -> {
                when (instructions.rotate) {
                    Direction.NORTH -> transFunc = { r, c -> Point(r, c) }

                    Direction.EAST -> {
                        transFunc = { r, c -> Point(c, rowMax - r + offset.first) }
                        newOffset = Point(offset.second, offset.first)
                    }

                    Direction.SOUTH -> {
                        transFunc = { r, c -> Point(rowMax - r + offset.first, columnMax - c + offset.second) }
                    }

                    Direction.WEST -> {
                        transFunc = { r, c -> Point(columnMax - c + offset.second, r) }
                        newOffset = Point(offset.second, offset.first)
                    }
                }

                for (row in rowProg) {
                    for (column in columnProg) {
                        if (gridMap.keys.contains(Point(row, column))) {
                            result[transFunc(row, column)] = gridMap.getValue(Point(row, column))
                        }
                    }
                }
                result.toMap()
            }

            else -> error("Unknown orientation ${instructions.orient}")
        }

        return Grid(result.toMap(), newOffset)
    }

    fun printExisting() =
        println(toPrintableStringExisting())

    fun toPrintableStringExisting(): String =
        buildString {
            for (row in offset.first..rowMax) {
                for (column in offset.second..columnMax) {
                    if (gridMap.containsKey(Point(row, column))) {
                        append(if (gridMap.getValue(Point(row, column)).value) '#' else '.')
                    } else {
                        append(" ")
                    }
                }
                appendLine()
            }
        }.removeSuffix(System.lineSeparator())

    fun printWithDefault(default: Boolean = false) =
        println(toPrintableStringWithDefault(default))

    fun toPrintableStringWithDefault(default: Boolean = false): String =
        buildString {
            for (row in rowMin..rowMax) {
                for (column in columnMin..columnMax) {
                    append(
                        if (gridMap.getOrDefault(
                                Point(row, column),
                                GridCellContainer(default)
                            ).value
                        ) '#' else '.'
                    )
                }
                appendLine()
            }
        }.removeSuffix(System.lineSeparator())

    companion object {
        fun of(
            input: List<String>,
            offset: Point = 0 to 0,
            ignoreChars: List<Char> = listOf(' '),
            relevantChars: List<Char> = listOf('#')
        ): Grid =
            input.mapIndexed { rowIndex, row ->
                row.mapIndexedNotNull { columnIndex, c ->
                    if (ignoreChars.contains(c)) {
                        null
                    } else {
                        val cell = Point(rowIndex + offset.first, columnIndex + offset.second)
                        cell to GridCellContainer(relevantChars.contains(c), cell.copy())
                    }
                }
            }.flatten().toMap().let { Grid(it, offset) }

        fun of(
            input: String,
            offset: Point = 0 to 0,
            ignoreChar: List<Char> = listOf(' '),
            relevantChars: List<Char> = listOf('#')
        ): Grid =
            of(input.lines(), offset, ignoreChar, relevantChars)
    }
}