package com.example.chatserver.chat.repository;

import com.example.chatserver.chat.domain.ChatRoom;
import com.example.chatserver.chat.domain.ReadState;
import com.example.chatserver.member.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStateRepository extends JpaRepository<ReadState,Long> {
    List<ReadState> findByChatRoomIdAndMember(Long roomId, Member member);

    Long countByChatRoomAndMemberAndIsReadFalse(ChatRoom chatRoom, Member member);
}
