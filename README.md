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


```

