---
tags: basic, SQL
---

# MariaDB - 進階語法操作 DML

## 前言

本篇認識到關聯式資料庫相關知識及如何學習透過 SQL 語法，對資料進行 CRUD 操作。

## 目錄

* [MariaDB－進階語法操作 DML](#MariaDB---進階語法操作-DML)
  * [前言](#前言)
  * [目錄](#目錄)
  * [介紹/基本概念](#介紹基本概念)
    * [關聯式資料庫（RDBMS）](#關聯式資料庫（RDBMS）)
    * [什麼是 DML](#什麼是-DML)
    * [資料庫正規化](#資料庫正規化)
    * [ACID、Transaction 定義](#ACID、Transaction-定義)
    * [INSERT 插入資料](#insert-插入資料)
    * [SELECT](#SELECT)
    * [UPDATE](#UPDATE)
    * [DELETE](#DELETE)
    * [JOIN](#JOIN)
  * [應用情境說明](#應用情境說明)
  * [建置環境](#建置環境)
  * [實作過程](#實作過程)
    * [建立資料表及新增資料](#建立資料表及新增資料)
    * [練習 1](#練習-1)
    * [練習 2](#練習-2)
    * [練習 3](#練習-3)
    * [練習 4](#練習-4)
  * [撰寫紀錄](#撰寫紀錄)
  * [參考資料](#參考資料)

## 介紹/基本概念

### 關聯式資料庫（RDBMS）

關聯式資料庫是由多個資料表（Table）所組成，資料表間透過特定欄位將資料串接，除了達到表與表之間的關聯，也能透過正規化（Normalization）有效儲存資料。
資料是由使用者操作 DML 指令修改，而一個交易（Transaction）是指由一系列操作組成一個完整的過程，每一筆交易都必須要滿足 ACID 的特性來確保資料完整性。

### 什麼是 DML

DML 資料庫操作語言（Data Manipulation Language）:
能查詢或修改資料表的紀錄的語言，主要指令有 INSERT、UPDATE、DELETE 等等

### 資料庫正規化

資料庫正規化是指將原先資料表的所有資訊，在經過正規化分解一至數個新資料表之後，仍能由新資料表與原先資料表比較得到相同的資訊，同時提昇儲存資料與資料庫操作效率。

正規化是一個循序漸進的過程，整個過程分成數個規則，而在實務上，BCNF 被視為大部分應用程式所需的最高階正規形式。滿足正規化第一條規則，則資料庫就稱為「第一正規化形式（1NF）」，必須滿足前二條規則，則資料庫就被視為「第二正規化形式（2NF）」依此類推，我們直接經由下面一張尚未正規化資料表來依序介紹各項規則。

#### 原始的資料表

| 購買日期 | 會員 ID | 會員名稱 | 商品 ID | 單價 | 數量 |
| -------- | --- | ---- | ---------- | -------- | ---- |
| 20220710 | 001 | John | 6214, 2330 | 100, 200 | 5, 3 |
| 20220713 | 002 | Mary | 6214, 2303 | 100,150  | 7, 9 |

1. 1NF 定義
   * 一格欄位只能有一筆資料
   * 沒有出現完全一樣的兩筆資料
   * 每筆資料都有一個唯一的主鍵（Primary Key）作為識別，而其他所有的欄位都相依於主鍵

   > 主鍵（Primary Key）
   > 一個資料表只能有一個主鍵，主鍵可連結一至多個欄位，且必須是唯一值不可為 NULL

   根據 1NF 定義，發現商品 ID、單價、數量都違反第一個原則，我們先把同一欄位有多筆資料一併分解新的一筆資料。

   | 編號 | 購買日期 | 會員 ID | 會員名稱 | 商品 ID | 單價 | 數量 |
   | ---- | -------- | ------ | -------- | -------- | ---- | ---- |
   | 1    | 20220710 | 001    | John     | 6214     | 100  | 5    |
   | 2    | 20220710 | 001    | John     | 2330     | 200  | 3    |
   | 3    | 20220713 | 002    | Mary     | 6214     | 100  | 7    |
   | 4    | 20220713 | 002    | Mary     | 2303     | 150  | 9    |

   在分解重複的資料後對照原始的資料表，發現編號 1 和 2 原本是同一時間的訂單，現在被拆開來無法辨識是否為同一筆訂單，因此需要一個欄位識別同一筆訂單才符合原本的資料表，並將訂單編號、會員 ID、商品 ID 當作聯合主鍵。

   | 編號 | 訂單編號 | 購買日期 | 會員 ID | 會員名稱 | 商品 ID | 單價 | 數量 |
   | ---- | -------- | -------- | ------ | -------- | -------- | ---- | ---- |
   | 1    | a001     | 20220710 | 001    | John     | 6214     | 100  | 5    |
   | 2    | a001     | 20220710 | 001    | John     | 2330     | 200  | 3    |
   | 3    | a002     | 20220713 | 002    | Mary     | 6214     | 100  | 7    |
   | 4    | a002     | 20220713 | 002    | Mary     | 2303     | 150  | 9    |

2. 2NF
   * 符合 1NF
   * 不可「部分功能相依」於主鍵

   > 部分功能相依：
   > 每一非鍵屬性必須「完全相依」於主鍵，且「部分功能相依」只有當複合主鍵時才會發生

   「單價」只相依於「商品 ID」，所以把商品 ID、單價組成新的表格，留下商品 ID 關聯新的資料表。

   | 編號 | 訂單編號 | 購買日期 | 會員 ID | 會員名稱 | 商品 ID | 數量 |
   | ---- | -------- | -------- | ------ | -------- | -------- | ---- |
   | 1    | a001     | 20220710 | 001    | John     | 6214     | 5    |
   | 2    | a001     | 20220710 | 001    | John     | 2330     | 3    |
   | 3    | a002     | 20220713 | 002    | Mary     | 6214     | 7    |
   | 4    | a002     | 20220713 | 002    | Mary     | 2303     | 9    |

   商品 ID、單價組成的新資料表：**Product**

   | 商品 ID | 單價 |
   | -------- | ---- |
   | 2303     | 150  |
   | 2330     | 200  |
   | 6214     | 100  |

3. 3NF
   * 符合 1NF~2NF
   * 不可「遞移相依」於主鍵

   > 遞移相依：
   > 舉例來說，X 相依 Y，Y 相依 Z，故 X 遞移相依 Z

   由於每一份訂單都會有下訂單的會員，「會員 ID」相依「訂單編號」，「會員名稱」相依「會員 ID」，故「會員名稱」遞移相依「訂單編號」，因此將他們重新組成新資料表。

   新資料表：**Order**

   | 編號 | 訂單編號 | 購買日期 | 會員 ID | 商品 ID | 數量 |
   | ---- | -------- | -------- | ------ | -------- | ---- |
   | 1    | a001     | 20220710 | 001    | 6214     | 5    |
   | 2    | a001     | 20220710 | 001    | 2330     | 3    |
   | 3    | a002     | 20220713 | 002    | 6214     | 7    |
   | 4    | a002     | 20220713 | 002    | 2303     | 9    |

   新資料表：**Customer**

   | 會員 ID | 會員名稱 |
   | ------ | -------- |
   | 001    | John     |
   | 002    | Mary     |

   既有資料表：**Product**

   | 商品 ID | 單價 |
   | -------- | ---- |
   | 2303     | 150  |
   | 2330     | 200  |
   | 6214     | 100  |

4. BCNF（Boyce-Codd NormalForm）

   * 如果資料表的「主鍵」只由「單一欄位」組合而成，則符合 3NF 的資料表，亦符合 BCNF 正規化
   * 如果資料表的「主鍵」由「多個欄位」組成，則資料表就必須要符合以下條件，表示資料表符合 BCNF 正規化的形式
     * 符合 3NF
     * 「主鍵」中的各欄位不可以相依於其他非主鍵的欄位

透過上述條件，來驗證 Order 資料表是否符合 BCNF，對訂單編號、會員 ID，商品 ID 而言，都沒有相依購買日期與數量，所以 Order 資料表是符合 BCNF。

### ACID、Transaction 定義

Transaction 是指對資料庫下動作指令，由各種資料庫語法（Select、Insert 等等）所組成，Transaction 可能包含了多個資料庫語法來完成一次動作，一次交易可進行 Commit 傳遞交易或 Rollback 進行回滾，並且滿足 ACID 特性。

ACID 分別為 A（Atomicity）原子性、C（Consistency）一致性、I（Isolation）隔離性、D（Durability）持續性。

A（Atomicity）原子性：
在一次 Transaction 過程中，如果不慎指令執行失敗，在同一個 Transaction 已執行部分都必須回歸到未進行 Transaction 前（Rollback），反之所有指令都成功完成則可以提交變更。

:::success
舉例來說，John 要匯款給 Mary，該系統未實現原子性的狀況下，指令執行到一半失敗，導致 Mary 既沒收到款項，John 還被扣匯款的金額。
:::

C（Consistency）一致性：
每一筆 Transaction 的資料改動，都必須遵循最新訂好的資料規則，否則交易失敗。

:::success
例如：匯款交易金額不可小於零，若有其中一筆指令違反就會必須回歸到未進行 Transaction 前。
:::

I（Isolation）隔離性：
確保同一筆資料不會被多個 Transaction 同時進行更改。

:::success
例如：假設 John 同時需要付款兩筆 100 金額，John 原先帳戶內有 1000 元，第一筆取得帳戶金額 1000 後進行扣款，餘額 900 元，
系統在未遵守隔離性的情況下，同時第二筆也取得帳戶金額 1000 後進行扣款，餘額 900 元，
所以 Transation 在執行的時候必須要先把欄位鎖起來（不給操作），防止其他的操作也修改到同一個值。
:::

D（Durability）持續性：
在 Transaction 成功提交變更（Commit）之後，除非存放空間的硬體受損，否則伺服器當機、斷電，已經修改的數據都會存在。

### INSERT 插入資料

新增資料至指定資料表，INSERT INTO 可以每個欄位的值都依序填入及指定欄位填入。

```sql=
INSERT INTO customer VALUES 
    (001, 'John'),
    (002, 'Mary');
```

```sql=
INSERT INTO customer(`id`,`name`) VALUES
    (001, 'John'),
    (002, 'Mary');
```

### SELECT

SELECT 查詢的要求會返回一個結果資料表，大致架構為

```sql=
SELECT 欄位1, 欄位2, 欄位3 ... // 查詢全部欄位用 SELECT *
FROM 資料表
WHERE 欄位1 運算子 value;
```

補充常見運算子：

| 運算子   | 說明           |
| ------- |:---------------------:|
| <> !=   | 不等於          |
| <= !>   | 小於等於         |
| AND     | 兩個條件為真，則為真   |
| OR      | 其中一個條件為真，則為真 |
| BETWEEN | 兩值之間         |
| IS NULL | 為 NULL 值        　|
| IN      | 過濾一組值        |
| NOT     | 否定一個條件       |

例如：查詢會員 ID 為 1 的所有購買 6214 或 2330 的訂單

```sql=
SELECT *
FROM `order`
WHERE customer_id = 1 AND (product_id = 6214 OR product_id = 2330);
```

#### ORDER BY

ORDER BY 可以將取得的資料依照欄位順序來作排序，排序預設是由小至大 ASC（ascending）或由大至小 DESC（descending）。

```sql=
// 由小至大
SELECT 欄位1, 欄位2...
FROM 資料表
ORDER BY 欄位1 ASC, 欄位2 ASC...

// 由大至小
SELECT 欄位1, 欄位2...
FROM 資料表
ORDER BY 欄位1 DESC, 欄位2 DESC...
```

#### FUNCTION、GROUP BY

SQL 內建函數常見的有聚合、字串、數值、時間函數。

| 函數   　| 說明        |
| ------- |:----------------:|
| SUM()   | 取得 column 之和  |
| AVG()   | 取得 column 平均  |
| UPPER() | 轉換大寫符號    |
| NOW()   | 取得當前日期與時間 |
| YEAR()  | 取得日期年份的部分 |

GROUP BY 通則：

* GROUP BY 子句出現在 WHERE 子句之後，ORDER BY 子句之前
* SELECT 的每一欄位都必須出現在 GROUP BY 子句中，除了聚合函數之外
* HAVING 語句可過濾分組

例如：依照會員 ID 查詢總購買數量大於 2

```sql=
SELECT customer_id , SUM(subtotal)
FROM `order`
GROUP BY customer_id
HAVING SUM(subtotal) > 2;
```

### UPDATE

當我們需要更新資料可以使用 UPDATE，依據要更新的欄位 SET 值，記得要加上 WHERE 條件式，不然會全部都更新。

```sql=
UPDATE customer
SET name = 'Tom', 欄位2 = value, 欄位3 = value···
WHERE id = 001;
```

### DELETE

DELETE FROM 是用來刪除資料表中的資料，記得要加上 WHERE 條件式，不然會全部都刪除。

```sql=
DELETE FROM customer
WHERE id = 001;
```

### JOIN

SQL JOIN 利用不同資料表之間欄位的關連性來結合多資料表之檢索，條件語句為 ON，JOIN 比子查詢的效率快，不同資料表可以用 AS 給別名替代，大致分為

* INNER JOIN
  查詢結果只會返回符合連接條件的資料，也就是資料表 1 和資料表 2 的集合
  ![1](https://i.imgur.com/FYzCb75.png)

* LEFT JOIN
  即便資料表 2 沒有資料，左側資料表 1 的所有記錄都會加入到查詢結果中
  ![2](https://i.imgur.com/GZhAY7r.png)

* RIGHT JOIN
  即便資料表 1 沒有資料，右側資料表 2 的所有記錄都會加入到查詢結果中
  ![3](https://i.imgur.com/40YX8MF.png)

* NATURAL JOIN
  加上 NATURAL 這個關鍵字之後，相當於 INNER JOIN，只不過兩資料表之間同名的欄位會被自動結合在一起

```sql=
SELECT 欄位1, 欄位2...
FROM 資料表1 AS A
INNER JOIN 資料表2 AS B 
ON A.欄位1 = B.欄位1;
```

```sql=
SELECT 欄位1, 欄位2...
FROM 資料表1
NATURAL JOIN 資料表2;
```

:::info
使用 INNER JOIN + NULL 值的判斷優於使用 NOT IN，因為 NOT IN 需要透過子查詢，較容易影響效能，再加上「負向查詢」（NOT）的判斷常會讓查詢最佳化模組無法有效地使用索引更讓查詢效率降低。
可參考練習 3
:::

## 應用情境說明

透過練習的題目，熟悉 DML 的使用，並且思考如何去篩選出需要的資料，或是對一段 SQL，能夠預期可能篩選出有哪些資料。

## 建置環境

參考 [MariaDB－基礎建置 DDL](https://hackmd.io/@user57/H1Z2wPYs9)
資料庫：MariaDB
GUI Tool: HeidiSQL

## 實作過程

### 建立資料表及新增資料

先在查詢將下列 SQL 貼上，並按下紅框的執行 SQL，建立資料表在資料庫內，再新增資料至資料表。

![4](https://i.imgur.com/5RPtwPg.png)

```sql=
CREATE TABLE `customer` (
    `id` TINYINT(3) UNSIGNED NOT NULL,
    `name` VARCHAR(8) NOT NULL COLLATE 'latin1_swedish_ci',
    PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `product` (
    `id` SMALLINT(5) UNSIGNED NOT NULL,
    `price` INT(10) UNSIGNED NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
;

CREATE TABLE `order` (
    `id` VARCHAR(32) NOT NULL COLLATE 'utf8mb4_general_ci',
    `create_date` DATETIME NOT NULL,
    `customer_id` TINYINT(3) UNSIGNED NOT NULL,
    `product_id` SMALLINT(5) UNSIGNED NOT NULL,
    `subtotal` INT(10) UNSIGNED NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `FK_order_customer` (`customer_id`) USING BTREE,
    INDEX `FK_order_product` (`product_id`) USING BTREE,
    CONSTRAINT `FK_order_customer` FOREIGN KEY (`customer_id`) REFERENCES `sprint4`.`customer` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT `FK_order_product` FOREIGN KEY (`product_id`) REFERENCES `sprint4`.`product` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;
```

```sql=
INSERT INTO  customer VALUES
    (1, 'John'),
    (2, 'Mary'),
    (3, 'Jack'),
    (4, 'Tom');

```

```sql=
INSERT INTO  product VALUES
    (6214, 100),
    (2330, 200),
    (2303, 150);

```

```sql=
INSERT INTO  `order` VALUES
    ('a001', '2022-07-03 15:46:39', 1, 6214, 6),
    ('a002', '2022-07-14 15:46:39', 1, 2330, 8),
    ('a003', '2022-07-15 15:46:39', 1, 2303, 11),
    ('a004', '2022-07-23 15:46:39', 1, 6214, 13),
    ('a005', '2022-08-14 15:46:39', 1, 2330, 2),
    ('a006', '2022-08-17 15:46:39', 1, 2303, 15),
    ('a007', '2022-07-01 15:46:39', 2, 6214, 12),
    ('a008', '2022-08-14 15:46:39', 2, 2330, 12),
    ('a009', '2022-08-17 15:46:39', 2, 2303, 8),
    ('a010', '2022-07-01 15:46:39', 2, 6214, 6),
    ('a011', '2022-07-13 15:46:39', 2, 2330, 17),
    ('a012', '2022-07-18 15:46:39', 2, 2303, 18),
    ('a013', '2022-07-02 15:46:39', 3, 6214, 16),
    ('a014', '2022-07-13 15:46:39', 3, 2330, 7),
    ('a015', '2022-07-18 15:46:39', 3, 2303, 19),
    ('a016', '2022-07-01 15:46:39', 3, 6214, 6),
    ('a017', '2022-07-16 15:46:39', 3, 2330, 7),
    ('a018', '2022-07-19 15:46:39', 3, 2303, 11);
```

### 練習 1

#### 查詢在 2022 年 7 月間且單次購買數量大於 10 的訂單

![5](https://i.imgur.com/o5FrQ73.png)

:::spoiler 解答

```sql=
SELECT * 
FROM `order` 
WHERE subtotal > 10 AND (create_date BETWEEN '2022-07-01' AND '2022-07-31');  
```

:::

### 練習 2

#### 按照商品價格降冪排序，查詢會員 ID 為 2 的訂單

![6](https://i.imgur.com/tj0JnGv.png)

:::spoiler 解答
在 WHERE 條件句中，透過商品 ID 將兩個資料表關聯，再判斷會員 ID 為 2 的條件，最後依照 product 資料表的價格降冪排序。

```sql=
SELECT * 
FROM `order` AS o, `product` AS p
WHERE o.product_id = p.id AND o.customer_id = 2
order BY p.price DESC;   
```

:::

### 練習 3

#### 查詢從未下訂單的會員名稱

![7](https://i.imgur.com/elXnS8G.png)

:::spoiler 解答
透過子查詢先把所有會員 ID 都找出來，在主查詢中，透過 NOT IN 把 customer 資料表會員 ID 不包含子查詢資料找出來。先前有提到這樣做法效率是很差，所以再對此做優化。

```sql=
SELECT *
FROM  `customer`
WHERE id NOT IN ( SELECT customer_id FROM `order`);
```

先將 customer 與 order 透過 LEFT JOIN 關聯，如此一來，未在 order 資料表的會員 ID 會是 NULL，藉此找到不在訂單上的會員。

![8](https://i.imgur.com/jghdBNq.png)

```sql=
SELECT `name`
FROM `customer` c 
    LEFT JOIN `order` o
    ON c.id = o.customer_id
WHERE
    o.customer_id IS NULL; 
```

:::

### 練習 4

#### 查詢與訂單編號 a001 相同商品的所有訂單

![9](https://i.imgur.com/Y13PaiE.png)

:::spoiler 解答
子查詢先將訂單編號 a001 商品 ID 找出來，在主查詢再去對應每筆的商品 ID。
也可以改寫成 INNER JOIN 方法。

```sql=
SELECT *
FROM `order`
WHERE product_id = (
        SELECT product_id
        FROM `order`
        WHERE id = "a001")
```

```sql=
SELECT o1.*
FROM `order` AS o1 INNER JOIN `order` AS o2
ON o1.product_id = o2.product_id AND o2.id = "a001";
```

:::

## 參考資料

* [資料庫系統應用 - 關聯式資料庫](https://ithelp.ithome.com.tw/articles/10185512)
* [資料庫正規化](http://cc.cust.edu.tw/~ccchen/doc/db_04.pdf )
* [淺談關聯式資料庫與 ACID 特性](https://medium.com/appxtech/%E8%B3%87%E6%96%99%E5%BA%AB-%E6%B7%BA%E8%AB%87%E9%97%9C%E8%81%AF%E5%BC%8F%E8%B3%87%E6%96%99%E5%BA%AB%E8%88%87acid%E7%89%B9%E6%80%A7-83a1b4178981)

## 撰寫紀錄

| 人員 | 日期 | 修改紀錄 |
| - | - | - |
| 佳諭 | 2022/7 | 初版 |
