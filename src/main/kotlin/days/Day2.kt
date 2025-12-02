package days

import utils.AdventDay

class Day2 : AdventDay {
    override fun part1(input: String) {
        var out = 0L
        input.split(",").forEach { range ->
            val (start, end) = range.split("-").map { it.toLong() }
            for (i in start..end) {
                val numberInString = i.toString().trimStart('0').ifEmpty { "0" }

                if (numberInString.length % 2 != 0) continue

                val s = numberInString.take(numberInString.length / 2)
                val e = numberInString.drop(numberInString.length / 2)

                if (s == e) {
                    out += i
                }
            }
        }
        println(out)
    }

    override fun part2(input: String) {
        var out = 0L
        input.split(",").forEach { range ->
            val (start, end) = range.split("-").map { it.toLong() }
            for (i in start..end) {
                val numberInString = i.toString().trimStart('0').ifEmpty { "0" }

                val divisors = (1..numberInString.length / 2).filter { numberInString.length % it == 0 }
                for (div in divisors) {
                    if (numberInString.chunked(div).toSet().size == 1) {
                        out += i
                        break
                    }
                }
            }
        }
        println(out)
    }
}