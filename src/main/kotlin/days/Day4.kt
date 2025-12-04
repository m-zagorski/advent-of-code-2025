package days

import utils.AdventDay
import utils.Position
import utils.WorldDirection
import utils.inRange

class Day4 : AdventDay {
    override fun part1(input: List<String>) {
        val matrix = Array(input.size) { Array(input.first().length) { '.' } }
        val rollsOfPaper = mutableListOf<Position>()
        input.forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                matrix[y][x] = c
                if (c == '@') rollsOfPaper.add(Position(x, y))
            }
        }
        val directions = WorldDirection.WITH_DIAGONAL
        var totalCount = 0
        rollsOfPaper.forEach { position ->
            var count = 0
            for (direction in directions) {
                val (ox, oy) = direction.offset()
                val np = position.withOffset(ox, oy)
                if (matrix.inRange(np.x, np.y) && matrix[np.y][np.x] == '@') {
                    count++
                }
            }
            if (count < 4) totalCount++
        }

        print(totalCount)
    }

    override fun part2(input: List<String>) {
        val matrix = Array(input.size) { Array(input.first().length) { '.' } }
        val rollsOfPaper = mutableListOf<Position>()
        input.forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                matrix[y][x] = c
                if (c == '@') rollsOfPaper.add(Position(x, y))
            }
        }
        val directions = WorldDirection.WITH_DIAGONAL
        var totalCount = 0
        while (true) {
            val removedRollsOfPapers = mutableListOf<Position>()
            rollsOfPaper.forEach { position ->
                var count = 0
                for (direction in directions) {
                    val (ox, oy) = direction.offset()
                    val np = position.withOffset(ox, oy)
                    if (matrix.inRange(np.x, np.y) && matrix[np.y][np.x] == '@') {
                        count++
                    }
                }
                if (count < 4) {
                    totalCount++
                    matrix[position.y][position.x] = '.'
                    removedRollsOfPapers.add(position)
                }
            }
            if (removedRollsOfPapers.isEmpty()) {
                break
            }
            removedRollsOfPapers.forEach { rollsOfPaper.remove(it) }
        }
        print(totalCount)
    }
}