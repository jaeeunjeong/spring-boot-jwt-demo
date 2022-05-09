# Spring-Boot-JWT
> 스프링 시큐리티를 적용하여 로그인 서버를 구현

## Index
- 주요 기능
- 관련 개념
- 개발 환경
- 디렉토리 구조
- ~~API 설명~~
- Database Schema

### 주요 기능
JWT를 이용하여 인증 및 인가를 수행한다.
### 관련 개념
principal
Authenticate, Authentication
Authorize
권한
### 개발 환경
- Spring Boot 2.6.4
- Gradle
- Spring Security
- Windows
- JDK 8
- H2
### 패키지 구조
```
┌── README.md                                - 리드미 파일
│
├── src/                                     - 어플리케이션 폴더
│   ├── main
│   │   ├── java/me/toyproject/loginjwt      - JWT를 이용한 로그인 구현 
│   │   │   ├── config/                      - 설정 폴더(시큐리티 등)
│   │   │   ├── controller/                  - 요청에 따라 응답하기 위한 객체의 경로 지정 디렉토리
│   │   │   ├── dto/                         - 요청에 맞게 데이터를 가져오는 디렉토리
│   │   │   ├── entity/                      - DB와 직접적으로 맵핑된 객체가 있는 디렉토리
│   │   │   ├── jwt/                         - JWT 관련 디렉토리
│   │   │   ├── repository/                  - 데이터를 실행하게 할 쿼리가 있는 인터페이스의 디렉토리
│   │   │   ├── service/                     - 요청에 맞게 데이터를 가공하는 디렉토리
│   │   │   ├── util/                        - 어플리케이션의 핵심 로직 외의 어플리케이션 디렉토리
│   │   │   └── LoginJwtApplication.java     - 어플리케이션 실행 파일
│   │ 
│   ├── resources/                           - .java를 제외한 어플리케이션 개발 시 필요한 파일이 있는 디렉토리
│   │   ├── static/                          - 
│   │   ├── templates/                       - 
│   │   ├── application.yml                  - 외부 리소스를 사용 및 설정을 위한 파일
│   │   └── data.sql                         - 서버가 시작될 때마다 테이블이 새로 생성되기에 편의를 위해 초기 데이터를 미리 만들어서 넣어줌.
│   │ 
│   └── test/                                - 테스트 폴더
│       ├── java/me/toyproject/loginjwt      - JWT를 이용한 로그인 테스트 구현
│       └── LoginJwtApplicationTest.java     - 클라이언트 테스트 파일 
│ 
├── build.gradle                             - 의존성/ 플러그인 설정을 위한 스크립트
├── gradlew                                  - 맥용 실행 배치 스크립트
├── gradlew.bat                              - 윈도우용 실행 배치 스크립트
└── settings.gradle                          - 프로젝트 구성 정보 스크립트
```
### Database Schema
#### User
|필드 | 타입 | Unique | Key| DESC|
|:----:|:-----:|:-----:|:-----:|:----:|
|userId|  Long  | |PK|사용자를 구분할 기본키|
|username| String|unique| |사용자 아이디|
|password| String| | |사용자 비밀번호|
|nickname| String|  | |사용자 닉네임|
|activated| boolean| | |사용자 활성 상태|
#### Authority
|필드 | 타입 | Unique | Key| DESC|
|:----:|:-----:|:-----:|:-----:|:----:|
|authorityName|  String  | |PK| 토큰 정보|

### 참고
https://silvernine.me/wp/?p=1078  
https://yunb2.tistory.com/3?category=1089736
