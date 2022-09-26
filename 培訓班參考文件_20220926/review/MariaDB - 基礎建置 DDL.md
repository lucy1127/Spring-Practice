---
tags: basic, SQL
---

# MariaDB - 基礎建置 DDL

## 前言

這篇文章主要是在介紹如何安裝 MariaDB（+ HeidiSQL），並在 HeidiSQL 上建立資料庫與資料表，以及使用 SQL 語法對資料表進行基本的「增修刪查」等動作。

:::danger

* 操作環境｜Win10｜64 位元
* 安裝後，未有任何動作，所佔記憶體｜約 550MB

:::

## 目錄

* [MariaDB - 基礎建置 DDL](#MariaDB---基礎建置-DDL)
  * [前言](#前言)
  * [目錄](#目錄)
  * [介紹/基本概念](#介紹基本概念)
  * [應用情境說明](#應用情境說明)
  * [建置環境](#建置環境)
    * [Step 1. 官網下載](#Step-1-官網下載)
    * [Step 2. 執行安裝檔（mariadb-==*++10.8.3++*==-winx64.msi）](#Step-2-執行安裝檔（mariadb-10.8.3-winx64.msi）)
    * [Step 3. 勾選同意](#Step-3-勾選同意)
    * [Step 4. 設定安裝元件、儲存位置](#Step-4-設定安裝元件、儲存位置)
    * [Step 5. 設定 root 密碼](#Step-5-設定-root-密碼)
    * [Step 6. 設定 MariaDB 系統參數](#Step-6-設定-MariaDB-系統參數)
    * [Step 7. 準備安裝](#Step-7-準備安裝)
    * [Step 8. 安裝完成](#Step-8-安裝完成)
  * [實作過程](#實作過程)
    * [HeidiSQL（資料庫管理工具 GUI Tools）](#HeidiSQL（資料庫管理工具-GUI-Tools）)
      * [新增 Project + 登入 root](#新增-Project-+-登入-root)
      * [［Create］DataBase](#［Create］DataBase)
      * [［Create］Table](#［Create］Table)
    * [基礎 SQL 語法（HeidiSQL）](#基礎-SQL-語法（HeidiSQL）)
      * [新增資料](#新增資料)
      * [查詢資料](#查詢資料)
      * [修改資料](#修改資料)
      * [刪除資料](#刪除資料)
      * [清空資料](#清空資料)
      * [修改資料表](#修改資料表)
      * [刪除資料庫／資料表](#刪除資料庫資料表)
  * [參考資料](#參考資料)
  * [撰寫紀錄](#撰寫紀錄)

## 介紹/基本概念

> **DDL**
> 資料定義語言（Data Definition Language）
> 相關 SQL 語法：`CREATE`、`ALTER`、`DROP`
---

* MySQL 的分支
* 免費、開源
* [RDBMS（關聯式資料庫管理系統）]((https://www.oracle.com/tw/database/what-is-a-relational-database/))
:::info
* 缺點：不適合用於大型專案
* 各 Project，使用共同的 DB 與資料
* :pushpin: 安裝卸載，皆不須重開機
:::

## 應用情境說明

> **為什麼需要資料庫系統？**
> 　
> &emsp;&emsp;*以前手機尚未普及的時代，不像現在人人一支手機，就需要善用自己的記憶力，把每個人的號碼背起來，有事找時，就從腦海中翻出對方的電話號碼來聯繫，縱使記憶力超強，但長期來看卻不是個好做法，也很難把電話資料共享給其他人使用，就算記錄在紙本上，也不容易保存和快速查找。*
> 　
> &emsp;&emsp;*有邏輯的資料結構可以讓我們更有效率來管理與處理資料，但當我們關上應用程式之後，它們所處理的資料就會從電腦的記憶體中消失。*
> &emsp;&emsp;*此時，我們會需要一個工具，不僅能長期存放我們需要應用的資料，也方便隨時查詢與管理資料，而這個工具就是資料庫系統。*
>
> [name=Pierce Shih] [time=Jun 2, 2019]
> [原文](https://medium.com/pierceshih/%E7%AD%86%E8%A8%98-%E4%BD%95%E8%AC%82%E8%B3%87%E6%96%99%E5%BA%AB%E7%B3%BB%E7%B5%B1-53b59aacbfb7)

## 建置環境

### <span class="step">Step 1. [官網下載](https://mariadb.org/mariadb/all-releases/)</span>

    - ［當前最新穩定版本］MariaDB Server 10.8.3
    - Windows
    - x86_64（64 位元）
    - MSI Package

![download](https://i.imgur.com/Ywa8QHu.png)

### <span class="step">Step 2. 執行安裝檔</span>

**mariadb-==*++10.8.3++*==-winx64.msi**

![download2](https://i.imgur.com/pUrrKcJ.png)

### <span class="step">Step 3. 勾選同意</span>

#### GNU 開放原始碼授權條款

![download3](https://i.imgur.com/cf5Z40Y.png)

### <span class="step">Step 4. 設定安裝元件、儲存位置</span>

    - MariaDB Server
        - 佔記憶體：157MB
    - C:\Program Files\MariaDB 10.8\

![download4](https://i.imgur.com/8KxjOWF.png)

### <span class="step">Step 5. 設定 root 密碼</span>

    - Enable access from remote machines for 'root' user
        - 允許從遠端以 root 身分登入 DB
        📢 一般不需要
    - Use UTF8 as default server's character set
        - 系統預設的編碼
    - Data Directory: ［C:\Program Files\MariaDB 10.8\data\］
        - 資料保存位置

![download5](https://i.imgur.com/7GsqATo.png)

### <span class="step">Step 6. 設定 MariaDB 系統參數</span>

    📢 通常不用更改，使用預設值。
    - 此圖僅為範例，看看即可。

![download6](https://i.imgur.com/yIeuz3J.png)

:::danger
🔔 請確認 port: 3306 是否有被其他應用程式使用
Step 1. 開啟［cmd］
Step 2. netstat -ano | findstr 0.0:3306

![download7](https://i.imgur.com/ALDh6uj.png)
:::

### <span class="step">Step7. 準備安裝</span>

![download8](https://i.imgur.com/f0JrtIf.png)

### <span class="step">Step8. 安裝完成</span>

![download9](https://i.imgur.com/DZ30Ifo.png)

    - HeidiSQL     資料庫管理工具
    - Error log    查看 MariaDB Server 的錯誤訊息
    - my.ini       MariaDB 設定檔

![download10](https://i.imgur.com/0zQjUYN.png)

## 實作過程

### HeidiSQL（資料庫管理工具 GUI Tools）

#### <span class="label2">新增 Project ＋ 登入 root</span>

    - 儲存：保存設定

![HeidiSQL１](https://i.imgur.com/WyI08fJ.png)

:::warning

* <kbd>開啟</kbd>
![HeidiSQL２](https://i.imgur.com/FsmJJ65.png)
:::

:::danger

* 對 DEMO.testdb.staff 進行増修刪 = DEMO2.testdb.staff 會跟著同步資料
![HeidiSQL３](https://i.imgur.com/MDsHHT5.png)

:::

:::info

* 若其中一個 Project 只要顯示某些 DB（＂;＂區隔）
![HeidiSQL４](https://i.imgur.com/CTCx6L7.png)　</br>
![HeidiSQL５](https://i.imgur.com/QoOUb1i.png)
:::

#### <span class="label2">［Create］DataBase</span>

![Create 1](https://i.imgur.com/UVuOycv.png)

:::warning

* 名稱    設定 DB 名稱
* 排序規則  <預設>utf8mb4_general_ci

![Create 2](https://i.imgur.com/V0sSQWw.png)

:::

#### <span class="label2">［Create］Table</span>

![Create 3](https://i.imgur.com/DO1RRRf.png)

---

:::warning

* <span class="label">基本</span>
  * 設置欄位

  >* 資料類型<常用>
      - INT
      - VARCHAR
      - CHAR
      - TEXT
      - DATE          'YYYY-MM-DD'
      - TIMESTAMP     'YYYY-MM-DD HH:mm:ss'
  >* 預設
      - AUTO_INCREMENT
        - 自動遞增
        - 初始值為［1］
      - CURRENT_TIMESTAMP()
        - 自動代入當天日期
  >

![type](https://i.imgur.com/VBlkNQ6.png)
:::

* <span class="label">索引</span>
  * 設置 PK（Primary Key）
![pk](https://i.imgur.com/7CtYygD.png)

:::warning

* <span class="label">CREATE 代碼</span>
  *- 查看建立的 SQL 語法
  * 此 code 為範例參考
  
  ```sql=
  CREATE TABLE `staff` (
    `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '員編',
    `name` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '姓名' COLLATE 'utf8mb4_general_ci',
    `age` INT(10) UNSIGNED NOT NULL COMMENT '年齡',
    `email` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
    `phone` VARCHAR(12) NULL DEFAULT NULL COMMENT '手機號碼' COLLATE 'utf8mb4_general_ci',
    `date` TIMESTAMP NOT NULL DEFAULT current_timestamp() COMMENT '到職日',
    PRIMARY KEY (`id`) USING BTREE
  )
  COMMENT='員工紀錄'
  COLLATE='utf8mb4_general_ci'
  ENGINE=InnoDB
  AUTO_INCREMENT=2
  ;
  ```

:::

---

### 基礎 SQL 語法（HeidiSQL）

#### <span class="label">新增資料</span>

:::info
`'id'`&`'date'`欄位，會自動產生 value，故**無須設定**
:warning: 須寫出其餘對應的欄位名稱與值
:::

```sql=
INSERT INTO staff (name, age, email, phone)
VALUES ('Jack', 25, 'aaa@gmail.com', '0912-123-456');
```

#### <span class="label">查詢資料</span>

```sql=
SELECT * FROM staff;
```

:::warning

* 指定條件

```sql=
SELECT *
FROM staff
WHERE id = 1;
```

:::

#### <span class="label">修改資料</span>

```sql=
UPDATE staff
SET NAME = 'May', age = 41
WHERE id = 1;
```

#### <span class="label">刪除資料</span>

```sql=
/* 資料全刪除； 'id'指標不會重設初始化；不推薦 */ 
DELETE FROM staff;
```

:::warning

* 指定條件

```sql=
DELETE FROM staff
WHERE id = 1;
```

:::

#### <span class="label">清空資料</span>

```sql=
/* 資料全刪除； 'id'指標會重設初始化 */
TRUNCATE TABLE staff;
```

#### <span class="label">修改資料表</span>

```sql=
/* 增加欄位<ADD> */
ALTER TABLE staff ADD note VARCHAR(10);

/* 修改欄位型別<MODIFY COLUMN> */
ALTER TABLE staff MODIFY COLUMN note TEXT;

/* 刪除欄位<DROP COLUMN> */
ALTER TABLE staff DROP COLUMN note;
```

#### <span class="label">刪除資料庫／資料表</span>

```sql=
/* 刪除資料表 */
DROP TABLE staff;

/* 刪除資料庫 */
DROP DATABASE testdb;
```

## 參考資料

* [Windows 安裝 MariaDB 資料庫教學](https://officeguide.cc/windows-install-mariadb-database-server-tutorial/)
* [MariaDB-安裝－iT 邦幫忙](https://ithelp.ithome.com.tw/articles/10194334)
* [MariaDB－維基百科](https://zh.wikipedia.org/zh-tw/MariaDB)
* [筆記 - 何謂資料庫系統](https://medium.com/pierceshih/%E7%AD%86%E8%A8%98-%E4%BD%95%E8%AC%82%E8%B3%87%E6%96%99%E5%BA%AB%E7%B3%BB%E7%B5%B1-53b59aacbfb7)
* [資料定義語言－維基百科](https://zh.m.wikipedia.org/zh-tw/%E8%B3%87%E6%96%99%E5%AE%9A%E7%BE%A9%E8%AA%9E%E8%A8%80)

## 撰寫紀錄

| 人員 | 日期 | 修改紀錄 |
| - | - | - |
| 佳穎 | 2022/7 | 初版 |

<style>
.step {
    color: #fccb82;
}
.label {
    color: #0bb9a8;
    font-size: 20px;
}
.label2 {
    color: #fccb82;
    font-size: 20px;
}
</style>
