package rocks.gruenewald.aoc2023

import rocks.gruenewald.println
import rocks.gruenewald.readInput
import rocks.gruenewald.verifyTestResult

fun main() {

    fun fanOut(currentLine: List<Long>, lines: List<List<Long>>): List<List<Long>> {
        lines.addLast(currentLine)
        if (currentLine.all { it == 0L }) return lines

        val newLine = buildList<Long>(currentLine.size - 1) {
            for (index in currentLine.indices) {
                if(index + 1 >= currentLine.size) break
                addLast(currentLine[index + 1] - currentLine[index])
            }
        }
        return fanOut(newLine, lines)
    }

    fun nextSequenceNumber(current: Long, index: Int, sequenceNumbers: List<List<Long>>): Long {
        val newCurrent = sequenceNumbers[index].last() + current
        if(index == 0) {
            return newCurrent
        }
        return nextSequenceNumber(newCurrent, index - 1, sequenceNumbers)
    }

    fun previousSequenceNumber(current: Long, index: Int, sequenceNumbers: List<List<Long>>): Long {
        val newCurrent = sequenceNumbers[index].first() - current
        if(index == 0) {
            return newCurrent
        }
        return previousSequenceNumber(newCurrent, index - 1, sequenceNumbers)
    }

    fun part1(lines: List<String>): Long {
        var sumOfNextSequenceNumbers = 0L

        for (line in lines) {
            var numbersInLine = line.split(" ").map { it.toLong() }
            val fanOutLines = fanOut(numbersInLine, mutableListOf())

            sumOfNextSequenceNumbers += nextSequenceNumber(0, fanOutLines.size - 1, fanOutLines)
        }
        return sumOfNextSequenceNumbers
    }

    fun part2(lines: List<String>): Any {
        var sumOfFirstSequenceNumbers = 0L
        for (line in lines) {
            var numbersInLine = line.split(" ").map { it.toLong() }
            val fanOutLines = fanOut(numbersInLine, mutableListOf())

            sumOfFirstSequenceNumbers += previousSequenceNumber(0, fanOutLines.size - 1, fanOutLines)
        }
        return sumOfFirstSequenceNumbers
    }

    val part1TestInput = """
        0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45
    """.trimIndent().split("\n")
    verifyTestResult(part1(part1TestInput), 114L)

    val inputOfTheDay = readInput("day09")
    part1(inputOfTheDay).println()

    verifyTestResult(part2(part1TestInput), 2L)
    part2(inputOfTheDay).println()
}

