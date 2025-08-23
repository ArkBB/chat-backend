package com.example.chatserver.chat.repository;

import com.example.chatserver.chat.domain.ChatParticipant;
import com.example.chatserver.chat.domain.ChatRoom;
import com.example.chatserver.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant,Long> {

    List<ChatParticipant> findByChatRoomId(Long chatRoomId);

    Optional<ChatParticipant> findByChatRoomAndMember(ChatRoom chatRoom, Member member);

    List<ChatParticipant> findAllByMember(Member member);

    List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);

    @Query("SELECT cp1.chatRoom FROM ChatParticipant cp1 JOIN ChatParticipant cp2 ON cp1.chatRoom.id = cp2.chatRoom.id WHERE cp1.member.id = :myId and cp2.member.id = :otherMemberId and cp1.chatRoom.isGroupChat = 'N'")
    Optional<ChatRoom> findPrivateChatRoom(@Param("myId") Long myId, @Param("otherMemberId") Long otherMemberId);
}
