---
tags: basic, Git
---

# 學習 Git 觀念、操作與使用

## 前言

在專案開發過程中，會有許多來回修改的部分，或是某一版調整暫時不過版，先過後面版本的
情境，亦或是需要多人協同合作的情況，都會需要某種方式將每個開發者的程式碼整合，大多數的解決方式是使用 GIT 進行版本控制。

## 目錄

* [學習 Git 觀念、操作與使用](#學習-GIT-觀念操作與使用)
  * [前言](#前言)
  * [目錄](#目錄)
  * [介紹/基本概念](#介紹基本概念)
    * [GIT 是什麼？](#GIT-是什麼)
    * [名詞基本介紹](#名詞基本介紹)
    * [控管規則](#控管規則)
  * [應用情境說明](#應用情境說明)
  * [建置環境](#建置環境)
    * [Sourcetree 的下載](#Sourcetree-的下載)
  * [實作過程](#實作過程)
    * [Sourcetree 的實作](#Sourcetree-的實作)
    * [Git flow 知識](#Git-flow-知識)
  * [參考資料](#參考資料)
  * [撰寫紀錄](#撰寫紀錄)

## 介紹/基本概念

### GIT 是什麼？

* GIT 是一個版控軟體，用來控管檔案狀態、版本協同的情境
* 屬於分散式的控管
* 常見提供 Git 服務公司 GitHub、GitLab、BitBucket
* Sourcetree 則是操作 Git 的一種 UI 操作工具，讓我們更容易使用 Git

### 名詞基本介紹

* 環境：
  * Remote 遠端
  * Local 本地端
  * Repository 倉庫
* 指令：
  * Clone 複製 Remote 檔案至 Local
  * Pull 取得最新狀態
  * Push 推送 commit 至遠端
  * Fetch 更新 local 至最新狀態
  * Commit 一個或多個變更檔案的集合
  * Branch 分支，用來記錄 commit
  * Stage 暫時提交檔案，可以將多的檔案一起提交為一個 commit
  * Merge 合併兩個 branch 的變更
  * Checkout 切換 branch
  * Discard 捨棄變更狀態
  * Revert 還原 branch 狀態
  * Stash 暫時儲存變更至暫存區，且還原至為修改狀態
* 注意檔案
  * .gitignore：Git 控管的例外檔案列表（EX：.dll / .jar / .log ... ）
  * .git：Git 控管所有紀錄（不可隨意移除 !!）

![１](https://i.imgur.com/5w1SyV0.png)

### 控管規則

* Branch 開立情境
* Branch 的命名規則
* 每日或每週哪一時段進行 Merge ( 或換版操作 )
* 未處理完異動檔案前，不應該 Checkout 到其他 Branch
* Master 為主要 Branch(產品線)，Dev or Develop 為開發人員用的分支
* Push 應配合各團隊負責 Merge Master 或 Dev 的人員時間！
* 修改程式前應該先 Fetch、Pull、(新增 Branch)

## 應用情境說明

* Clone：把遠端的資料克隆一份
  * `git clone 網址`
* Branch：用於將修改記錄分開儲存，讓分開的分支不受其他分支的影響
  * `git branch 分支名稱`
* Checkout：切換到其他分支
  * `git checkout 分支名稱`
* Pull：取得遠端分支的程式碼，更新自己本地端分支同步與遠端一樣
  * `git pull origin 遠端分支`
  * git pull 其實就等於 git fetch 加上 merge 指令
* Add：將修改過的資料放進暫存區
  * `git add 檔案名`
  * `git add .`
* Commit：修改過的資料做提交更新的動作
  * `git commit -m 修改資訊`
* Push：用於修改後的程式可以上傳到遠端分支
  * `git push origin 遠端分支`

## 建置環境

以 Sourcetree 作為範例說明

### Sourcetree 的下載

官網：<https://www.sourcetreeapp.com/>

* 下載 SourceTree（windows 範例）
    ![2](https://i.imgur.com/3dwMvoY.png)
    ![3](https://i.imgur.com/jcZfyp9.png)

* step 1：註冊
  * (可直接跳過)
    ![4](https://i.imgur.com/2n4qhZz.png)
* step 2：安裝工具
  * 安裝 Sourcetree 時會檢測是否已經有安裝 Git，如果沒有的話可以在這邊選擇安裝。
    ![5](https://i.imgur.com/5uQjkv6.png)
  * 安裝 GIT 成功後下一步
    ![6](https://i.imgur.com/wrzy398.png)
* step 3：設置人員（之後也可再做修改）
  * 上面欄位輸入姓名，下面輸入信箱
    ![7](https://i.imgur.com/oPAxSer.png)
* step 4：設置 SSH Key，點選 NO
  * 如果有 SSH Key 可以在這個時候增加上去，之後也可以在設定中加上
    ![8](https://i.imgur.com/fH1dDR3.png)

## 實作過程

以 Sourcetree 作為範例說明

### Sourcetree 的實作

1. 如何 Clone 一個 Repository - 複製遠端資料至本地端

   * soruce Path/URL (需要下載的連結)，下方欄位為需要 clone 到本地的位置
    　![9](https://i.imgur.com/De7JH6i.png)
   * 複製成功後，會跳轉到 histroy
    　![10](https://i.imgur.com/WNGRkJm.png)

2. 先做 Fetch，再執行 Pull - 取得遠端最新歷史記錄，更新自己本地端的程式

    * 按下 Fetch 過後，發現 origin/master 在節點之上
    　　![11](https://i.imgur.com/IPXiTFz.png)
    * 執行 Pull 動作讓本地程式跟遠端一樣
    　　![12](https://i.imgur.com/Ws5tICw.png)
    * 成功後就可以看到跟遠端節點一樣
    ![13](https://i.imgur.com/0XhlojB.png)

3. 修改前，記得先在 develop 右鍵選擇 Branch，依照專案要求輸入修改的分支名稱-為了修改記錄分開儲存，分支不相互影響

    * 先在 develop 右鍵選擇 Branch
      ![14](https://i.imgur.com/h4uepQ2.png)
    * 依照專案要求輸入修改的分支名稱
      ![15](https://i.imgur.com/uHetLHO.png)

4. 修改完成後，執行 Stage、Commit：修改過的資料做提交更新的動作
    到 File Status(1)，檢查 Unstaged files 檔案 (2)，此區塊會是先前修改尚未 Stage 的
    檔案，確認檔案要異動且要保留時，將檔案 Stage 到 Staged files (3) 的區塊，
    紀錄這一個 Commit 的資訊 (4)，確認後點 Commit (5)

    * 可以看到修改 index.html 以及新增 test.js 這兩個檔案
      ![16](https://i.imgur.com/xSueet7.png)
    * 將檔案 Stage 到 Staged files(3) 的區塊，紀錄這一個 Commit 的資訊 (4)，確認後點 Commit(5)
      ![17](https://i.imgur.com/7l9XdMQ.png)
    * commit 完成後，在 history 會出現一個節點
      ![18](https://i.imgur.com/JvHYinQ.png)

5. 將 Branch Push 上 Remote：Push 推送 Commit 至遠端

    * 確認多出一個節點，Push 推送 Commit 至遠端
      ![19](https://i.imgur.com/YzHIL3e.png)
    * 跳出視窗，打勾要推送的 Commit
      ![20](https://i.imgur.com/umWsdrH.png)

6. 檢查狀態 確定有 Push 成功，前方會有 origin/

    * 確認是否有 origin/ 如果有就代表成功推版
      ex: origin/Dev_dino_XXXXXX_20220712
      ![21](https://i.imgur.com/EU6vRGH.png)

7. Merge 的使用時機：如果需要合併分支時所使用
    Fetch 過後發現，自己的分支在 develop 之下，需要把資料推到跟 develop 一樣，可以
    對著 develop 主線，右鍵 Merge，Push 框 會自動顯示有幾個檔案需要 Push。

    * Fetch 過後，發現併版人員把 dev_test_dino 並進 master 中，想把分支同步跟 master 一樣
      ![22](https://i.imgur.com/HBA83SQ.png)
    * 對著 develop，右鍵 merge，跳出彈窗詢問是否 merge
      ![23](https://i.imgur.com/J49x2aL.png)
    * 確認後 push 框會自動顯示有幾個檔案需要 push，push 按鈕按下後同第五步驟
      ![24](https://i.imgur.com/1kfAKBW.png)
    * 確認有 push 成功，前方有 origin/ 代表已成功
      ![25](https://i.imgur.com/FOR9rBi.png)

8. Checkout 的使用時機：分支之間的切換

    對著想要的分支，右鍵 Checkout，即可跳轉到此分支。如果不是自己的分支，則會跳出是否新建分支視窗。

    * 如果要 Checkout 到 dev_test 這分支，右鍵 Checkout
      ![26](https://i.imgur.com/j3TxqhD.png)
    * 因不是自己的分支，跳出是否新建分支視窗
      ![27](https://i.imgur.com/l3KVmL5.png)
    * 確定後可以看到 BRANCHES 自動新建了這個分支
      ![28](https://i.imgur.com/Cl8mUAO.png)

### Git flow 知識

Git Flow 主要的分支有 Master、Develop、Feature、Hotfix 以及 Release 這五種分支，
各種分支負責不同的功能，其中 Master 以及 Develop 這兩個分支又被稱做長期分支，因為他們會一直存活在整個 Git Flow 裡，而其它的分支大多會因任務結束而被刪除。

* Master
  主要是用來放穩定、隨時可上線的版本，來源只能從別的分支合併過來，開發者不能直接 Commit 到這個分支，是穩定版本會在這個分支上的 Commit 上打上版本號標籤。

* Develop
  主要是所有開發的基礎分支，當要新增功能的時候，所有的 Feature 分支都是從這個分支切出去的。

* Feature
  開始新增功能的時候，就是使用 Feature 分支的時候。Feature 分支都是從 Develop 分支來的

* Hotfix
  當線上產品發生緊急問題的時候，會從 Master 分支開一個 Hotfix 分支出來進行修復，
  Hotfix 分支修復完成之後，合併回 Master 分支，也同時會合併一份到 Develop 分支。
  為什麼一開始不從 Develop 分支切出來修？因為 Develop 分支的功能可能尚在開發中，這時候硬是要從這裡切出去修再合併回 Master 分支，可能會有更多錯誤。

* Release
  在 Develop 分支發布正式版本到 Master 分支之前，可以先進行一個預備發布的本版本進行測試。
  從 Develop 分支切出來，測試完成後，Release 分支將會同時合併到 Master 以及 Develop 這兩個分支上。
  Master 分支是上線版本，而合併回 Develop 分支的目的，是因為可能在 Release 分支上還會測到並修正一些問題，所以需要跟 Develop 分支同步，免得之後的版本又再度出現同樣的問題。

* 在 SourceTree 使用 Git Flow\
  [使用 Git Flow](https://gitbook.tw/chapters/gitflow/using-git-flow)

## 參考資料

* [Git Flow 是什麼？為什麼需要這種東西？](https://gitbook.tw/chapters/gitflow/why-need-git-flow)
* [常見的三種工作流程 - Git flow、GitHub Flow 與 Gitlab Flow](https://ithelp.ithome.com.tw/articles/10281080)
* [Sourcetree - git 的 GUI 管理軟體](https://ithelp.ithome.com.tw/articles/10206852)
* [新手也能懂的 Git 教學](https://medium.com/@flyotlin/%E6%96%B0%E6%89%8B%E4%B9%9F%E8%83%BD%E6%87%82%E7%9A%84git%E6%95%99%E5%AD%B8-c5dc0639dd9)
* [用 Sourcetree 實現基礎版本管控](https://medium.com/samumu-clan/%E7%94%A8-sourcetree-%E5%AF%A6%E7%8F%BE%E5%9F%BA%E7%A4%8E%E7%89%88%E6%9C%AC%E7%AE%A1%E6%8E%A7-b007254e95c5)

## 撰寫紀錄

| 人員 | 日期 | 修改紀錄 |
| - | - | - |
| 佳諭 | 2022/7 | 初版 |
