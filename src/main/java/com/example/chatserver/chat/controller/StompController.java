package com.example.chatserver.chat.controller;

import com.example.chatserver.chat.dto.ChatMessageDto;
import com.example.chatserver.chat.service.ChatService;
import com.example.chatserver.chat.service.RedisPubSubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {

    private final ChatService chatService;
    private final RedisPubSubService redisPubSubService;

    public StompController(ChatService chatService, RedisPubSubService redisPubSubService) {
        this.chatService = chatService;
        this.redisPubSubService = redisPubSubService;
    }

    //    // 방법1 . MessageMapping(수신)과 SenTo(topic에 메시지 전달) 한꺼번에 처리
//    @MessageMapping("/{roomId}") //클라이언트에서 특정 public/roomId 형태로 메시지 발행시 MessageMapping 수신
//    @SendTo("/topic/{roomId}") // 해당 roomId에 메시지를 발행하여 구독중인 클라이언트에게 메시지 전송
//    public String sendMessage(@DestinationVariable Long roomId, String message) {
//        System.out.println(message);
//        return message;
//    }
    // 방법2 MessageMapping 어노테이션만 활용
    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageDto chatMessageReqDto)
            throws JsonProcessingException {

     //   messageSendingOperations.convertAndSend("/topic/" + roomId, chatMessageReqDto);
        chatService.saveMessage(roomId,chatMessageReqDto);

        chatMessageReqDto.setRoomId(roomId);
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(chatMessageReqDto);
        redisPubSubService.publish("chat", message);

    }
    //topic에 메시지 발행

}
