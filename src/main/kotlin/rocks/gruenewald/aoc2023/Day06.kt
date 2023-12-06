package rocks.gruenewald.aoc2023

import rocks.gruenewald.println
import rocks.gruenewald.readInput
import rocks.gruenewald.verifyTestResult
import kotlin.math.pow

fun main() {
    fun Long.squared() = this.toDouble().pow(2).toLong()

    fun distanceForBoat(durationOfHold: Long, durationOfRace: Long): Long =
        (durationOfRace * durationOfHold) - durationOfHold.squared()

    fun calculatePositiveOutcomesPerRace(raceTime: Long, distanceToReach: Long): Long {
        var positiveOutcomesPerRace = 0L
        for (holdTime in 1 until raceTime) {
            val distanceForBoat = distanceForBoat(holdTime, raceTime)
            if (distanceForBoat > distanceToReach) {
                positiveOutcomesPerRace = positiveOutcomesPerRace.inc()
            }
        }
        return positiveOutcomesPerRace
    }

    fun part1(lines: List<String>): Long {
        val times = lines[0].split(' ').filter { it.toLongOrNull() != null }.map { it.toLong() }
        val distances = lines[1].split(' ').filter { it.toLongOrNull() != null }.map { it.toLong() }

        val allPositiveOutcomes = mutableListOf<Long>()
        times.forEachIndexed { index, time ->
            allPositiveOutcomes.add(calculatePositiveOutcomesPerRace(time, distances[index]))
        }
        return allPositiveOutcomes.reduce { acc, i -> acc * i }
    }

    fun part2(lines: List<String>): Long {
        val time = lines[0].split(' ').filter { it.toLongOrNull() != null }.reduce { acc, s -> acc + s }.toLong()
        val distance = lines[1].split(' ').filter { it.toLongOrNull() != null }.reduce { acc, s -> acc + s }.toLong()

        return calculatePositiveOutcomesPerRace(time, distance)
    }

    val part1TestInput = """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent().split("\n")
    verifyTestResult(part1(part1TestInput), 288L)

    val inputOfTheDay = readInput("day06")
    part1(inputOfTheDay).println()

    verifyTestResult(part2(part1TestInput), 71503L)
    part2(inputOfTheDay).println()
}

