package io.pemassi.heartbeat.interfaces

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Telegram API Interface
 *
 * @author Kyungyoon Kim (ruddbs5302@gmail.com)
 */
interface TelegramAPI
{
    /**
     * Send message to specific user or chat room.
     */
    @POST("https://api.telegram.org/bot{botId}/sendmessage")
    fun send(
            @Path("botId")
            botId: String,

            @Query("chat_id")
            chatId: Int,

            @Query("text")
            text: String,

            @Query("parse_mode")
            parseMode: String? = null,

            @Query("disable_web_page_preview")
            disableWebPagePreview: Boolean? = null,

            @Query("disable_notification")
            disableNotification: Boolean? = null,

            @Query("reply_to_message_id")
            replyToMessageId: Int? = null,

            @Query("reply_markup")
            replyMarkup: Any? = null
    ): Call<Any>

    /**
     * Send message to specific user or chat room.
     * (For String chat_id)
     */
    @POST("https://api.telegram.org/bot{botId}/sendmessage")
    fun sendWithStringChatId(
            @Path("botId")
            botId: String,

            @Query("chat_id")
            chatId: String,

            @Query("text")
            text: String,

            @Query("parse_mode")
            parseMode: String? = null,

            @Query("disable_web_page_preview")
            disableWebPagePreview: Boolean? = null,

            @Query("disable_notification")
            disableNotification: Boolean? = null,

            @Query("reply_to_message_id")
            replyToMessageId: Int? = null,

            @Query("reply_markup")
            replyMarkup: Any? = null
    ): Call<Any>
}