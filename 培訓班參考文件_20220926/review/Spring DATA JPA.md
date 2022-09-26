---
tags: Java, Spring Boot
---

# Spring DATA JPA

## 前言

Spring 框架支持使用 Spring Data JPA，其內建功能有分頁、排序等，並且在撰寫程式時可以避免使用 SQL 語法，以容易閱讀的程式碼來對資料庫進行搜尋，讓開發者可以快速撰寫程式，並且容易 debug。

## 目錄

* [Spring DATA JPA](#spring-data-jpa)
  * [前言](#前言)
  * [目錄](#目錄)
  * [介紹/基本概念](#介紹基本概念)
    * [ORM（Object Relational Mapping）](#ORM（Object-Relational-Mapping）)
    * [Spring Data JPA](#Spring-Data-JPA)
    * [Entity](#Entity)
    * [Repository](#Repository)
  * [建置環境](#建置環境)
    * [Eclipse 下載](#Eclipse-下載)
    * [MariaDB 下載](#MariaDB-下載)
    * [Postman 下載](#Postman-下載)
  * [實作過程](#實作過程)
    * [添加 Maven 依賴項](#添加-Maven-依賴項)
    * [設定檔](#設定檔)
    * [建立 Entity](#建立-Entity)
    * [建立 Repository](#建立-Repository)
    * [建立 Service](#建立-Service)
    * [建立 Controller](#建立-Controller)
    * [測試](#測試)
  * [補充資料](#補充資料)
    * [Spring Data JPA 接口和核心概念](#Spring-Data-JPA-接口和核心概念)
    * [Spring Data JPA 方法命名](#Spring-Data-JPA-方法命名)
    * [CrudRepository<T, ID> 提供的方法](#CrudRepositoryltT,-IDgt-提供的方法)
    * [PagingAndSortingRepository<T, ID> 提供的方法](#PagingAndSortingRepositoryltT,-IDgt-提供的方法)
    * [JpaRepository<T, ID> 提供的方法](#JpaRepositoryltT,-IDgt-提供的方法)
  * [參考資料](#參考資料)
  * [撰寫紀錄](#撰寫紀錄)

## 介紹/基本概念

### ORM（Object-Relational Mapping）

ORM，即 Object-Relational Mapping（物件關聯對映），它的作用是在「資料庫」和「Model 資料容器」之間作一個映射，這樣就不需要使用複雜的 SQL 語句，可以更簡便、安全地去從資料庫讀取資料。

### Spring Data JPA

Spring Data JPA 是 Spring 根據 ORM 框架和 JPA 規範而封裝的 JPA 應用框架，只需撰寫 Repository，並利用 JPA 的命名規則來撰寫，剩下的 Spring 會自動幫你實作其功能。

### Entity

JPA 中的 Entity 是一個 POJO（Plain old Java object），表示可以持久保存到數據庫的數據。Entity 表示存儲在數據庫中的 table。

### Repository

傳遞 Entity 資料到某個介面 Interface，以達到儲存或讀取 Entity 的能力，這個抽象化介面就是 Repository。

## 建置環境

### Eclipse 下載

* [IDE 操作 - Eclipse 篇](https://hackmd.io/@muyumiya1201/eclipse)

### MariaDB 下載

* [MariaDB－基礎建置 (DDL)](https://hackmd.io/huMkJeDCQMifhIDpEYeqww)

### Postman 下載

* [Postman 基礎使用學習](https://hackmd.io/_VtLv7Y5TS-r7y4KPPqJtg)

## 實作過程

### 添加 Maven 依賴項

將下列程式碼加入專案目錄底下的 pom.xml 中，讓專案可以使用 Spring Data JPA 以及 MySQL 的資料庫

```xml=
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

![1](https://i.imgur.com/R6zpcPb.png)

:::warning
如果使用的不是 MySQL 資料庫，可以到 [Maven Repository](https://mvnrepository.com/) 找到對應的資料庫依賴
:::

### 設定檔

添加設定檔的參數，編輯 application.properties 檔案，可以在專案目錄 /src/main/resources 底下找到，並將下列程式碼添加進去

```xml=
# MySQL資料庫設定
spring.datasource.url=jdbc:mysql://localhost:3306/testdb?cachePrepStmts=true&rewriteBatchedStatements=true&useUnicode=yes&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2B8
spring.datasource.username=root
spring.datasource.password=ROOT
```

:::warning

* spring.datasource.url=jdbc:mysql://localhost:3306/testdb...
    1. localhost：代表資料庫在本機上，若不是在本機上，則替換成對應的 IP 位址
    2. 3306：代表資料庫所在的 port，MySQL 預設為 3306
    3. testdb：為資料庫名稱
* spring.datasource.username=root
    登入資料庫時設定的帳號
* spring.datasource.password=ROOT
    登入資料庫時設定的密碼
:::

```xml=
# 是否顯示SQL語法
spring.jpa.show-sql=false

# 是否format SQL語法
spring.jpa.properties.hibernate.format_sql=true

# 根據綁定的物件去建立或更新資料表
spring.jpa.hibernate.ddl-auto=none

# 是否啟用OSIV
spring.jpa.open-in-view=false

# 每批次由幾筆資料組成
spring.jpa.properties.hibernate.jdbc.batch_size=100

# 是否使用批次insert
spring.jpa.properties.hibernate.order_inserts=true

# 是否使用批次update
spring.jpa.properties.hibernate.order_updates=true

# 是否開啟hibernate 信息
spring.jpa.properties.hibernate.generate_statistics=false
```

![2](https://i.imgur.com/smsLVJ1.png)

### 建立 Entity

對 src/main/java 按右鍵 → New → Class

Package：為存放的資料夾，這邊取名為 com.example.demo.entity

Name：為 Class 名稱，這邊取名為 Staff
![3](https://i.imgur.com/1GePkS1.png)

添加下列程式碼到 Staff 中

```java=
package com.example.demo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

// 宣告為 Entity
@Entity
// 對應資料表名稱
@Table(name = "staff")
public class Staff {

    // 宣告這個變數為主鍵
    @Id
    // 主鍵由數據庫自動維護 (AUTO_INCREMENT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 對應資料表中的欄位名稱
    @Column(name = "id")
    private Long id;
    

    @Column(name = "name")
    private String name;
    
    @Column(name = "age")
    private Long age;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "date")
    @CreatedDate()
    private Data date;
    
        public Staff() {
        super();
    }
    
    public Staff(String name, Long age, String email, String phone) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }
    
        public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
```

:::warning

* @Entity：告訴 Spring 這是一個 entity
* @Table：Table 的 name 對應到資料庫中的資料表名稱
* @Id：是此資料表的 Primary Key
* @GeneratedValue：告訴此 Column 的生成方式。
  * GenerationType.AUTO 讓容器來自動產生
  * GenerationType.IDENTITY 讓資料庫自己維護
* @Column：對應到 Table 的欄位中的欄位名稱
:::

### 建立 Repository

對 src/main/java 按右鍵 → New → Interface

Package：為存放的資料夾，這邊取名為 com.example.demo.repository

Name：為 Interface 名稱，這邊取名為 StaffRepository
![5](https://i.imgur.com/NwU1Yzi.png)

添加下列程式碼到 StaffRepository 中

```java=
package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    // 使用自動化命名規則進行條件搜尋
    Staff findByName(String name);

    // 使用自動化命名規則進行條件搜尋 (多條件)
    List<Staff> findByNameAndAge(String name, Long age);

    // 自定義 SQL 查詢
    @Query(value = "select * from staff where name = ?1", nativeQuery = true)
    Staff queryByName(String name);
}
```

:::warning

* @Repository：告訴 Spring 這是一個 repository
* JpaRepository：繼承了 PagingAndSortingRepository 和 CrudRepository，因此擁有基本 CRUD 功能、分頁以及排序功能
* ?1：代表第一個傳入的參數，若有多個參數則以 ?2、?3...以此類推
* nativeQuery：告訴 Spring 這是否為原生 SQL
* 自定義 SQL 查詢：方法名稱可以自行定義，例如將 queryByName 改成 getByName

:::

`JpaRepository<Staff ,Long>`

* Staff：對應的資料表實體物件
* Long：資料表 ID 的型態

### 建立 Service

建立一個 Servic 來操作 Repository
對 src/main/java 按右鍵 → New → Class

Package：為存放的資料夾，這邊取名為 com.example.demo.service

Name：為 Class 名稱，這邊取名為 StaffService
![6](https://i.imgur.com/ECF5xBF.png)

添加下列程式碼到 StaffService 中

```java=
package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Staff;
import com.example.demo.repository.StaffRepository;

@Service
public class StaffService {

    // import repository
    @Autowired
    private StaffRepository repository;

    // 使用自動化命名規則進行條件搜尋
    public Staff findByName(String name) {
        return repository.findByName(name);
    }

    // 使用自動化命名規則進行條件搜尋 (多條件)
    public List<Staff> findByNameAndAge(String name, Long age) {
        return repository.findByNameAndAge(name, age);
    }

    // 自定義 SQL 查詢
    public Staff queryByName(String name) {
        return repository.queryByName(name);
    }

}
```

:::warning

* @Service：告訴 Spring 這是一個 Service
* @Autowired：預設會依注入對象的類別型態來選擇容器中相符的物件來注入。
:::

### 建立 Controller

建立一個 Controller，可以透過 HTTP 請求得到回饋
對 src/main/java 按右鍵 → New → Class

Package：為存放的資料夾，這邊取名為 com.example.demo.controller

Name：為 Class 名稱，這邊取名為 StaffController
![7](https://i.imgur.com/sX7uxn8.png)

添加下列程式碼到 StaffController 中

```java=
package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Staff;
import com.example.demo.service.StaffService;

@RequestMapping("/api")
@RestController
public class StaffController {

    @Autowired
    private StaffService service;

    @GetMapping("/findByName")
    // 使用自動化命名規則進行條件搜尋
    public Staff findByName(String name) {
        return service.findByName(name);
    }

    @GetMapping("/findByNameAndAge")
    // 使用自動化命名規則進行條件搜尋 (多條件)
    public List<Staff> findByNameAndAge(String name, Long age) {
        return service.findByNameAndAge(name, age);
    }

    @GetMapping("/queryByName")
    // 自定義 SQL 查詢
    public Staff queryByName(String name) {
        return service.queryByName(name);
    }

}
```

:::warning

* @RequestMapping：具體指出 request 的類別
* @RestController：告訴 Spring 這是一個 RestController
* @GetMapping("")：代表請求為 GET，() 內的字串為對應方法的 URL
:::

### 測試

啟動專案，並打開 Postman，輸入 URL 跟參數並按下 SEND

***使用自動化命名規則進行條件搜尋***
![9](https://i.imgur.com/s45kdBD.png)

***使用自動化命名規則進行條件搜尋 (多條件)***
![8](https://i.imgur.com/XsWGdYc.png)

***自定義 SQL 查詢***
![10](https://i.imgur.com/CrRMnKE.png)

## 補充資料

### Spring Data JPA 接口和核心概念

| 接口 | 說明 |
| -------- | -------- |
| Repository | 最頂層的接口，是一個空的接口，目的是為了統一所有 Repository 的類型，且能讓組件掃描的時候自動識別 |
| CrudRepository | 是 Repository 的子接口，提供 CRUD 的功能 |
| PagingAndSortingRepository | 是 CrudRepository 的子接口，添加分頁和排序的功能 |
| JpaRepository | 是 PagingAndSortingRepository 的子接口，增加了一些實用的功能，比如：批量操作等 |
| JpaSpecificationExecutor | 用來做負責查詢的接口 |
| Specification | 是 Spring Data JPA 提供的一個查詢規範，要做複雜的查詢，只需圍繞這個規範來設置查詢條件即可 |

### Spring Data JPA 方法命名

方法 findByNameAndAge 裡面的 name 跟 age 為 Staff 裡的類別，JP A 可以透過方法命名的方式轉換成 SQL 語法。
findByNameAndAge —> where name = ? AND age = ?

| 關鍵字 | 方法命名 | sql where 字句 |
| -------- | -------- | -------- |
|Distinct|findDistinctByNameAndPwd|select distinct … where name = ? and pwd = ? |
| And | findByNameAndPwd | where name= ? and pwd =? |
| Or | findByNameOrSex |where name= ? or sex=? |
| Is,Equals | findById,findByIdEquals | where id= ? |
| Between | findByIdBetween | where id between ? and ? |
| LessThan | findByIdLessThan | where id < ? |
| LessThanEquals | findByIdLessThanEquals | where id <= ? |
| GreaterThan | findByIdGreaterThan | where id > ? |
| GreaterThanEquals | findByIdGreaterThanEquals | where id > = ? |
| After | findByIdAfter | where id > ? |
| Before | findByIdBefore | where id < ? |
| IsNull | findByNameIsNull | where name is null |
| isNotNull,NotNull | findByNameNotNull | where name is not null |
| Like | findByNameLike | where name like ? |
| NotLike | findByNameNotLike | where name not like ? |
| StartingWith | findByNameStartingWith | where name like ‘?%’ |
| EndingWith | findByNameEndingWith | where name like ‘%?’ |
| Containing | findByNameContaining | where name like ‘%?%’ |
| OrderBy | findByIdOrderByXDesc | where id=? order by x desc |
| Not | findByNameNot | where name <> ? |
| In | findByIdIn(Collection<?> c) | where id in (?) |
| NotIn | findByIdNotIn(Collection<?> c) | where id not in (?) |
| True | findByAaaTrue | where aaa = true |
| False | findByAaaFalse | where aaa = false |
| IgnoreCase | findByNameIgnoreCase | where UPPER(name)=UPPER(?) |

### CrudRepository<T, ID> 提供的方法

```java=
    /**
     * 儲存一個實體。
     */
    <S extends T> S save(S entity);

    /**
     * 儲存提供的所有實體。
     */
    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    /**
     * 根據 id 查詢對應的實體。
     */
    Optional<T> findById(ID id);

    /**
     * 根據 id 查詢對應的實體是否存在。
     */
    boolean existsById(ID id);

    /**
     * 查詢所有的實體。
     */
    Iterable<T> findAll();

    /**
     * 根據給定的 id 集合查詢所有對應的實體，返回實體集合。
     */
    Iterable<T> findAllById(Iterable<ID> ids);

    /**
     * 統計現存實體的個數。
     */
    long count();

    /**
     * 根據 id 刪除對應的實體。
     */
    void deleteById(ID id);

    /**
     * 刪除給定的實體。
     */
    void delete(T entity);

    /**
     * 刪除給定的實體集合。
     */
    void deleteAll(Iterable<? extends T> entities);

    /**
     * 刪除所有的實體。
     */
    void deleteAll();
```

### PagingAndSortingRepository<T, ID> 提供的方法

```java=
    /**
     * 返回所有的實體，根據 Sort 引數提供的規則排序。
     */
    Iterable<T> findAll(Sort sort);

    /**
     * 返回一頁實體，根據 Pageable 引數提供的規則進行過濾。
     */
    Page<T> findAll(Pageable pageable);
```

### JpaRepository<T, ID> 提供的方法

```java=
    /**
     * 將所有未決的更改重新整理到資料庫。
     */
    void flush();

    /**
     * 儲存一個實體並立即將更改重新整理到資料庫。
     */
    <S extends T> S saveAndFlush(S entity);

    /**
     * 在一個批次中刪除給定的實體集合，這意味著將產生一條單獨的 Query。
     */
    void deleteInBatch(Iterable<T> entities);

    /**
     * 在一個批次中刪除所有的實體。
     */
    void deleteAllInBatch();

    /**
     * 根據給定的 id 識別符號，返回對應實體的引用。
     */
    T getOne(ID id);
```

## 參考資料

* [[Day20] 資料庫設計概念 - ORM](https://ithelp.ithome.com.tw/articles/10207752)
* [SpringBoot - 第十四章 | Spring-data-jpa 訪問資料庫](https://morosedog.gitlab.io/springboot-20190328-springboot14/)
* [Spring Data JPA 之 JpaRepository](https://www.796t.com/content/1548547943.html)
* [甚麼是 Repository ?](https://ithelp.ithome.com.tw/articles/10231702)

## 撰寫紀錄

| 人員 | 日期 | 修改紀錄 |
| - | - | - |
| 偉倫 | 2022/7 | 初版 |
