package rocks.gruenewald.aoc2023

import rocks.gruenewald.println
import rocks.gruenewald.readInput
import rocks.gruenewald.verifyTestResult

fun main() {

    fun part1(lines: List<String>): Any {
        TODO("To be implemented.")
    }

    fun part2(lines: List<String>): Any {
        TODO("To be implemented")
    }

    val part1TestInput = """        
    """.trimIndent().split("\n")
    verifyTestResult(part1(part1TestInput), Any())

    val inputOfTheDay = readInput("day23")
    part1(inputOfTheDay).println()

    val part2TestInput = """
    """.trimIndent().split("\n")
    verifyTestResult(part2(part2TestInput), Any())
    part2(inputOfTheDay).println()
}

