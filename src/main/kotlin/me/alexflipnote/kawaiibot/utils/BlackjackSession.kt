package me.alexflipnote.kawaiibot.utils

import com.google.common.collect.Sets
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.extensions.await
import kotlin.math.min

class BlackjackSession(private val ctx: CommandContext) {
    private val ranks = setOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
    private val suits = setOf("♣", "♦", "♥", "♠")

    private var playing = true
    private var endStatus = "playing"

    private var deck = mutableListOf<String>()

    private val userCards = mutableListOf<String>()
    private val botCards = mutableListOf<String>()

    suspend fun start() {
        initialCards()
        while (playing) {
            ctx.channel.sendMessage(getLayoutText())
            if (getPoints(userCards) == 21){
                endStatus = "won"
            } else {
                askCard()
            }

            val msg = ctx.channel.sendMessage("Want to play again? Type `yes` if you do~").await()
            val choice = ctx.waitFor(ctx.sameContext(), 60).message.contentRaw
            if (choice != "yes") {
                playing = false
            } else {
                userCards.clear()
                botCards.clear()
            }
        }
    }

    private fun getLayoutText(hideBot: Boolean = true): String {
        val botSet = "${botCards[0]} ${botCards.subList(1, botCards.size-1).map { it -> if (hideBot) "??" else it }.joinToString(" ")}"
        val userSet = userCards.joinToString(" ")
        return "```Bot (${if (hideBot) getValue(botCards[0]) else getPoints(botCards)}: $botSet\n\nYou ${getPoints(userCards)}: $userSet```"
    }

    private suspend fun initialCards() {
        botCards.add(drawCard())
        userCards.add(drawCard())
        botCards.add(drawCard())
        userCards.add(drawCard())
    }

    private suspend fun askCard() {
        var choice: String? = null
        while (choice.isNullOrBlank()) {
            val msg = ctx.channel.sendMessage("type `hit` to get another card, or `stand` to stop!").await()
            choice = ctx.waitFor(ctx.sameContext(), 60).message.contentRaw
            msg.delete().queue()
            when (choice) {
                "hit" -> {
                    userCards.add(drawCard())
                    when {
                        getPoints(userCards) > 21 -> {
                            endStatus = "lost"
                        }
                        getPoints(userCards) == 21 -> {
                            processBot()
                        }
                        else -> {
                            choice = null  // Let them pick again
                        }
                    }
                }
                "stand" -> {
                    // Move to bot part
                    processBot()
                }
                null -> {
                    // Ignore and ask again
                }
            }
        }
    }

    private suspend fun processBot(){
        if (endStatus == "playing") {
            val reach = min(17, getPoints(userCards))
            while (getPoints(botCards) < reach) {
                botCards.add(drawCard())
            }

            when {
                getPoints(userCards) > getPoints(botCards) -> {
                    endStatus = "won"
                }
                getPoints(userCards) == getPoints(botCards) -> {
                    endStatus = "tied"
                }
                getPoints(userCards) < getPoints(botCards) -> {
                    endStatus = "lost"
                }
            }
        }

        ctx.channel.sendMessage("You $endStatus against me!\n\n${getLayoutText(false)}")
    }

    private suspend fun drawCard(): String {
        if (deck.isEmpty())
            newDeck()
        return deck.removeAt(0)
    }

    private fun getPoints(cards: List<String>): Int = cards.map { getValue(it) }.sum()

    private fun getValue(card: String): Int = ranks.indexOf(card[0].toString())+1

    private suspend fun newDeck(){
        val msg = ctx.channel.sendMessage("Shuffling deck...").await()
        val newDeck = Sets.cartesianProduct(ranks, suits).map { it -> it.joinToString("") }
        deck.addAll(newDeck.shuffled())
        msg.delete().queue()
    }
}