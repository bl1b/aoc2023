package rocks.gruenewald.aoc2023

import rocks.gruenewald.println
import rocks.gruenewald.readInput
import rocks.gruenewald.verifyTestResult

fun main() {

    fun parseIntoInstructionsAndNodes(lines: List<String>): Pair<List<Char>, HashMap<String, Pair<String, String>>> {
        val instructions = lines[0].map { it }

        val nodeMap = HashMap<String, Pair<String, String>>()
        lines.drop(2).map { line ->
            val splitExpr = "^(.*?) = \\((.*?), (.*?)\\)$".toRegex()
            splitExpr.matchEntire(line)?.let { lineMatch ->
                nodeMap.put(lineMatch.groupValues[1], Pair(lineMatch.groupValues[2], lineMatch.groupValues[3]))
            }
        }
        return Pair(instructions, nodeMap)
    }

    fun findStepsToTargetNode(
        startingNode: String,
        endCondition: (String) -> (Boolean),
        nodeMap: HashMap<String, Pair<String, String>>,
        instructions: List<Char>,
        firstStep: Long = 0L
    ): Pair<String, Long> {
        var steps = firstStep
        var currentNode = startingNode
        while (!endCondition(currentNode)) {
            val (left, right) = nodeMap.getValue(currentNode)
            val instruction = instructions[steps.mod(instructions.size)]
            currentNode = if (instruction == 'L') left else right
            steps++
        }
        val pair = Pair(currentNode, steps)
        return pair
    }

    fun greatestCommonDivisor(a: Long, b: Long): Long {
        if (b == 0L) {
            return a
        }
        return greatestCommonDivisor(b, a % b)
    }

    fun part1(lines: List<String>): Long {
        val (instructions, nodeMap) = parseIntoInstructionsAndNodes(lines)
        val (_, steps) = findStepsToTargetNode("AAA", { it == "ZZZ" }, nodeMap, instructions)
        return steps
    }

    fun part2(lines: List<String>): Long {
        val (instructions, nodeMap) = parseIntoInstructionsAndNodes(lines)
        val startingNodes = nodeMap.keys.filter { it.endsWith("A") }

        // DISCLAIMER: using LCM is an idea borrowed from here:
        // https://github.com/xiaowuc1/aoc-2023-kotlin/blob/main/src/Day08.kt
        var lcm = 1L
        for (startingNode in startingNodes) {
            val (_, stepsOnNode) = findStepsToTargetNode(startingNode, { it.endsWith("Z") }, nodeMap, instructions)
            lcm = lcm / greatestCommonDivisor(lcm, stepsOnNode) * stepsOnNode
        }
        return lcm
    }

    val part1TestInput = """
        LLR

        AAA = (BBB, BBB)
        BBB = (AAA, ZZZ)
        ZZZ = (ZZZ, ZZZ)
    """.trimIndent().split("\n")
    verifyTestResult(part1(part1TestInput), 6L)

    val inputOfTheDay = readInput("day08")
    part1(inputOfTheDay).println()

    val part2TestInput = """
        LR

        11A = (11B, XXX)
        11B = (XXX, 11Z)
        11Z = (11B, XXX)
        22A = (22B, XXX)
        22B = (22C, 22C)
        22C = (22Z, 22Z)
        22Z = (22B, 22B)
        XXX = (XXX, XXX)
    """.trimIndent().split("\n")
    verifyTestResult(part2(part2TestInput), 6L)
    part2(inputOfTheDay).println()
}