package days

import utils.AdventDay
import utils.numbersRegex
import kotlin.math.abs

class Day1 : AdventDay {
    override fun part1(input: List<String>) {
        print(sol(input, false))
    }

    override fun part2(input: List<String>) {
        print(sol(input, true))
    }

    private fun sol(input: List<String>, countAllRotations: Boolean): Int {
        var initialRotation = 50

        return input.sumOf { line ->
            val rotation = line.first()
            val number = numbersRegex.find(line)?.value?.toInt() ?: 0
            val fullRotations = number / 100
            val offset = number % 100

            var initialCount = if (countAllRotations) fullRotations else 0

            fun wrapLeft(value: Int): Int = if (value < 0) 99 - (abs(value) - 1) else value
            fun wrapRight(value: Int): Int {
                return when {
                    value == 100 -> 0
                    value > 100 -> value - 100
                    else -> value
                }
            }

            when (rotation) {
                'L' -> {
                    if (offset > initialRotation && initialRotation != 0 && countAllRotations) initialCount++

                    initialRotation = wrapLeft(initialRotation - offset)

                    if (initialRotation == 0) initialCount++
                }

                'R' -> {
                    if (offset + initialRotation > 100 && countAllRotations) initialCount++

                    initialRotation = wrapRight(initialRotation + offset)

                    if (initialRotation == 0) initialCount++
                }
            }
            initialCount
        }
    }
}