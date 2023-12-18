package com.itzbubschki.aoc2023.day07

import com.itzbubschki.aoc2023.utils.println
import com.itzbubschki.aoc2023.utils.readInput

val input = readInput("Day07")
fun main() {
    part1().println()
    part2().println()
}

fun part1(): Int {
    val cards = input.map(::getCardRankPart1)
    return sortAndCalculate(cards)
}

fun part2(): Int {
    val cards = input.map(::getCardRankPart2)
    return sortAndCalculate(cards, true)
}

private fun sortAndCalculate(cards: List<CardCollection>, jokersAsWeakest: Boolean = false): Int {
    val sortedCards =
        cards.sortedWith(compareBy<CardCollection> { it.rank.ordinal }.then { cardCollection1, cardCollection2 ->
            compareCardLists(
                cardCollection1.cards,
                cardCollection2.cards,
                jokersAsWeakest
            )
        })

    var sum = 0
    sortedCards.withIndex().forEach { (index, cardCollection) ->
        val reversedIndex = sortedCards.size - index
        sum += cardCollection.bid * reversedIndex
    }
    return sum
}


private fun compareCardLists(cardList1: List<SingleCard>, cardList2: List<SingleCard>, jokersAsWeakest: Boolean): Int {
    var result = 0
    for (i in cardList1.indices) {
        result =
            if (jokersAsWeakest) cardList1[i].rank.compareTo(cardList2[i].rank) else cardList1[i].ordinal.compareTo(
                cardList2[i].ordinal
            )
        if (result != 0) break
    }
    return result
}

fun getCardRankPart1(line: String): CardCollection {
    val parts = line.split(" ")
    val cards = parts.first()
    val parsedCards = cards.map { SingleCard.byValue(it) }
    val bid = parts.last().toInt()
    cards.map { cardChar ->
        val remaining = cards.filter { it != cardChar }
        when (remaining.length) {
            0 -> return CardCollection(CardRank.Five, parsedCards, bid)
            1 -> return CardCollection(CardRank.Four, parsedCards, bid)
            2 -> return if (remaining.first() == remaining.last()) CardCollection(
                CardRank.FullHouse, parsedCards, bid
            ) else CardCollection(CardRank.Three, parsedCards, bid)

            3 -> return CardCollection(getThreeRemainingRankNoJokers(remaining), parsedCards, bid)

            else -> {}
        }
    }
    return CardCollection(CardRank.High, parsedCards, bid)
}

fun getCardRankPart2(line: String): CardCollection {
    val cardCollections = SingleCard.entries.filter { it != SingleCard.J }
        .map { getCardRankPart1(line.replace("J", it.value.toString())) }
    val parsedCards = line.split(" ").first().map { SingleCard.byValue(it) }

    val highest =
        cardCollections.sortedWith(compareBy<CardCollection> { it.rank.ordinal }.then { cardCollection1, cardCollection2 ->
            compareCardLists(
                cardCollection1.cards,
                cardCollection2.cards,
                true
            )
        }).first()
    return CardCollection(highest.rank, parsedCards, highest.bid)
}

fun getThreeRemainingRankNoJokers(remaining: String): CardRank {
    return when (remaining.toSet().size) {
        1 -> CardRank.FullHouse
        2 -> CardRank.TwoPair
        else -> CardRank.Pair
    }
}

enum class CardRank {
    Five,
    Four,
    FullHouse,
    Three,
    TwoPair,
    Pair,
    High
}

enum class SingleCard(val value: Char, val rank: Int) {
    A('A', 0),
    K('K', 1),
    Q('Q', 2),
    J('J', 12),
    T('T', 3),
    Nine('9', 4),
    Eight('8', 5),
    Seven('7', 6),
    Six('6', 7),
    Five('5', 8),
    Four('4', 9),
    Three('3', 10),
    Two('2', 11);

    companion object {
        fun byValue(value: Char): SingleCard {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("No enum constant for value $value")
        }
    }
}

data class CardCollection(val rank: CardRank, val cards: List<SingleCard>, val bid: Int)