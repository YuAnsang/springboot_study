s# springboot

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

