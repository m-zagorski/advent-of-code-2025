package days

import utils.AdventDay
import utils.numbersRegex

object Day12 : AdventDay {

    override fun part1(input: String) {
        val split = input.split("\n\n")
        val presentSizes = IntArray(split.dropLast(1).size) { 0 }
        split.dropLast(1).forEachIndexed { index, s ->
            presentSizes[index] = s.split("\n").drop(1).sumOf { line ->
                line.count { it == '#' }
            }
        }
        val ans = split.last().split("\n").count { line ->
            val (width, height) = numbersRegex.findAll(line.substringBefore(":")).toList().map { it.value.toInt() }
            val requiredSizeForPresents = line.substringAfter(":").trim().split(" ")
                .mapIndexed { index, count -> presentSizes[index] * count.toInt() }.sum()
            width * height >= requiredSizeForPresents
        }
        print(ans)
    }

    override fun part2(input: String) {
        print("\uD83C")
    }
}