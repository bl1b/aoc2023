package rocks.gruenewald.aoc2023

import rocks.gruenewald.println
import rocks.gruenewald.readInput
import rocks.gruenewald.verifyTestResult
import kotlin.math.pow

data class Card internal constructor(
    val cardNumber: Int,
    val winningNumbers: List<String>,
    val myNumbers: List<String>
) {
    companion object {
        fun fromLine(line: String): Card {
            val lineMatches = "^Card\\s+(\\d+)?: (.*?) \\| (.*\$)".toRegex()
            check(lineMatches.matches(line))
            val matchResult = lineMatches.matchEntire(line)!!
            return Card(matchResult.groupValues[1].toInt(),
                matchResult.groupValues[2].split(' ').filter { it.isNotBlank() },
                matchResult.groupValues[3].split(' ').filter { it.isNotBlank() })
        }
    }

    val wins: Int
        get() = myNumbers.filter { winningNumbers.contains(it) }.size

    val score: Int
        get() = if (wins == 1) 1 else if (wins > 1) 2.toDouble().pow(wins - 1).toInt() else 0
}

fun main() {

    fun part1(lines: List<String>): Int = lines.sumOf { Card.fromLine(it).score }

    fun part2(lines: List<String>): Int {
        val refCards = lines.map { Card.fromLine(it) }
        val cardToAmount = mutableMapOf<Int, Int>()

        // add 1 initial copy for each card
        refCards.map { Pair(it.cardNumber, 1) }.forEach { cardToAmount.plusAssign(it) }

        for (refCardEntry in cardToAmount) {
            val winsOnCard = refCards.find { refCardEntry.key == it.cardNumber }?.wins ?: 0
            if (winsOnCard > 0) {
                for (additionalCardNumber in refCardEntry.key + 1..refCardEntry.key + winsOnCard) {
                    val currentCardCount = cardToAmount[additionalCardNumber]
                    // don't overboard
                    currentCardCount?.let {
                        cardToAmount[additionalCardNumber] = (it + refCardEntry.value)
                    }
                }
            }
        }
        return cardToAmount.values.sum()
    }

    val part1TestInput = """        
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    """.trimIndent().split("\n")

    verifyTestResult(part1(part1TestInput), 13)

    val inputOfTheDay = readInput("day04")
    part1(inputOfTheDay).println()

    val part2TestInput = """
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    """.trimIndent().split("\n")
    verifyTestResult(part2(part2TestInput), 30)
    part2(inputOfTheDay).println()
}

