package me.alexflipnote.kawaiibot.apis.botlists

class BotsGG(botId: String, key: String) : BotList(
    "Bots.gg",
    "https://discord.bots.gg/api/v1/bots/$botId/stats",
    key,
    "guildCount"
)
