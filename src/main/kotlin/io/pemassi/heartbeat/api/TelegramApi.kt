package io.pemassi.heartbeat.api

import io.pemassi.heartbeat.api.dto.telegram.SendMessageDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

/**
 * Telegram API Interface
 */
@FeignClient(name = "telegram-api", url="https://api.telegram.org")
interface TelegramApi
{
    /**
     * Send message to specific user or chat room.
     */
    @PostMapping("/bot{botId}/sendmessage")
    fun send(
            @PathVariable("botId")
            botId: String,

            @RequestBody
            dto: SendMessageDto,
    ): String
}