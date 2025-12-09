package days

import utils.AdventDay
import utils.Position
import kotlin.math.abs
import kotlin.math.max

object Day9 : AdventDay {
    override fun part1(input: List<String>) {
        val positions = input.map { s ->
            val (x, y) = s.split(",").map { it.toInt() }
            Position(x, y)
        }

        var ans = 0L
        for (i in positions.indices) {
            val sp = positions[i]
            for (j in i + 1 until positions.size) {
                val ep = positions[j]

                val nx = if (sp.x > ep.x) sp.x - ep.x + 1 else ep.x - sp.x + 1
                val ny = if (sp.y > ep.y) sp.y - ep.y + 1 else ep.y - sp.y + 1
                val newSize = ny * nx.toLong()
                ans = max(ans, newSize)

            }
        }
        print(ans)
    }

    data class Edge(val sp: Position, val ep: Position)

    override fun part2(input: List<String>) {
        val positions = input.map { s ->
            val (x, y) = s.split(",").map { it.toInt() }
            Position(x, y)
        }
        val edges = positions.zipWithNext { a, b -> Edge(a, b) }
            .plus(Edge(positions.last(), positions.first()))

        var ans = 0L
        for (from in positions) {
            for (to in positions) {
                val area = (abs(to.x - from.x) + 1L) * (abs(to.y - from.y) + 1L)
                if (area <= ans) continue

                if (!rectangleIntersectsPolygon(from, to, edges)) {
                    ans = maxOf(ans, area)
                }
            }
        }
        print(ans)
    }

    private fun rectangleIntersectsPolygon(x1: Position, x2: Position, edges: List<Edge>): Boolean {
        val minX = minOf(x1.x, x2.x)
        val maxX = maxOf(x1.x, x2.x)
        val minY = minOf(x1.y, x2.y)
        val maxY = maxOf(x1.y, x2.y)

        for ((sp, ep) in edges) {
            val eMinX = minOf(sp.x, ep.x)
            val eMaxX = maxOf(sp.x, ep.x)
            val eMinY = minOf(sp.y, ep.y)
            val eMaxY = maxOf(sp.y, ep.y)

            if (minX < eMaxX && maxX > eMinX && minY < eMaxY && maxY > eMinY) {
                return true
            }
        }
        return false
    }

    /**
     * This approach is slightly hacky. The points we use originate from the way
     * the shape was constructed: the original polygon was split into two smaller
     * shapes (top and bottom) by a hole. Because of this, the largest area must
     * start from the point where the hole begins or ends.
     */
    private fun morning_part2(input: List<String>) {
        val positions = input.map { s ->
            val (x, y) = s.split(",").map { it.toInt() }
            Position(x, y)
        }
        val edges = positions.zipWithNext { a, b -> Edge(a, b) }
            .plus(Edge(positions.last(), positions.first()))

        fun checkIfPointsInPolygon(x1: Position, x2: Position, x3: Position, x4: Position): Boolean {
            return x1.isInsidePolygon(edges) && x2.isInsidePolygon(edges) && x3.isInsidePolygon(edges) && x4.isInsidePolygon(
                edges
            )
        }

        var ans = 0L
        fun doAllTheCalculations(sp: Position, ep: Position, check: (Position, Position) -> Boolean): Long {
            if (sp == ep) return 0L

            if (check(sp, ep)) {
                val nx = if (sp.x > ep.x) sp.x - ep.x + 1 else ep.x - sp.x + 1
                val ny = if (sp.y > ep.y) sp.y - ep.y + 1 else ep.y - sp.y + 1
                val newSize = ny * nx.toLong()
                if (newSize <= ans) return 0L

                val leftPoint = if (sp.x < ep.x) sp else ep
                val rightPoint = if (sp.x < ep.x) ep else sp

                if (leftPoint.y < rightPoint.y) {
                    val bottomLeft = Position(leftPoint.x, rightPoint.y)
                    val topRight = Position(rightPoint.x, leftPoint.y)
                    val proper = checkIfPointsInPolygon(leftPoint, bottomLeft, topRight, rightPoint)
                    if (proper) {
                        return newSize
                    }
                } else {
                    val topLeft = Position(leftPoint.x, rightPoint.y)
                    val bottomRight = Position(rightPoint.x, leftPoint.y)
                    val proper = checkIfPointsInPolygon(topLeft, leftPoint, rightPoint, bottomRight)
                    if (proper) {
                        return newSize
                    }
                }
            }
            return 0L
        }

        val startPositionTop = Position(94876, 48734)
        val startPositionBottom = Position(94876, 50058)
        for (element in positions) {
            ans = max(ans, doAllTheCalculations(startPositionTop, element, { p1, p2 -> p2.y <= p1.y }))
            ans = max(ans, doAllTheCalculations(startPositionBottom, element, { p1, p2 -> p2.y >= p1.y }))

        }
        println(ans)
    }

    private fun Position.isInsidePolygon(edges: List<Edge>): Boolean {
        var isInside = false

        for ((sp, ep) in edges) {

            if (x == sp.x && y == sp.y || x == ep.x && y == ep.y) return true

            val cross = (x - sp.x) * (ep.y - sp.y) - (y - sp.y) * (ep.x - sp.x)
            if (cross == 0 &&
                x >= minOf(sp.x, ep.x) && x <= maxOf(sp.x, ep.x) &&
                y >= minOf(sp.y, ep.y) && y <= maxOf(sp.y, ep.y)
            ) return true

            val intersects = ((sp.y > y) != (ep.y > y)) &&
                    (x < (ep.x - sp.x) * (y - sp.y) / (ep.y - sp.y) + sp.x)

            if (intersects) isInside = !isInside
        }

        return isInside
    }
}