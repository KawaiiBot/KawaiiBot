package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.devoxin.flight.annotations.Command
import me.devoxin.flight.api.Context
import me.devoxin.flight.arguments.Greedy
import me.devoxin.flight.models.Attachment
import me.devoxin.flight.models.Cog

class Images : Cog {
  private fun ImageAPICommand(ctx: Context, endpoint: String, querystring: String) {
    KawaiiBot.httpClient.get("${KawaiiBot.config.getProperty("api_url")}/$endpoint?$querystring").submit().thenAccept {
      val body = it.body()
      if (!it.isSuccessful || body == null) {
        ctx.send("There was an error creating the image, try again later.")
        body?.close()
      } else {
        val toUpload = Attachment.from(body.byteStream(), "${endpoint}.png")
        ctx.send(toUpload)
        body.close()
      }
    }.exceptionally {
      ctx.send("I couldn't process the request, sorry ;-;")
      null
    }
  }

  @Command(aliases = ["ah"], description = "Get those achievements like in Minecraft!")
  fun achievement(ctx: Context, @Greedy text: String) {
    ImageAPICommand(ctx, "achievement", text)
  }
}
