package rocks.gruenewald.aoc2023

import rocks.gruenewald.println
import rocks.gruenewald.readInput
import rocks.gruenewald.verifyTestResult
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun part1(lines: List<String>): Int {
        val partnumsAdjacentToSymbols = mutableListOf<Int>()
        val symbolCoordinates = lines.flatMapIndexed { rowIndex, line ->
            line.mapIndexedNotNull { lineIndex, charInLine ->
                if (!charInLine.isDigit() && charInLine != '.') Coordinate(rowIndex, lineIndex)
                else null
            }
        }

        lines.forEachIndexed { lineIndex, line ->
            val matches = "\\d+".toRegex().findAll(line)
            for (match in matches) {
                val colIndicesForSymbol =
                    IntRange(max(0, match.range.first - 1), min(line.length - 1, match.range.last + 1))
                val lineIndicesForSymbol = IntRange(max(0, lineIndex - 1), min(lines.size - 1, lineIndex + 1))

                var hasSourroundingSymbol = false
                checkLoop@ for (lineIndexForSymbol in lineIndicesForSymbol) {
                    for (rowIndexForSymbol in colIndicesForSymbol) {
                        if (symbolCoordinates.contains(Coordinate(lineIndexForSymbol, rowIndexForSymbol))) {
                            hasSourroundingSymbol = true
                            break@checkLoop
                        }
                    }
                }
                if (hasSourroundingSymbol) {
                    partnumsAdjacentToSymbols.add(match.value.toInt())
                }
            }
        }
        return partnumsAdjacentToSymbols.sum()
    }

    fun part2(lines: List<String>): Int {
        val gearRatios = mutableListOf<Int>()

        val gearCoordinates = lines.flatMapIndexed { rowIndex, line ->
            line.mapIndexedNotNull { lineIndex, charInLine ->
                if (charInLine == '*') Coordinate(rowIndex, lineIndex)
                else null
            }
        }

        for (gearCoordinate in gearCoordinates) {
            val possibleIndexLines = mutableListOf<Int>()
            if (gearCoordinate.line > 0) possibleIndexLines.add(gearCoordinate.line - 1)
            possibleIndexLines.add(gearCoordinate.line)
            if (gearCoordinate.line < lines.size - 1) possibleIndexLines.add(gearCoordinate.line + 1)

            val matchedRatios = mutableListOf<Int>()
            for (possibleIndexLine in possibleIndexLines) {
                val line = lines[possibleIndexLine]
                val possibleNumberCols =
                    IntRange(max(0, gearCoordinate.col - 1), min(line.length - 1, gearCoordinate.col + 1))

                val numberMatches = "\\d+".toRegex().findAll(line)
                for (numberMatch in numberMatches) {
                    if (possibleNumberCols.intersect(numberMatch.range).isNotEmpty()) {
                        matchedRatios.add(numberMatch.value.toInt())
                    }
                }
            }
            if (matchedRatios.size >= 2) gearRatios.add(matchedRatios.reduce { acc, i -> acc * i })
        }
        return gearRatios.sum()
    }

    val part1TestInput = """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...${'$'}.*....
        .664.598..
    """.trimIndent().split("\n")
    verifyTestResult(part1(part1TestInput), 4361)

    val inputOfTheDay = readInput("day03")
    part1(inputOfTheDay).println()

    val part2TestInput = """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...${'$'}.*....
        .664.598..
    """.trimIndent().split("\n")
    verifyTestResult(part2(part2TestInput), 467835)
    part2(inputOfTheDay).println()
}

data class Coordinate(val line: Int, val col: Int)
