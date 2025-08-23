package com.example.chatserver.chat.service;


import com.example.chatserver.chat.domain.ChatMessage;
import com.example.chatserver.chat.domain.ChatParticipant;
import com.example.chatserver.chat.domain.ChatRoom;
import com.example.chatserver.chat.domain.ReadState;
import com.example.chatserver.chat.dto.ChatMessageDto;
import com.example.chatserver.chat.dto.ChatRoomListResDto;
import com.example.chatserver.chat.dto.MyChatListResDto;
import com.example.chatserver.chat.repository.ChatMessageRepository;
import com.example.chatserver.chat.repository.ChatParticipantRepository;
import com.example.chatserver.chat.repository.ChatRoomRepository;
import com.example.chatserver.chat.repository.ReadStateRepository;
import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatParticipantRepository chatParticpantRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final ReadStateRepository readStateRepository;

    private final MemberRepository memberRepository;

    public ChatService(ChatRoomRepository chatRoomRepository, ChatParticipantRepository chatParticpantRepository,
                       ChatMessageRepository chatMessageRepository, ReadStateRepository readStateRepository,
                       MemberRepository memberRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticpantRepository = chatParticpantRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.readStateRepository = readStateRepository;
        this.memberRepository = memberRepository;
    }

    public void saveMessage(Long roomId, ChatMessageDto chatMessageReqDto) {

        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room not found"));
        // 보낸 사람 조회
        Member member = memberRepository.findByEmail(chatMessageReqDto.getSenderEmail()).orElseThrow(() -> new EntityNotFoundException("member not found"));

        // 메시지 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .member(member)
                .content(chatMessageReqDto.getMessage())
                .build();
        chatMessageRepository.save(chatMessage);
        // 사용자별로 읽음 여부 저장
        List<ChatParticipant> chatParticipants = chatRoom.getChatParticipants();
        for(ChatParticipant chatParticipant : chatParticipants){
            ReadState readState = ReadState.builder()
                    .chatRoom(chatRoom)
                    .member(chatParticipant.getMember())
                    .chatMessage(chatMessage)
                    .isRead(chatParticipant.getMember().equals(member) ? true : false)
                    .build();

            readStateRepository.save(readState);
        }


    }

    public void createGroupRoom(String roomName) {

        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("member not found"));
//      채팅방 생성

        ChatRoom chatRoom = ChatRoom.builder()
                .name(roomName)
                .isGroupChat("Y")
                .build();
        chatRoomRepository.save(chatRoom);
        // 채팅 참여자로 멤버 추가
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
        chatParticpantRepository.save(chatParticipant);
    }

    public List<ChatRoomListResDto> getGroupchatRooms() {

        List<ChatRoom> chatRooms = chatRoomRepository.findByIsGroupChat("Y");
        List<ChatRoomListResDto> chatRoomListResDtos = new ArrayList<>();
        for(ChatRoom chatRoom : chatRooms){
            ChatRoomListResDto chatRoomListResDto =ChatRoomListResDto
                    .builder()
                    .roomId(chatRoom.getId())
                    .roomName(chatRoom.getName())
                    .build();
            chatRoomListResDtos.add(chatRoomListResDto);
        }

        return chatRoomListResDtos;

    }

    public void addParticipantToGroupChat(Long roomId) {

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room not found"));
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("member not found"));

        Optional<ChatParticipant> paricipant = chatParticpantRepository.findByChatRoomAndMember(chatRoom,member);

        if(chatRoom.getIsGroupChat().equals("N")){
            throw new IllegalArgumentException("그룹채팅이 아닙니다.");
        }

        if(paricipant.isEmpty()){
            addParticipantToChatRoom(chatRoom, member);
        }

    }

    public void addParticipantToChatRoom(ChatRoom chatRoom, Member member) {
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
        chatParticpantRepository.save(chatParticipant);
    }

    public List<ChatMessageDto> getChatHistory(Long roomId) {
        // 내가 해당 채팅방의 참여자가 아닐 경우 history 불러올 수 있게 하면 안됨
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room not found"));
        List<ChatParticipant> chatParticipants = chatRoom.getChatParticipants();
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("member not found"));

        boolean check = false;
        for(ChatParticipant chatParticipant : chatParticipants){
            if(chatParticipant.getMember().equals(member)){
                check = true;
                break;
            }
        }

        if(!check)throw new IllegalArgumentException("본인이 속하지 않은 채팅방입니다.");
