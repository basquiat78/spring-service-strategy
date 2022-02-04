# spring-service-strategy
스프링에서 서비스를 인터페이스로 갈아끼우기

## 인터페이스 활용을 통한 서비스 갈아끼우기

최근의 프로젝트들의 모습을 보면 Strategy Pattern 을 이용한 서비스를 작성하는 경우가 많지 않다.
도메인 단위로 역할을 나누고 그에 맞춰서 서비스 로직을 작성하는 경우가 많아서이기도 한데 초창기 스프링 프레임워크 2.x 또는 3.x대의  
경우에는 전략 패턴을 필요로 하지 않는 경우에도 불구하고 마치 관습처럼 나눠서 로직을 작성하던 시기가 있었다.

~~남들이 그렇게 쓰다보니....~~

예를 들면 인터페이스에 메소드를 정의하고 그것을 구현하는 서비스를 말할 수 있는데 다음 코드로 한번 살펴보자.

```
public interface TestService {
    String getValue();
}

public class TestServiceImpl implements TestService {

    @Override
    public String getValue() {
        return "Hello!";
    }

}
```

이런 식의 코드를 의미없이 사용해왔다.

생각해보면 이 코드 자체만을 보면 인퍼페이스를 만들고 그 인터페이스를 구현해야하는 수고를 할 이유가 전혀 없다.

인터페이스를 두는 이유는 무엇일까? 장황하게 설명하기 힘들지만 자바 펀더멘탈을 공부할 때 햄버거 가게를 많이 인용하는데

예를 들면 고객이 햄버거를 주문할 때 고객의 입장에서 햄버거를 어떻게 만드는지 중요할까?

~~물론 위생적으로 만들고 좋은 재료를 사용해서 만들어야 하는 것은 중요하다!~~

고객 입장에서는 만드는 곳에서 어떤 방식으로 만들든 중요한게 아니다. 

또한 1명이 만들든 2명이든 3명이 만들든 상관없다.

그저 자신이 주문한 햄버거가 정확하게 나오면 그만인 것이다.

어느 브랜드의 매장을 가더라도 주문한다는 행위는 바뀌지 않는다. 다만 브랜드마다 햄버거를 만드는 방식, 사용 재료는 다를 수 있다.

즉, 고객은 주문하다는 행위를 통해서 뒤에서 어떤 로직, 어떤 생산 방식을 이용하든 요청한 결과를 정확하게 받는것에 초점을 맞춰져 있다.

그렇다면 인터페이스를 통해 서비스를 갈아끼운다는 것은 어떤 것일까?

아마도 이 방식은 스프링을 좀 공부하고 자바를 공부한 사람이라면 누구나 알 수 있는 방식이지만 또 의외로 잘 모르는 경우가 많다.

참고로 인터페이스를 활용하게 되면 IDE를 통해서도 알 수 있는 부분인데 인터페이스를 구현하지 않으면 IDE가 해당 인터페이스를 구현하라고
뻘겋게 뜨는 것을 확인할 수 있다.

## 시나리오

다음과 같은 컨트롤러가 있다고 가장을 해보자.

```
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StrategyController {

    private final StrategyService strategyService;

    /**
     * 심플하게 스트링 값 하나 던져준다.
     * @return String
     */
    @GetMapping("/strategy/value/{value}")
    public String getStrategy(@PathVariable("value")String value) {
        return strategyService.getValue(value);
    }

}

public class StrategyService {
    public String getValue(String value) {
        return "My Value is " + value;
    }
}

```

그런데 어떤 요청 사항으로 다음과 같이 스트링 값을 반환하는 서비스가 필요하고 이 서비스를 이용해야한다면

```
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StrategyController {

    private final StrategyService strategyService;

    /**
     * 심플하게 스트링 값 하나 던져준다.
     * @return String
     */
    @GetMapping("/strategy/value/{value}")
    public String getStrategy(@PathVariable("value")String value) {
        return startegyService.getValue(value);
    }

}

public class StrategyService {
    public String getValue(String value) {
        return "My Value is " + value + ", OK";
    }
}

```
정말 단순하게 그냥 해당 서비스를 위 코드처럼 수정하면 된다.

그런데 기존 서비스 로직도 필요하고 새로운 서비스 로직도 필요하다고 한다면

```
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StrategyController {

    private final NewStrategyService newStrategyService;

    /**
     * 심플하게 스트링 값 하나 던져준다.
     * @return String
     */
    @GetMapping("/strategy/value/{value}")
    public String fetchValue(@PathVariable("value")String value) {
        return newStartegyService.getValue(value);
    }

}

/**
 * 기존 서비스 코드
 */
public class OldStrategyService {
    public String getValue(String value) {
        return "My Value is " + value;
    }
}

/**
 * 새로운 서비스 코드
 */
public class NewStrategyService {
    public String getValue(String value) {
        return "My Value is " + value + ", OK";
    }
}

```
위와 같은 방식으로 서비스를 나눈다거나 아니면?

