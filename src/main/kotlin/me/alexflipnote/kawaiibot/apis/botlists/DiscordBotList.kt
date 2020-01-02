package me.alexflipnote.kawaiibot.apis.botlists

class DiscordBotList(botId: String, key: String) : BotList(
    "Bots.gg",
    "https://discordbotlist.com/api/bots/$botId/stats",
    key,
    "guilds"
)
