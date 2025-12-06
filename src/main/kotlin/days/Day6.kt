package days

import utils.AdventDay
import kotlin.math.max

object Day6 : AdventDay {
    override fun part1(input: List<String>) {
        val numbersPerIndex = mutableMapOf<Int, MutableList<Long>>()
        val regex = """\s+""".toRegex()
        var output = 0L
        val instructions = mutableListOf<String>()
        input.forEachIndexed { y, l ->
            l.trim().split(regex).forEachIndexed { x, c ->
                if (y != input.size - 1) {
                    numbersPerIndex.getOrPut(x) { mutableListOf() }.add(c.toLong())
                } else {
                    instructions.add(c)
                }
            }
        }
        instructions.forEachIndexed { index, s ->
            output += when (s) {
                "*" -> numbersPerIndex.getValue(index).fold(1L) { prev, current -> prev * current }
                "+" -> numbersPerIndex.getValue(index).sum()
                else -> error("Shouldnt happen")
            }
        }
        print(output)
    }

    override fun part2(input: List<String>) {
        val maxLineLength = input.maxOf { it.length } - 1

        var output = 0L
        val numbers = input.dropLast(1)
        val instructions = input.takeLast(1).first()

        fun buildFromColumn(index: Int): Long? {
            val sb = StringBuilder()
            numbers.forEach { n ->
                n.getOrNull(index)?.takeIf { !it.isWhitespace() }?.let(sb::append)
            }
            return sb.toString().takeIf { it.isNotEmpty() }?.toLong()
        }

        fun calculateInstruction(instruction: Char, items: List<Long>): Long {
            return when (instruction) {
                '*' -> items.fold(1L) { prev, current -> prev * current }
                '+' -> items.sum()
                else -> 0L
            }
        }

        val op = mutableListOf<Long>()
        for (i in maxLineLength downTo 0) {
            buildFromColumn(i)?.let(op::add)
            instructions.getOrNull(i)?.takeIf { !it.isWhitespace() }?.let { s ->
                output += calculateInstruction(s, op)
                op.clear()
            }
        }
        println(output)
    }

    private fun morningPart2(input: List<String>) {
        val numbersPerIndex = mutableMapOf<Int, MutableList<String>>()
        var output = 0L

        val instructions = mutableListOf<Char>()
        val numberLengths = mutableListOf<Int>()

        val lines = input.dropLast(1)
        var maxLineLength = 0

        fun handleInstructionsLine(line: String) {
            maxLineLength = max(maxLineLength, line.length)
            var prev = 0
            line.forEachIndexed { index, c ->
                if (c == '+' || c == '*') {
                    instructions.add(c)
                    (index - prev - 1).takeIf { it > 0 }?.let(numberLengths::add)
                    prev = index
                }
            }
            numberLengths.add(maxLineLength - prev)
        }
        handleInstructionsLine(input.last())

        lines.forEach { line ->
            var prevIdx = 0
            for (i in 0 until instructions.size) {
                val numberLength = numberLengths[i]
                val tmp = if (line.length < prevIdx + numberLength) {
                    line.drop(prevIdx)
                } else {
                    line.substring(prevIdx, prevIdx + numberLength).also {
                        prevIdx += numberLength + 1
                    }
                }
                val filledNumber = tmp.replace(" ", ".").padEnd(numberLength, '.')
                numbersPerIndex.getOrPut(i) { mutableListOf() }.add(filledNumber)
            }
        }

        instructions.forEachIndexed { index, s ->
            val numberLength = numberLengths[index]
            val numbers = numbersPerIndex.getValue(index)

            val outputNumbers = mutableListOf<Long>()
            for (i in 0 until numberLength) {
                val sb = StringBuilder()
                numbers.forEach { number ->
                    number[i].takeIf { it != '.' }?.let(sb::append)
                }
                outputNumbers.add(sb.toString().toLong())
                sb.clear()
            }

            output += when (s) {
                '*' -> outputNumbers.fold(1L) { prev, current -> prev * current }
                '+' -> outputNumbers.sum()
                else -> error("Shouldnt happen")
            }
        }
        print(output)
    }
}