//      특정 room에 대한 message 조회
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomOrderByCreatedAtAsc(chatRoom);
        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();
        for(ChatMessage chatMessage : chatMessages){
            ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                    .message(chatMessage.getContent())
                    .senderEmail(chatMessage.getMember().getEmail())
                    .build();
            chatMessageDtos.add(chatMessageDto);
        }

        return chatMessageDtos;


        }

    public boolean isRoomParticipant(String email, long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room not found"));
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("member not found"));

        List<ChatParticipant> chatParticipants = chatRoom.getChatParticipants();
        for(ChatParticipant chatParticipant : chatParticipants){
            if(chatParticipant.getMember().equals(member)){
                return true;
            }
        }

        return false;
    }

    public void messageRead(Long roomId) {


        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room not found"));
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("member not found"));

        List<ReadState> readStates = readStateRepository.findByChatRoomIdAndMember(roomId,member);


        for(ReadState readState : readStates){
            readState.updateReadState(true);
        }
    }

    public List<MyChatListResDto> getMyChatRooms() {
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("member not found"));

        List<ChatParticipant> chatParticipants = chatParticpantRepository.findAllByMember(member);

        List<MyChatListResDto> myChatListResDtos = new ArrayList<>();
        for(ChatParticipant chatParticipant : chatParticipants){
            ChatRoom chatRoom = chatParticipant.getChatRoom();
            Long count = readStateRepository.countByChatRoomAndMemberAndIsReadFalse(chatParticipant.getChatRoom(),member);
            MyChatListResDto myChatListResDto = MyChatListResDto.builder()
                    .roomId(chatRoom.getId())
                    .roomName(chatRoom.getName())
                    .isGroupChat(chatRoom.getIsGroupChat())
                    .unReadCount(count)
                    .build();
            myChatListResDtos.add(myChatListResDto);
        }


        return myChatListResDtos;
    }

    @Transactional
    public void leaveGroupChatRoom(Long roomId) {

        // 1. 참여자 객체 삭제
        // 2. 모두가 나간 경우 모든 엔티티 삭제
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room not found"));
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("member not found"));

        if(chatRoom.getIsGroupChat().equals("N")){
            throw new IllegalArgumentException("단체 채팅방이 아닙니다.");
        }

        ChatParticipant chatParticipant = chatParticpantRepository.findByChatRoomAndMember(chatRoom,member).orElseThrow(() -> new EntityNotFoundException("참여자를 찾을 수 없습니다."));

     //   chatRoom.getChatParticipants().remove(chatParticipant);

        chatParticpantRepository.delete(chatParticipant);

        List<ChatParticipant> chatParticipants = chatParticpantRepository.findByChatRoom(chatRoom);

       // if(chatRoom.getChatParticipants().isEmpty()) { //cascade 옵션에 따라 모두 삭제
         if(chatParticipants.isEmpty()) {
            chatRoomRepository.delete(chatRoom);
        }
    }

    public Long getOrCreatePrivateRoom(Long otherMemberId) {

        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("member not found"));
        Member otherMember = memberRepository.findById(otherMemberId).orElseThrow(() -> new EntityNotFoundException("other member not found"));

        // 나와 상대방이 1:1 채팅에 이미 참여하고 있다면 해당 roomId return

        Optional<ChatRoom> privateChatRoom = chatParticpantRepository.findPrivateChatRoom(member.getId(),
                otherMember.getId());

        if(privateChatRoom.isPresent()){
            return privateChatRoom.get().getId();
        }

        // 만약 1:1 채팅방 없을 경우 기존 채팅방 개설
       ChatRoom newRoom = ChatRoom.builder()
               .isGroupChat("N")
               .name(member.getName() + "-" + otherMember.getName())
               .build();

        chatRoomRepository.save(newRoom);

        // 두 사람 모두 참여자로 새롭게 추가

        addParticipantToChatRoom(newRoom, member);
        addParticipantToChatRoom(newRoom, otherMember);

        return newRoom.getId();
    }
}
