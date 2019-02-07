# springboot

# 1. 스프링부트 소개 
- 스프링 부트는 제품수준의 스프링 기반의 application을 만들때 쉽게 만들 수 있음.
- 널리쓰이는 기본설정을 제공함. (원하는대로 쉽고 빠르게 커스터마이징 가능)
- 서드파티 라이브러리도 지본적으로 제공 (ex. tomcat)
- 스프링 개발시 빠르고 폭넓은 환경을 제공
- XML을 이용한 설정을 하지 않음 
- code generation 사용하지 않음

# 2. 스프링부트 원리
```
의존성 관리 이해
-------------
버전을 주지 않는 이유 : spring boot의 dependency-management의 기능
좋은 이유
- 관리하는 의존성의 수가 줄어듦(일이 줄어든다)
- 버전을 명시하면 override하여 가져옴
- parent로 받는 것과 dependencyManagement 선언은 다름
  -> parent 가 더 많은 기능(ex. plugin 등)
- parent로 선언하는 것을 추천  
```

```
자동설정 이해
----------
@SpringBootApplication는 3가지를 합쳐 놓음
1. @ComponentScan
2. @SpringBootConfiguration (@Configuration과 거의 동일)
3. @EnableAutoConfiguration 

SpringBootApplication은 Bean을 두 번 등록한다
1. @ComponentScan
2. @EnableAutoConfiguration로 읽어온 Bean들을 등록함


@ComponentScan
- @Component을 가진 class를 스캔하여 Bean으로 등록.
- @Configuration @Repository @Service @Controller @RestController

@EnableAutoConfiguration
- Spring Meta File
   -> spring-boot resource META-INF 파일안의 spring.factories에 키값에 설정되어 있는 클래스를 설정함.
   -> 전부 등록하는것은 아니고 조건에 따라 등록하기도 하고 안하기도함.(@ConditionalOnXXXXX 사용) 
```

```
Custom AutoConfiguration

1. 의존성 추가
- spring-boot-autoconfigure, spring-boot-autoconfigure-processor
2. @Configuration 설정 파일 추가
3. resource -> META-INF -> spring.factories 파일 추가
4. spring.factories에 EnableAutoConfiguration 추가

해당 방식은 문제점이 있음.
- @EnableAutoConfiguration이 나중에 하기 때문에 Bean을 덮어 쓴다.

해결책(ComponentScan만 적용되도록)
1. @ConditionalOnMissingBean 추가
```

```
내장 서블릿 컨테이너

# 스프링부트는 서버가 아니다.
1. 톰캣 객체 생성
2. 포트 설정
3. 톰캣에 컨택스트 추가
4. 서블릿 생성
5. 톰캣에 서블릿 추가
6. 컨택스트에 서블릿 매핑
7. 톰캣 실행 및 대기

위와 같은 과정을 보다 설정하고 실행해주는것이 스프링의 자동설정
- ServletWebServerFactoryAutoConfiguration (서블릿 웹서버 생성)
- TomcatServletWebServerFactoryCustomizer (서버 커스터마이징)
- DispatcherServletAutoConfiguration (서블릿을 생성 및 등록)
```

```
내장 웹 서버 응용 : 컨테이너와 포트

# 다른 컨테이너(WAS) 사용밥
- spring boot starter web에 dependency가 걸려있는 tomcat을 제거 해야함.
- 다른 서버추가 후 기동 ex) jetty, undertow

# Web server 사용하지 않기
property 파일을 사용
- spring.main.web-application-type=none

# port 변경
- server.port = 7070 (0으로 입력 시 랜덤)
- ApplicationListener<ServletWebServerInitializedEvent>를 사용하여 기동중인 port를 알 수 있음
```

