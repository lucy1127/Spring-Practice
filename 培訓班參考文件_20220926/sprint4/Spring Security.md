---
tags: Java, Spring Boot
---

# Spring Security

## 前言

網頁安全一直以來都是開發團隊需要專注處理的細節，就拿每天要回的家來說，回家的時候，我們需要鑰匙才能進入大門，而基本上我們是不能隨便進別人的房間，除非今天房間的主人准許我們進他的房間。

這個例子，就可以簡單帶到 Spring Security 關注的兩個部分：++驗證（Authentication）++與++授權（authorization）++。
需要鑰匙的大門，就像是 Spring Security 在驗證我們是否有權限進入。
想要進入別人的房間，就像是 Spring Security 在授權是否今天這個使用者可以使用這個功能。

所以今天就讓我們來學習如何用 Spring Security 來保護我們的網頁吧！

## 目錄

* [Spring Security](#spring-security)
  * [前言](#前言)
  * [目錄](#目錄)
  * [介紹/基本概念](#介紹基本概念)
    * [Spring Security 是一個 Filter](#Spring-Security-是一個-Filter)
    * [驗證（Authentication）](#驗證（Authentication）)
    * [授權（Authorization）](#授權（Authorization）)
    * [JWT](#JWT)
    * [如何將 JWT 加入驗證流程](#如何將-JWT-加入驗證流程)
  * [應用情境說明](#應用情境說明)
  * [建置環境](#建置環境)
    * [專案架構](#專案架構)
    * [使用 spring initializr 建立專案](#使用-spring-initializr-建立專案)
    * [在 pom.xml 匯入 JWT 的 dependency](#在-pom.xml-匯入-JWT-的-dependency)
    * [配置 application.properties](#配置-application.properties)
    * [建立資料表及資料](#建立資料表及資料)
  * [實作過程](#實作過程)
    * [建立 entity 及相關 classes](#建立-entity-及相關-classes)
    * [建立 JWT 相關 classes](#建立-JWT-相關-classes)
    * [訂定這個 application 的授權及驗證規則](#訂定這個-application-的授權及驗證規則)
    * [使用 Postman 驗證結果](#使用-Postman-驗證結果)
  * [參考資料](#參考資料)
  * [撰寫紀錄](#撰寫紀錄)

## 介紹/基本概念

Spring Security 如前言所說，主要關注的問題在於驗證（Authentication）與授權（authorization），以下會簡單介紹 Spring Security 到底是什麼，以及它如何處理驗證跟授權的邏輯，最後會另外簡介在實務上時常與 Spring Security 綁定的 JWT token。

### <span class="orange">Spring Security 是一個 Filter</span>

在 Web 世界裡的 Spring Security 是由 Client 端發送請求到 Servlet 間過程中的其中一個 filter，具體的 filter 形式為 FilterChainProxy。

在 Spring Boot 中 security filter 是以 @Bean 的形式存在於 ApplicationContext，也就是預設上 security filter 適用於所有 requests。

此外，雖然從 container 的層面來看，Spring Security 是一個單一的 filter，但其實這個 FilterChainProxy 是由多個 filter 組成的 filter chain，如下圖：
![1](https://i.imgur.com/RCpkdrd.png)

而此圖則是 FilterChainProxy 內含的 filter
![1.5](https://i.imgur.com/SGLcYe2.png)

當然同一個 FilterChainProxy 也可以依照不同路徑擁有多個不同樣式的 filter chain（container 並不會知道有多少組），如下圖
![2](https://i.imgur.com/7j2w02v.png)

### <span class="orange">驗證（Authentication）</span>

驗證主要是來處理++請求者是不是合法使用者++的這個問題，以下為 Spring Security 的預設驗證流程

![2.5](https://i.imgur.com/nYSCIJY.png)

首先 AuthenticationManager 是負責 Spring Security 驗證的主要策略接口，而這個接口內只有一個 method 如下：

```java=
public interface AuthenticationManager {

  Authentication authenticate(Authentication authentication)
    throws AuthenticationException;
}
```

在 authenticate() 這個方法中，主要會回傳三種狀態。

1. 如果驗證有效，則回傳 Authentication
2. 如果驗證無效，則拋出一個 AuthenticationException（runtime exception）；在 HTTP 的世界裡，return code 就會直接丟 401 回去。
3. 如果它不能決定，就會回傳一個 null

而通常實作 AuthenticationManager 的 class 是 ProviderManager，在 ProviderManager 的程式碼中，我們可以發現通常會注入 AuthenticationProvider，那什麼又是 AuthenticationProvider?

```java=
@Override
public class ProviderManager implements AuthenticationManager, MessageSourceAware, InitializingBean {
    
    private List<AuthenticationProvider> providers = Collections.emptyList();
    
    // 其中一個建構子
    public ProviderManager(AuthenticationProvider... providers) {
        this(Arrays.asList(providers), null);
    }
    
    // ...
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
        for (AuthenticationProvider provider : getProviders()) {
            // ...
        }
        // ...
    }
}
```

AuthenticationProvider 也是一個接口，它可以根據不同種的驗證方式來從對應的儲存庫取得使用者資料，舉例來說有 DaoAuthenticationProvider、LdapAuthenticationProvider、OpenIDAuthenticationProvider 等等，跟 AuthenticationManager 的差別在於前者多出一個 method 可以使用。

```java=
public interface AuthenticationProvider {

    Authentication authenticate(Authentication authentication) throws AuthenticationException;
    
    // 用來確認是否支援傳入的 Authentication 類型，因為在同一個應用程式下的 ProviderManager 可以有不同種的 Authentication 機制
    boolean supports(Class<?> authentication);
}
```

所以在 AuthenticationManager 這邊的驗證邏輯會是通過 ProviderManager 實作 AuthenticationManager 的 authenticate() 來實現。在 ProviderManager 的 authenticate() 中，會遍歷 List&lt;AuthenticationProvider&gt;，如果其中一個 AuthenticationProvider 調用 supports() 的返回值為 true，則就會去調用該 AuthenticationProvider 底下的 authenticate() 函數。如果認證成功，則整個認證過程結束；如果不成功，則會繼續嘗試下一個 AuthenticationProvider。

最後，讓我們來簡單串起整個驗證流程，首先在 Spring Security 的 filter chain 中會有一個處理目前驗證方式的 filter，如果此 filter 為  UsernamePasswordAuthenticationFilter 的話，會透過其中的 attemptAuthentication() 來調用 AbstractUserDetailsAuthenticationProvider 的 authenticate()。

```java=
public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    
    //...
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = obtainUsername(request);
        username = (username != null) ? username.trim() : "";
        String password = obtainPassword(request);
        password = (password != null) ? password : "";
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
```

在 attemptAuthentication() 中的 UsernamePasswordAuthenticationToken 對應的 provider 為 DaoAuthenticationProvider，這邊會透過 DaoAuthenticationProvider 父類（AbstractUserDetailsAuthenticationProvider）的 authenticate() 來實作驗證機制。其中的 retrieveUser() 為 DaoAuthenticationProvider 內的方法，用來取得 UserDetails 的資訊。

```java=
public class DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    protected final UserDetails retrieveUser(String username,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        prepareTimingAttackProtection();
        try {
            // 獲取使用者資訊
            UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        }
        catch (UsernameNotFoundException ex) {
            ……
        }
    }
}
```

此外，Spring Security 會在一開始啟動程式時建立一組 user 的憑證在應用程式的內存中，我們可以在 console 中找到由 Spring Security 建立的使用者密碼。

```text
Using generated security password: 2fb5fe53-aa00-4b97-8ca3-b27a97120c98
```

Spring Security 也提供了可以幫助工程師快速配置 AuthenticationManager 的 AuthenticationManagerBuilder，可以用來配置 user detail 或是加入客製化的 UserDetailsService，範例如下：

```java=
@Configuration
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(AuthenticationManagerBuilder builder) {
    builder.inMemoryAuthentication()
        .withUser("user1").password(passwordEncoder().encode("123456")).roles("USER")
        .and()
        .withUser("user2").password(passwordEncoder().encode("qweasd")).roles("USER")
        .and()
        .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
  }

}
```

:::danger
🔔 WebSecurityConfigurerAdapter 在 Spring Security 高於並包含 5.7.1 或 Spring Boot 高於並包含 2.7.0 的版本中已經被廢棄了，下面的實作會使用新的方式。
:::

### <span class="orange">授權（Authorization）</span>

授權主要是來處理++合法使用者可以做什麼事++的這個問題，首先先讓我們來看一下整個授權機制的流程圖：

![103](https://i.imgur.com/XaBp230.png)

1. FilterSecurityInterceptor 啟動檢核是否當前 request 可以使用要求的資源
2. 如果需要授權 FilterSecurityInterceptor 會從 SecurityContextHolder 獲取 Authenticated 的物件，一般來說通常驗證後才會進入授權的步驟
3. 通知 AccessDecisionManager 使用 Authentication、FilterInvocation 跟 ConfigAttribute 來決定此使用者是否通過授權
4. 如果授權失敗，將會拋出 AccessDeniedException


在 Spring Security 中主要是透過 AccessDecisionManager 來決定使用者是否通過授權，但主要的授權邏輯是交由 AccessDecisionVoter 去判斷與執行，這邊讓我們看一下這些物件的相對關係。

![104](https://i.imgur.com/niX9KHf.png)

其中 AccessDecisionManager 管理著對不同規則進行判斷與表決的多個 AccessDecisionVoter，因為 AccessDecisionVoter 只會對自己支持的規則進行表決，如果一個 resource 的訪問規則有多個，則最終需要一個平台來審視最後的結果，也就是 AccessDecisionManager 的角色。所以我們可以看到實作 AccessDecisionManager 的 AbstractAccessDecisionManger 底下有三種的決定方式，Spring Security 默認的決定方式為  AffirmativeBased(代表只要有一個 voter 表決通過即通過)。

### <span class="orange">JWT</span>

JWT 全名為 Json Web Token，它是一種前後端分離架構 (Client-Server) 的驗證機制，它比起以往使用 Session 和 Cookie 實作驗證機制更貼合  RESTful API 的「Stateless 無狀態」原則，也就是每一次客戶端向伺服器端發出的請求都是獨立的，使用者經過驗證後，在伺服器端不會將用戶驗證狀態透過 Session 儲存起來，因此每次客戶端發出的請求都將帶有伺服器端需要的所有資訊 — 從客戶端發出給伺服器端的請求將帶有 JWT 字串表明身份。流程如下圖：

![100](https://i.imgur.com/4ZqABtc.png)


#### 使用 JWT 有的兩大好處

1. 跨平台：舉例來說，你可能只會有一個 API 系統，但或許會有多個前端像是 web、mobile app，但行動裝置是不能套用 session-cookie 機制，這時候就可以用 JWT 來進行驗證。
2. 資訊交換：因為 JWT 會透過演算法進行加密，所以 token 上的資訊是無法被竄改的，這樣可以確保前後端交換的資訊是正確且安全的。

#### JWT 的結構

JWT 是一組字串，透過（.）切分成三個為 Base64 編碼的部分：

1. Header：含 Token 的種類及產生簽章（signature）要使用的雜湊演算法
2. Payload：帶有欲存放的資訊（例如用戶資訊）
3. Signature：編譯後的 Header、Payload 與密鑰透過雜湊演算法所產生

```text
// base64(Header) + base64(Payload) + base64(Signature)
xxxxx.yyyy.zzzzz
```

* Header

Header 包含定義 Token 種類（type）及雜湊演算法（alg）的資訊。

```json
// 此為 Header 解密後的形式
{
    "alg":"HS256",
    "typ":"JWT"
} 
```

* Payload

Payload 則包含 claims，claims 代表資訊和一些附加資料，分成三種類型

1. registered
公認建議需要有的 claims，但不強迫放置

    ```text
    iss(issuer) 發布者
    exp(expiration time) 失效時間
    sub(subject) 主題
    iat(issued at) 發布時間
    …
    ```

2. public
公開 Claims，基本上不常使用

3. private
私有 Claims，可以自行定義，放一些不機密的資訊，像是 userName、userRole 等等

:::danger
❗️不要將隱私資訊存放在 Payload 當中
Payload 和 Header 被轉換成 Base64 編碼後，能夠被輕易的轉換回來
因此不應該把如用戶密碼等重要資料存取在 Payload 當中
:::

* Signature

簽章（Signature）是將被轉換成 Base64 編碼的 Header、Payload 與自己定義的密鑰，透過在 Header 設定的雜湊演算法方式所產生的。

由於密鑰並非公開，因此伺服器端在拿到 Token 後，能透過解碼，確認資料內容正確，且未被變更，以驗證對方身份。

:::warning
可以使用<https://jwt.io/>來解析 JWT

![101](https://i.imgur.com/7MrEr2R.png)
:::

### <span class="orange">如何將 JWT 加入驗證流程</span>

![102](https://i.imgur.com/CdTRfjH.png)

簡單來說，將 JWT 加入驗證流程就是於 Spring Security 的 filter chain 中加入自己的客製化 filter，加入的位置會位於  UsernamePasswordAuthenticationFilter 前，在這個 filter 裡，我們可以從 request 取得 token，並做對應的邏輯處理。

## 應用情境說明

在以下實作中，我們會建立一個登入 api 以及一個只能由管理者進入的 api，而使用者的相關資料會由 H2 取得，此外會導入 JWT token 做為驗證使用者的方式。

## 建置環境

### <span class="orange">專案架構</span>

![106](https://i.imgur.com/ZNsDAO1.png)

### <span class="orange">使用 spring initializr 建立專案</span>

點擊 [spring initializr](https://start.spring.io/)，進入後配置如下圖即可：
![105](https://i.imgur.com/yVJs5mQ.png)

### <span class="orange">在 pom.xml 匯入 JWT 的 dependency</span>

```xml=
<dependency>
  <groupId>com.auth0</groupId>
  <artifactId>java-jwt</artifactId>
  <version>4.0.0</version>
</dependency>
```

### <span class="orange">配置 application.properties</span>

```xml=
# H2 相關配置
spring.datasource.url=jdbc:h2:mem:<資料庫名稱>
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=<自己的帳號>
spring.datasource.password=<自己的密碼>
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true

# 開啟 console log 打印 Spring Security 的流程
logging.level.org.springframework.security=TRACE

# 解決 response.sendError() 內的 timestamp 時間與正確時間不一致問題
spring.jackson.time-zone= GMT+8
```

:::warning
H2 相關資訊可以參考 [H2 資料庫](https://hackmd.io/dSePR-VpRPuXh2_527w2aQ?view)
:::

### <span class="orange">建立資料表及資料</span>

#### schema.sql

```sql=
CREATE TABLE user_data (
    user_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    user_name VARCHAR(20) NOT NULL,
    user_password VARCHAR(70) NOT NULL,
    user_auth VARCHAR(30) NOT NULL
);
```

#### import.sql

```sql=
-- 密碼的明碼為 password
INSERT INTO user_data (user_name, user_password, user_auth) VALUES ('Doris', '$2a$08$fL7u5xcvsZl78su29x1ti.dxI.9rYO8t0q5wk2ROJ.1cdR53bmaVG', 'user,admin');
INSERT INTO user_data (user_name, user_password, user_auth) VALUES ('Amy', '$2a$08$fL7u5xcvsZl78su29x1ti.dxI.9rYO8t0q5wk2ROJ.1cdR53bmaVG', 'user');
```

## 實作過程

### <span class="orange">建立 entity 及相關 classes</span>

#### UserData

```java=
@Entity
@Table(name = "user_data")
public class UserData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int uuid;
    
    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "user_password")
    private String userPassword;
    
    @Column(name = "user_auth")
    private String userAuth;

    // 省略 getter and setter...
}
```

#### UserDataRepository

```java=
@Repository
public interface UserDataRepository extends JpaRepository<UserData, Integer> {
    
    UserData findByUserName(String userName);
}
```

#### LoginRequestBody

```java=
public class LoginRequestBody {

    private String username;

    private String password;
    
    // 省略 getter and setter...
}
```

#### LoginResponseBody

```java=
public class LoginResponseBody {

    private String token;
    
    // 省略 getter and setter...
}
```

#### AuthController

```java=
@RestController
public class AuthController {
    
    @Autowired
    UserDataService userDataService;

    // 登入
    @PostMapping("/login")
    public ResponseEntity<LoginResponseBody> login(@RequestBody LoginRequestBody requestBody) throws Exception {
        return ResponseEntity.ok(userDataService.login(requestBody));
    }

    // 驗證是否為管理者
    @GetMapping("/testAdmin")
    public ResponseEntity<String> testAdmin() {
        return ResponseEntity.ok("PASS.");
    }

}
```

#### UserDataService

```java=
@Service
public class UserDataService {

    @Autowired
    private UserDataRepository userDataRepository;
    
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponseBody login(LoginRequestBody requestBody) throws Exception {

        UserData userData = userDataRepository.findByUserName(requestBody.getUsername());

        // 先查詢是否有此 user
        if(userData == null)
            throw new Exception("Username is wrong.");

        // 比對傳入密碼及 db 儲存之密碼
        if (!encoder.matches(requestBody.getPassword(), userData.getUserPassword()))
            throw new Exception("PW is incorrect.");

        String[] auths = userData.getUserAuth().split(",");

        JWTCreator.Builder builder = JWT.create()
            // 加入客製化的 claims
            .withClaim("roles", Arrays.stream(auths).collect(Collectors.toList()))
            .withClaim("userId", userData.getUuid());

        // 產生 token
        String accessToken = jwtUtil.generateToken(builder, userData.getUserName());

        LoginResponseBody responseBody = new LoginResponseBody();
        responseBody.setToken(accessToken);

        return responseBody;
    }

}
```

### <span class="orange">建立 JWT 相關 classes</span>

#### JwtUtil

```java=
@Component
public class JwtUtil {

    private final String SECRET_KEY = "secret";

    public String extractUsername(DecodedJWT token) {
        return token.getSubject();
    }

    // JWT token 解密
    public DecodedJWT decodeToken(String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public String generateToken(JWTCreator.Builder builder, String username) {
        // the builder has withClaim
        return createToken(builder, username);
    }

    // 建立 JWT token
    private String createToken(JWTCreator.Builder builder, String subject) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        return builder
            .withSubject(subject)
            .withIssuedAt(new Date(System.currentTimeMillis()))
            .withExpiresAt(new Date(System.currentTimeMillis() + 20 * 60 * 1000)) // 20min
            .sign(algorithm);
    }

}
```

#### JwtTokenFilter

```java=
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // 不用驗證授權的 URIs
    private static final String[] EXCLUDES = new String[] { "/login", "/h2" };

    public static final String HEADER = "Authorization";

    public static final String TOKEN_HEAD = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String httpMethod = request.getMethod();

        var doFilter = true;

        // 排除不用驗證的 api
        for (String exclude : EXCLUDES) {
            var template = new UriTemplate(exclude);
            if (uri.contains(template.toString())) {
                doFilter = false;
                break;
            }
        }

        if (doFilter) {
            String header = request.getHeader(HEADER);
            // 驗證是否有帶 token
            if (header != null && header.startsWith(TOKEN_HEAD)) {
                setAuthentication(header);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // 驗證 token 是否合法，若合法則建立一個 UsernamePasswordAuthenticationToken 並放入 SecurityContextHolder，這樣表示已通過身分驗證，並可於後續執行授權
    private void setAuthentication(String authorizationHeader) {
        String username = null;
        DecodedJWT decodedJWT = jwtUtil.decodeToken(authorizationHeader);
        username = jwtUtil.extractUsername(decodedJWT);

        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Stream.of(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
```

### <span class="orange">訂定這個 application 的授權及驗證規則</span>

先建立一個 WebSecurityConfig，由於 WebSecurityConfigurerAdapter 在新版本已經被廢棄，這邊會採用新的方式來實作。

```java=
// 用來啟用 Spring Security 所需的各項配置，並且此 annotation 包含 ＠Configuration 的功能，所以不用在 class 上標註 ＠Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 
        http
        // 開始自訂授權規則
        .authorizeRequests()
        .antMatchers("/h2/**").permitAll()
        // 定義要被規範的網址列或 HttpMethod 的形式
        // * 網址列的路徑可以用萬用字元表示：
        // 「*」：代表 0 到多個字元。如「/folder/*」適用於「/folder」、「/folder/123」，但不適用「/folder/123/draft」
        // 「**」：代表 0 到多個路徑。如「/folder/**」適用於「/folder」底下任何路徑。
        // 「?」：代表一個字元。如「/folder/?*」適用於「/folder/1」、「/folder/123」，但不適用「/folder」。
        .antMatchers(HttpMethod.POST, "/login")
        // 允許直接通行
        .permitAll()
        .antMatchers("/testAdmin")
        // 授權可以存取的對象
        .hasAuthority("admin")
        .and()
        // 加入客製化 filter
        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
        // If you are only creating a service that is used by non-browser clients, you will likely want to disable CSRF protection.
        .csrf().disable()
        // STATELESS : Spring Security will never create an HttpSession, and it will never use it to obtain the SecurityContext.
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        // 解決 H2 的 console 因加入 Spring Security 而無法顯示的問題
        .headers().frameOptions().disable();

        return http.build();
    }

}
```

### <span class="orange">使用 Postman 驗證結果</span>

* 使用不合法使用者登入

RequestBody :

```json=
{
    "username" : "Doris1",
    "password" : "password1"
}
```

![107](https://i.imgur.com/SqH39A7.png)

* 使用合法使用者登入

RequestBody :

```json=
{
    "username" : "Amy",
    "password" : "password"
}
```

![108](https://i.imgur.com/BxcVztb.png)

* 使用使用者 token 打測試管理員權限的 api
![109](https://i.imgur.com/TcjUWE0.png)

* 使用合法管理者登入
![110](https://i.imgur.com/7KgDw8w.png)

* 使用管理者 token 打測試管理員權限的 api
![111](https://i.imgur.com/uvQ710v.png)

## 參考資料

* [Spring Security Architecture](https://spring.io/guides/topicals/spring-security-architecture)
* [Guide to UserDetailsService in Spring Security](https://howtodoinjava.com/spring-security/inmemory-jdbc-userdetails-service/#:~:text=The%20default%20implementation%20of%20UserDetailsService,when%20the%20spring%20context%20loads.)
* [Spring Security Authorization – How authorization work](https://www.javadevjournal.com/spring-security/spring-security-authorization/)
* [[筆記] 透過 JWT 實作驗證機制](https://medium.com/%E9%BA%A5%E5%85%8B%E7%9A%84%E5%8D%8A%E8%B7%AF%E5%87%BA%E5%AE%B6%E7%AD%86%E8%A8%98/%E7%AD%86%E8%A8%98-%E9%80%8F%E9%81%8E-jwt-%E5%AF%A6%E4%BD%9C%E9%A9%97%E8%AD%89%E6%A9%9F%E5%88%B6-2e64d72594f8)

## 撰寫紀錄

| 人員 | 日期 | 修改紀錄 |
| - | - | - |
| 于慧 | 2022/7 | 初版 |
| 于慧 | 2022/9 | 加入 JWT token 的介紹及調整授權、驗證和實作案例 |

<style>
.orange {
    color: #f28500;
}
</style>