```
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StrategyController {

    private final StrategyService strategyService;

    /**
     * 심플하게 스트링 값 하나 던져준다.
     * @return String
     */
    @GetMapping("/strategy/{value}")
    public String fetchValue(@PathVariable("value")String value) {
        return startegyService.getNewValue(value);
    }

}

public class StrategyService {
    public String getOldValue(String value) {
        return "My Value is " + value;
    }
    
    public String getNewValue(String value) {
        return "My Value is " + value + ", OK";
    }
}

```
기존 서비스에 새로운 메소드를 추가하는 방식을 고려해 볼 수 있다.

하지만 이런 요청 사항이 앞으로 많아질 것이라고 본다면 어떻게 할 것인가?

그때마다 저런 방식으로 하는 것은 차후 유지보수 측면에서 좋지 않다.

지금이야 예제 자체가 단순하기 때문에 문제될 것이 없어 보이지만 언제나 '소프트웨어의 가치'에 대한 생각이나
앞으로의 로직의 확장이나 어플리케이션이 거대해진다면 한번쯤은 고민하게 될 문제이다.

그렇다면 이런 의문이 들것이다.

'너 님이 처음 예제에서 보여준 인터페이스를 구현하는 방식으로 어떻게 해결 할 수 있는데?'

스프링에서 빈을 등록하는 방식이 무엇인지 여러분들은 잘 알 것이다.

보통 Configuration을 통해서 필요한 빈들을 @Bean을 통해서 등록하게 된다.

또한 같은 객체의 Bean을 등록할 경우에는 아이디를 부여해서 사용하는 방식을 사용한다.

예를 들면 redisTemplate를 사용하는 경우 특정 목적에 따라서 정의해서 따로 사용하고 싶은 경우가 발생한다.

아래 코드를 살펴보자.

```
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedisConfiguration {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean("objRedisTemplate")
    public RedisTemplate<String, Object> objectRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(new ObjectMapper()));
        return redisTemplate;
    }

}
```

보통 아이디를 부여하지 않는다면 메소드명을 아이디로 부여하는데 위에서 볼 수 있듯이 메소드명을 따라가기 싫다면
@Bean에 이름을 통해 명시적으로 아이디를 부여할 수 있다.

뜬금없이 이 이야기를 하는 이유는 이제부터 예제 코드를 통해서 천천히 알게 될것이다.

가장 좋은 방법은 기존의 로직을 변경하지 않는 것이다.

그렇다면 어떻게 해야 할까?

가장 무식한 방법은 맨처음 시도했던 새로운 서비스 객체를 만들고 같은 메소드를 만들어서 구현하는 방법이다.

하지만 이 방법은 협업하는 입장에서는 변경될 소지가 많다.

이유는 개발자가 메소드 명을 어떻게 만들지 모르기 때문이다. 만일 이 두개의 서비스를 각기 다른 상황에서 사용하게
된다면 더 문제가 발생한다.

따라서 다음과 같이 인터페이스를 제공하고 이것을 구현하게 만드는 것이다.

```
public interface StrategyService {
    String getValue(String value);
}
```
다음과 같이 기존의 코드를 활용해서 하나의 인터페이스를 제공한다.

헬로 서비스를 구현한다고 가정을 해보자.

예전에는 이 서비스는 구현체라는것을 명시하기 위해서 HelloServiceImpl처럼 클래스명을 명시하는 경우가 있지만 여기서는 그냥 진행해본다.

```
@Service("hello")
public class HelloService implements StrategyService {
    @Override
    public String getValue(value) {
        return "Hello! My Value is " + value;
    }
}
```

그리고 월드 서비스도 하나 구현해보자.

```
@Service("world")
public class WorldService implements StrategyService {
    @Override
    public String getValue(value) {
        return "World! My Value is " + value;
    }
}
```


이때 @Service에 아이디를 부여한다. 물론 부여하지 않으면 클래스 명으로 'helloService'가 부여되지만
간략하게 사용하고자 명시적으로 부여를 한다.

그렇다면 Controller는 어떻게 작성할까?

```
@Slf4j
@RestController
@RequestMapping("/api")
public class StrategyController {

    @Autowired
    @Qualifier("hello")
    private StrategyService strategyService;

    /**
     * 심플하게 스트링 값 하나 던져준다.
     * @return String
     */
    @GetMapping("/strategy/value/{value}")
    public String fetchValue(@PathVariable("value")String value) {
        return strategyService.getValue(value);
    }

}
```

@Qualifier는 인터페이스를 구현한 동일한 타입의 객체중 부여된 아이디를 명혹하게 집어서 가져온다.

이 부분에 대해서는 따로 설명하지 않겠다.

여기서 만일 WorldService를 사용하고자 한다면 기존의 로직을 변경할 필요가 없이 @Qualifier에 들어간 아이디 값을
'world'로 @Qualifier("world")로 변경하면 끝나는 일이다.

다만 이런 경우에는 롬복의 @RequiredArgsConstructor를 활용한 생성자 주입이 불가능해 @Autowired를 사용해야하거나 

@Autowired를 사용하기 싫다면 생성자를 직접 만들어서 해당 시그니처에 @Qualifier를 붙여야 한다.

또 다른 방법은 서비스에서 @Primary를 활용하는 방법이 있다.