```
내장 웹 서버 응용2 : HTTPS와 HTTP2

# HTTPS 설정하기
server.ssl.key-store=keystore.p12
server.ssl.key-store-password=rlacl15
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias=spring

# HTTP 커넥터 코딩으로 설정하기
- ServletWebServerFactory @Bean 등록

# HTTP2 설정
SSL 적용 필수
server.http2.enabled=true

HTTP2 제약사항
- undertow : https되어 있으면 아무 설정도 필요없음
- tomcat 8.5 복잡잼 (하지마!!)
- tomcat9 + JDK9++은 아무 설정도 필요없음
```

```
독립적으로 실행 가능한 JAR

"그러고보니 JAR 파일 하나로 실행이 가능???"
    - mvn package를 실행하면 JAR파일 "하나가" 생성 됨.
    - spring-maven-plugin이 해주는일 (패키징)
    - 과거에는 uber.jar를 사용
        모든 클래스를(의존성 및 어플리케이션) 하나로 압축하는 방법
        문제점 : 뭐가 어디에서 온건지 뭘 사용하는지 알 수 없음 + 내용은 다르지만 파일명이 같다면??
    - 스프링부트의 전략
        내장 JAR : 기본적으로 자바에는 내장 JAR를 로딩하는 표준적인 방법이 없음
        애플리케이션 클래스와 라이브러리 위치 구분
        JAR 파일을 읽을 수 있는 파일을 만듦(Loader)
            -> org.springframework.boot.loader.jar.JarFile을 사용해서 내장 JAR를 읽는다
            -> org.springframework.boot.loader.Launcher를 사용해서 실행한다.
```

```
스프링부트의 원리 정리
1. 의존성 관리
2. 자동 설정
3. 내장 웹 서버
```

# 3. 스프링부트 활용

핵심 기능과 각종 기술 연동 2가지 파트로 나눠서 진행한다.

스프링부트 핵심 기능
- SpringApplication
- 외부 설정
- 프로파일
- 로깅
- 테스트
- Spring-Dev-Tools

각종 기술 연동
- Web Mvc
- 스프링 데이터
- 스프링 시큐리티
- REST API 클라이언트
- 다루지 않은 내용들

```
SpringApplication 

- Customizng을 하기 위해선 instance 생성 ( new SpringApplication())
- 로그 레벨을 Debug로 하면 어떠한 자동 설정이 되어 있는지(안되어 있는지) 알 수 있음(VM option : -Ddebug)
- 배너 
  -> resources 경로에 banner.txt(jpg png gif)
  -> 다른 경로일 경우 spring.properties에 spring.banner.location 값 설정
- SpringApplicationBuilder 객체로 Builder 패턴 사용 가능.

SpringApplication 2부

Event 등록(EventListener 등록)
- 이벤트는 다양한 시점이 있음
- Application Context 생성 시점에 따라 등록 방법이 다름.
- Application Context가 생성 된 후의 발생하는 이벤트들은 Bean으로 등록하면 동작한다. (생성 전에 발생하는 이벤트는 직접 등록 해줘야함)

WebApplicationType 설정

Application Arguments
- ApplicationArguments를 Bean으로 등록해주니 가져다 사용하면 됨.(JVM 옵션이 아님)

애플리케이션 실행한 뒤 뭔가 실행하고 싶을 때
- ApplicationRunner (추천) 또는 CommandLineRunner
- @Order로 순서 지정 가능
```

```
# 외부 설정

사용 할 수 있는 외부 설정
- properties
- YAML
- 환경 변수
- 커맨드 라인 아규먼트

프로퍼티 우선 순위
1. 유저 홈 디렉토리에 있는 spring-boot-dev-tools.properties
2. 테스트에 있는 @TestPropertySource
3. @SpringBootTest 애노테이션의 properties 애트리뷰트
4. 커맨드 라인 아규먼트
5. SPRING_APPLICATION_JSON (환경 변수 또는 시스템 프로티) 에 들어있는 프로퍼티
6. ServletConfig 파라미터
7. ServletContext 파라미터
8. java:comp/env JNDI 애트리뷰트
9. System.getProperties() 자바 시스템 프로퍼티
10. OS 환경 변수
11. RandomValuePropertySource
12. JAR 밖에 있는 특정 프로파일용 application properties
13. JAR 안에 있는 특정 프로파일용 application properties
14. JAR 밖에 있는 application properties
15. JAR 안에 있는 application properties
16. @PropertySource
17. 기본 프로퍼티 (SpringApplication.setDefaultProperties)

# 외부 설정 2부

여러 프로퍼티를 묶어서 읽어 올 수 있음
빈으로 등록해서 다른빈에 주입 가능
- @EnableCongifurationProperties
- @Component
- @Bean

융통성 있는 바인딩
- context-path, context_path, contextPath, CONTEXTPATH 모두 바인딩 해줌

프로퍼티 타입 컨버전
- @DurationUnit

프로퍼티 값 검증
- @Validated
- @JSR-303 (@NotNull, @NotEmpty ...)

@Value보다는 @ConfigurationProperties을 쓰는것이 더 낫다
```

