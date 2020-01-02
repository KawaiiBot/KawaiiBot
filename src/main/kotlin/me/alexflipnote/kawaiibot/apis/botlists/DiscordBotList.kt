package me.alexflipnote.kawaiibot.apis.botlists

class DiscordBotList(botId: String, key: String) : BotList(
    "DiscordBotList",
    "https://discordbotlist.com/api/bots/$botId/stats",
    key,
    "guilds"
)
