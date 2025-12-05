package days

import utils.AdventDay

class Day5 : AdventDay {

    override fun part1(input: String) {
        val (freshIds, ids) = input.split("\n\n")
        val freshRanges: List<LongRange> = freshIds.split("\n").map {
            val (l, r) = it.split("-")
            LongRange(l.toLong(), r.toLong())
        }

        val count = ids.split("\n")
            .count { id -> freshRanges.any { range -> range.contains(id.toLong()) } }
        print(count)
    }

    override fun part2(input: String) {
        val (fresh, _) = input.split("\n\n")
        val freshRanges: List<LongRange> = fresh.split("\n").map {
            val (l, r) = it.split("-")
            LongRange(l.toLong(), r.toLong())
        }.sortedBy { it.first }

        val ranges = mutableListOf<LongRange>()

        freshRanges.forEach { range ->
            if (ranges.isEmpty()) ranges.add(range)

            val lastRange = ranges.last()
            when {
                range.first > lastRange.last -> ranges.add(range)
                range.last > lastRange.last -> ranges.removeLast()
                    .also { ranges.add(LongRange(lastRange.first, range.last)) }
            }
        }

        val sum = ranges.sumOf { it.last - it.first + 1 }
        print(sum)
    }
}