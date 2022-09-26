---
tags: basic, Log4J
---

# Log4J

## 前言

> *當問題發生時，就需要有 Log 來讓我們方便找有哪些錯誤訊息，以推斷問題出在哪裡。*

這篇文章主要是在介紹 Apache 所推出跟日誌相關的 ==Log4J2==，與如何實作。

:::danger

* 操作環境｜Win10｜64 位元
* Spring Tools 4
:::

## 目錄

* [Log4J](#log4j)
  * [前言](#前言)
  * [目錄](#目錄)
  * [介紹/基本概念](#介紹基本概念)
    * [Log 級別（Logger）](#Log-級別（Logger）)
    * [Log 輸出目的地（Appender）](#Log-輸出目的地（Appender）)
    * [Log 輸出格式（Layout）](#Log-輸出格式（Layout）)
    * [埋 Log 的時機](#埋-Log-的時機)
  * [應用情境說明](#應用情境說明)
  * [建置環境](#建置環境)
    * [Step 1. 建立專案](#Step-1.-建立專案)
    * [Step 2. 加入 Maven 依賴](#Step-2.-加入-Maven-依賴)
    * [Step 3. 建立配置文件](#Step-3.-建立配置文件)
  * [實作過程](#實作過程)
    * [Demo 1: ConsoleAppender + PatternLayout](#Demo-1:-ConsoleAppender-+-PatternLayout)
    * [Demo 2: FileAppender + PatternLayout](#Demo-2:-FileAppender-+-PatternLayout)
    * [Demo 3: RollingFileAppender + PatternLayout](#Demo-3:-RollingFileAppender-+-PatternLayout)
  * [延伸閱讀 - 了解 Log4j 核彈級資安漏洞](#延伸閱讀-\-\-了解-Log4j-核彈級資安漏洞)
  * [參考資料](#參考資料)
  * [撰寫紀錄](#撰寫紀錄)

## 介紹/基本概念

* ==Log4j== 全名為 **Log for java**

:::danger

* log4j
  * 2015 年**停止更新**，最新版本 1.2.17
  * 配置文件
    * src/main/resource/log4j.**++properties++**
:::

:::info

* log4j2
  * ==效能優於 log4j==
  * 配置文件
    * src/main/resource/log4j2.**++xml++**
:::

### <span class="label">Log **++級別（Logger）++**</span>

> 為了方便對日誌的資料訊息的輸出顯示，進行分級管理。
>
> *哪些訊息需要輸出，哪些訊息不需輸出，只需在++配置文件++中稍加修改即可，省時又省力。*

* 高 → 低
  1. `off`
  2. `fatal`
  3. `error`
  4. `warn`
  5. `info`
  6. `debug`
  7. `trace`
  8. `all`

### <span class="label">Log **++輸出目的地（Appender）++**</span>

:::success
舉幾個 Appender 為範例，詳細內容與其餘 Appender 請閱讀[官網](https://logging.apache.org/log4j/2.x/manual/appenders.html)
:::

#### <span class="label2">*ConsoleAppender* 控制台輸出</span>

```xml=
<Console name="appenderLabel" target="SYSTEM_OUT"></Console>
```

* `target`
  * `SYSTEM_OUT`
  * `SYSTEM_ERR`

---

#### <span class="label2">*FileAppender* 建立檔案</span>

```xml=
<File name="appenderLabel" fileName="logF/record.log" append="false"></File>
```

* `fileName`：輸出位置與檔名
  * 若「logF」未建立，會自動幫忙建立
* `append`：是否追加寫入；設定為 `false` 即為覆蓋寫入
* Log 輸出後檔案位置為［*++...YourProject/logF/record.log++*］

---

#### <span class="label2">*RollingFileAppender* 滾動更新（rollover）檔案</span>

> ***++rollover++**
> 當滿足一定條件（e.g. 達到指定大小、指定時間）後，就重新命名原 Log 檔案進行歸檔，並生成新 Log 檔案用於寫入。*
>
> * 可以限制每個 LOG 檔案的大小
> * 若超過限制，則建立新的 LOG 檔

```xml=
<RollingFile name="appenderLabel" fileName="logF/rollF.log" filePattern="logF/$${date:yyyyMMdd}/%d{yyyyMMdd-HH}_%i.log">
    <ThresholdFilter level="warn" onMatch="ACCEPT" onMismatch="DENY"/>
    
    <Policies>
        <TimeBasedTriggeringPolicy interval="1" modulate="true" />
        
        <SizeBasedTriggeringPolicy size="100MB" />
        
        <OnStartupTriggeringPolicy />
    </Policies>
        
    <DefaultRolloverStrategy max="10" />
</RollingFile>
```

* `fileName`：**++寫入 ing++**－輸出位置與檔名
  * 若資料夾或檔案未建立，會自動幫忙建立
* `filePattern`：**++觸發 rollover，檔案的重新命名++**－輸出位置與檔名

1. 🎈 `ThresholdFilter`：級別過濾
   * `level`：級別
   * `onMatch`：該級別以上是否輸出
     * `ACCEPT`：允許
     * `DENY`：拒絕
     * `NEUTRAL`：中立
   * `onMismatch`：其餘級別是否輸出，帶入的值同 `onMatch`

   * e.g.
     * 只輸出「==debug==」的級別

     ```xml=
     <ThresholdFilter level="info" onMatch="DENY" onMismatch="NEUTRAL" />
     <ThresholdFilter level="debug" onMatch="ACCEPT" onMismatch="DENY" />
     ```

     * 輸出「==info==」以上的級別

     ```xml=
     <ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY" />
     ```

2. TriggeringPolicy 觸發策略
   * 🎈 `TimeBasedTriggeringPolicy` 更新時機：每隔一段時間
     * 取決於檔案命名 filePattern=＂`%d{yyyyMMdd-HH}`＂的設定
     * `interval`：hour
     * `modulate` 可設為 `true`｜`false`；調節 `interval`，取最小時間單位
     * e.g.
       * 間隔「==1 天==」觸發一次 rollover

       ```xml=
       <RollingFile filePattern="%d{yyyyMMdd}.log"></RollingFile>
       <TimeBasedTriggeringPolicy interval="1" modulate="true" />
       ```

       * 間隔「==1 分鐘==」觸發一次 rollover

       ```xml=
       <RollingFile filePattern="%d{HH-mm}.log"></RollingFile>
       <TimeBasedTriggeringPolicy interval="1" modulate="true" />
       ```

   * 🎈 `SizeBasedTriggeringPolicy` 更新時機：超過檔案大小
     * `size`：KB,MB,GB

   * 🎈 `OnStartupTriggeringPolicy` 更新時機：JVM（Java 虛擬機）啟動
     * 無參數設置
     * 自行判斷 Log 檔案的創建時間和 JVM 的啓動時間
     * 若 Log 檔案的創建時間早於 JVM 的啓動時間，則將原來的 Log 檔案歸檔，然後創建一個新的空白 Log 檔案

3. 🎈 `DefaultRolloverStrategy` 限制更新檔案保留－計數方式

   :::danger
  
   * 取決於檔案命名 filePattern=＂`%i`＂ 的設定
     * 若無 `%i`，`max` 將不起作用
   * 📢 勿認為 `max` = 需要保留的日誌檔案最大數量
   :::

   * `max` 當計數器達到最大值，最舊的檔案將被覆寫

### <span class="label">Log **++輸出格式（Layout）++**</span>

:::success
以下為常用的 Layout，其餘請閱讀[官網](https://logging.apache.org/log4j/2.x/manual/layouts.html)
:::

* **PatternLayout**

```xml=
<PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5p %c - %m%n" />
```

```java=
logger.debug("debug test");

// 控制台輸出
// 09:31:04.480 [main] DEBUG com.example.demo.Log4jTestApplication - debug test
```

* `%d{yyyy/MM/dd HH:mm:ss}`：日期時間
* `%p`｜`level`：Log 級別
  * e.g.
    * `%-5p`：**靠左**
    * `%5p`：**靠右**
* `%c`：Log 所在的 package.class
  * e.g.
    * `%c`：***com.example.demo.Log4jTestApplication***
    * `%c{1}`：***Log4jTestApplication***
    * `%c{-2}`：***demo.Log4jTestApplication***
    * `%c{3.}`：***com.exa.dem.Log4jTestApplication***
* `%L`：Log 程式碼所在的行數
* `%m`｜`%msg`：Log message
* `%t`：Thread name
* `%n` ：換行
* `%l`：Log 程式碼所在的 location = `%c.%t(%c{1}.java:%L)`

### <span class="label">**++埋 Log 的時機++**</span>

1. debug 時
2. 發生 Exception 時，紀錄錯誤
3. 針對特殊的動作記錄，例：帳號登入失敗、帳號鎖定、建立了新帳號、使用者帳號狀態變更

## 應用情境說明

專門用於 `Java` 語言的日誌記錄工具

## 建置環境

:::danger
🎈 操作前，請建立環境

1. Download [OpenJDK 11](https://download.java.net/java/GA/jdk11/13/GPL/openjdk-11.0.1\_osx-x64\_bin.tar.gz)
2. Download [Spring Tools 4](https://spring.io/tools)｜for Eclipse｜WINDOWS X86_64
3. [Setting JDK in eclipse](https://stackoverflow.com/questions/13635563/setting-jdk-in-eclipse)
   * Window > Preferences > Java > Installed JREs
:::

### <span class="label">Step 1. 建立專案</span>

File > New > Spring Starter Project

:::info

* 初始專案結構如下，僅供參考
![1](https://i.imgur.com/OMs8Gaz.png)
:::

### <span class="label">Step 2. 加入 Maven 依賴</span>

> 🔔 For Spring Boot

* 🎈 排除預設引入的 `spring-boot-starter-logging`

```xml=
<exclusions>
    <exclusion>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-logging</artifactId>
    </exclusion>
</exclusions>
```

* 🎈 引入 log4j2

```xml=
<!-- log4j2 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
```

:::info

```xml=
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-logging</artifactId>
            </exclusion>
        </exclusions>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>


    <!-- log4j2 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-log4j2</artifactId>
    </dependency>
</dependencies>
```

:::

### <span class="label">Step 3. 建立配置文件</span>

* src/main/resources/`log4j2.xml`
* 大略格式，僅供參考

```xml=
<?xml version="1.0" encoding="UTF-8"?>
<!-- 設置 Log4J2 自身內部的輸出訊息，可不設置 -->
<Configuration status="INFO">
    <Properties>
        <Property name="param">value</Property>
    </Properties>
    <!-- 📢 Appender&Layout -->
    <Appenders>
        <!--
            <輸出目的地(Appender)>
                <輸出格式(Layout) />
            </輸出目的地>
        -->
    </Appenders>
    <Loggers>
        <!--
            name          針對此 package 裡面的 class
            level         限制只能輸出 "warn" 級別以上的 Log
            additivity    是否追加輸出 => 是否輸出 Root 的內容
        -->
        <Logger name="com.example.demo" level="warn" additivity="true">
            <AppenderRef ref="Appenders_xxxAppender_name" />
        </Logger>
        <Root level="debug">
            <AppenderRef ref="Appenders_xxxAppender_name" />
        </Root>
    </Loggers>
</Configuration>
```

## 實作過程

```java=
// xxx.java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
```

```java=
// xxx.java
private static final Logger logger = LogManager.getLogger(Log4jTestApplication.class);
```

### <span class="label3">Demo 1: ConsoleAppender + PatternLayout</span>

* log4j2.xml

```xml=
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="warn">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%t %d{MM/dd HH:mm:ss} %-5p %logger %m%n" />
        </Console>
    </Appenders>
    <Loggers>
        <Root level="trace">
            <AppenderRef ref="Console" />
        </Root>
    </Loggers>
</Configuration>
```

* Log4jTestApplication.java

```java=
logger.debug("log logger: {}", "trace");
logger.error("Error Message: ", new NullPointerException("NullError"));
```

* ==🎉 控制台==
  ![2](https://i.imgur.com/ppS3bMy.png)

  :::info

  * 若 log4j2.xml：`<Root level="trace">`改成`<Root level="info">`
    * 只會輸出`info`以上的 Log 級別，故不會輸出`logger.debug`的內容
    ![2](https://i.imgur.com/TfTrWMZ.png)
  :::

### <span class="label3">Demo 2: FileAppender + PatternLayout</span>

* log4j2.xml
  > 若「logs」資料夾未建立，會自動幫忙建立

```xml=
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="warn">
    <Properties>
        <property name="LOG_HOME">D:\logs</property>
    </Properties>
    <Appenders>
        <File name="File" fileName="${LOG_HOME}/record.log" append="false">
            <PatternLayout pattern="%t %d{MM/dd HH:mm:ss} %-5p %logger %m%n" />
        </File>
    </Appenders>
    <Loggers>
        <Root level="trace">
            <AppenderRef ref="File" />
        </Root>
    </Loggers>
</Configuration>
```

* Log4jTestApplication.java

```java=
logger.debug("log logger: {}", "trace");
logger.error("Error Message: ", new NullPointerException("NullError"));
```

* ==🎉 D:\logs\record.log==
  ![3](https://i.imgur.com/HNOjKGJ.png)

### <span class="label3">Demo 3: RollingFileAppender + PatternLayout</span>

* log4j2.xml

```xml=
<?xml version="1.0" encoding="UTF-8"?>

<Configuration status="warn">

    <Properties>
        <property name="LOG_HOME">D:\logs</property>
    </Properties>

    <Appenders>

        <RollingFile name="RollingFile" fileName="${LOG_HOME}/rollF.log" filePattern="${LOG_HOME}/$${date:yyyyMMdd}/%d{HH-mm}_%i.log">

            <ThresholdFilter level="warn" onMatch="ACCEPT" onMismatch="DENY"/>

            <Policies>
                <TimeBasedTriggeringPolicy interval="1" modulate="true" />

                <SizeBasedTriggeringPolicy size="100MB" />

                <OnStartupTriggeringPolicy />
            </Policies>

            <DefaultRolloverStrategy max="3" />

        </RollingFile>

    </Appenders>

    <Loggers>
        <Root level="trace">
            <AppenderRef ref="File" />
        </Root>
    </Loggers>

</Configuration>
```

* Log4jTestApplication.java

```java=
logger.error("Error Message: ", new NullPointerException("NullError"));
```

* ==🎉 D:\logs==
  ![3](https://i.imgur.com/ObgLrL9.png)

  :::info
  時間［10:30］，最多只能存 3 個檔，因設定
  
  > 1. `TimeBasedTriggeringPolicy` 的 `interval:1` & `modulate:true`
  > 2. `DefaultRolloverStrategy` 的 `max:3`
  => 1 分鐘內，`%i` 最多到 3
  
  ，故當 `%i` 計數到 3 後，再次輸出 Log 會覆蓋最舊的檔案
  :::
  
## 延伸閱讀 - 了解 Log4j 核彈級資安漏洞

### 前言
2021 年末資安界最大的新聞莫過於 Log4j 的漏洞，編號為 CVE-2021-44228，又被稱為 Log4Shell，甚至被一些人形容為「核彈級漏洞」，可見這個漏洞的影響程度之深遠。

### Log4j 的漏洞 - 以監視攝影機舉例
我有個朋友叫小明，他家是開雜貨店的。就跟其他商店一樣，在店裡有一支監視攝影機，怕有什麼消費糾紛或是有人來搶劫或偷東西，因此讓攝影機 24 小時全程錄影，真的發生什麼事了，就會有證據留存下來。

原本這支攝影機用了十幾年都沒什麼事情，畢竟不就是把影像記錄起來嗎，能有什麼事情？但最近卻突然有人發現一個攝影機的隱藏功能（嚴格來講不是隱藏功能，因為攝影機的說明書上其實有提到，可是大家都懶得看那一百多頁的說明書，所以很少人知道這個功能）

這個功能是什麼呢？那就是除了錄影以外，這台監視攝影機還有個智慧圖片辨識的功能，如果它看到特定的影像，會根據影像的內容去執行相對應的動作。舉例來說好了，這個圖片辨識功能需要把指令寫在 100×100 的板子上，一定要黑底白字加上特定格式，像這個樣子：
![指令圖](https://i.imgur.com/4qjjxcM.png)

當攝影機看到上面的圖，符合特定格式，就執行了上面的指令：「關機」，就真的關機了！但關機還沒什麼，指令還可以寫說「把攝影機資料全都給我」之類的，再者，攝影機本來就會即時連線到其他伺服器，這個指令也可以對那些伺服器做操作，例如說把上面的資料全都偷下來等等。

總之呢，一旦讓攝影機拍到指定格式的東西，就會幫你執行指令。

這個功能被爆出來以後，血流成河，因為太多地方都有監視攝影機了，因此許多人都帶著這個板子去看看會不會觸發這個功能。攝影機有分型號，只有一台叫做 Log4j 的攝影機會出事，其他不會，但要注意的事情是有些攝影機它雖然不叫做這名字，可其實是從 Log4j 作為基底改出來的，就一樣會出事。

而有些東西儘管不是攝影機也會出事，例如說有台智慧冰箱，號稱內部有微型攝影機可以即時監控冰箱內部狀況，恰巧這個微型攝影機就是 log4j 這個型號的攝影機改版出來的，所以也有同樣的問題。

以上是對於 Log4j 漏洞的簡單比喻，在這個故事中雜貨店就像是你的網站，而攝影機的功能就是拿來紀錄（log）對於網站的那些請求（request），整個故事只要記兩個重點就好：

1. Log4j 是拿來記錄東西用的
2. 漏洞原理是只要紀錄某些特定格式的文字，就會觸發一個功能可以執行程式碼

### 為什麼這個漏洞如此嚴重？

1. Log4j 這個套件使用的人數極多，只要你有用 Java，幾乎都會用這個套件來紀錄 log

2. 觸發方式容易，你只要在 request 的各個地方塞滿這些有問題的字串，server 只要有記錄下來其中一個，就能夠觸發漏洞，而前面我們有提到紀錄 log 本來就是家常便飯的事情

3. 能造成的影響極大，漏洞被觸發之後就是最嚴重的 RCE，可以直接執行任意程式碼

### 該如何修補？
治本的方法就是把 Log4j 停用或是升版，升級到不會被這個漏洞影響的版本，但有些時候第一時間的改版可能沒有把漏洞完全補掉，因此記得更新完以後還是要密切注意是否有更新的版本。


## 參考資料

* [JAVA - JAVA Log4j 專門用於 Java 語言的日誌記錄工具](https://ithelp.ithome.com.tw/articles/10261299)
* [Log4j 簡易教學 (Java)](https://dotblogs.azurewebsites.net/Leon-Yang/2021/01/06/155519)
* [Log4j2－官網](https://logging.apache.org/log4j/2.x/manual/)
* [Spring Boot 2 中如何使用 Log4j2 记录日志](https://segmentfault.com/a/1190000041208002)
* [log4j2 常用 Appender 介紹](https://www.twblogs.net/a/5b7c6e862b71770a43daf440)
* [RollingFileAppender](https://www.796t.com/content/1549952122.html)
* [log4j2 ThresholdFilter onMatch/onMismatch neutral/accept](https://blog.csdn.net/londa/article/details/125122301)
* [【非技術背景的人也能看懂！】一文了解 Log4j 核彈級資安漏洞，為何嚴重到火星也受害？](https://buzzorange.com/techorange/2021/12/21/what-is-log4j-and-log4shell/)

## 撰寫紀錄

| 人員 | 日期 | 修改紀錄 |
| - | - | - |
| 佳穎 | 2022/7 | 初版 |

<style>

.label {
    color: #fccb82;
    font-size: 20px;
}

.label2 {
    color: #0bb9a8;
    font-size: 20px;
}

.label3 {
    color: #ce7a4b;

</style>
