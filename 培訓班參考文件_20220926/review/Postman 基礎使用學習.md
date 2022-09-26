---
tags: basic, Postman
---

# Postman 基礎使用學習

## 前言

本篇目標主要介紹基礎使用 Postman，讓讀者快速學會操作。

## 目錄

* [Postman 基礎使用學習](#Postman-基礎使用學習)
  * [前言](#前言)
  * [目錄](#目錄)
  * [介紹/基本概念](#介紹基本概念)
  * [應用情境說明](#應用情境說明)
  * [建置環境](#建置環境)
    * [下載桌面程式](#下載桌面程式)
    * [新增 Postman 帳號](#新增-Postman-帳號)
    * [開啟 Postman 桌面程式](#開啟-Postman-桌面程式)
    * [介面介紹](#介面介紹)
  * [實作過程](#實作過程)
    * [實作範例（get、post、put）](#實作範例（get、post、put）)
    * [save/import/export collection](#save/import/export-collection)
  * [參考資料](#參考資料)
  * [撰寫紀錄](#撰寫紀錄)

## 介紹/基本概念

Postman 是一個可以模擬 HTTP Request 的工具，其中包含常見的 HTTP 的請求方式（GET、POST、PUT、DELETE）。
主要功能就是能夠快速的測試 API 是否能夠正常的請求資料，並得到正確的請求結果。
Postman 早期為單純的 API 測試，接著陸續加入了腳本、整合多個測試、個人同步、團隊協作甚至是開發 API 與 Mock server(模擬伺服器)。

## 應用情境說明

在開發時會先使用 Postman 測試 API 是否正常，以免實際接上 API 後才發現問題。
Postman 也支援團隊協作，使開發過程更加便利。

## 建置環境

### 下載桌面程式

* 依照自己的系統電腦作業系統選擇（windows 範例）
![1](https://i.imgur.com/O2eAaqk.png)
![2](https://i.imgur.com/C201vw9.png)

### 新增 Postman 帳號

* 依上圖右上方註冊一個 Postman 的帳號，點選後畫面如下，依照步驟申請一個帳號
  ![1](https://i.imgur.com/JwCshXZ.png)

* 成功後網頁會跳轉到網頁版的 Postman 代表成功新建帳號
  ![2](https://i.imgur.com/gCJSr4N.png)

### 開啟 Postman 桌面程式

* 開啟後，會跳出登入的畫面
  ![1](https://i.imgur.com/Qk5dK5Q.png)

* 成功登入後，會有訊息訊問確定開啟 Postman，按開啟選項
  ![2](https://i.imgur.com/wRoLvQU.png)

* 開啟後看到桌面程式跟網頁畫面雷同代表 Postman 環境完成
  ![3](https://i.imgur.com/REZA1hE.png)

### 介面介紹

首先我們點選 workspaces 選擇 My Workspace，進到使用介面
![1](https://i.imgur.com/ep8Yy8k.png)

使用介面如同畫面
![2](https://i.imgur.com/X4Oy000.png)

#### 左側功能區

![1](https://i.imgur.com/B6eI1wE.png)

#### 右側功能區

![1](https://i.imgur.com/FlOSEhk.png)

新增請求頁面
  ![1](https://i.imgur.com/DYPCEHi.png)

* 網址區塊
  ![2](https://i.imgur.com/dnKhVzK.png)

* 參數區塊
  ![3](https://i.imgur.com/0g31Iio.png)

* 回應區塊
  ![4](https://i.imgur.com/Mrsowyt.png)

## 實作過程

### 實作範例（get、post、put）

以下使用 [JSONPlaceholder](https://jsonplaceholder.typicode.com/)
當範例說明，這網站是提供用於測試的免費 API。
| Action  | Verb   | URL    |  body|
| ------  | ------ | ------ | ------ |
| 瀏覽全部郵件 | GET | `http://jsonplaceholder.typicode.com/posts`| No |
| 瀏覽一筆郵件 | GET | `http://jsonplaceholder.typicode.com/posts/:id`| No |
| 新增一筆郵件 | POST | `http://jsonplaceholder.typicode.com/posts`| { "userId":1,"title":"hello","body":"hello world"} |
| 修改一筆郵件 | PUT | `http://jsonplaceholder.typicode.com/posts/:id`| { "userId":1,"title":"hello","body":"hello world"} |

* 瀏覽全部郵件
  ![1](https://i.imgur.com/OJii4Ps.png)

* 瀏覽一筆郵件
  ![2](https://i.imgur.com/0gqpq2W.png)

* 新增一筆郵件
  ![3](https://i.imgur.com/3obVYeH.png)

* 修改一筆郵件
  ![4](https://i.imgur.com/6kYhoWC.png)

### save/import/export collection

#### save（儲存 API）

![1](https://i.imgur.com/Mt8KS6N.png)

* 因為沒有 collection 沒法儲存
  ![2](https://i.imgur.com/Xgh6bNn.png)

* 先新建一個 collection
  ![3](https://i.imgur.com/lKwrde7.png)

* 成功後可以看到 collections 多出了剛新建的
  ![4](https://i.imgur.com/9lU2gFM.png)

* 選擇剛新建的 collection，就可以儲存
  ![5](https://i.imgur.com/uECYFJc.png)
  ![6](https://i.imgur.com/3OdRoos.png)

* 成功後，就記憶了這筆 API
  ![7](https://i.imgur.com/jWV4aS6.png)

#### export collection

* 如果要輸出一個 collection 提供給其他人 右鍵選擇 export，即可輸出這筆 collection
  ![1](https://i.imgur.com/MiswJMC.png)

  ![2](https://i.imgur.com/RFPLzyN.png)

#### import collection

* 點選介面上的 import
  ![1](https://i.imgur.com/AU3ep6J.png)

* 選擇需要 import 的檔案
  ![2](https://i.imgur.com/8PIqf6K.png)

  ![3](https://i.imgur.com/jq6FDYC.png)

* 成功後就會在 collections 中看到 import 的資料
  ![4](https://i.imgur.com/Ex11Eyn.png)

#### 其他小知識

* 環境設定變數使用
  * 點選右邊的眼睛 ICON
    ![1](https://i.imgur.com/w2xJ5N3.png)
  * 選擇 Globals => Add
    ![2](https://i.imgur.com/b79hzse.png)
  * 輸入變數名（VARIABLE） =>貼上需要取代的變數參數（INITIALVALUE、CURRENT VALUE）=> Save
    ![3](https://i.imgur.com/Yp73v0I.png)
  * 儲存成功後，在 URL 就可以使用變數
    EX:`http://{{變數名}}/posts`
    ![4](https://i.imgur.com/coc0rWS.png)

## 參考資料

* [Postman, API 測試工具教學](https://blog.myctw.cc/post/7af5.html)
* [Postman - 測試 API 神器 1/2](https://ithelp.ithome.com.tw/articles/10201503)
* [Postman 新手教學｜使用 Postman 開發出你的第一支 API](https://tw.alphacamp.co/blog/postman-api-tutorial-for-beginners)
* [跟著我一起快速入門 Postman 吧！](https://israynotarray.com/other/20211207/427026/)
* [POSTMAN 从入门到精通系列（六）：postman 账户](https://blog.csdn.net/u012014531/article/details/100738341)
* [如何在 POSTMAN 中針對不同環境設定參數進行測試](https://blog.yowko.com/postman-parameter-test/)

## 撰寫紀錄

| 人員 | 日期 | 修改紀錄 |
| - | - | - |
| 鎮瑋 | 2022/7 | 初版 |