```
프로파일

@Profile 애너테이션은 어디에??
- @Configuration, @Component

어떤 프로파일을 활성화?
- spring.profiles.active(properties, JVM, application arguments)

프로파일용 프로퍼티 파일도 가능
- application-${profile}.properties

spring.profiles.include로 프로파일 추가 가능
```

```
# 스프링부트 로깅

로깅 퍼사드 vs 로거
- Commons Logging 사용, SLF4j (로거 API들을 추상화해놓은 Interface들) (로깅 퍼사드)
- JUL, Log4j2, Logback (퍼사드를 사용하여 코딩하면 로깅 라이브러리를 바꿔 사용 할 수 있음)
- 스프링부트는 기본적으로 Commons Logging을 사용

스프링부트 5 로거 변경사항
- Spring-JCL
- Commons Logging -> SLF4j or Log4j2
- maven, gradle에 exclude안해도 됨.

스프링부트 로깅
- 기본 포맷
- --debug(일부 핵심 라이브러리만 디버깅 모드) -> embedded container, hibernate, spring boot
- --trace(전부 다 디버깅 모드로)
- 컬러출력 : spring.output.ansi.enabled
- 파일출력 : loggin.file or logging.path
- 로그 레벨 조정 : logging.level.패키지 = 로그 레벨

# 스프링부트 로깅 커스터마이징

logback : logback-spring.xml
log4j2 : log4j2-spring.xml
JUL (비추) : logging.properties

Logback extention
- 프로파일 <springProfile name="프로파일">
- Environment 프로퍼티 <springProperty>
```

```
스프링부트 테스트

시작은 일단 spring-boot-starter-test를 추가!!

@SpringBootTest
- @RunWith(SpringRunner.class)랑 같이 써야 함. 
- 빈 설정 파일은 설정을 안해주나? 알아서 찾습니다. (@SpringBootApplication)
- webEnvironment
    MOCK: mock servlet environment. 내장 톰캣 구동 안 함.
    RANDON_PORT, DEFINED_PORT: 내장 톰캣 사용 함.
    NONE: 서블릿 환경 제공 안 함.


@MockBean
- ApplicationContext에 들어있는 빈을 Mock으로 만든 객체로 교체 함.
- 모든 @Test 마다 자동으로 리셋.

슬라이스 테스트(위에애들은 SpringBootApplication을 다 끌고옴)
- 레이어 별로 잘라서 테스트하고 싶을 때
- @JsonTest
- @WebMvcTest : Conttoller만 등록 가능
- @WebFluxTest
- @DataJpaTest 등

테스트유틸
- OutputCapture (유용)
- TestPropertyValues
- TestRestTemplate
- ConfigFileApplicationContextInitializer
```

```
Spring-Boot-Devtools (안쓸듯...)

- 캐시 설정을 개발환경에 맞게 변경(아무 설정도 안하고 dependencies만 설정해도!)
- classpath에 있는 파일이 변경 될 때 마다 자동으로 재시작
    -> 수동 재시작보다 훨씬 빠름
    -> 리로딩보다는 느리다
- 라이브 리로르 : 리스타트 했을 때 브라우저 자동 리프레시 기능
- 글로벌 설정
    -> ~/spring-boot-devtools.properties
```

