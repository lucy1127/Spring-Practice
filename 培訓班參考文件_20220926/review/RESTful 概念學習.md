---
tags: basic, RESTful
---

# RESTful 概念學習

## 前言

與後端技術同事或開發部門的溝通上，常常聽到 API 等專有名詞，到底什麼是 API？用來做什麼？這篇文章將為整理相關的知識，讓讀者可以快速的抓住重點。

## 目錄

* [RESTful 概念學習](#RESTful-概念學習)
  * [前言](#前言)
  * [目錄](#目錄)
  * [介紹/基本概念](#介紹基本概念)
    * [什麼是 HTTP](#什麼是-HTTP)
    * [什麼是 API](#什麼是-API)
    * [什麼是 RESTful](#什麼是-RESTful)
    * [REST 架構風格](#REST-架構風格)
    * [RESTful URL 應用](#RESTful-URL-應用)
    * [@PathVariable 與 @RequestParam](#PathVariable-與-RequestParam)
  * [參考資料](#參考資料)

## 介紹/基本概念

### 什麼是 HTTP

* [什麼是 HTTP](https://hackmd.io/@kazzy/H1F8j8Roq)

### 什麼是 API

API，全名 Application Programming Interface（應用程式介面）是一種接口，讓第三方可以應用在自身的產品上的系統溝通介面，各種軟體組件之間一套明確定義的溝通方法。

簡單說 API 就是幕後功臣，擔當著郵差的角色，遊走於應用程式（app）、資料庫（database）、裝置（device）之間，互相傳送資料，最後創造連結，並成功為顧客提供服務。

當 A 程式需要另一個 B 程式幫他做某件事或拿某些資料的時候，這中間就會透過 API（我們稱 C）來幫忙溝通。這時 A 並不需要知道 B 做了什麼，怎麼做的。
A 只需要知道三件事：

1. 向 C 要求做這件事之前需要提供什麼資料？
2. 成功的話 C 會回覆什麼？
3. 失敗的話 C 會回覆什麼？

API 就像是櫃檯人員，擔任兩個程式之間的溝通橋樑。除了回覆正確資料外，當碰到例外狀況，API 也會針對輸入資料檢核，並在碰到問題時回應錯誤訊息。

### 什麼是 RESTful

REST 是 Representational State Transfer 的縮寫
可譯為「具象狀態傳輸」，由 Roy Fielding 博士在 2000 年的博士論文中所提出，他同時也是 HTTP 規範的主要作者之一。

符合 REST 風格的網站架構可以稱為 RESTful，把各個單字拆開來解釋的話即如下

* Representational：表現形式，如 JSON，XML 等等
* State Transfer：狀態變化。可利用 HTTP 動詞們來做呼叫

簡單的說，就是觀察發出的 HTTP Request 裡面所包含的資訊，就可以**直接預期**這個 Request 會收到怎樣類型的資料。

REST 是一種**軟體架構風格**（並非標準），目的是幫助在世界各地不同軟體、程式在網際網路中能夠互相傳遞訊息。
每一個網頁都可視為一個資源（resource）提供使用者使用，而你可以透過 URL（Uniform Resource Locator），也就是這些資源的地址，來取得這些資源並在你的瀏覽器上使用。

### REST 架構風格

* 客戶端 - 伺服器（Client-Server）
  * 使用者介面所關注的邏輯和資料儲存所關注的邏輯分離開來
  * 簡化伺服器模組
* 無狀態（Stateless）
  * 伺服器獨立於所有之前的請求，完成每個用戶端請求的通訊方法。
  * 用戶端可以按任何順序請求資源，並且每個請求都為無狀態，或與其他請求隔離。
* 快取（Cacheability）
  * 客戶端和中間的通訊傳遞者可以將回覆快取起來
  * 管理良好的快取機制可以減少客戶端 - 伺服器之間的互動
* 統一介面（Uniform Interface）
    簡化了系統架構，減少了耦合性，可以讓所有模組各自獨立的進行改進，下列有四個限制
  * 請求中包含資源的 ID（Resource identification in requests）
    * 使用統一的資源識別符來執行此操作
  * 資源通過標識來操作（Resource manipulation through representations）
    * 通過客戶端可以修改原資源的狀態
  * 訊息的自我描述性（Self-descriptive messages）
    * 每一個訊息都包含足夠的資訊來描述如何來處理這個資訊
  * 用超媒體驅動應用狀態（Hypermedia as the engine of application state (HATEOAS)）
    * 處理以超媒體為基礎的狀態變化
* 分層系統（Layered System）
  * 客戶端一般不知道是否直接連接到了最終的伺服器，或者是路徑上的中間伺服器
* 按需代碼（Code-On-Demand，可選）
  * 伺服器可以通過傳送可執行代碼給客戶端的方式臨時性的擴充功能或者客製化功能

### RESTful URL 應用

RESTful 風格的網址設計強調從路由結構就能看出要對什麼資料、進行什麼操作，那麼 RESTful 風格的 CRUD 路由就會這樣寫。

假設有個 to-do LIST 使用 RESTful 風格

![2](https://i.imgur.com/pGkpa9T.png)

| Action           | Verb | URL |
| ---------------- | ------ | ------------ |
| 瀏覽全部待辦事項 | GET | <https://www.example.com/api/todos> |
| 瀏覽一筆待辦事項 | GET | <https://www.example.com/api/todos/:ID> |
| 新增一筆待辦事項 | POST | <https://www.example.com/api/todos> |
| 修改一筆待辦事項 | PUT | <https://www.example.com/api/todos/:ID> |
| 刪除一筆待辦事項 | DELETE | <https://www.example.com/api/todos/:ID> |

可以看到固定結構是（`:ID` 為此資料的 ID，意指的 Primary Key）

* 瀏覽全部資料：GET + 資源名稱
* 瀏覽特定資料：GET + 資源名稱 + :ID
* 新增一筆資料：POST + 資源名稱
  * 需要回傳新增的資料
* 修改特定資料：PUT + 資源名稱 + :ID
  * 需要回傳修改的資料
* 刪除特定資料：DELETE + 資源名稱 + :ID

下圖對照了「完全採用 RESTful 風格」和實際的路由規劃做對照，用 `*` 標記出了不同的地方。
![1](https://i.imgur.com/QKcVUcx.png)

以下網址為開源的，可以嘗試測試看看（可使用 Postman 測試）
「[JSONPlaceholder](https://jsonplaceholder.typicode.com/)」提供用於測試的免費 API

| Action  | Verb   | URL    |  body|
| ------  | ------ | ------ | ------ |
| 瀏覽全部郵件 | GET | <http://jsonplaceholder.typicode.com/posts>|No|
| 瀏覽一筆郵件 | GET | <http://jsonplaceholder.typicode.com/posts/:id>|No|
| 新增一筆郵件 | POST | <http://jsonplaceholder.typicode.com/posts>|{ "userId":1,"title":"hello","body":"hello world"}|
| 修改一筆郵件 | PUT | <http://jsonplaceholder.typicode.com/posts/:id>|{ "userId":1,"title":"hello","body":"hello world"}|

### 額外狀況應用

上述的 RESTful，可能會遇到額外狀況，例如：登入、註冊、登出或相關功能等等
這類的 API 路徑，會出現不符合 RESTful 的規則！
不用害怕！你不會遭到 RESTful 之神的懲罰，只要遵循開發團隊的規畫即可，不用強制符合

下方舉些例子，RESTful URL 或 URL 的路徑皆可，沒有說哪個一定是正確的

| Action  | Verb   | RESTful URL | URL |
| ------  | ------ | ------ | ------ |
| 登入 | POST | {resource}/login | /login |
| 註冊 | POST | {resource}/signup | /signup |
| 登出 | POST | {resource}/logout | /logout |
| 忘記密碼 | POST | {resource}/recovery | /recovery |
| 修改密碼 | PUT | {resource}/password | /password |

### @PathVariable 與 @RequestParam

RESTful API 接參數的方式也與上方介紹的 API 命名有關，而 RESTful 接 url 參數的方式有兩種，就是 **@PathVariable** 與 **@RequestParam**

這兩個都是去接 API 的參數，但是有甚麼差呢？

這裡就要來解釋 @PathVariable 與 @RequestParam 的差別！

:::info

＠PathVariable 在 Spring MVC 官方文件是稱作 URI Template Pattern 的 Annotation
@RequestParam 則是 HTTP Request 所帶的參數

:::

看不懂？沒關係！我們用實際的例子來講解！

#### @RequestParam

```java=
@GetMapping("/user")
public User get(@RequestParam String id) {
    System.out.println("@RequestParam String id 值為： " + id);
    return demoService.getUser(id);
}
```

假設呼叫的 url 為：/user?id=xxx
此時被賦予 @RequestParam 的參數，就會**自動按照變數名稱代入**(id = xxx)

> 自動按照變數名稱代入：你 Controller 所下的參數會跟 url 問號後面的參數名稱對應

舉例：

當 url 為 `/user?id=123`
此時接到的 @RequestParam String id ，該 id 的值會是 123

![1](https://i.imgur.com/9Sm869I.png)

![1](https://i.imgur.com/BqkwASi.png)

當 url 為 `/user?id=`

![1](https://i.imgur.com/jEXANsP.png)

![1](https://i.imgur.com/9hGNuqu.png)

**@RequestParam 就是 Get 最經典用法，問號後面代參數來取得條件！**

:::spoiler **叛逆式呼叫 API**

我就是叛逆，不想帶 id 給它怎麼樣！😈

可以試試 `/user?name=123`

![1](https://i.imgur.com/n36vu0b.png)

哈哈400了吧 笑你

別哭了，我們來看看 java 報了甚麼錯

```
WARN 11472 --- [nio-8080-exec-1] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id' for method parameter type String is not present]
```

<font color=red>**注意：Required request parameter 'id' for method parameter type String is not present**</font>

這個 400 並不是因為程式碼或資料面的錯誤，而是因為 **API 參數是必填**，你沒有帶給它 id 而是 name，它找不到對應參數，所以報 400

> Controller 本人：🤬

你還想試試看僅呼叫`/user`？

恭喜你，情況跟上述一樣！還是乖乖帶指定參數吧！

:::

#### @PathVariable

```java=
@DeleteMapping("/user/{id}")
public String delete(@PathVariable String id) {
    System.out.println("@PathVariable String id 值為： " + id);
    demoService.deleteUser(id);
    return "OK";
}
```

我們呼叫的 url 為：/user/xxx
此時被賦予 @PathVariable 的參數，就會**自動按照 url pattern 取得**(id = xxx)

自動按照 url pattern 代入：Controller 所需參數會跟 url 位置對應

舉例：

當我們 url 為： /user/123
此時接到的 @RequestParam String id ，該 id 的值會是 123

![1](https://i.imgur.com/uA0c0m1.png)

![1](https://i.imgur.com/PiFEVvE.png)

當我們 url 為： /user

![1](https://i.imgur.com/nkm01i2.png)

一樣，讓我們關心一下 java

```
WARN 26780 --- [nio-8080-exec-9] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'DELETE' not supported]
```

<font color=red>**注意：Request method 'DELETE' not supported**</font>

它不支援你這個呼叫法，需要補上指定的參數

**@PathVariable 就是 RESTful 經典用法，不需要加問號，直接帶入參數取得條件！**

#### 總結

:::info

* ＠PathVariable
  * 在 Spring MVC 官方文件是稱作 URI Template Pattern 的 Annotation
  * 會解析 url 的 pattern，取得該 API 所需的參數
* @RequestParam
  * HTTP Request 所帶的參數
  * 抓問號後面的參數，並對應到 Controller 指定的參數名稱

:::

## 參考資料

* [API 是什麼？學會串接 Web API](https://tw.alphacamp.co/blog/api-introduction-understand-web-api-http-json?gclid=CjwKCAjw2rmWBhB4EiwAiJ0mtde94cgudGCc28eEUjMtoLPYOJ51foPGCqdDoSteRxKXZ7wBUFjlFBoCZSMQAvD_BwE)
* [什麼是 API？](https://www.youtube.com/watch?v=zvKadd9Cflc)
* [什麼是 REST? 認識 RESTful API 路由語義化設計風格](https://tw.alphacamp.co/blog/rest-restful-api)
* [什麼是 RESTful API？](https://aws.amazon.com/tw/what-is/restful-api/)
* [RESTful API 與 MVC 名詞介紹](https://ithelp.ithome.com.tw/articles/10191925)
* [一文看清 API、CRUD 及 Restful](https://mtache.com/rest-api)
* [什麼是 API？CRUD？Restful API 又是什麼？
(搭配) 一文看清 API、CRUD 及 Restful)](https://www.youtube.com/watch?v=PlaKAMShvHc)
* [API 是什麼？RESTful API 又是什麼？](https://medium.com/itsems-frontend/api-%E6%98%AF%E4%BB%80%E9%BA%BC-restful-api-%E5%8F%88%E6%98%AF%E4%BB%80%E9%BA%BC-a001a85ab638)
* [筆記 REST 到底是什麼](https://medium.com/@jinghua.shih/%E7%AD%86%E8%A8%98-rest-%E5%88%B0%E5%BA%95%E6%98%AF%E4%BB%80%E9%BA%BC-170ad2b45836)
* [簡單理解 REST 設計風格與 RESTful API](https://hackmd.io/@monkenWu/Sk9Q5VoV4/https%3A%2F%2Fhackmd.io%2F%40gen6UjQISdy0QDN62cYPYQ%2FHJh9zOE7V?type=book)
* [前端小字典三十天【每日一字】– REST](https://ithelp.ithome.com.tw/articles/10161048)
* [淺談 REST 軟體架構風格 (Part.I) - 從了解 REST 到設計 RESTful！](https://blog.toright.com/posts/725/representational-state-transfer-%E8%BB%9F%E9%AB%94%E6%9E%B6%E6%A7%8B%E9%A2%A8%E6%A0%BC%E4%BB%8B%E7%B4%B9-part-i-%E5%BE%9E%E4%BA%86%E8%A7%A3-rest-%E5%88%B0%E8%A8%AD%E8%A8%88-restful%EF%BC%81.html)
* [不是工程師 休息 (REST) 式架構？寧靜式 (RESTful) 的 Web API 是現在的潮流？](https://progressbar.tw/posts/53)
* [HTTP 請求方法](https://developer.mozilla.org/zh-TW/docs/Web/HTTP/Methods)
* [什麽才是真正的 RESTful 架構？](https://www.itread01.com/articles/1477725342.html)
* [淺談 REST](https://ithelp.ithome.com.tw/articles/10235633?sc=rss.iron)
* [HTTP/1.1 — 訊息格式 (Message Format)](https://notfalse.net/39/http-message-format)
* [一文读懂 Http Headers 为何物 (超详细)](https://segmentfault.com/a/1190000018234763)
* [什么是 HTTP Headers？](https://kb.cnblogs.com/page/119118/)
* [HTTP Header & Status Code 心得](https://ithelp.ithome.com.tw/articles/10212102)
* [HTTP Messages](https://developer.mozilla.org/en-US/docs/Web/HTTP/Messages)
* [http 请求时 Form Data & Request Payload 的区别](https://segmentfault.com/a/1190000039666590)
* [Request Payload 是什么](https://blog.liuyunzhuge.com/2020/05/15/Request-Payload%E6%98%AF%E4%BB%80%E4%B9%88/)
* [工程師整天掛在嘴邊的 API 是什麼？](https://medium.com/@Tommmmm/%E5%B7%A5%E7%A8%8B%E5%B8%AB%E6%95%B4%E5%A4%A9%E6%8E%9B%E5%9C%A8%E5%98%B4%E9%82%8A%E7%9A%84api%E6%98%AF%E4%BB%80%E9%BA%BC-7ab8b522d3bc)
* [[Controller]使用@PathVariable以及@RequestParam存取網頁特定網址](https://ithelp.ithome.com.tw/articles/10159679)
* [Spring @RequestParam vs @PathVariable Annotations](https://www.baeldung.com/spring-requestparam-vs-pathvariable)

## 撰寫紀錄

| 人員 | 日期 | 修改紀錄 |
| - | - | - |
| 鎮瑋 | 2022/7 | 初版 |
| 玟君 | 2022/7 | 新增什麼是 API的內文 |
| 珮慈 | 2022/9 | 新增 @RequestParam & @PathVariable 比較說明 |
