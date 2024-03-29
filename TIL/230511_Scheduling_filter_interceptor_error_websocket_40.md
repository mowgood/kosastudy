## [day40] 2023.05.11 목 학습 내용 정리
1. Spring Scheduling(TASK)
2. 필터와 인터셉터
3. 오류 처리
4. 웹소켓
---
## 1. Spring Scheduling(TASK)  
특정 시간에 반복적으로 처리되는 코드를 스케줄링한다.  
이 때 반복적으로 수행되는 코드를 TASK라고 한다.  

### 환경 설정
 - dependencies에 스케줄링 관련 API에 대한 의존성 정보를 추가한다
    ```
    implementation 'org.springframework.boot:spring-boot-starter-quartz'
    testImplementation 'org.springframework.boot:spring-boot-starter-test
    ```
- XXXApplication 클래스(실행 파일)에 @EnableScheduling을 추가한다.  

### Task 기능의 메서드 정의
설정된 주기(스케줄링)에 맞춰서 호출되는 Task 메서드 앞에 @Scheduled 어노테이션을 아래 속성중 하나를 정의하여 추가한다
    - cron : CronTab에서의 설정과 같이 cron = = "10 30 12 * * 5"와 같은 설정이 가능하다.
    - fixedDelay  이전에 실행된 Task의 종료시간으로부터 정의된 시간만큼 지난 후 Task를 실행한다.
    - fixedRate : 이전에 실행된 Task의 시작시간으로부터 정의된 시간만큼 지난 후 Task를 실행한다.

### Cron 표현식
\*  초(0~59)
\*  분(0~59)
\*  시(0~23)
\*  일(1~31)
\*  월(1~12)
\*  요일(0~6)
\*  년도(생략 가능)

    - 특수 문자 
        - \* : 모든 값(ex. 매시, 매일, 매주)
        - ? : 특정 값이 아닌 어떤 값이든 상관 X
        - \- : 범위를 지정
        - , : 여러 값을 지정
        - / : 증분값, 즉 초기값과 증가치를 설정할 때
        - L : 지정할 수 있는 범위의 마지막 값 표시
        - W : 가장 가까운 평일(weekday)을 설정
        - \# : N번째 특정 요일을 설정


### 스프링 스케줄링 예시
```java
package com.example.springedu.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class SpringSchedulerTest {	
	@Scheduled(cron = "5 27 10 * * 4")// 초, 분, 시, 일, 월, 요일(0:일요일)
//	@Scheduled(fixedDelay = 5000) // 5초에 한 번씩
	public void scheduleRun() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = 
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("**** 스케줄 실행 : " + 
				dateFormat.format(calendar.getTime()));
		
	}
}
```

## 2. 필터와 인터셉터  
<img src="https://github.com/mowgood/kosastudy/blob/main/TIL/img/스프링동작과정.PNG"> 
<br>   
공통적인 여러 작업을 대신 처리하며 개발시 중복된 코드를 제거할 수 있다.  

- 필터  
    - J2EE 표준 스펙 기능
    - DispatcherServlet에 요청이 전달되기 전/후에 url 패턴에 맞는 모든 요청에 대한 부가작업을 처리할 수 있는 기능을 제공한다.  
    - 스프링 범위 밖에서 처리된다.
    - 스프링 컨테이너가 아닌 톰캣과 같은 웹 컨테이너(서블릿 컨테이너)에 의해 관리가 된다.(스프링 빈으로 등록은 된다)
    - **DispatcherServlet 전/후에 처리한다.**
    - 스프링과 관련 없는 기능을 처리할 때 사용한다.
    - 필터 등록 방법
        ```java
        @Component
        @Slf4j
        @Order(2)
        public class TestFilter1 implements Filter {
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                log.info("[필터1] 요청 자원 수행 전");
                chain.doFilter(request, response);
                log.info("[필터1] 요청 자원 수행 후");
            }
        }
        ```

    ### 필터 메서드 3가지  
    필터를 추가하기 위해서 javax.servlet의 Filter 인터페이스를 구현(implements)해야 한다.
    ```java
    public interface Filter {
        public default void init(FilterConfig filterConfig) throws ServletException {}
        public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {}
        public default void destroy() {}
    }
    ```
    1. init 메서드  
    필터 객체를 초기화하고 서비스에 추가하기 위한 메서드.  
    웹 컨테이너가 1회 init 메서드를 호출하여 필터 객체를 초기화하면 이후의 요청들은 doFilter를 통해 처리된다.

    2. doFilter 메서드  
    url-pattern에 맞는 모든 HTTP 요청이 DispatcherServlet으로 전달되기 전에 웹 컨테이너에 의해 실행되는 메서드.  
    doFilter의 파라미터인 FilterChain은 doFilter를 통해 다음 대상으로 요청을 전달하게 된다. chain.doFilter() 전/후에 필요한 처리 과정을 넣어줌으로써 원하는 처리를 진행할 수 있다.

    3. destroy 메서드  
    필터 객체를 서비스에서 제거하고 사용하는 자원을 반환하기 위한 메서드. 이는 웹 컨테이너에 의해 1번 호출되며 이후에는 doFilter에 의해 처리되지 않는다.