일반적으로 동일 타입의 빈이 2개 이상 존재하게 되면 오류가 발생하게 되는데 @Primary가 붙여 해당 빈을 우선적으로 찾게 만들면 된다.

어째든 인터페이스에 제공하는 메소도를 구현해야 하기 때문에 개발자는 새로운 서비스 로직을 구현한다 하더라도
명시된 메소드 내에서 구현하게 하기 때문에 아무런 수고도 없이 서비스를 교체하기 쉬워진다.

만일 이것을 좀 더 확장해 보자.

```
public interface StrategyService {
    String getValue(String value);
    String getCode(String code);
}

@Service("hello")
public class HelloService implements StrategyService {
    @Override
    public String getValue(String value) {
        return "Hello! My Value is " + value;
    }
    
    @Override
    public String getCode(String code) {
        return "Hello! My Code is " + code;
    }
}

@Service("world")
public class WorldService implements StrategyService {
    @Override
    public String getValue(String value) {
        return "World! My Value is " + value;
    }

    @Override
    public String getCode(String code) {
        return "Hello! My Code is " + code;
    }
}

```

이렇게 확장이 된다면

```
@Slf4j
@RestController
@RequestMapping("/api")
public class StrategyController {

    @Autowired
    @Qualifier("hello")
    private StrategyService strategyService;

    /**
     * 심플하게 스트링 값 하나 던져준다.
     * @return String
     */
    @GetMapping("/strategy/value/{value}")
    public String fetchValue(@PathVariable("value")String value) {
        return strategyService.getValue(value);
    }

    /**
     * 심플하게 스트링 값 하나 던져준다.
     * @return String
     */
    @GetMapping("/strategy/code/{code}")
    public String fetchCode(@PathVariable("code")String code) {
        return strategyService.getCode(code);
    }

}
```
서비스를 변경해야 한다면 기존의 로직은 손댈 필요가 없다. 

그저 @Qualifier에 원하는 서비스 아이디를 명시하면 된다.

또는 위에서처럼 @Autowired를 사용하고 싶지 않다면 갈아끼울 서비스 구현체에 @Primary를 붙이면 된다.

물론 두개의 서비스를 동시에 주입해서 사용할 수도 있다.

```
@Slf4j
@RestController
@RequestMapping("/api")
public class StrategyController {

    @Autowired
    @Qualifier("hello")
    private StrategyService hello;

    @Autowired
    @Qualifier("world")
    private StrategyService world;

    /**
     * 심플하게 스트링 값 하나 던져준다.
     * @return String
     */
    @GetMapping("/strategy/value/{value}")
    public String fetchValue(@PathVariable("value")String value) {
        return hello.getValue(value);
    }

    /**
     * 심플하게 스트링 값 하나 던져준다.
     * @return String
     */
    @GetMapping("/strategy/code/{code}")
    public String fetchCode(@PathVariable("code")String code) {
        return world.getCode(code);
    }

}
```
근데 보통은 저렇게 잘 사용하진 않는다. 

또한 테스트 코드 작성시에 유용하게 활용할 수 있다.

사실 내용은 그리 대단하지 않다. 하지만 몇일 전 친한 동료로부터 이와 관련 고민을 듣게 되었다.

"형! 기존의 컨트롤러쪽이 좀 복잡하긴 해도 그 로직 플로우가 변경될 일은 없어서 건들고 싶진 않은데 
A라는 업체쪽 API를 호출하는 A서비스를 B라는 업체쪽 API를 호출하는 B서비스로 바꾸고 싶기도 하고 A서비스도 유지해야 할 필요가 있는데 좀 쉬운 방법없을까?
그리고 우리가 하는 일이 많아서 몇몇 벤더의 API를 호출하는 서비스를 또 확장할 계획이 있어서 뭐 좋은 방법이 없을까?"

원래 이 친구는 경력이 좀 되지만 C#, nodeJs가 메인이고 자바와 스프링을 시작한지 얼마 안된 친구라 고민했던 모양이다.

그리고 몇일 후에 이렇게 해서 기존 로직을 건드리지 않고 서비스를 분리해서 그에 필요한 로직만 작성하면서 금방 마무리를 했다고 한다.

뭐 사실 인터페이스를 구현하지 않고 복붙복 해서 하는 방법도 상관없다. 잘만 돌아가면 말이지. 

# At A Glance

이 부분을 잘 아는 개발자라면 그냥 넘어갈 수 있는 내용이긴 하지만 모르는 개발자라면 도움이 되길 바라는 입장에서 간략하게 소스 코드를 작성했다.

또한 굳이 이런 방식을 사용하지 않고도 뭐 막강한 복붙복 스킬을 이용해 서비스를 만들면 장땡이기도 하다.

하지만 협업을 하게 된다면 이렇게 강제적으로 인터페이스를 구현하게 해 휴먼 미스테이크를 줄이는 효과가 있다.

두 개의 서비스중 WorldService에 @Primary를 붙여 이 빈이 먼저 우선적으로 찾게 만든 데모 어플리케이션이기 때문에 
@Primary를 다른 서비스에 붙이면서 변화되는 것을 확인해 보길 바란다. 