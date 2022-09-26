---
tags: Java, IDE, IntelliJ
---

# IDE 操作 - IntelliJ 篇

## 前言

對於每個工程師來說，IDE 是寫程式碼的不可或缺工具之一，是整合開發環境的簡稱，它綜合了編輯器、編譯器、連結器和除錯器等等。

而 IntelliJ 正是針對 Java 程式語言所開發的 IDE，當然也有另一款 Eclipse，但 IntelliJ 相較起來開發速度更快且更加容易使用。

首先我們會先安裝 IntelliJ，安裝完成後，先使用 spring.io 建立 Spring Boot 專案，再透過 IntelliJ 開發和啟動。

## 目錄

* [IDE 操作 - IntelliJ 篇](#ide-操作---intellij-篇)
  * [前言](#前言)
  * [目錄](#目錄)
  * [介紹/基本概念](#介紹基本概念)
  * [應用情境說明](#應用情境說明)
  * [建置環境](#建置環境)
  * [實作過程](#實作過程)
  * [參考資料](#參考資料)
  * [撰寫紀錄](#撰寫紀錄)

## 介紹/基本概念

IntelliJ 特性及優勢

* 開發效率
  * 快速新建專案
  * 大量快捷鍵可以使用
* 程式碼解析
  * 自動檢查程式碼，並確保程式碼品質
  * 程式碼智慧提示，可使用 Auto Completion
* 快速的導覽及搜尋檔案
* 導入 Git 介面，能使用 IDE 操作 VCS（Version of Control）

## 應用情境說明

開發 JAVA SE、JAVA EE 和 Sprint Boot 專案皆可以使用

## 建置環境

1. IntelliJ 官網下載 IntelliJ IDEA Community（社群版）

    [Download IntelliJ IDEA: The Capable & Ergonomic Java IDE by JetBrains](https://www.jetbrains.com/idea/download/#section=windows)

2. 選擇 exe
    ![1](https://i.imgur.com/RkG6pJH.jpg)

3. 點擊 iedalC-2022.1.3.exe (檔名會因下載日期有所不同)
   * 點擊 next
   ![1](https://i.imgur.com/SV8EFwd.png)

   * 可指定目錄或 Default，點擊 next
   ![2](https://i.imgur.com/HvfhPcL.png)

   * 勾選建立桌面捷徑和加入環境變數
   ![3](https://i.imgur.com/mwDBvJ0.png)

   * Default 為 JetBrains，點擊 Install
   ![4](https://i.imgur.com/zVpzBNW.png)

   * 安裝完成，重新開機
   ![6](https://i.imgur.com/QPSmlLu.png)

4. 安裝完成後執行 IntelliJ IDEA，安裝成功
   ![7](https://i.imgur.com/56ttvgq.png)

## 實作過程

1. 使用 [https://start.spring.io/](https://start.spring.io/) 建立 Spring Boot 專案
    * Project 選擇 Maven，Language 選擇 Java，Spring Boot 版本則 Default 選項即可
        ![8](https://i.imgur.com/ELqT3Dp.png)

    * Project metadata
        * Group → 網域
        * Artifact → 專案名稱
        * Packaging → Jar
        * Java → 11 (版本)
        ![9](https://i.imgur.com/rMDTL9W.png)

    * Dependencies 依賴，加入 Spring Web
        ![10](https://i.imgur.com/riTM4dh.png)

    * 點擊 Generate 產生壓縮檔
        ![11](https://i.imgur.com/068m3O3.png)

    * 將壓縮檔放置在特定目錄下，並右鍵將其解壓縮至此
        ![12](https://i.imgur.com/F8gaVAO.png)

2. 確認本機是否有安裝 Oracle Java 11 JDK
    * 開啟終端機，確認 Java 版本

        ```bash
        java -version
        ```

        ![13](https://i.imgur.com/3tjN3Um.png)

    * 如果尚未安裝 Java 11 請參考 ****Java JDK 下載、安裝與環境變數設定教學-Windows 篇****

        [Java JDK 下載、安裝與環境變數設定教學-Windows 篇 | KJie Notes](https://www.kjnotes.com/devtools/35)

3. 使用 IntelliJ 點擊 open 按鈕 import 專案
    * 指定專案目錄下的 pom.xml
        ![14](https://i.imgur.com/vlVVbod.png)

    * 選擇 Open as Project
        ![15](https://i.imgur.com/IvUobwg.png)

    * 選擇 Trust Project
        ![16](https://i.imgur.com/qfpUmou.png)

    * 專案載入後，點擊上方的 File，選擇 Project Structure，SDK 設定為 11
        ![17](https://i.imgur.com/0sifuuU.png)

    * SDK 選擇為 11
        ![18](https://i.imgur.com/LIcuL0o.jpg)

4. 執行專案
    * 找到 src/main/java/com.example.springbootdemo/SpringBootDemoApplication
    * 加入 sayHello 方法，並且在該 class 加上 @RestController 的 annotation

    ```java
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.bind.annotation.RestController;
    
    @SpringBootApplication
    @RestController
    public class SpringBootDemoApplication {
    
        public static void main(String[] args) {
            SpringApplication.run(SpringBootDemoApplication.class, args);
        }
    
        @GetMapping("/hello")
        public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
            return String.format("Hello %s!", name);
        }
    }
    ```

    * 點擊綠色箭頭執行專案
    * 顯示 Started SpringBootDemoApplication 代表執行成功
        ![19](https://i.imgur.com/IkcQ1WJ.jpg)

    * 連結到 [localhost:8080/hello](http://localhost:8080/hello) 畫面會顯示 Hello world
        ![20](https://i.imgur.com/P4OVUcm.png)

5. 快捷鍵操作
    * [【IntelliJ IDEA 入門指南】Java 開發者的神兵利器](https://ithelp.ithome.com.tw/articles/10255147)
    * [官方快捷鍵說明書](https://resources.jetbrains.com/storage/products/intellij-idea/docs/IntelliJIDEA_ReferenceCard.pdf)

## 參考資料

* [IntelliJ vs Eclipse - javatpoint](https://www.javatpoint.com/intellij-vs-eclipse)
* [IntelliJ vs Eclipse](https://www.interviewbit.com/blog/intellij-vs-eclipse/)
* [「討論」IntelliJ IDEA vs Eclipse：哪個更適合 Java 工程師？](https://www.gushiciku.cn/pl/pitG/zh-tw)
* [IntelliJ IDEA Community 建立 Spring Boot 專案教學 create spring boot project](https://matthung0807.blogspot.com/2019/10/intellij-idea-community-create-spring.html)

## 撰寫紀錄

| 人員 | 日期 | 修改紀錄 |
| - | - | - |
| 乃維| 2022/7 | 初版 |
