package days

import utils.AdventDay

class Day3 : AdventDay {
    override fun part1(input: List<String>) {
        print(input.slidingWindow(2))
    }

    override fun part2(input: List<String>) {
        print(input.slidingWindow(12))
    }

    private fun List<String>.slidingWindow(windowSize: Int): Long {
        return sumOf { line ->
            val output = CharArray(windowSize)
            var startIndex = 0

            for (i in 0 until windowSize) {
                val endIndex = line.length - (windowSize - i) + 1

                var maxChar = '0'
                var maxPos = 0

                for (j in startIndex until endIndex) {
                    val c = line[j]
                    if (c > maxChar) {
                        maxChar = c
                        maxPos = j
                    }
                }
                output[i] = maxChar
                startIndex = maxPos + 1
            }
            output.concatToString().toLong()
        }
    }

    private fun List<String>.functional(windowSize: Int): Long {
        return sumOf { line ->
            val output = CharArray(windowSize)
            (0 until windowSize).fold(0) { prevIndex, i ->
                val current = line.substring(0, line.length - (windowSize - i) + 1).drop(prevIndex)
                val maxValue = current.max()
                output[i] = maxValue
                prevIndex + current.indexOf(maxValue) + 1
            }
            output.concatToString().toLong()
        }
    }
}
