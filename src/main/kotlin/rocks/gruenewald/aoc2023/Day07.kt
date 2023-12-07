package rocks.gruenewald.aoc2023

import rocks.gruenewald.println
import rocks.gruenewald.readInput
import rocks.gruenewald.verifyTestResult

enum class Handtype(val rank: Int) {
    FIVE_OF_A_KIND(7),
    FOUR_OF_A_KIND(6),
    FULL_HOUSE(5),
    THREE_OF_A_KIND(4),
    TWO_PAIRS(3),
    ONE_PAIR(2),
    HIGH_CARD(1)
}

fun main() {
    val cardStrengths = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
    val cardStrengthsJoker = listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')

    class Hand(
        val cards: List<Char>,
        val bid: Int,
        val jokerMode: Boolean = false
    ) : Comparable<Hand> {
        val type: Handtype

        init {
            type = initHandType()
        }

        override fun compareTo(other: Hand): Int {
            if (type != other.type) {
                return type.rank.compareTo(other.type.rank)
            }
            for (cardIndex in cards.indices) {
                if (cards[cardIndex] != other.cards[cardIndex]) {
                    return if (!jokerMode) {
                        cardStrengths.indexOf(cards[cardIndex])
                            .compareTo(cardStrengths.indexOf(other.cards[cardIndex]))
                    } else {
                        cardStrengthsJoker.indexOf(cards[cardIndex])
                            .compareTo(cardStrengthsJoker.indexOf(other.cards[cardIndex]))
                    }

                }
            }
            return 0
        }

        override fun toString(): String {
            return """{ "cards": ${cards.map { "\"$it\"" }}, "bid": $bid, "type": "$type" }"""
        }

        private fun initHandType(): Handtype {
            val cardGroups = cards.groupBy { it }
            if (!jokerMode || !cards.contains('J')) {
                return when (cardGroups.size) {
                    1 -> Handtype.FIVE_OF_A_KIND
                    4 -> Handtype.ONE_PAIR
                    5 -> Handtype.HIGH_CARD
                    2 -> {
                        if (cardGroups.values.first().size == 1 || cardGroups.values.first().size == 4) Handtype.FOUR_OF_A_KIND
                        else Handtype.FULL_HOUSE
                    }

                    3 -> {
                        if (cardGroups.values.first().size == 2 || cardGroups.values.last().size == 2) Handtype.TWO_PAIRS
                        else Handtype.THREE_OF_A_KIND
                    }

                    else -> error("Illegal card-grouping ${cardGroups.size}")
                }
            }

            val maxSizeOfNonJoker = cardGroups.values.maxOfOrNull { if (it.contains('J')) 0 else it.size } ?: 1
            val numberOfJokers = cards.count { it == 'J' }
            return when (val sizeIncludingJokers = maxSizeOfNonJoker + numberOfJokers) {
                5 -> Handtype.FIVE_OF_A_KIND
                4 -> Handtype.FOUR_OF_A_KIND
                3 -> {
                    if (numberOfJokers == 0 && cardGroups.size == 2 || numberOfJokers == 1 && cardGroups.size == 3)
                        Handtype.FULL_HOUSE
                    else Handtype.THREE_OF_A_KIND
                }
                2 -> {
                    if (cardGroups.size == 3) Handtype.TWO_PAIRS
                    else Handtype.ONE_PAIR
                }
                1 -> Handtype.HIGH_CARD
                else -> error("Illegal max group-size $sizeIncludingJokers ($cards)")
            }
        }


    }

    fun part1(lines: List<String>): Any {
        val hands = lines.map { line ->
            val parts = line.split(" ")
            Hand(parts[0].map { it }, parts[1].toInt())
        }
        return hands.sorted().mapIndexed { index, hand -> ((index + 1) * hand.bid).toLong() }.sum()
    }

    fun part2(lines: List<String>): Any {
        val hands = lines.map { line ->
            val parts = line.split(" ")
            Hand(parts[0].map { it }, parts[1].toInt(), true)
        }
        return hands.sorted().mapIndexed { index, hand -> ((index + 1) * hand.bid).toLong() }.sum()
    }

    val part1TestInput = """        
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """.trimIndent().split("\n")
    verifyTestResult(part1(part1TestInput), 6440L)

    val inputOfTheDay = readInput("day07")
    part1(inputOfTheDay).println()

    verifyTestResult(part2(part1TestInput), 5905L)
    part2(inputOfTheDay).println()
}

