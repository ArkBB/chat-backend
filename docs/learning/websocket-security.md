
- 비로그인 회원도 채팅은 볼 수 있어야한다.
- 비로그인 회원은 채팅을 작성할 수는 없다.
- 로그인 회원은 채팅을 볼 수 있고, 작성도 가능하다.


비로그인 회원도 채팅은 볼 수 있어야 하기 때문에, 웹소켓 연결 자체는 맺어져야 한다.

-> 웹소켓 핸드셰이크가 시작되는 HTTP 엔드포인트에 대한 HTTP Security 설정을 permitAll()로 해줘야 한다.

STOMP 메시지 권한 부여
(MessageSecurityMetadataSourceRegistry)

