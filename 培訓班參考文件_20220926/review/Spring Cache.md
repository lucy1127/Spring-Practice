---
tags: Java, Spring Boot
---

# Spring Cache

## 前言

專案中一些 API 常常會到 DB 查找資料，如果查找的資料量很龐大，會對 DB 造成很大的負擔，也會有效能上的議題，Spring Cache 就能解決這個問題。

## 目錄

* [Spring Cache](#Spring-Cache)
  * [前言](#前言)
  * [目錄](#目錄)
  * [介紹/基本概念](#介紹基本概念)
    * [認識相關的 Annotaion](#認識相關的-Annotaion)
    * [認識相關的介面](#認識相關的介面)
    * [Caffeine](#Caffeine)
  * [應用情境說明](#應用情境說明)
  * [建置環境](#建置環境)
  * [實作過程](#實作過程)
    * [情境一：使用 ConcurrentMapCache](#情境一：使用-ConcurrentMapCache)
    * [情境二：使用 @Primary](#情境二：使用-@Primary)
    * [情境三：使用 CachingConfigurerSupport](#情境三：使用-CachingConfigurerSupport)
    * [情境四：使用 CacheResolver](#情境四：使用-CacheResolver)
  * [參考資料](#參考資料)
  * [撰寫紀錄](#撰寫紀錄)

## 介紹/基本概念

Cache 機制會在第一次使用時會呼叫 Method，此後將資料站存到 Cache 中提高 Method 反應速度，而清除 Cache 後，會再去呼叫 Mehotd 重新將資料暫存至 Cache 中。
Spring Cache 其實為 AOP 的操作，Spring Cache 的註解會幫忙在方法上創建一個切面（aspect），並觸發緩存註解的切點（poinitcut），在目標 Method 上攔截，並判斷是否要執行該 Method 或回傳已存在之值。

### 認識相關的 Annotaion

#### @Cacheable

@Cacheable 可以標記在一個方法上，也可以標記在類別上。當標記在一個方法上時表示該方法是支援快取的，當標記在一個類上時則表示該類所有的方法都是支援快取的。
對於一個支援快取的方法，Spring 會在其被呼叫後將其返回值快取起來，以保證下次利用同樣的引數來執行該方法時，可以直接從快取中獲取結果，而不需要再次執行該方法。
這個註解一般用在**查詢**方法上，可以指定四個屬性：value、key、condition 和 keyGenerator。

* `value`
value 屬性是必須指定的，其表示當前方法的返回值是會被快取在哪個 Cache 上的，對應 Cache 的名稱。

* `key`
key 屬性是用來指定 Spring 快取方法的返回結果時對應 key，支援 SpringEL 表示式。
當沒有指定該屬性時，Spring 將使用預設策略生成 key。自定義策略是指可以通過 Spring 的 EL 表示式來指定 key。這裡的 EL 表示式可以使用方法引數及它們對應的屬性。使用方法引數時我們可以直接使用「#引數名」或者「#p 引數 index」。

* `condition`
有的時候我們可能並不希望快取一個方法所有的返回結果，通過 condition 屬性可以實現這一功能。
condition 屬性預設為空，表示將快取所有的呼叫情形。其值是通過 SpringEL 表示式來指定的，當為 true 時表示進行快取處理；當為 false 時表示不進行快取處理，即每次呼叫該方法時該方法都會執行一次。

* `keyGenerator`
keyGenerator 指定生成 key 的 method，key 和 keyGenerator 是互斥的一對。
當指定 key 的時候就會使用指定的 key + 引數作為快取 key，否則則使用預設 keyGenerator(SimpleKeyGenerator) 或者你自定義的 Generator 來生成 key。

#### @CachePut

@CachePut 一般用在**新增**方法上，與 @Cacheable 不同的是使用 @CachePut 標註的方法在執行前不會去檢查快取中是否存在之前執行過的結果，而是每次都會執行該方法，並將執行結果以鍵值對的形式存入指定的快取中。
使用 @CachePut 時我們可以指定的屬性跟 @Cacheable 是一樣的。

#### @CacheEvict

@CacheEvict 會清空指定的 Cache，一般用在**更新**或者**刪除**的方法上。@CacheEvict 可以指定的屬性有 value、key、condition、allEntries 和 beforeInvocation。其中 value、key 和 condition 的語義與 @Cacheable 對應的屬性類似。
value 表示清除操作是發生在哪些 Cache 上的（對應 Cache 的名稱）；
key 表示需要清除的是哪個 key，如未指定則會使用預設策略生成的 key；condition 表示清除操作發生的條件。

* `allEntries`
allEntries 是 boolean 型別，表示是否需要清除快取中的所有元素，
預設為 false，表示不需要。當指定了 allEntries 為 true 時，Spring Cache 將忽略指定的 key。有的時候我們需要將 Cache 裡的元素清除，這比一個一個清除元素更有效率。

* `beforeInvocation`
清除操作預設是在對應方法成功執行之後觸發的，即方法如果因為丟擲異常而未能成功返回時也不會觸發清除操作。
使用 beforeInvocation 可以改變觸發清除操作的時間，當我們指定該屬性值為 true 時，Spring 會在呼叫該方法之前清除快取中的指定元素。

#### @Caching

@Caching 註解可以讓我們在一個方法或者類別上同時指定多個 Spring Cache 相關的註解。
其擁有三個屬性：cacheable、put 和 evict，分別用於指定 @Cacheable、@CachePut 和 @CacheEvict。

#### @CacheConfig

它是一個類別級別的註解，可以在類別級別上配置 cacheNames、keyGenerator、cacheManager、cacheResolver 等。

### 認識相關的介面

#### KeyGenerator

key 生成是通過 KeyGenerator 生成的。

#### CacheManager

CacheManager 是 Spring 定義的一個用來管理 Cache 的介面，也就是說針對不同的緩存技術，需要實現不同的 CacheManager，以下列出一些不同的 CacheManager。

| CacheManger | 描述 |
| - | - |
| SimpleCacheManager | 使用簡單的 Collection 來存儲緩存，主要用於測试 |
| ConcurrentMapCacheManager | 使用 ConcurrentMap 作為緩存技術（預設），需要手動的刪除緩存，無過期機制 |
| NoOpCacheManager | 僅測試用途，不會實際存儲緩存 |
| EhCacheCacheManager | 使用 EhCache 作為緩存技術 |
| GuavaCacheManager | 使用 Google Guava 的 GuavaCache 作為緩存技術(1.5版本已不建議使用） |
| CaffeineCacheManager | 使用 Java 8 對 Guava 緩存的重寫，Spring Boot2 開始用 Caffeine 取代 Guava |
| JCacheCacheManager | 使用 JCache 標準的實現作為緩存技術，如 Apache Commons JCS |
| RedisCacheManager | 使用 Redis 作為緩存技術 |
| HazelcastCacheManager | 使用 Hazelcast 作為緩存技術 |

### Caffeine

上述羅列了多種的緩存技術，程式設計師應該因應不同的情境設計不同的緩存技術，但因為本文主要不是著重於如何設計緩存機制，所以如果對設計緩存機制有興趣，可以操考下文：
[用 Caffeine 和 Redis 管理 Cache 案例分析](https://medium.com/fcamels-notes/%E7%94%A8-caffeine-%E5%92%8C-redis-%E7%AE%A1%E7%90%86-cache-%E6%A1%88%E4%BE%8B%E5%88%86%E6%9E%90-23e88291b289)。

而在這篇文章中，主要會介紹的緩存技術為 Caffeine。

#### 基本介紹

Caffeine 是使用 Java 8 基於對 Google Guava 緩存改寫的一種本地緩存技術，在官方文件有指出，Caffeine 的緩存命中率已經接近最優值。

Caffeine 本質上很類似於 ConcurrentMap，這兩者中的差別在於若使用 ConcurrentMap 存儲緩存，則需要手動移除；而 Caffeine 可以透過配置，自動移除不常用的緩存，可以避免非必要的內存占用。

:::info

* 分布式緩存（Distributed Cache）：應用服務的 process 和緩存的 process 通常分布在不同的服務器上，不同 process 之間通過 RPC 或 HTTP 的方式通信。這種緩存的優點是緩存和應用服務解耦，支持大數據量的存儲，缺點是數據要經過網絡傳輸，性能上會有一定損耗。諸如 Redis。

* 本地緩存（Local cache）：緩存的 process 和應用服務的 process 是同一個，也就是數據的讀寫都在一個 process 內完成，這種方式的優點是沒有網絡開銷，訪問速度很快。缺點是受 JVM 內存的限制，不適合存放大數據。諸如 Caffeine、Guava Cache 等。
:::

#### 四種緩存添加策略

* 手動加載（Cache）：

透過手動呼叫 put() 將緩存加入，不過透過這種方式加載，會跟 Map 內的 put() 概念一致，如果新舊 key 一致會覆寫舊的資料。

此外，如果想要的緩存值不存在時，想將此值加入緩存，則可以呼叫 get(key, k -> value)，此方法可以避免多線程的寫入競爭，也就是如果有另一個線程同時調用本方法進行競爭，則後一線程會被阻塞，直到前一線程更新緩存完成。

最後，可以呼叫 invalidate() 來手動使緩存失效（invalidation ）。

```java=
Cache<String, String> testCache = Caffeine.newBuilder().build();

String key = "123";

String value = "456";

testCache.put(key, value);
System.out.println("The value of key 123 is " + testCache.getIfPresent(key));

testCache.invalidate(key);
System.out.println("The value of key 123 is " + testCache.getIfPresent(key));

testCache.get(key, k -> value);
System.out.println("The value of key 123 is " + testCache.getIfPresent(key));

```

> 打印結果:
> The value of key 123 is 456
> The value of key 123 is null
> The value of key 123 is 456

* 自動加載（Loading Cache）：

LoadingCache 是一種自動加載的緩存。和普通緩存不同的地方在於，當緩存++不存在++或緩存++已過期++時，若調用 get() 方法，則會自動呼叫 CacheLoader.load() 方法加載最新值。調用 getAll() 方法將遍歷所有的 key 調用 get()，除非有另外覆寫 CacheLoader.loadAll() 方法。

使用 LoadingCache 時，需要指定 CacheLoader，並實現其中的 load() 方法供沒有該緩存時自動加載的機制。

多線程情況下，當兩個線程同時調用 get()，則後一線程將被阻塞，直至前一線程更新緩存完成。

```java=
LoadingCache<String, String> testCache = Caffeine.newBuilder()
    .build(new CacheLoader<String, String>() {
        @Override
        // 必需實作此方法
        public String load(@NonNull String k) throws Exception {
            return "456";
        }
    });

String key = "123";

System.out.println("The value of key 123 is " + testCache.getIfPresent(key));

testCache.get(key);
System.out.println("The value of key 123 is " + testCache.getIfPresent(key));

testCache.invalidate(key);
System.out.println("The value of key 123 is " + testCache.getIfPresent(key));

testCache.getAll(Arrays.asList(key));
System.out.println("The value of key 123 is " + testCache.getIfPresent(key));
```

> 打印結果:
> The value of key 123 is null
> The value of key 123 is 456
> The value of key 123 is null
> The value of key 123 is 456

* 手動異步加載（AsyncCache）：

AsyncCache 是 Cache 的一個變體，AsyncCache 提供了在 Executor 上生成緩存元素並返回 [CompletableFuture](https://popcornylu.gitbooks.io/java_multithread/content/async/cfuture.html) 的能力。 預設情況下，緩存計算使用 ForkJoinPool.commonPool() 作為線程池，如果想要指定線程池，則可以覆寫 Caffeine.executor(Executor) 方法。

synchronous() 提供了阻塞直到異步緩存生成完畢的能力，它將以 Cache 進行返回。

多線程情況下，當兩個線程同時調用 get(key, k -> value)，則會返回同一個 CompletableFuture 物件。

```java=
AsyncCache<String, String> testCache = Caffeine.newBuilder().buildAsync();

String key = "123";

// 查詢緩存元素，因為不存在，所以回傳null
CompletableFuture<String> completableFuture = testCache.getIfPresent(key);
System.out.println("The value of key 123 is " + completableFuture);

// 查詢緩存元素，如果不存在，則異步生成
completableFuture = testCache.get(key, k -> "456");
System.out.println("The value of key 123 is " + completableFuture.get());

// 移除一个缓存元素
testCache.synchronous().invalidate(key);
completableFuture =  testCache.getIfPresent(key);
System.out.println("The value of key 123 is " + completableFuture);
```

> 打印結果:
> The value of key 123 is null
> The value of key 123 is 456
> The value of key 123 is null

* 自動異步加載（AsyncLoadingCache）：

是 Loading Cache 和 Async Cache 的功能組合。AsyncLoadingCache 支持以異步的方式，對緩存進行自動加載。

類似 LoadingCache，同樣需要指定 CacheLoader，並實現其中的 load() 方法供沒有該緩存時自動加載的機制，此方法將自動在 ForkJoinPool.commonPool() 線程池中提交。如果想要指定 Executor，則可以實現 AsyncCacheLoader().asyncLoad() 方法。

```java=
AsyncLoadingCache<String, String> cache = Caffeine.newBuilder()
// .buildAsync(new AsyncCacheLoader<String, String>() {
//     @Override
//     // 自定義線程池加載
//    public @NonNull CompletableFuture<String> asyncLoad(@NonNull String key, @NonNull Executor executor) {
//        return CompletableFuture.supplyAsync(() -> {
//            return "456";
//        });
//    }
//})

    .buildAsync(new CacheLoader<String, String>() {
        @Override
        // OR，使用預設線程池加載（二者選其一）
        public String load(@NonNull String key) throws Exception {
            return "456";
        }
    });

String key = "123";

// 查詢緩存元素，如果不存在，將會異步進行生成
CompletableFuture<String> completableFuture = cache.get(key);
System.out.println("The value of key 123 is " + completableFuture.get());
```

> 打印結果:
> The value of key 123 is 456

#### 三種緩存驅逐（eviction）策略

驅逐策略需要在建立緩存的時候指定。因為緩存有三種驅逐策，所以可以任意組合使用，當其中一項的驅逐策略生效後，該緩存即被驅逐。

* 基於時間

Caffeine 提供了三種基於時間的驅逐:

expireAfterAccess(long, TimeUnit): 一個緩存在上一次讀寫操作完的指定時間後，若沒有再次訪問被將會被認定為過期。

```java=
// 建立一個訪問一小時後過期的緩存
Caffeine.newBuilder().expireAfterAccess(1, TimeUnit.HOURS).build(); 
```

expireAfterWrite(long, TimeUnit): 一個緩存將會在其創建或者最近一次被更新之後的一段時間後被認定為過期。

```java=
// 建立一個寫入十小時後過期的緩存
Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.HOURS).build(); 
```

expireAfter(Expiry): 一個緩存將會在特定操作後的一段時間後被認定為過期。

```java=
// 建立一個創建後一秒過期的緩存
Cache<String, String> cache = Caffeine.newBuilder()
    .expireAfter(new Expiry<String, String>() {
        public long expireAfterCreate(String key, String value, long currentTime) {
            return TimeUnit.SECONDS.toNanos(1);
        }
        public long expireAfterUpdate(String key, String value, long currentTime, long currentDuration) {
            return currentDuration;
        }
        public long expireAfterRead(String key, String value, long currentTime, long currentDuration) {
            return currentDuration;
        }
    })
    .build();

String key = "123";
String value = "456";

cache.get(key, k -> value);

Thread.sleep(2000);
System.out.println(cache.getIfPresent(key));
```

> 打印結果:
> null

* 基於容量

當緩存容量達到最大值時，Caffeine 將使用 [LRU 策略](https://zhuanlan.zhihu.com/p/85846117)對緩存進行驅逐。

```java=
// 創建一個最大容量為1000的緩存
Caffeine.newBuilder().maximumSize(1000).build(); 
```

* 基於引用

Caffeine 在配置後允許 GC 來幫助清理緩存當中的元素，其中 key 只支援弱引用，而 value 則支援弱／軟引用；此外 AsyncCache 不支援軟引用和弱引用。

:::info
由於 GC 只依賴於引用相等性，因此緩存只會透過 == 來進行 key／value 而不是對象相等 equals()去進行之間的比較。
:::

Caffeine.weakKeys()：在保存 key 的時候將會進行弱引用。這允許在 GC 的過程中，當 key 沒有被任何強引用指向的時候去將緩存元素回收。

Caffeine.weakValues()：在保存 value 的時候將會使用弱引用。這允許在 GC 的過程中，當 value 沒有被任何強引用指向的時候去將緩存元素回收。

```java=
// 當 key 和緩存元素都不再存在其他強引用的時候驅逐
Cache<String, String> cache = Caffeine.newBuilder()
    .weakKeys()
    .weakValues()
    .build();
```

Caffeine.softValues()：在保存 value 的時候將會使用軟引用。為了符合內存的需要，在 GC 過程中被軟引用的對象將會被通過 LRU 算法回收。由於使用軟引用可能會影響整體性能，通常建議使用基於緩存容量的驅逐策略代替軟引用的使用。

```java=
// 當進行GC的時候進行驅逐
Cache<String, String> cache = Caffeine.newBuilder()
    .softValues()
    .build();
```

#### 刷新機制

當緩存運行過程中，有些緩存值我們需要定期進行刷新，Caffeine 也提供了刷新機制 refreshAfterWrite()，此方法將在 key 允許刷新後的首次訪問時，立即返回舊值，同時異步地對緩存值進行刷新。

當然也可以透過基於時間的驅逐策略 expireAfterWrite()來定期刷新緩存，但此方式帶來的問題是一旦緩存過期，下次重新加載緩存時將使得調用線程處於阻塞狀態。然而使用 refreshAfterWrite() 不至於因為緩存驅逐而被阻塞。

最後刷新機制只支持 LoadingCache 和 AsyncLoadingCache。

```java=
// 依據執行時的現在秒數寫入不同的緩存值
 public static void main(String[] args) throws InterruptedException {

        LoadingCache<String, String> cache = Caffeine.newBuilder().refreshAfterWrite(3, TimeUnit.SECONDS).
                build(k -> returnValueBaseOnTime());

        String key = "123";

        cache.get(key);
        System.out.println("first => " + cache.getIfPresent(key));

        Thread.sleep(30000);
        
        System.out.println("second => " + cache.getIfPresent(key));
    }

    static String returnValueBaseOnTime() {

        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
        if (second <= 30)
            return "123";
        else
            return "456";
    }
```

> 打印結果:
> first => 456
> second => 123

## 應用情境說明

在第一個情境中，我們將使用 ConcurrentMapCache 來對使用者的查詢資料做緩存；而在第二、三、四個情境中，將使用不同方式新增 CaffeineCache 的緩存技術至專案中。

## 建置環境

1.建立 Spring Boot 專案後，開啟 pom.xml 檔加入依賴

```xml=
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>

<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
    <version>3.1.1</version>
</dependency>
```

2.建立 User.java，用來回傳值

```java=
package com.example.demo.model;

public class User {
    private String id;
    private String name;
    
    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
```

3.建立 StatusResource.java，用來回傳異動資料的狀態

```java=
public class StatusResource {
    private String message;
    
    public StatusResource(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

```

## 實作過程

### 情境一：使用 ConcurrentMapCache

#### 1. 建立 CacheConfig.java 設定 key 的生成與 Cache 管理

要在 class 上加註解 @Configuration、@EnableCaching

```java=
package com.example.demo.config;

import java.lang.reflect.Method;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    // key 值命名
    // Cache key 生成方式
    @Bean
    public KeyGenerator wiselyKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params){
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    // Cache 管理
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager manager = new ConcurrentMapCacheManager();
        return manager;
    }
}

```

#### 2. 建立 UserService.java

要讓方法支援快取要加上 @Cacheable
清除 Cache 的方法要加上 @CacheEvict

```java=
package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import com.example.demo.model.User;

@Component
public class UserService {

    // 取得所有 user
    @Cacheable(value = "getAllUsers", keyGenerator = "wiselyKeyGenerator")
    public List<User> getAllUsers() {
        System.out.println("----- getAllUsers start --------");
        List<User> users = findAllUser();
        System.out.println("----- getAllUsers end --------");
        return users;
    }

    // 取得 user by id
    @Cacheable(value = "user", key = "#p0")
    public User getUserById(String id) {
        System.out.println("----- getUserById start --------");
        User user = findUserById(id);
        System.out.println("----- getUserById start --------");
        return user;
    }

    // 清除 getAllUsers cache
    @CacheEvict(value = "getAllUsers", allEntries = true)
    public void clearGetAllUsersCache() {

    }

    // 清除 cache by id
    @CacheEvict(value = "user", key = "#p0")
    public void clearGetUserById(String id) {

    }
    
    
    private List<User> findAllUser(){
        List<User> users = new ArrayList<>();
        users.add(new User("00001", "Tracy"));
        users.add(new User("00002", "Peggy"));
        users.add(new User("00003", "Kazzy"));
        users.add(new User("00001", "Bill"));
        return users;
    }
    
    private User findUserById(String id) {
        List<User> users = findAllUser();
        User user = users.stream().filter(u -> id.equals(u.getId())).findFirst().get();
        return user;
    }
}


```

#### 3. 建立 CacheController.java

並在 controller 實作 4 個 API
在 class 上加上 @RestController

1. 取得所有 user
2. 清除 getAllUsers cache
3. 取得 user by id
4. 清除 cache by id

```java=
package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.resource.StatusResource;
import com.example.demo.service.UserService;

@RestController
public class CacheController {
    
    @Autowired
    private UserService service;

    // 取得所有 user
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    // 清除 getAllUsers cache
    @DeleteMapping("/all")
    public StatusResource clearGetAllUsersCache() {
        service.clearGetAllUsersCache();
        return new StatusResource("OK");
    }

    // 取得 user by id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return service.getUserById(id);
    }

    // 清除 cache by id
    @DeleteMapping("/{id}")
    public StatusResource clearGetUserById(@PathVariable String id) {
        service.clearGetUserById(id);
        return new StatusResource("OK");
    }

}


```

#### 4. 測試

* 取得所有 user
Http method：Get
url：<http://localhost:8080/all>
第一次打 API，有回傳結果，有進入 method
![1](https://i.imgur.com/3UZQ2Gm.png)

![2](https://i.imgur.com/xn2zYn2.png)

第二次打 API，就沒有再進入 method
![3](https://i.imgur.com/0hiO88j.png)

* 清除 getAllUsers cache
上一步驟已經將所有的 user 存入 cache 中
接下來測試清除 cache
Http method : Delete
url：<http://localhost:8080/all>
打 API 後回傳 OK
![1](https://i.imgur.com/OV19SBk.png)

再打一次取得所有 user 的 API，會進入 method

![2](https://i.imgur.com/F3LM7Aj.png)

* 取得 user by id
Http method : Get
url：<http://localhost:8080/00001>
第一次打 API，有回傳結果，有進入 method
![1](https://i.imgur.com/qmhCOQG.png)

![2](https://i.imgur.com/EEJJmvz.png)

第二次打 API，就沒有再進入 method
![3](https://i.imgur.com/trEYpfI.png)

* 清除 cache by id
上一步驟已經將 00001 的 user 存入 cache 中
接下來測試清除 cache
Http method : Delete
url ：<http://localhost:8080/00001>
打 API 後回傳 OK
![1](https://i.imgur.com/8PXzeWM.png)

再打一次取得 00001 user 的 API，會進入 method
![2](https://i.imgur.com/SnxKCJe.png)

### 情境二：使用 @Primary

#### 1. 調整 CacheConfig.java

* 新增 @Primary，此註釋表示此 CacheManager 為預設 CacheManager
* 新增第二個 CacheManager

```java=
// 此兩處 @Bean name 可加可不加，加的話可以方便用  @Qualifier 來觀察使用到的緩存
@Bean(name="cacheManager")
@Primary
public CacheManager cacheManager() {
    ConcurrentMapCacheManager manager = new ConcurrentMapCacheManager();
    return manager;
}

@Bean(name="alternateCacheManager")
public CacheManager alternateCacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager();
    cacheManager.setCaffeine(Caffeine.newBuilder()
                             // 建立後三十秒即過期
                             .expireAfterWrite(30, TimeUnit.SECONDS));
    return cacheManager;
}
```

#### 2. 調整 CacheController

* 新增 getAllUsersForCaffeine()

```java=
@GetMapping("/getAllUsersForCaffeine")
public List<User> getAllUsersForCaffeine() {
    return service.getAllUsersForCaffeine();
}
```

#### 3. 調整 UserService

* 新增 getAllUsersForCaffeine()

```java=
// 指定使用的 cacheManager 為 alternateCacheManager
@Cacheable(value = "caffeineCache", cacheManager = "alternateCacheManager")
public List<User> getAllUsersForCaffeine() {
    System.out.println("----- getAllUsersForCaffeine start --------");
    List<User> users = findAllUser();
    System.out.println("----- getAllUsersForCaffeine end --------");
    return users;
}
```

#### 4. 再次測試

因為情境二、三、四的測試情境一致，主要都是確認是否使用 CaffeineCache 和觀察是否有自動移除緩存，所以可以參考以下方式搭配不同情境測試。

* 取得所有 user for caffeine
Http method：Get
url：<http://localhost:8080/getAllUsersForCaffeine>
第一次打 API，有回傳結果，有進入 method
![100](https://i.imgur.com/sAHE6GA.png)

![101](https://i.imgur.com/5QSJn2b.png)

第二次打 API，就沒有再進入 method
![102](https://i.imgur.com/1swX7Jl.png)

過30秒再打 API，有再進入一次 method（因為我們有設計緩存建立30秒後自動清除緩存）
![103](https://i.imgur.com/x8UYzzc.png)

### 情境三：使用 CachingConfigurerSupport

#### 1. 再次調整 CacheConfig.java

* 讓 CacheConfig 繼承 CachingConfigurerSupport
* 移除 @Primary

```java=
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
    
    @Bean(name="cacheManager")
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager manager = new ConcurrentMapCacheManager();
        return manager;
    }
    
    //...省略 alternateCacheManager()
}
```

### 情境四：使用 CacheResolver

#### 1. 新增 MultipleCacheResolver.java

```java=
public class MultipleCacheResolver implements CacheResolver {
    
    private final CacheManager simpleCacheManager;
    private final CacheManager caffeineCacheManager;

    public MultipleCacheResolver(CacheManager simpleCacheManager, CacheManager caffeineCacheManager) {
        this.simpleCacheManager = simpleCacheManager;
        this.caffeineCacheManager = caffeineCacheManager;
    }

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        Collection<Cache> caches = new ArrayList<Cache>();
        
        // 只有 method name 為 getAllUsersForCaffeine 才使用 caffeineCache
        if ("getAllUsersForCaffeine".equals(context.getMethod().getName())) {
            caches.add(caffeineCacheManager.getCache("caffeineCache"));
        } else {
            caches.add(simpleCacheManager.getCache("concurrentMapCache"));
        }
        return caches;
    }
}
```

#### 2. 再次調整 CacheConfig.java

* 新增 CacheResolver 的 Bean

```java=
@Bean
public CacheResolver cacheResolver() {
    return new MultipleCacheResolver(cacheManager(), alternateCacheManager());
}
```

#### 3. 再次調整 UserService.java

* 把所有的 value 拿掉 => 其實拿不拿掉沒有關係，只不過因為在 MultipleCacheResolver 有指定使用的 cache value，所以就算在 @Cacheable 內有指定 value，還是會以 MultipleCacheResolver 內指名的 cache value 為準。
* 拿掉 cacheManager = "alternateCacheManager"，因為已經交由 MultipleCacheResolver 指定想使用的緩存機制

```java=
@Cacheable(keyGenerator = "wiselyKeyGenerator")
public List<User> getAllUsers() {
    System.out.println("----- getAllUsers start --------");
    List<User> users = findAllUser();
    System.out.println("----- getAllUsers end --------");
    return users;
}

@Cacheable()
public List<User> getAllUsersForCaffeine() {
    
    System.out.println(LocalDateTime.now() + " ----- getAllUsersForCaffeine start --------");
    List<User> users = findAllUser();
    System.out.println(LocalDateTime.now() + " ----- getAllUsersForCaffeine end --------");       
    return users;
}

// 取得 user by id
@Cacheable(key = "#p0")
public User getUserById(String id) {
    System.out.println("----- getUserById start --------");
    User user = findUserById(id);
    System.out.println("----- getUserById start --------");
    return user;
}

// 清除 getAllUsers cache
@CacheEvict(allEntries = true)
public void clearGetAllUsersCache() {

}

// 清除 cache by id
@CacheEvict(key = "#p0")
public void clearGetUserById(String id) {

}
```



## 參考資料

* [Spring Cache，从入门到真香](https://zhuanlan.zhihu.com/p/266804094)
* [Spring 快取註解 @Cacheable、@CacheEvict、@CachePut 使用](https://iter01.com/472936.html)
* [spring boot中的cache使用](https://www.796t.com/content/1549588153.html)
* [Caffeine 官方 github](https://github.com/ben-manes/caffeine)
* [Java 高性能缓存库 - Caffeine](https://ghh3809.github.io/2021/05/31/caffeine/)
* [Using Multiple Cache Managers in Spring](https://www.baeldung.com/spring-multiple-cache-managers)

## 撰寫紀錄

| 人員 | 日期 | 修改紀錄 |
| - | - | - |
| 婉瑜 | 2022/7 | 初版 |
| 于慧 | 2022/9 | 加入 Caffeine 介紹及新增情境 |
