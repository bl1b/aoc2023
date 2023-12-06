package rocks.gruenewald.aoc2023

import rocks.gruenewald.println
import rocks.gruenewald.readInput
import rocks.gruenewald.verifyTestResult
import kotlin.math.min

fun main() {

    data class Mapping(
        val source: String,
        val destination: String,
        val sourceBounds: List<Pair<Long, Long>> = mutableListOf(),
        val destinationBounds: List<Pair<Long, Long>> = mutableListOf()
    ) {

        fun destinationNumberBySourceNumber(sourceNumber: Long): Long {
            for (index in sourceBounds.indices) {
                if (sourceNumber >= sourceBounds[index].first && sourceNumber <= sourceBounds[index].second) {
                    return destinationBounds[index].first + (sourceNumber - sourceBounds[index].first)
                }
            }
            return sourceNumber
        }

        fun sourceNumberByDestinationNumber(destinationNumber: Long): Long {
            for (index in destinationBounds.indices) {
                if (destinationNumber >= destinationBounds[index].first && destinationNumber <= destinationBounds[index].second) {
                    return sourceBounds[index].first + (destinationNumber - destinationBounds[index].first)
                }
            }
            return destinationNumber
        }
    }

    fun findDestinationMapping(
        mappings: List<Mapping>,
        source: String,
        sourceNumber: Long,
        destination: String
    ): Long? {
        val sourceMapping = mappings.find { it.source == source } ?: return null
        val destinationNumber = sourceMapping.destinationNumberBySourceNumber(sourceNumber)
        if (destination == sourceMapping.destination) {
            return destinationNumber
        }
        return findDestinationMapping(mappings, sourceMapping.destination, destinationNumber, destination)
    }

    fun findSeedPerDestination(
        mappings: List<Mapping>,
        destination: String,
        destinationNumber: Long,
        source: String
    ): Long? {
        val destinationMapping = mappings.find { it.destination == destination } ?: return null
        val sourceNumber = destinationMapping.sourceNumberByDestinationNumber(destinationNumber)
        if (source == destinationMapping.source) {
            return sourceNumber
        }
        return findSeedPerDestination(mappings, destinationMapping.source, sourceNumber, source)
    }


    fun parseLinesIntoMappings(lines: List<String>): MutableList<Mapping> {
        val mapStartIdentifier = "^(.*?)-to-(.*?) map:$".toRegex()
        val mapEndIdentifier = "^$".toRegex()
        val mappings = mutableListOf<Mapping>()
        var activeMapping: Mapping? = null
        for (line in lines) {
            if (mapStartIdentifier.matches(line)) {
                val parsedLine = mapStartIdentifier.matchEntire(line)
                if (parsedLine != null) {
                    activeMapping = Mapping(parsedLine.groupValues[1], parsedLine.groupValues[2])
                }
                continue
            }

            if (activeMapping != null) {
                if (mapEndIdentifier.matches(line)) {
                    mappings.addLast(activeMapping)
                    activeMapping = null
                    continue
                }
                val mappingEntries = line.split(" ").map { it.toLong() }
                activeMapping.sourceBounds.addLast(
                    Pair(
                        mappingEntries[1],
                        mappingEntries[1] + mappingEntries[2] - 1
                    )
                )
                activeMapping.destinationBounds.addLast(
                    Pair(
                        mappingEntries[0],
                        mappingEntries[0] + mappingEntries[2] - 1
                    )
                )
            }
        }
        activeMapping?.let { mappings.add(it) }
        return mappings
    }

    fun part1(lines: List<String>): Long {
        val seeds = lines[0].split(" ").filter { it.all { possibleSeed -> possibleSeed.isDigit() } }.map { it.toLong() }

        val mappings = parseLinesIntoMappings(lines)
        var lowestDestination = Long.MAX_VALUE
        for (seed in seeds) {
            findDestinationMapping(mappings, "seed", seed, "location")?.let {
                lowestDestination = min(lowestDestination, it)
            }
        }
        return lowestDestination
    }

    fun part2(lines: List<String>): Long {
        val mappings = parseLinesIntoMappings(lines)

        val higestDestination = mappings
            .find { it.destination == "location" }
            ?.destinationBounds
            ?.map { it.second }
            ?.max() ?: 0L

        val seedRanges = lines[0].split(" ")
            .filter { it.all { possibleSeed -> possibleSeed.isDigit() } }
            .map { it.toLong() }
            .chunked(2) { LongRange(it[0], it[0] + it[1] - 1) }

        for (destination in 0L..higestDestination) {
            val seedNumber = findSeedPerDestination(mappings, "location", destination, "seed")
            for (seedRange in seedRanges) {
                if (seedRange.contains(seedNumber)) {
                    return destination
                }
            }
        }
        return 0
    }

    val part1TestInput = """
        seeds: 79 14 55 13

        seed-to-soil map:
        50 98 2
        52 50 48

        soil-to-fertilizer map:
        0 15 37
        37 52 2
        39 0 15

        fertilizer-to-water map:
        49 53 8
        0 11 42
        42 0 7
        57 7 4

        water-to-light map:
        88 18 7
        18 25 70

        light-to-temperature map:
        45 77 23
        81 45 19
        68 64 13

        temperature-to-humidity map:
        0 69 1
        1 0 69

        humidity-to-location map:
        60 56 37
        56 93 4
    """.trimIndent().split("\n")
    verifyTestResult(part1(part1TestInput), 35L)

    val inputOfTheDay = readInput("day05")
    part1(inputOfTheDay).println()

    verifyTestResult(part2(part1TestInput), 46L)
    part2(inputOfTheDay).println()
}
