package com.example.chatserver.member.service;


import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.domain.Role;
import com.example.chatserver.member.dto.MemberListResDto;
import com.example.chatserver.member.dto.MemberLoginReqDto;
import com.example.chatserver.member.dto.MemberSaveReqDto;
import com.example.chatserver.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member create(MemberSaveReqDto memberSaveReqDto) {
        if (memberRepository.findByEmail(memberSaveReqDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Member newMember = Member.builder()
                .name(memberSaveReqDto.getName())
                .email(memberSaveReqDto.getEmail())
                .password(passwordEncoder.encode(memberSaveReqDto.getPassword()))
                .role(Role.USER)
                .build();

        return memberRepository.save(newMember);
    }

    public Member login(MemberLoginReqDto memberLoginReqDto) {

        Member member = memberRepository.findByEmail(memberLoginReqDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));
        if (passwordEncoder.matches(memberLoginReqDto.getPassword(), member.getPassword())) {
            return member;
        }

        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

    }

    public List<MemberListResDto> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberListResDto::of)
                .collect(Collectors.toList());
    }

}