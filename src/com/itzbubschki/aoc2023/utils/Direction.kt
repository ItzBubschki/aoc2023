package com.itzbubschki.aoc2023.utils

val DirectionToPositionMap = mapOf(
    Direction.NORTH to (0 to -1),
    Direction.EAST to (1 to 0),
    Direction.SOUTH to (0 to 1),
    Direction.WEST to (-1 to 0)
)

enum class Direction(val char: Char, val sign: Char) {
    EAST('R', '>') {
        override val left get() = NORTH
        override val right get() = SOUTH
        override val opposite get() = WEST
    },
    SOUTH('D', 'v') {
        override val left get() = EAST
        override val right get() = WEST
        override val opposite get() = NORTH
    },
    WEST('L', '<') {
        override val left get() = SOUTH
        override val right get() = NORTH
        override val opposite get() = EAST
    },
    NORTH('U', '^') {
        override val left get() = WEST
        override val right get() = EAST
        override val opposite get() = SOUTH
    };

    abstract val left: Direction
    abstract val right: Direction
    abstract val opposite: Direction

    val pointPositiveDown: Point
        get() = DirectionPointMapping.downPositive[this]

    val pointPositiveUp: Point
        get() = DirectionPointMapping.upPositive[this]

    companion object {
        private val map = entries.associateBy(Direction::char)

        fun fromChar(char: Char): Direction {
            return map[char] ?: throw IllegalArgumentException("Unknown direction char: $char")
        }
    }
}

class DirectionPointMapping(positiveY: Direction, positiveX: Direction) {

    val up = if (positiveY == Direction.NORTH) Point(0, 1) else Point(0, -1)
    val down = Point(0, -up.second)
    val left = if (positiveX == Direction.WEST) Point(1, 0) else Point(-1, 0)
    val right = Point(-left.first, 0)

    operator fun get(direction: Direction): Point {
        return when (direction) {
            Direction.NORTH -> up
            Direction.SOUTH -> down
            Direction.WEST -> left
            Direction.EAST -> right
        }
    }

    companion object {
        val downPositive = DirectionPointMapping(Direction.SOUTH, Direction.EAST)
        val upPositive = DirectionPointMapping(Direction.NORTH, Direction.EAST)
    }
}