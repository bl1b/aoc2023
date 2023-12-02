package rocks.gruenewald.aoc2023

import rocks.gruenewald.println
import rocks.gruenewald.readInput
import rocks.gruenewald.verifyTestResult
import kotlin.math.max

fun main() {
    data class Drawing(val amount: Int, val color: String)

    fun extractDrawings(line: String): MutableList<Drawing> {
        val drawings = mutableListOf<Drawing>()
        line.substringAfter(':')
            .split(';')
            .forEach { gameSet ->
                gameSet.trim().split(',')
                    .forEach { drawingAsString ->
                        val drawParts = drawingAsString.trim().split(' ')
                        drawings.add(Drawing(drawParts.first().toInt(), drawParts.last()))
                    }
            }
        return drawings
    }

    fun extractMaxPulls(drawings: MutableList<Drawing>): Triple<Int, Int, Int> {
        var maxPullOnRed = 0
        var maxPullOnGreen = 0
        var maxPullOnBlue = 0
        for (drawing in drawings) {
            when (drawing.color) {
                "red" -> maxPullOnRed = max(maxPullOnRed, drawing.amount)
                "green" -> maxPullOnGreen = max(maxPullOnGreen, drawing.amount)
                "blue" -> maxPullOnBlue = max(maxPullOnBlue, drawing.amount)
            }
        }
        return Triple(maxPullOnRed, maxPullOnGreen, maxPullOnBlue)
    }

    fun part1(lines: List<String>): Number {
        var sumOfPossibleGameIDs = 0
        for (line in lines) {
            val currentGameID = line.substringAfter(' ').substringBefore(':').toInt()
            val drawings = extractDrawings(line)
            val (maxPullOnRed, maxPullOnGreen, maxPullOnBlue) = extractMaxPulls(drawings)
            sumOfPossibleGameIDs += if (maxPullOnRed <= 12 && maxPullOnGreen <= 13 && maxPullOnBlue <= 14) currentGameID else 0
        }
        return sumOfPossibleGameIDs
    }

    fun part2(lines: List<String>): Long {
        var totalPower: Long = 0
        for (line in lines) {
            val drawings = extractDrawings(line)
            val (maxPullOnRed, maxPullOnGreen, maxPullOnBlue) = extractMaxPulls(drawings)
            totalPower += (maxPullOnRed * maxPullOnGreen * maxPullOnBlue)
        }
        return totalPower
    }

    val part1TestInput = """
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent().split("\n")
    verifyTestResult(part1(part1TestInput), 8)
    part1(readInput("day2")).println()

    val part2TestInput = """
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent().split("\n")
    verifyTestResult(part2(part2TestInput), 2286L)
    part2(readInput("day2")).println()
}

