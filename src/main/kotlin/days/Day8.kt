package days

import utils.AdventDay
import kotlin.math.pow
import kotlin.math.sqrt

object Day8 : AdventDay {
    data class Point3d(val x: Int, val y: Int, val z: Int) {
        fun euclideanDistanceTo(p2: Point3d): Double {
            return sqrt(
                (x - p2.x).toDouble().pow(2) + (y - p2.y).toDouble().pow(2) + (z - p2.z).toDouble().pow(2)
            )
        }
    }
    data class Edge(val p1: Point3d, val p2: Point3d, val distance: Double)

    override fun part1(input: List<String>) {
        val points = input.map { l ->
            val (x, y, z) = l.split(",").map { it.toInt() }
            Point3d(x, y, z)
        }

        val edges = mutableListOf<Edge>()

        for (i in points.indices) {
            val p1 = points[i]
            for (j in i + 1 until points.size) {
                val p2 = points[j]
                edges.add(Edge(p1, p2, p1.euclideanDistanceTo(p2)))
            }
        }

        edges.sortBy { it.distance }


        val graph = mutableMapOf<Point3d, MutableList<Point3d>>()
        edges.take(1000).forEach { (p1, p2, _) ->
            graph.getOrPut(p1) { mutableListOf() }.add(p2)
            graph.getOrPut(p2) { mutableListOf() }.add(p1)
        }

        val visited = mutableSetOf<Point3d>()
        val output = mutableListOf<Set<Point3d>>()
        graph.forEach { (t, _) ->
            val group = mutableSetOf<Point3d>()
            fun dfs(sp: Point3d) {
                val queue = ArrayDeque<Point3d>()
                queue.add(sp)
                group.add(sp)

                while (queue.isNotEmpty()) {
                    val element = queue.removeFirst()
                    if (!visited.contains(element)) {
                        visited.add(element)
                        val anotherElements = graph.getValue(element)
                        group.addAll(anotherElements)
                        anotherElements.forEach { queue.add(it) }
                    }
                }
                output.add(group)
            }
            if (!visited.contains(t)) dfs(t)
        }
        val ans = output.sortedByDescending { it.size }.take(3).fold(1) { prev, current -> prev * current.size }
        print(ans)
    }

    override fun part2(input: List<String>) {
        val points = input.map { l ->
            val (x, y, z) = l.split(",").map { it.toInt() }
            Point3d(x, y, z)
        }

        val edges = mutableListOf<Edge>()

        for (i in points.indices) {
            val p1 = points[i]
            for (j in i + 1 until points.size) {
                val p2 = points[j]
                edges.add(Edge(p1, p2, p1.euclideanDistanceTo(p2)))
            }
        }

        edges.sortBy { it.distance }

        val idx = points.withIndex().associate { it.value to it.index }

        val unionFind = UnionFind(points.size)
        var pointsSize = points.size

        for (e in edges) {
            if(unionFind.union(idx.getValue(e.p1), idx.getValue(e.p2))) {
                pointsSize--

                if(pointsSize == 1) {
                    val output = e.p1.x * e.p2.y.toLong()
                    print(output)
                    break
                }
            }
        }
    }

    private class UnionFind(n: Int) {
        private val p = IntArray(n) { it }
        private val size = IntArray(n) { 1 }

        fun find(x: Int): Int {
            if(p[x] != x) p[x] = find(p[x])
            return p[x]
        }

        fun union(p1: Int, p2: Int): Boolean {
            var a = find(p1)
            var b = find(p2)
            if(a == b) return false

            if(size[a] == size[b]) {
                val t = a
                a = b
                b = t
            }

            p[b] = a
            size[a] += size[b]
            return true
        }
    }
}