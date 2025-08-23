package com.example.chatserver.chat.repository;

import com.example.chatserver.chat.domain.ChatRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    List<ChatRoom> findByIsGroupChat(String isGroupChat);

    @Query("SELECT DISTINCT cr FROM ChatRoom cr " +
            "LEFT JOIN FETCH cr.chatParticipants " +
            "LEFT JOIN FETCH cr.chatMessages cm " +
            "LEFT JOIN FETCH cm.readState " +
            "WHERE cr.id = :roomId")
    Optional<ChatRoom> findByIdWithDetails(@Param("roomId") Long roomId);

}