```
# Spring Web MVC 1부

스프링 부트 MVC
- 자동 설정으로 제공하는 여러 기본 기능 (차후 학습)

스프링 MVC 확장
- @Configuration + WebMvcCOnfigurer

스프링 MVC 재정의
- @Configuration
- @EnableWebMvc (해당 애너테이션을 선언하면 스프링부트 자동 설정은 적용 되지 않음)

# Spring Web MVC 2부 (HttpMessageConverters)

HttpMessageConverter
- Spring framework에서 제공하는 interface (web mvc에서 사용)
- 요청 본문을 객체로 매핑하거나 객체를 응답 본문으로 매핑하는데 사용됨
- @RequestBody, @ResponseBody

스프링 부트
- 뷰 리졸버 설정 제공
- HttpMessageConvertersAutoConfiguration

# Spring Web MVC 3부 (ViewResolver)

스프링 부트 ContentNegotiatingViewResolver 설정
- 들어오는 요청의 accept header에 따라 응답이 달라짐

# Spring Web MVC 4부 (정적 리소스 지원)

정적 리소스 매핑(/**)
- 기본 리소스 위치
    -> classpath:/static
    -> classpath:/public
    -> classpath:/resources
    -> classpath:/META-INF/resources
    -> spring.mvc.static-path-pattern : 매핑 설정 변경 가능
    -> spring.mvc.static-locations : 리소스 찾을 위치 변경 가능
- Last-Modified 헤더를 보고 304 응답을 보냄. (캐싱)
- ResourcesHttpRequestHandler로 커스터마이징 가능 (WebMvcConfigurer)
    -> 리소스 핸들러를 추가하는 것(위의 기존 애들도 그대로 사용 가능)

# Spring Web MVC 5부 (웹 JAR)

- 부스스트랩, jQuery, vue.js 등 JAR 파일 형태로 추가 할 수 있음.
- Webjar locator core 의존성을 추가하면 버전 명시를 안해도 된다.

# Spring Web MVC 6부 (Index 페이지와 파비콘)

웰컴페이지(루트 요청)
- index.html을 찾아 보고 있으면 제공
- index.템플릿을 찾아 보고 있으면 제공
- 둘 다 없으면 에러페이지

파비콘
- favicon.ico
- 파비콘 만들기 https://favicon.io/

# Spring Web MVC 7부 (Thymeleaf)

템플릿 엔진??
- 주로 View를 만드는데 사용
- Code generation, Email Template 등등

스프링부트가 자동 설정을 지원하는 템플릿 엔진
- FreeMaker
- Groovy
- Thymeleaf
- Mustache

JSP를 권장하지 않는 이유
- JAR 패키징 할 때는 동작하지 않고, WAR 패키징 해야함.
- Undertow는 JSP를 지원하지 않음.

# Spring Web MVC 8부 (HtmlUnit)

htmlUnit : html을 단위테스트 하기 위한 Tool

# Spring Web MVC 9부 (ExceptionHandler)

스프링 @MVC 예외 처리 방법
- @ContollerAdvice
- @ExchangepHandler

스프링부트가 제공하는 기본 예외 처리기
- BasicErrorController
- 커스터마이징 방법 -> ErrorController 구현

커스텀 에러 페이지
- 상태 코드 값에 따라 에러페이지 보여주기
- /src/main/resources/static(template)/error
- 코드와 파일명을 맞춰야함(4xx, 5xx도 가능)
- ErrorViewResolver 구현

# Spring Web MVC 10부 (Spring HATEOAS)

HATEOAS : Hypermedia As The Engine Of Application State

서버 : 현재 리소스와 연관된 링크 정보를 클라이언트에게 제공한다.
클라이언트 : 연관된 링크 정보를 바탕으로 리소스에 접근한다.

# Spring Web MVC 11부 (CORS)

SOP와 CORS
- Single-Origin Policy
- Cross-Origin Resource Sharing
- Origin이란? 아래 세개를 조합 한 것!
    -> URI 스키마 (http, https)
    -> hostname
    -> port
    
스프링 MVC @CrossOrigin
- @Controller나 @RequestMapping에 추가
- @WebMvcConfigurer를 사용하여 글로벌 설정    
```

