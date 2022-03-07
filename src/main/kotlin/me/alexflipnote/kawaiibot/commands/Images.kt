package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.extensions.urlWithQueryParams
import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command
import me.devoxin.flight.api.annotations.Greedy
import me.devoxin.flight.api.entities.Attachment
import me.devoxin.flight.api.entities.Cog
import net.dv8tion.jda.api.Permission

class Images : Cog {
  private val apiBaseUrl = KawaiiBot.config.getProperty("api_url")

  private fun ImageAPICommand(ctx: Context, endpoint: String, query: Map<String, String>) {
    KawaiiBot.httpClient.request { urlWithQueryParams("$apiBaseUrl/$endpoint", query) }
        .submit()
        .thenAccept {
          val body = it.body()
          if (!it.isSuccessful || body == null) {
            ctx.send("There was an error creating the image, try again later.")
            body?.close()
          } else {
            val toUpload = Attachment.from(body.byteStream(), "$endpoint.png")
            ctx.send(toUpload)
            body.close()
          }
        }.exceptionally {
          ctx.send("I couldn't process the request, sorry ;-;")
          null
        }
  }

  @Command(aliases = ["ah"], description = "Get those achievements like in Minecraft!",
      botPermissions = [Permission.MESSAGE_ATTACH_FILES])
  fun achievement(ctx: Context, @Greedy text: String) {
    ImageAPICommand(ctx, "achievement", mapOf("text" to text))
  }

  @Command(aliases = ["ch"], description = "Get those achievements like in Minecraft!",
      botPermissions = [Permission.MESSAGE_ATTACH_FILES])
  fun challenge(ctx: Context, @Greedy text: String) {
    ImageAPICommand(ctx, "challenge", mapOf("text" to text))
  }
}
