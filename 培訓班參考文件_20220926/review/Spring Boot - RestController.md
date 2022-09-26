---
tags: Java, Spring Boot
---

# Spring Boot - RestController

## 前言

這篇文章主要是為了瞭解什麼是 Spring Boot，建立 Web MVC 專案和建立 RESTful Web Service 專案，和介紹簡單的 annotation（註解）。

## 目錄

* [Spring Boot - RestController](#Spring-Boot---RestController)
  * [前言](#前言)
  * [目錄](#目錄)
  * [介紹/基本概念](#介紹基本概念)
    * [Spring Boot 簡介](#Spring-Boot-簡介)
    * [Rest Controller 簡介](#Rest-Controller-簡介)
    * [MVC 概念介紹](#MVC-概念介紹)
    * [基礎 annotation](#基礎-annotation)
  * [應用情境說明](#應用情境說明)
  * [建置環境](#建置環境)
  * [實作過程](#實作過程)
    * [使用指令部署程式](#使用指令部署程式)
  * [註解說明](#註解說明)
  * [參考資料](#參考資料)
  * [撰寫紀錄](#撰寫紀錄)

## 介紹/基本概念

### Spring Boot 簡介

Spring Boot 是一個新型的框架，簡化了 Spring 的應用開發，同時集成了大量常用的第三方庫配置，
例如 Jackson、JDBC、Mongo、Redis、Mail 等等，Spring Boot 應用中這些第三方庫幾乎可以零配置的開箱即用。

且大部分的 Spring Boot 應用都只需要非常少量的配置代碼，讓開發者能夠更加專注於業務邏輯。
另外 Spring Boot 通過集成大量的框架，使得依賴包的版本沖突及引用的不穩定性等問題得到了很好的解決。

> 簡而言之，Spring Boot 的目的是提供一組工具，以便快速構建 + 容易配置 Spring 應用程序。

### Rest Controller 簡介

Rest Controller 是透過 REST 以及 JSON 格式與前端溝通，採用 REST 及 JSON 的好處是，前後端的開發平台可以完全獨立。
後端可以是 Spring、PHP、.Net 或者 Python，前端可以是各種框架，也可以是手機 App，
甚至可以透過 REST 及 JSON 與其他的伺服器交換資料。現在很多的 open data 的平台都是採用這樣的方式交換資料。

### MVC 概念介紹

#### 什麼是 MVC ?

MVC 是一種軟體架構模式，目標是將軟體區分為

* M：Model（模型）
* V：View（視圖）
* C：Controller（控制器）

簡單來說：使用者會看到 View 的東西，操作 Controller 的方法，透過 Model 進行商業邏輯，透過角色的界定，將每個 class 的職責分離。

* 範例 1
  ![範例](https://i.imgur.com/PPwch1I.png)
  * 顧客（View）向服務生（Controller）點餐 => client send Request to server
  * 服務生（Controller）告知廚師（Model）要處理的餐點
  * 廚師（Model）到冰箱（DB）取出食材
  * 廚師（Model）料理餐點（負責商業邏輯的處理）
  * 廚師（Model）出餐給服務生（Controller）
  * 服務生（Controller）將餐點遞給顧客（View）=> server send Response to client
  
* 範例 2
  * 以電商網站登入為例子
  * 使用者在 View 輸入帳號及密碼
  * 點擊「確認」發送 reqeust 到後端
  * 後端 Controoler 接收，並呼叫 Model 處理
  * Model 到 DB 取出使用者的資料做驗證
  * 驗證成功，Controller 回傳結果給 View（前端）

#### 實踐情境中

* 以 Spring Boot 來說，我們會建立 Contorller、Service、Model 的 Package，用來儲放我們在 MVC 中定義的角色，而 Spring Boot 便提供了相對應的 @Annotation 來幫助我們更快速的定義 class 的角色。
* 對於 Contorller 該怎麼介接 Model 呢？其實就是將 Model 的 class 透過 @Autowired 或是 new 的方式引用進來，就可以呼叫要執行的商業邏輯。
* 在 MVC 的理念中，最優先要做到的是職責分離，有這樣的程式開發概念後，後續再去設計程式架構，讓所有工程師都能夠有相似的概念進行開發。
* 當我們的架構不是後端渲染方式時，其實 View 的部分可能不會歸納在 Server 的專案中，Server 可能只會實現 Controller、Model 的部分。
* 以 Server 的部分來舉例，Controller 就可以被定義為 API 的入口，負責檢查資料及介接商業邏輯，而要串接 Database、資料來源、其他服務...等，都應該盡可能定義在 Model 中。
* 這邊的名詞有一些變化型：
  * 原本定義的 Model（模型）可以包含商業邏輯、資料格式定義。
  * 但後來比較新一代的 MVC 框架會多定義 Service 的階層，由 Service 來串接所有的商業邏輯，而 Model 則是負責定義資料格式（ex: Request、Response、Entity...）。

### 基礎 annotation

* @Service
  處理商業邏輯的地方
* @Repository
  操作資料庫，像是取資料、異動資料的地方
* @Controller
  API 接口，舉例來說，當 client 端發送請求時，server 端的 controller 會是第一層接收並在呼叫 service 處理商業邏輯
* @RequestMapping
  負責用來接收 request 的處理器，透過 UI 發送 reqeust，URL 所對應到的路徑會和 RequestMapping 所設定 value 相對應
  e.g. 使用者需要取得使用者資訊時，<http://localhost:8080/user>
* @AutoWired
  Spring 運用 DI 和 IoC 的概念，將 Spring 所註解的 Service、Controller 和 Repository 視為 Bean 封裝在容器內，當要取得某個 Bean 時加上 AutoWired 的註解，Spring 就會從容器中取出 Bean 供 class 調用。
* @GetMapping
  @GetMapping 的用法和 @RequestMapping 相同，但只能在 class 內使用
* @PathVariable
  將 URL 的路徑參數視為方法參數傳入
  e.g. <http://localhost:8080/user/1>
* @RequestParam
  將 URL 的 query string 視為方法參數傳入
  e.g. <http://localhost:8080/user?name=bill>
  
#### @Controller 和 @RestController 的區別

**@RestController 註解等同於 @ResponseBody + @Controller**
@RestController 和 @Controller 都是都用來表示某個類是否可以接收 HTTP 請求，二者區別：

1. @RestController 無法返回指定頁面，而 @Controller 可以返回
2. @RestController 前者可以直接返回數據，@Controller 需要 @ResponseBody 輔助

這就是為什麼在 Controller 專案中，demoGet() 方法返回的參數為 HTML 的名稱，
且必須創建一個 HTML 來顯示其內容，而 RestController 專案中，demoGet() 方法返回的則是 JSON 格式的數據。

#### @RequestMapping 和各種 Mapping

@GetMapping 就是 @RequestMapping (method = RequestMethod.GET)，
將 RequestMethod.GET 改成 POST、PUT、DELETE 就可替換為其他 Mapping，
差別在於 RequsetMapping 可以放在 Class 外，但其他 Mapping 不行。

1. @GetMapping：處理 get 請求，判斷實體是否存在或查找實體對象
2. @PostMapping：處理 post 請求，用來向服務器提交信息
3. @PutMapping：用來向服務器提交信息。
4. @DeleteMapping：用在刪除某個或批量實體類

> 如果是添加信息，傾向於用 Post，如果是更新信息，傾向於用 Put

#### @RequestParam、@Pathvariable 和 @RequestBody

@RequestParam 和 @PathVariable 都能夠完成類似的功能，因為本質上，它們都是使用者的輸入，只不過輸入的部分不同，簡單的說就是 URL 的寫法不同：

* @RequestParam
使用 @RequestParam 時，在 URL 上輸入的參數為 <http://host:port/path？參數名=參數值>
@RequestParam 還有一些引數可以使用：
  * defaultValue：如果本次請求沒有攜帶這個引數，或者引數為空，那麼就會啟用預設值
  * name：引數的名稱，要跟 URL 上面的一樣
  * required：這個引數不是必須的，如果為 true，不傳引數會報錯
  * value：跟 name 一樣的作用，是 name 屬性的一個別名

* @PathVariable
使用 @PathVariable 時，輸入在 URL 路徑中為 <http://host:port/path/參數值>

* @RequestBody（不能用於 GET 請求）
通常後端與前端的互動大多情況下是 POST 請求，尤其是傳遞大量引數時，畢竟大量引數暴露在路徑上是不太好的，
而 @RequestBody 是將請求引數設定在請求 Body 中的，也就是請求體，所以可以放大量資料並避免暴露資料，而 GET 無請求體所以無法使用。

> 使用 @RequestBody 需要 Content-Type 為 application/json，確保傳遞是 JSON 資料

## 應用情境說明

在普通的 Java 項目中，大量的 XML 文件配置是很繁瑣的事情，而這會導致開發效率低，整合第三方框架的配置也可能存在衝突問題等等。而 Spring 是一個可以快速開發的框架

* 快速整合第三方框架（Maven 依賴關係，Maven 繼承）
* 打包方式完全採用註解化
  （Spring 3.0 之後採用註解方式啟動 Spring MVC，Spring Boot Web 組件集成 Spring MVC 框架）
* 簡化 XML 的配置
* 內置嵌入 HTTP 服務器（Tomcat、Jetty），降低了對環境的要求
* 最終以 Java 應用程序進行執行

## 建置環境

* [IDE 操作 - Eclipse 篇](https://hackmd.io/@muyumiya1201/eclipse)
* [IDE 操作 - IntelliJ 篇](https://hackmd.io/@BillYang3416/BJzxkz6j9)

## 實作過程

* [Web MVC 實作 - Eclipse 篇](https://hackmd.io/@harrychang/S1Uv8n0s5)
* [Web MVC 實作 - IntelliJ 篇](https://hackmd.io/@xiang38f/HynWY0X35)
* [RESTful Web Service 實作 - Eclipse 篇](https://hackmd.io/@harrychang/B1Nd830jq)
* [RESTful Web Service 實作 - IntelliJ 篇](https://hackmd.io/@xiang38f/ByfVW1En9)

### 使用指令部署程式

部署程式前，第一步要先使用 maven 來將程式打包

1. Maven install

   **Eclpise**：
   專案右鍵 -> Run As -> Maven install
   ![1](https://i.imgur.com/hpYiSmO.png)
   ![2](https://i.imgur.com/pQxdxGv.png)

   **IntelliJ**：
   畫面最右邊點開 maven 視窗->點擊 clean ->點擊 install
   ![click clean](https://i.imgur.com/h50Q1Iy.png)

2. 若執行出現 BUILD SUCCESS，表示執行成功，這時專案底下會多一個 target 資料夾
   ![3](https://i.imgur.com/gNg4awo.png)

3. 打開 target 資料夾，裡面有個打包後的 jar 檔
   ![4](https://i.imgur.com/fR1Hd8K.png)

4. 在該目錄底下用 command 執行 jar 檔後執行
   ![5](https://i.imgur.com/FWAJfrb.png)

5. 輸入以下程式碼，demo-0.0.1-SNAPSHOT.jar 為檔案名稱
   ![6](https://i.imgur.com/f4dVD7M.png)

   ``` java
   java -jar demo-0.0.1-SNAPSHOT.jar
   ```

6. 執行完，在瀏覽器打以下路徑，結果應如下
   <http://localhost:8080/DemoController/demoGet/{id}>
   ![7](https://i.imgur.com/tQq8PhK.png)

:::info
延伸思考
在 IDE 上執行 Spring Boot App 跟打包出來後用 command 執行的差別在哪？
:::

### maven 常用指令

| 指令 | 執行動作 | 說明 |
| ---------   | --------- | -------- |
| mvn clean   | 清理 | 清理已經編譯好的檔案 |
| mvn compile | 編譯 | 將 Java 程式碼編譯成 Class 檔案 |
| mvn test    | 測試 | 專案測試 |
| mvn package | 打包 | 將專案打包成 jar 包或者 war 包 |
| mvn install | 安裝 | 手動向本地倉庫安裝一個 jar |
| mvn deploy  | 上傳 | 將 jar 上傳到私服 |

## 參考資料

* [菜鳥工程師 肉豬：spring-boot](https://matthung0807.blogspot.com/search/label/spring-boot)
* [Java Spring Boot MVC 系列](https://maiccaejfeng.medium.com/java-spring-boot-mvc-%E7%B3%BB%E5%88%97-1-%E5%85%A5%E9%96%80%E6%95%99%E5%AD%B8-8fe7793c6d78)
* [30 天帶你潮玩 Spring Boot Zone](https://ithelp.ithome.com.tw/articles/10213097)
* [快速建立](https://spring.io/quickstart)
* [Hello World](https://spring.io/guides/gs/spring-boot/)
* [Restful Web Service](https://spring.io/guides/gs/rest-service/)
* [Spring-Boot-專案介紹](https://hackmd.io/mwTJYAiMQr2AQ8IR1qKNqA?view#%F0%9F%9A%A9-Spring-Boot-%E5%B0%88%E6%A1%88%E4%BB%8B%E7%B4%B9)

## 撰寫紀錄

| 人員 | 日期 | 修改紀錄 |
| - | - | - |
| 博元 | 202  1 | 初版 |
| 鈞昊 | 2022/7 | 新增註解說明 |
| 乃維 | 2022/7 | 新增註解說明 |
| 偉翔 | 2022/7 | 新增 maven 常用指令 |
