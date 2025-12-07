package days

import utils.AdventDay
import utils.Position
import utils.WorldDirection

object Day7 : AdventDay {
    override fun part1(input: List<String>) {
        val matrix = Array(input.size) { Array(input.first().length) { '.' } }
        var sp = mutableSetOf<Position>()

        input.forEachIndexed { y, l ->
            l.forEachIndexed { x, c ->
                matrix[y][x] = c
                if (c == 'S') sp.add(Position(x, y))
            }
        }

        val splitterPositions = mutableSetOf<Position>()

        for (i in 0 until input.size - 1) {
            val tmpPosition = mutableSetOf<Position>()
            sp.forEach { current ->
                val np = current.withOffset(WorldDirection.South.offset())
                if (matrix[np.y][np.x] == '^') {
                    val nlp = np.withOffset(WorldDirection.East.offset())
                    val nrp = np.withOffset(WorldDirection.West.offset())
                    tmpPosition.add(nlp)
                    tmpPosition.add(nrp)
                    splitterPositions.add(np)
                } else {
                    tmpPosition.add(np)
                }
                sp = tmpPosition
            }
        }
        print(splitterPositions.size)
    }

    override fun part2(input: List<String>) {
        val matrix = Array(input.size) { Array(input.first().length) { '.' } }
        var startXPosition = 0

        input.forEachIndexed { y, l ->
            l.forEachIndexed { x, c ->
                if (c == 'S') startXPosition = x
                matrix[y][x] = c
            }
        }

        var output = LongArray(input.first().length) { 0L }
        output[startXPosition] = 1

        fun getNewPositions(current: Position): List<Int> {
            val np = current.withOffset(WorldDirection.South.offset())
            if (matrix[np.y][np.x] == '^') {
                val nlp = np.withOffset(WorldDirection.East.offset())
                val nrp = np.withOffset(WorldDirection.West.offset())
                return listOf(nlp.x, nrp.x)
            } else {
                return listOf(np.x)
            }
        }

        for (i in 1 until input.size - 1) {
            val nc = LongArray(output.size) { 0L }
            output.forEachIndexed { index, x ->
                if (x > 0) {
                    val nextPositions = getNewPositions(Position(index, i))
                    nextPositions.forEach { newX ->
                        nc[newX] += output[index]
                    }
                }
            }
            output = nc
        }
        print(output.sum())
    }
}