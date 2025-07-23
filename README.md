# 🛠️ Workflow Approval System

- Spring Boot 기반의 결재 · 승인 워크플로우 시스템.
 :JWT 인증 및 권한 처리, 상태 기반 흐름 관리, 동시 잠금 제어 등
  백엔드 설계 역량을 강조하기 위한 포트폴리오 프로젝트

## 주요 기능
- 사용자 회원가입 및 JWT 기반 로그인
- 신청서 작성 → 잠금/해제 → 제출 → 승인/반려
  TODO : 신청서 조회 시 다른 사용자는 승인/반려 처리 불가
- 승인/반려 시 메모 작성 기능

- Swagger 연동으로 API 테스트 편의 제공
- H2 Database 사용 (로컬 테스트용)

## 기술 스택
- Java 17
- Spring Boot 3.5.3 (with JPA)
- Maven
- JWT (io.jsonwebtoken)
- H2 Database
- Swagger / OpenAPI 3

## 실행 및 인증 테스트 방법
1. `/auth/signup` 으로 회원가입
2. `/auth/login` 으로 JWT 토큰 발급
3. Swagger 우측 상단 🔐 버튼 클릭 → `Bearer {토큰}` 입력
4. 이후 신청서 관련 API 사용 가능

---
TODO : 확장 계획 > 풀스택 포트폴리오용 블로그와 연동하여 게시글을 운영자에게 승인받아야 공개되도록 할 예정
