package com.clangengineer.surveymodus.web.websocket

import com.clangengineer.surveymodus.config.WebsocketConfiguration.Companion.IP_ADDRESS
import com.clangengineer.surveymodus.web.websocket.dto.ActivityDTO
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Controller
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import java.security.Principal
import java.time.Instant

@Controller
class ActivityService(private val messagingTemplate: SimpMessageSendingOperations) :
    ApplicationListener<SessionDisconnectEvent> {

    private val log = LoggerFactory.getLogger(javaClass)

    @MessageMapping("/topic/activity")
    @SendTo("/topic/tracker")
    fun sendActivity(@Payload activityDTO: ActivityDTO, stompHeaderAccessor: StompHeaderAccessor, principal: Principal): ActivityDTO {
        activityDTO.apply {
            userLogin = principal.name
            sessionId = stompHeaderAccessor.sessionId
            ipAddress = stompHeaderAccessor.sessionAttributes!![IP_ADDRESS].toString()
            time = Instant.now()
        }
        log.debug("Sending user tracking data $activityDTO")
        return activityDTO
    }

    override fun onApplicationEvent(event: SessionDisconnectEvent) {
        val activityDTO = ActivityDTO(sessionId = event.sessionId, page = "logout")
        messagingTemplate.convertAndSend("/topic/tracker", activityDTO)
    }
}