```
# 스프링 데이터 1부 (스프링 부트가 지원하는 스프링 데이터 기술)

SQL DB
- 인메모리 데이터베이스 지원
- DataSource 설정
- DBCP 설정
- JDBC 사용하기
- 스프링 데이터 JPA 사용하기
- JOOQ 사용하기
- 데이터베이스 초기화
- 데이터베이스 마이그레이션 툴 연동하기

NoSQL
- Redis (key/value)
- MongoDB (Document)
- Neo4J (Graph)

NoSQL (학습하지는 않음)
- Gemfire (IMDG)
- Solr (Search)
- Elasticsearch (Search & Analytics)
- Cassandra
- Couchbase
- LDAP
- InfluxDB

# 스프링 데이터 2부 (인메모리 데이터베이스)

스프링부트가 지원하는 인메모리 데이터베이스
- H2
- HSQL
- Derby

Spring-jdbc가 클래스패스에 있으면 자동으로 빈 설정을 해줌
- DataSource
- JdbcTemplate

인메모리 데이터베이스 기본 연결 정보 확인 방법
- JdbcProperties 클래스에서 확인 가능
- URL : "testdb"
- username : "sa"
- password : ""

H2 콘솔 사용하는 방법
- spring-boot-devtools 추가
- spring.h2.console.enabled=true 추가
- /h2-console로 접속

# 스프링 데이터 3부 (MySQL)

지원하는 DBCP
1. HikariCP (기본)
- https://github.com/brettwooldridge/HikariCP#frequently-used
2. Tomcat CP
3. Commons DBCP2

DBCP 설정
- spring.datasource.hikari.*
- spring.datasource.tomcat.*
- spring.datasource.db

MySQL 커넥터 의존성 추가
- implementation 'mysql:mysql-connector-java'

MySQL 추가 (도커 사용)
- docker run -p 3306:3306 -–name mysql_boot -e MYSQL_ROOT_PASSWORD=1 -e MYSQL_DATABASE=springboot -e MYSQL_USER=asyu -e MYSQL_PASSWORD=pass -d mysql
- docker exec -i -t mysql_boot bash
- mysql -u asyu -p

# 스프링 데이터 4부 (PostgreSQL)

docker run -p 5432:5432 -e POSTGRES_PASSWORD=pass -e POSTGRES_USER=asyu -e POSTGRES_DB=springboot --name postgres_boot -d postgres

docker exec -i -t postgres_boot bash

su - postgres

psql springboot

데이터베이스 조회
\list

테이블 조회
\dt

쿼리
SELECT * FROM account;

# 스프링 데이터 5부 (스프링데이터 JPA 소개)

ORM(Object Relational Mapping)과 JPA(Java Persistence API)
- 객체와 릴레이션 매핑할 때 발생하는 개념적 불일치를 해결하는 프레임워크
- JPA : ORM을 위한 자바 EE 표준

스프링 데이터 JPA
- @Repository
- 쿼리 메서드 자동 구현
- @EnableJpaRepositories (스프링부트가 자동으로 설정 해줌)
- SDJ -> JPA -> Hibernate -> Datasource

# 스프링 데이터 6부 (스프링데이터 JPA 연동)

스프링데이터 JPA 의존성 추가
- implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

스프링데이터 JPA 사용하기
- @Entity
- Repository 생성

스프링데이터 리파지토리 테스트 만들기
- 슬라이스 테스트를 위해선 만드시 인메모리 데이터베이스가 필요함(@DataJpaTest, H2 DB)

# 스프링 데이터 7부 (데이터베이스 초기화)

JPA를 사용한 데이터베이스 초기화
- spring.jpa.hibernate.ddl-auto=update
- spring.jpa.generate-ddl=true 설정해야 동작한다.

SQL 스크립트를 사용한 데이터베이스 초기화
- schema.sql 또는 schema-${platform}.sql
- data.sql 또는 data-${platform}.sql
- ${platform}값은 spring.datasource.platform으로 설정 가능

# 스프링 데이터 8부 (데이터베이스 마이그레이션)

의존성 추가
- implementation 'org.flywaydb:flyway-core'

마이그레이션 디렉토리
- db/migration 또는 db/migration/{vendor}
- spring.flyway.locations로 변경 가능

마이그레이션 파일 이름
- V숫자__이름.sql
- V는 꼭 대문자로.
- 숫자는 순차적으로 (타임스탬프 권장)
- 숫자와 이름 사이에 언더바 두 개.
- 이름은 가능한 서술적으로.

# 스프링 데이터 9부 (Redis)

캐시, 메시지 브로커, 키/밸류 스토어 등으로 사용 가능

Redis 설치 및 실행 (도커 사용)
- docker run -d -p 6379:6379 --name redis_boot redis
- docker exec -it redisboot redis-cli

스프링 데이터 Redis
- https://projects.spring.io/spring-data-redis/
- StringRedisTemplate 또는 RedisTemplate
- extends CrudRepository

Redis 주요 커맨드
- https://redis.io/commands
- keys *
- get {key}
- hgetall {key}
- hget {key} {column}
  
커스터마이징
- spring.redis.* (application.properties)

# 스프링 데이터 10부 (MongoDB)

MongoDB는 JSON 기반의 도큐먼트 데이터베이스(스키마가 없는 것이 특징)

의존성 추가
- implementation 'org.springframework.boot:spring-boot-starter-data-mongodb

MongoDB 설치 및 실행 (도커 사용)
- docker run -p 27017:27017 -d --name mongo_boot mongo
- docker exec -it mongo_boot bash

스프링 데이터 몽고DB
- MongoTemplate, MongoRepository
- 내장형 MongoDB (테스트용)
    -> de.flapdoodle.embed:de.flapdoodle.embed.mongo
- @DataMongoTest

# 스프링 데이터 11부 (Neo4j)

Neo4j는 노드간의 연관 관계를 영속화하는데 유리한 그래프 데이터베이스이다.

의존성 추가
- implementation 'org.springframework.boot:spring-boot-starter-data-neo4j'
- 하위호환성이 없으니 조심 할 것

Neo4j 설치 및 실행 (도커 사용)
- docker run -d -p 7474:7474 -p 7687:7687 --name neo4j_boot neo4j
- http://localhost:7474/browser

스프링데이터 Neo4j
- Neo4jTemplate (deprecated)
- SessionFactory
- Neo4jRepository

# 스프링 데이터 12부 (스프링데이터 정리)
- https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-sql
```

```
# 스프링 시큐리티 1부 (Starter-Security)

스프링 시큐리티
- 웹 시큐리티
- 메서드 시큐리티
- 다양한 인증방법 지원
    -> LDAP, 폼 인증, Basic 인증, OAuth 등...

스프링부트 시큐리티 자동 설정
- SecuriryAutoConfiguration
- UserDetailsServiceAutoConfiguration
- spring-boot-starter-securiry (스프링 시큐리티 5.* 의존성 추가)
- 모든 요청에 인증 필요
- 기본 사용자 생성
    -> username : user
    -> password : 어플리케이션 실행 때 마다 랜덤값 생성(콘솔 출력)
    -> spring.securiry.user.name
    -> spring.securiry.user.password
- 인증 관련 각종 이벤트 발생
    -> DefaultAuthenticationEventPublisher 빈 등록
    -> 다양한 인증 에러 핸들러 등록 가능
    
# 스프링 시큐리티 2부 (시큐리티 설정 커스터마이징)

웹 시큐리티 설정!

UserDetailsServie 구현
- https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#jc-authentication-userdetailsservice

PasswordEncoder 설정 및 사용
- https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#core-services-password-encoding
```