- 인터셉터
    - Spring이 제공하는 기술
    - DispatcherServlet이 컨트롤러를 호출하기 전과 후에 요청과 응답을 참고하거나 가공할 수 있는 기능을 제공한다. 
    - 웹 컨테이너(서블릿 컨테이너)에서 동작하는 필터와 달리 인터셉트는 스프링 컨텍스트에서 동작한다.
    - 스프링 컨테이너 내에서 동작하므로 필터를 거쳐 프론트 컨트롤러인 **DispatcherServlet이 요청을 받은 이후에 동작한다.**

    ### 인터셉터 메서드 3가지
    1. preHandle() 메서드  
    컨트롤러가 호출되기 전에 실행된다. 그렇기 때문에 컨트롤러 이전에 처리해야 하는 전처리 작업이나 요청 정보를 가공하거나 추가하는 경우에 사용할 수 있다.  
    2. PostHandle() 메서드  
    컨트롤러를 호출된 후에 실행된다. 따라서 컨트롤러 이후에 처리해야 하는 후처리 작업이 있을 때 사용할 수 있다.  
    컨트롤러 하위 계층에서 작업을 진행하다 중간에 예외가 발생하면 호출되지 않는다.
    3. afterCompletion() 메서드  
    모든 뷰에서 최종 결과를 생성하는 일을 포함해 모든 작업이 완료된 후에 실행된다.  
    요청 처리 중에 사용한 리소스를 반환할 때 사용하기에 적합하다.  
    PostHandle과 달리 컨트롤러 하위 걔층에서 작업을 진행하다 중간에 예외가 발생해도 반드시 호출된다.  
        ```java
        public interface HandlerInterceptor {
            default boolean preHandle(HttpServletRequest request, 
                    HttpServletResponse response, Object handler) throws Exception { 
                return true; // true일 경우 controller의 hanlder 메서드가 수행된다.
            }
            default void postHandle(HttpServletRequest request, HttpServletResponse response, 
                Object handler, @Nullable ModelAndView modelAndView) throws Exception {
            }
            default void afterCompletion(HttpServletRequest request, HttpServletResponse 
                response, Object handler, @Nullable Exception ex) throws Exception {
            }
        }
        ```
        preHandle의 handler 파라미터 : 핸들러 매핑이 찾아준 컨트롤러 빈에 매핑되는 HanlderMethod라는 새로운 타입의 객체로, @RequestMapping이 붙은 메서드의 정보를 추상화한 객체이다.

### 인터셉터의 등록  
```java
package com.example.springedu.config;
import com.example.springedu.interceptor.TestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
 @Override
 public void addInterceptors(InterceptorRegistry registry) {
 registry.addInterceptor(new TestInterceptor())
 .addPathPatterns("/hello");
 /*
 registry.addInterceptor(인터셉터객체)
 .addPathPatterns("/*") - 모든 Path 적용
 .addPathPatterns("/sample") - /sample Path 에 대해서만 적용
 .excludePathPatterns("/sample"); - /sample Path 에 대해서만 제외
 */
 }
}
```

### 필터와 인터셉터
|대상|필터|인터셉터|
|:---:|:---:|:---:|
|관리되는 컨테이너|서블릿 컨테이너|스프링 컨테이너|
|스프링의 예외처리 여부|X|O|
|Request/Response 객체 조작 가능 여부|O|X|
|용도|Spring과 분리되어야 하는 기능|Contoller로 넘겨주는 정보(데이터)의 가공|

## 3. 오류 처리  
 - @ExceptionHandler :  
 스프링 컨트롤러에서 정의한 메서드(@RequestMapping)에서 기술한 예외가 발생되면 자동으로 받아낼 수 있다. 
 - @ControllerAdvice :  
 @ControllerAdvice는 스프링 3.2 이상에서 사용가능 하며 @Controller나 
@RestController 에서 발생하는 예외 등을 catch하는 기능을 가지고 있다. 클래스 위에
@ControllerAdvice를 붙이고 어떤 예외를 잡아낼 것인지 내부 메서드를 선언하여 메서드 상단에
@ExceptionHandler(예외클래스명.class)와 같이 기술한다. 

## 4. 웹소켓
HTML5 표준 기술로, HTTP 환경에서 클라이언트와 서버 사이에 하나의 TCP 연결을 통해 실시간으
로 전이중 통신을 가능하게 하는 프로토콜이다. 여기서 전이중 통신이란, 일방적인 송신 또는 수신만
이 가능한 단방향 통신과 달리 가정에서의 전화와 같이 양방향으로 송신과 수신이 가능한 것을 말한
다. 양방향 통신이 아닌 단방향 통신의 예로는 텔레비전 방송, 라디오를 들 수 있는데, 데이터를 수
신만 할 수 있고, TV나 라디오를 통해 데이터를 보낼 수 없다.
실시간 알림, 실시간 채팅 등 실시간이라는 키워드가 들어가는 기능들을 위해서는 대부분 웹 소켓 기
술이 필요하다.

### 웹소켓 통신 방식


