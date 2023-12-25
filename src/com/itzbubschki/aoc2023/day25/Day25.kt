package com.itzbubschki.aoc2023.day25

import com.itzbubschki.aoc2023.utils.println
import com.itzbubschki.aoc2023.utils.readInput
import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultUndirectedGraph

val input =
    readInput("Day25")
//    readInput("Day25_test")
fun main() {
    part1().println()
}

fun part1(): Int {
    val graph = DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
    input.map { it.split(": ") }.forEach {
        val nodes = it.last().split(" ")
        val root = it.first()
        graph.addVertex(root)
        for (i in nodes) {
            graph.addVertex(i)
            graph.addEdge(root, i)
        }
    }

    val minCut = StoerWagnerMinimumCut(graph).minCut()
    graph.removeAllVertices(minCut)
    return graph.vertexSet().size * minCut.size
}