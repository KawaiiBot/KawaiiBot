package me.alexflipnote.kawaiibot.apis.botlists

class DiscordBoats(botId: String, key: String) : BotList(
    "DiscordBoats",
    "https://discord.boats/api/bot/$botId",
    key,
    "server_count"
)
