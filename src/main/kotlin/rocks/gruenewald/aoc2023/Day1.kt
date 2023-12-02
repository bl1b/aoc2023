package rocks.gruenewald.aoc2023

import rocks.gruenewald.println
import rocks.gruenewald.readInput
import rocks.gruenewald.verifyTestResult

fun main() {
    fun part1(lines: List<String>): Number {
        var calibrationSum = 0

        for (line in lines) {
            val firstNumber = line.find { it.isDigit() }
            val lastNumber = line.findLast { it.isDigit() }
            calibrationSum += "$firstNumber$lastNumber".toInt()
        }
        return calibrationSum
    }

    fun part2(lines: List<String>): Number {
        var calibrationSum = 0
        val numbersList = listOf(
            "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
        )
        for (line in lines) {
            val strStartWithNumber = line.substring(line.indexOfAny(numbersList))
            val strEndWithNumber = line.substring(line.lastIndexOfAny(numbersList))
            calibrationSum += "${numberFromString(strStartWithNumber)}${numberFromString(strEndWithNumber)}".toInt()
        }
        return calibrationSum
    }

    val testPart1 = """
        1abc2
        pqr3stu8vwx
        a1b2c3d4e5f
        treb7uchet
    """.trimIndent().split("\n")
    verifyTestResult(part1(testPart1), 142)
    part1(readInput("day1")).println()

    val testPart2 = """
        two1nine
        eightwothree
        abcone2threexyz
        xtwone3four
        4nineeightseven2
        zoneight234
        7pqrstsixteen
    """.trimIndent().split("\n")
    verifyTestResult(part2(testPart2), 281)
    part2(readInput("day1")).println()
}

private fun numberFromString(str: String): String =
    when {
        str.startsWith("1").or(str.startsWith("one")) -> "1"
        str.startsWith("2").or(str.startsWith("two")) -> "2"
        str.startsWith("3").or(str.startsWith("three")) -> "3"
        str.startsWith("4").or(str.startsWith("four")) -> "4"
        str.startsWith("5").or(str.startsWith("five")) -> "5"
        str.startsWith("6").or(str.startsWith("six")) -> "6"
        str.startsWith("7").or(str.startsWith("seven")) -> "7"
        str.startsWith("8").or(str.startsWith("eight")) -> "8"
        str.startsWith("9").or(str.startsWith("nine")) -> "9"
        else -> error("$str not starting with a known number.")
    }