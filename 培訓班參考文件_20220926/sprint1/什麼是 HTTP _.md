---
tags: basic, HTTP
---

# 什麼是 HTTP ?

## HTTP 簡介

- HyperText Transfer Protocol，縮寫：HTTP（超文本傳輸協定）
- 設計 HTTP 最初的目的是為了提供一種發布和接收 HTML 頁面的方法，
  透過 HTTP 或者 HTTPS 協定請求的資源由統一資源識別碼（Uniform Resource Identifiers，URI）來標識。
- 傳輸：表示 HTTP 本身是拿來傳輸資料用的，而傳輸的行為又可以分為發送與接收。
- 協定：在傳輸的發送與接收行為，會需要兩邊協調好要用什麼模式、方法來執行，這個「協調好的模式」就是協定。

## HTTP 的運作方式

- HTTP 會有發送與接收的角色
- 每一次發動都是獨立的事件，每次 HTTP 的動作不會有狀態保存
- HTTP 發動的觸發順序通常是 client → server（Request），再由 server → client（Response）
- 會有被竄改、偷取封包的風險
- 預設使用 80 port（HTTP）、443 port（HTTPS）
- 每次發動 HTTP，都要先確認以下幾件事：
  - protocol : HTTP or HTTPS（http://  or https:// ）
  - IP :
    - port : 127.0.0.1:8080
    - Domain Name : www.google.com
  - 目標位置（資源位置）: URI
    - HTTP 所需 HEAD 內容（Content-Type...）
    - HTTP Method
    - 參數傳遞格式（JSON、XML、電文...）

## HTTP Method

- GET
  - 通常用於讀取資料，但 GET 的參數可能會在網址上顯示，可能會有風險
  <http://127.0.0.1:8080/user&userId>=**AAA**&pwd=**BBB**
  - 帶參數在網址上，這種方法通常稱為 query string
- POST
  - 通常用於提交資料 (新增、更新)，資料通常放在 Request Body 中（請求本文），不會被使用者直接看到
  - 常見會將 Content-Type 定義為 **application/x-www-form-urlencoded**
  - Request Body 參數格式為 **userId=AAA&pwd=BBB**
  可以自己決定 Content-Type 與 Request Body 中存放資料的方式。
- PUT
  通常用於新增資料
- DELETE
  通常用於刪除資料
- OPTIONS
  通常用於跨網域請求時，要求支援的前置封包
- HEAD
  - HEAD 跟 GET 方法類似，差別在 HEAD 並不會回傳你所請求的資源在 Body 上，只回傳 HTTP Header
- TRACE
  - 回顯服務器收到的請求，主要用於測試或診斷
- CONNECT
  - 將連線請求轉換至 TCP/IP 隧道
- PATCH
  - 更換資源部分內容
  - 在現有的資料欄位中，增加或部分更新一筆新的資料
- 常用的幾個動作分別為：GET / POST / PUT / DELETE 對應到資料庫基本操作 CRUD。

## HTTP Header & HTTP Body（Message Body）

- HTTP Headers
  是 HTTP Request 和相應的核心，它承載了關于客戶端瀏覽器，請求頁面，服務器等相關的信息。
  Headers 根據實際用途被分為以下 4 種類型
  - 通用頭欄位 (General Header Fields)
  - **請求頭欄位 (Request Header Fields)**
  - **回應頭欄位 (Response Header Fields)**
  - 實體頭欄位 (Entity Header Fields)

  開啟 [網頁開發工具](https://support.google.com/campaignmanager/answer/2828688?hl=zh-Hant) 的 Network，點擊任何一個 Request 請求，即可發現每個 HTTP Headers 都包含以下部分 :

  - **Genaral**（不屬於 Headers，只用於收集 Request URL 和 Response 的 status 等信息）
    ![1](https://i.imgur.com/mukOzhS.png)
  - **Request Headers**
    ![2](https://i.imgur.com/UsJhrcW.png)
  - **Response Headers**
    ![3](https://i.imgur.com/1ttfjgg.png)
  - Request Payload (URL 中的查詢參數)
    ![4](https://i.imgur.com/hV1IXXC.png)
  - Query String (代表的是内容類型未知的請求數據)
    ![5](https://i.imgur.com/usL0zXG.png)
  - Form Data (代表的是内容類型固定的請求數據)
    ![6](https://i.imgur.com/LqkrEG2.png)

- HTTP Body（Message Body）
  - **in the case of HTTP/0.9 no headers are transmitted**
  ![4](https://i.imgur.com/OEeNxID.png)

## Status Code

會透過狀態碼，判斷每次 HTTP 行為的狀況，狀態碼由 3 碼組成，第一碼表示分類，再由後面兩碼區別情境，解釋如下：

- 1XX：訊息，Request 已被伺服器接收
- 2XX：成功，Request 已被伺服器接收，並接續處理
- 3XX：重新導向，如有後續操作才會發生（常發生在頁面跳轉行為）
- 4XX：請求錯誤，Request 內容有錯或無法被執行（沒有該資源）
- 5XX：伺服器錯誤，伺服器處理正確請求時發生錯誤（權限錯誤或發生 exception）
- EX：網頁開發工具的 Network，可以看出網頁中所有請求的狀態碼
  ![7](https://i.imgur.com/tIJAQ4e.png)

## 觀察 HTTP

開啟 [網頁開發工具](https://support.google.com/campaignmanager/answer/2828688?hl=zh-Hant) 的 Network 查看，以下舉兩個範例

- GET
  ![5](https://i.imgur.com/dNKV9fC.png)
  - HTTP Method 是 GET
  - 請求的資源是： /wiki/%E8%B6%85%E6%96%87%E6%9C%AC%E4%BC%A0%E8%BE%93%E5%8D%8F%E8%AE%AE
  - 有使用 cookie
  - 可接收 Reuqest 的 content-type 有：text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9

- POST
   ![6](https://i.imgur.com/lvVaNXy.jpg)
  - HTTP Method 是 POST
  - 請求的資源是：/log?format=json&hasfast=true&authuser=0
  - 有使用 cookie
  - 可接收 Reuqest 的 content-type 有：application/x-www-form-urlencoded;charset=UTF-8

## 撰寫紀錄

| 人員 | 日期 | 修改紀錄 |
| - | - | - |
| 博元 | 2021   | 初版 |
| 鎮瑋 | 2022/7 | 新增 HTTP Header & HTTP Body（Message Body） |
