---
tags: basic, JSON, XML, 電文
---

# JSON、XML、電文觀念

## 前言

在資料交換時，應該先定義清楚要用何種管道（ex：API、檔案傳輸）來進行溝通，同時也要詳細定義每次溝通的內容為何種格式（ex：JSON、XML），以避免對接服務時產生的各種問題。

目前常看到的資料格式有三種，分別為 JSON、XML、字串（電文）。

## 課程目標

理解 JSON、XML、電文的特性、相異處，並能夠閱讀三種不同格式的資料。

## 課程開始

### JSON

JSON（**J**ava**S**cript **O**bject **N**otaion）相較於其他種類的資料格式，具有易閱讀、易處理、檔案較小以及程式支援性較高的優勢。

* JSON 的特性：輕量化、有型別、常搭配 RESTful 風格
* JSON 的型別：
  * 數字
  * 字串
  * 布林值
  * 陣列
  * 物件
  * null
* 資料以 `Key: Value` 方式表示
* 資料以逗號區隔
* 範例一 :

```json=
{
  "number": 1,
  "string": "字串",
  "boolean": true,
  "array": [
    1,
    2,
    3,
    4
  ],
  "object": {
    "key1": "value1",
    "key2": "value2",
    "key3": "value3"
  }
}
```

* 範例二 :

```json=
{
  "uniqueId": 1,
  "userId": "A123456789",
  "isRich": false,
  "wishList": [
    "Tesla",
    "houses",
    "happiness"
  ],
  "userInfo": {
    "gender": "Male",
    "age": 30,
    "remark": "我還年輕"
  }
}
```

### XML

XML（E**x**tensible **M**arkup **L**anguage）

* XML 的特性：有階層、可定義格式、檔案較龐大
* 類似於 HTML，會用標籤式語言描述資料 `<element attr="attr">value</element>`
* 可定義 element、attribute 決定資料狀態
* 標籤必須為成對 (pair) 的，且大小寫有區別

  ```xml=
  valid  : <data>this is data</data>
  invalid : <Data>this is data</data>
  ```

* empty element tag 不需成對的標籤

  ```xml
  <data>this is data</data>
  <justALineBreak /> // empty element tag
  <car>Tesla</car>
  ```

* 可事先定義 [XSD 規範](https://zh.wikipedia.org/zh-tw/XML_Schema)，用以驗證此份 XML 資料的正確性，透過規範來驗證資料內容，可有效防止資料損壞。當此 XML 中的資料符合規範時，代表此份資料是有效的。
  * 貓的年齡（age）必須為正整數
  * ALTERED 與 DECLAWED 必須為布林值 （boolean）
  
![xsd example](https://i.imgur.com/CUpcq3J.png)

* 範例一 :

```xml=
<root>
  <number>1</number>
  <string>字串</string>
  <boolean>true</boolean>
  <array>
    <a>1</a1>
    <a1>2</a1>
    <a1>3</a1>
    <a1>4</a1>
  </array>
  <object>
    <key1>value1</key1>
    <key2>value3</key2>
    <key3>value3</key3>
  </object>
</root>
```

* 範例二 :

```xml=
<root>
  <uniqueId>1</uniqueId>
  <userId>A123456789</userId>
  <isRich>false</isRich>
  <wishList>
    <car>Tesla</car>
    <car>houses</car>
    <car>happiness</car>
  </wishList>
  <userInfo>
    <gender>Male</gender>
    <age>30</age>
    <remark>我還年輕</remark>
  </object>
</root>
```

### 電文

* 通常用字串顯示所有資料
* 需要雙方協定好資料長度
* 若需新增欄位，通常會接在最後面
* 無法隨意擴充
* 範例一 :
  > ++1 字串 true 1234      value1    value2    value3&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;++
* 範例二 郵遞區號：
  11014A123456789
* 範例三 匯款電文：
  ![dianweng example](https://i.imgur.com/i2zbVVj.png)
  
## 參考資料

* [初探 JSON](https://www.syscom.com.tw/Print_Preview.aspx?id=52&group=3)
* [JSON vs XML](https://avato.co/developers/pros-cons-json-vs-xml/)
* [電文匯款範例](https://blog.xuite.net/souhwa/twblog/151776738)
* [Microsoft XML 入門](https://support.microsoft.com/zh-hk/office/xml-%E5%85%A5%E9%96%80-a87d234d-4c2e-4409-9cbc-45e4eb857d44)

## 撰寫紀錄

| 人員 | 日期 | 修改紀錄 |
| - | - | - |
| 博元 | 2021   | 初版 |
| 乃維 | 2022/7 | 增加程式碼 |
| 玟君 | 2022/7 | 增加程式碼 |
| 學義 | 2022/7 | 增加各內容之敘述 |
