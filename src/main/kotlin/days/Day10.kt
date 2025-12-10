package days

import utils.AdventDay
import java.util.ArrayDeque
import kotlin.math.min

object Day10 : AdventDay {
    data class Machine(
        val targetIndices: List<Int>,
        val instructions: List<List<Int>>
    )

    override fun part1(input: List<String>) {
        val machines = input.map { line ->
            val split = line.split(" ").dropLast(1)
            val targetIndices = split.first().replace("[", "").replace("]", "")
                .mapIndexed { index, c -> index to c }.filter { (_, c) -> c == '#' }.map { it.first }
            val instructions = split
                .drop(1)
                .map { ins ->
                    ins.replace("(", "")
                        .replace(")", "")
                        .split(",")
                        .map { it.toInt() }
                }
            Machine(
                targetIndices = targetIndices,
                instructions = instructions,
            )
        }

        data class StateWithCount(
            val state: Set<Int>,
            val count: Int
        )

        fun canReach(instructions: List<List<Int>>, target: Set<Int>): Int {
            val queue = ArrayDeque<StateWithCount>()
            val visited = mutableSetOf<Set<Int>>()
            queue.add(StateWithCount(emptySet(), 1))

            var min = Int.MAX_VALUE

            while (queue.isNotEmpty()) {
                val (current, count) = queue.removeFirst()

                for (ins in instructions) {
                    val newState = current.toggle(ins).sorted().toSet()
                    if (target == newState) {
                        min = min(min, count)
                    } else {
                        if (!visited.contains(newState)) {
                            queue.add(StateWithCount(newState, count + 1))
                            visited.add(newState)
                        }
                    }
                }

            }

            return min
        }


        val ans = machines.sumOf { machine ->
            canReach(machine.instructions, machine.targetIndices.toSet())
        }
        print(ans)
    }

    private fun Set<Int>.toggle(ins: List<Int>): Set<Int> {
        val next = toMutableSet()
        for (x in ins) {
            if (next.contains(x)) next.remove(x) else next.add(x)
        }
        return next
    }


    override fun part2(input: List<String>) {
    }
}