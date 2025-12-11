package days

import utils.AdventDay
import kotlin.math.max

object Day11 : AdventDay {
    override fun part1(input: List<String>) {
        val graph = mutableMapOf<String, MutableList<String>>()
        input.forEach { line ->
            val start = line.substringBefore(":")
            val end = line.substringAfter(":").trim().split(" ")
            end.forEach { to ->
                graph.getOrPut(start) { mutableListOf() }.add(to)
            }
        }

        var ans = 0

        val queue = ArrayDeque<String>()
        queue.add("you")

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            if (current == "out") {
                ans++
                continue
            }

            for (n in graph[current] ?: emptyList()) {
                queue.add(n)
            }
        }
        print(ans)
    }

    override fun part2(input: List<String>) {
        val graph = mutableMapOf<String, MutableList<String>>()
        val inDegree = mutableMapOf<String, Int>()
        val items = mutableSetOf<String>()

        input.forEach { line ->
            val start = line.substringBefore(":")
            val end = line.substringAfter(":").trim().split(" ")
            end.forEach { to ->
                items.add(start)
                items.add(to)
                graph.getOrPut(start) { mutableListOf() }.add(to)
                inDegree[to] = (inDegree[to] ?: 0) + 1
            }
        }

        val result = mutableListOf<String>()
        val queue = ArrayDeque<String>()
        queue.add("svr")

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            result.add(current)
            for (n in graph[current] ?: emptyList()) {
                inDegree[n] = max(0, (inDegree[n] ?: 0) - 1)
                if (inDegree[n] == 0) queue.add(n)
            }
        }

        data class NodeInformation(
            var none: Long = 0,
            var fftOnly: Long = 0,
            var dacOnly: Long = 0,
            var both: Long = 0
        ) {
            fun merge(other: NodeInformation): NodeInformation {
                none += other.none
                fftOnly += other.fftOnly
                dacOnly += other.dacOnly
                both += other.both
                return this
            }
        }

        val numWays = items.associateWith { NodeInformation() }.toMutableMap()
        numWays["svr"] = NodeInformation(none = 1)

        for (node in result) {
            for (neighbour in graph[node] ?: emptyList()) {
                val currentNode = numWays.getValue(node)
                val next = when (neighbour) {
                    "fft" -> NodeInformation(fftOnly = currentNode.none, both = currentNode.dacOnly)
                    "dac" -> NodeInformation(dacOnly = currentNode.none, both = currentNode.fftOnly)
                    else -> currentNode
                }


                numWays[neighbour] = numWays.getValue(neighbour).merge(next)
            }
        }
        print(numWays["out"]?.both)
    }
}