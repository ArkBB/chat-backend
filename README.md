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

## 🤔 개발 기록 정리
https://pbh-studys-organization.gitbook.io/ark/chat-service/stomp


