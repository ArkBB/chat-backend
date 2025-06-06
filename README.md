# 🚀실시간 채팅 시스템

실시간 채팅 기능 구현 프로젝트입니다. 사용자들이 스트리밍 플랫폼처럼 실시간으로 메시지를 주고받을 수 있는 환경을 제공합니다.

## ✨ 주요 기능

*   **실시간 메시지 송/수신:** WebSocket (STOMP 프로토콜 활용)을 통한 실시간 메시지 통신
*   **채팅 메시지 표시:** 사용자들에게 수신된 메시지 리스트 표시
*   **메시지 전송:** 사용자의 메시지 입력 및 서버 전송
*   **(추가될 기능):** 다중 채팅방 지원, 사용자 목록 표시, 메시지 기록 조회 등

## 🛠️ 기술 스택

*   **백엔드:**
    *   Java
    *   Spring Boot
    *   Spring WebSocket (STOMP)
    *   Spring Security (인증/권한)
    *   Spring Data JPA
*   **프론트엔드:**
    *   Vue.js
    *   SockJS (WebSocket 폴백 라이브러리)
    *   webstomp-client (STOMP 클라이언트 라이브러리)
    

## 🚦 프로젝트 설정 및 실행

프로젝트를 로컬 환경에서 실행하기 위한 가이드입니다.

1.  **저장소 클론:**
    ```bash
    git clone [저장소 URL]
    cd [프로젝트 폴더 이름]
    ```

2.  **백엔드 설정 및 실행:**
    *   `backend` 폴더로 이동
    *   필요한 의존성 설치 (Maven 또는 Gradle 사용 시 자동으로 진행)
    *   데이터베이스 설정 (예: `application.properties` 또는 `application.yml` 파일 수정)
    *   Spring Boot 애플리케이션 실행:
        ./gradlew bootRun # Gradle
        ```

3.  **프론트엔드 설정 및 실행:**
    *   `frontend` 또는 해당 프론트엔드 폴더로 이동
    *   의존성 설치:
        ```bash
        npm install
        # 또는
        yarn install
        ```
    *   개발 서버 실행:
        ```bash
        npm run serve
        # 또는
        yarn serve
        ```

4.  **접속:** 브라우저에서 `http://localhost:8080` (또는 설정된 포트)으로 접속하여 채팅 기능을 확인합니다.

## 🤔 개발 과정에서 만났던 의문점들

프로젝트를 진행하며 부딪혔던 문제들과 그에 대한 고민들을 기록합니다. 

*   **[STOMP 보안 - 인증]:** 인증된 사용자만 웹소켓 연결을 맺고 메시지를 주고받게 하려면 Spring Security 설정을 어떻게 해야 할까? STOMP 메시지에 대한 보안 규칙은 어디서 정의해야 할까? (`@EnableWebSocketSecurity`, `MessageSecurityMetadataSourceRegistry`, `ChannelInterceptor`)
*   **[Spring Security - ChannelInterceptor]:** `configureClientInboundChannel` 메소드에서 `registration.interceptors()`를 통해 등록하는 `ChannelInterceptor`는 정확히 어떤 역할을 하며, 인증/권한 부여 로직과 어떤 관계가 있을까?


## ✅ 해결점 및 학습 내용

위에서 제기된 의문점들을 해결하기 위해 시도했던 방법들과 최종 해결책, 그리고 이를 통해 배운 내용들을 정리합니다.
*   **[STOMP 보안 - 인증]:** 인증된 사용자만 웹소켓 연결을 맺고 메시지를 주고받게 하려면 Spring Security 설정을 어떻게 해야 할까? STOMP 메시지에 대한 보안 규칙은 어디서 정의해야 할까? (`@EnableWebSocketSecurity`, `MessageSecurityMetadataSourceRegistry`, `ChannelInterceptor`)
*   **[Spring Security - ChannelInterceptor]:** `configureClientInboundChannel` 메소드에서 `registration.interceptors()`를 통해 등록하는 `ChannelInterceptor`는 정확히 어떤 역할을 하며, 인증/권한 부여 로직과 어떤 관계가 있을까?

## ⏭️ 향후 개선 계획

프로젝트를 더 발전시키기 위한 아이디어들입니다.

*   다중 채팅방 기능 구현
*   사용자 목록 실시간 표시
*   이전 채팅 기록 불러오기 (페이징 처리 등)
*   메시지 타입 확장 (이미지, 파일 등)
*   관리자 기능 (메시지 삭제, 사용자 차단)
*   사용자 인증 및 권한 부여 강화 (Spring Security 연동)
*   테스트 코드 작성

## 📄 라이선스


## 📞 문의


