# Global Exception Handler in Spring Boot

## 前言

當我們想要自己定義或處理 Exception，Spring 提供的 @ExceptionHandler 可以輕鬆的控制與使用。

常見的例外發生時，我們會將發生錯誤的當下記錄下來以利 debug，並回傳給前端。

本篇技術文件將說明如何捕捉**全域** Exception。

## 目錄

* [Global Exception Handler in Spring Boot](#Global-Exception-Handler-in-Spring-Boot)
  * [前言](#前言)
  * [目錄](#目錄)
  * [介紹/基本概念](#介紹/基本概念)
    * [認識相關的 Annotation](#認識相關的-Annotation)
    * [認識相關的 Exception](#認識相關的-Exception)
  * [應用情境說明](#應用情境說明)
  * [建置環境](#建置環境)
    * [加入依賴](#加入依賴)
    * [相關 class 建置](#相關-class-建置)
  * [實作過程](#實作過程)
    * [實作](#實作)
    * [測試](#測試)
  * [參考資料](#參考資料)
  * [撰寫紀錄](#撰寫紀錄)

## 介紹

在開發 RESTful API 時，可能會遇到許多 Exception 要經過處理後回傳前端。
以下 Annotation 是針對開發 RESTful API 遇到 Exception 時可以使用的。

### 認識相關的 Annotation

#### `@ControllerAdvice`

@ControllerAdvice 註解是 Spring 3.2 中新增的註解，作用是給 Controller 控制器新增統一的操作或處理。

@ControllerAdvice 是對 **class** 進行註解，定義了一個全域的異常處理器。

當自定義 class 加上 @ControllerAdvice 註解，function 需要返回 Json 數據時，每個方法還需要添加 @ResponseBody 註解。

![@RestControllerAdvice](https://i.imgur.com/z1aYTcE.png)

#### `@RestControllerAdvice`

與 @ControllerAdvice 的區別就和 @RestController 與 @Controller 的區別類似。

@RestControllerAdvice 註解包含了 @ControllerAdvice 註解和 @ResponseBody 註解。

當自定義 class 加上 @RestControllerAdvice 註解時，方法自動返回 Json 數據，每個方法無需再添加 @ResponseBody 註解。

:::danger
若需要回傳自定義 class 時，請使用此註解。
:::

#### `@ExceptionHandler`

配合 @RestControllerAdvice、@ControllerAdvice 註解使用，可以對異常進行**統一**的處理，回傳規定的 Json 格式。

@ExceptionHandler 是對 **function** 進行註解，作用於 Controller 級別。其作用主要在於宣告**一個**或**多個**型別的異常，當符合條件的 Controller throw 出這些異常之後，將會對這些異常進行 catch，然後按照其標註的方法的邏輯進行處理，從而改變返回的檢視資訊。

#### `@NotNull`、`@NotBlank`、`@NotEmpty`

| Annotation | 適用型態　　| 功能 |
| ---------- | --------  | --- |
| @NotNull   | 字串、數字　| 不能為 null，但可為 empty |
| @NotBlank  | 字串　　　　| 不能為 null，且調用 trim() 後，長度必 > 0 |
| @NotEmpty  | 字串、集合　| 不能為 null，且長度必 > 0 |

集合，Collection：Set、List、Map 等等

* 以字串舉例 :
  ○ : 可通過檢核
  × : 不可通過

| 情境 | @NotNull |  @NotBlank   | @NotEmpty |
|---- | -------- | ------------ | ---------- |
| String str = ""   | ○ | × | × |
| String str = " "  | ○ | × | ○ |
| String str = null | × | × | × |

#### `@Valid`

* 符合 JSR-303 規範：規定一些驗證規範的校驗註解，如 @Null、@NotNull、@Pattern，  註解位於 javax.validation.constraints 的 package 下。
* 不提供分組功能。
* 提供嵌套驗證功能。
* 可以用在**方法**、**建構子**、**方法參數**和**欄位**上

#### `@Validated`

* @Validated 是 @Valid 的封裝，是 Spring 提供的驗證機制。
* 提供分組功能：可以在方法參數驗證時，根據不同的分組採用不同的驗證機制。
* 不提供嵌套驗證功能。
* 可以用在**類別**、**方法**和**方法參數**上，但是**不能**用在欄位上。

### 認識相關的 Exception

#### `Exception`

所有的 Exception 是從 java.lang.Exception 類繼承的子類。

![1](https://i.imgur.com/h41gd2K.png)

#### `MethodArgumentNotValidException`

取得 RequestBody 驗證未通過，會拋出 MethodArgumentNotValidException。

#### `ConstraintViolationException`

取得 url 上的參數檢驗未通過，會拋出 ConstraintViolationException。

## 應用情境說明

捕捉所有的 Exception（包含 Spring Validator 拋出的 Exception），並回傳錯誤訊息物件給前端。

## 建置環境

### 加入依賴

因為我們要處理 Spring Validator 拋出的 Exception，所以到 pom.xml 加入 Spring Validation 的依賴。

```xml
<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### 相關 class 建置

#### 建立 Student.java

用來存 RequestBody 內容的類別。
欄位需要透過 Spring Validator 做基礎檢核 :

* 在 name 欄位上加上 `@NotBlank`，代表欄位不可以為空
* 在 age 欄位加上 `@NotNull`，代表欄位不可以為 null

```java=
package com.example.demo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    
    private String id;
    
    // 基礎檢核 : name 不可為空
    @NotBlank(message = "名字不可為空")
    private String name;
    
    // 基礎檢核 : age 不可為 null
    @NotNull(message = "年齡不可為空")
    private Integer age;

}
```

## 實作過程

### 實作

#### 建立 DemoController.java

在 class 上要加上`@RestController`、`@Validated`
建立 3 個 API

1. 第一個 API test 1 直接拋出 Exception
2. 第二個 API test 2 要模擬 RequestBody 驗證失敗

    :::warning
    須在參數前加上 @Valid 才會進行驗證
    :::

3. 第二個 API test 3 要模擬 Query String 驗證失敗
    :::warning
    須在類別上方加上 @Validated 才會進行驗證
    :::

```java=
package com.example.demo.controller;


@RestController
@Validated
public class DemoController {
    
    // 直接拋出 Exception
    @GetMapping("/test1")
    public void test1() throws Exception {
        throw new Exception("Test1 error");
    }
    
    // 透過 Student 類別做基礎檢核，模擬當不符合基礎檢核時拋 Exception
    @PostMapping("/test2")
    public void test2(@Valid @RequestBody Student s) {
        System.out.println(s.getName());
        System.out.println(s.getAge());
    }
    
    // 對 Query String 做基礎檢核，模擬當不符合基礎檢核時拋 Exception
    @GetMapping("/test3")
    public void test3(@NotEmpty(message = "message 不可為空值") String message, 
                      @NotEmpty(message = "name 不可為空值") String name) {
        System.out.println(message);
    }

}

```

#### 建立 ErrorResponse.java

這個 class 是用來回傳前端的物件。
會先取出 MethodArgumentNotValidException 、ConstraintViolationException 、Exception 裡的錯誤訊息再放入此物件的欄位中。

```java=
package com.example.demo;

public class ErrorResponse {
    
    private List<Map<String, String>> fieldError;
    
    private String error;
    
    // 處理 RequestBody 未通過基礎檢核所拋的 MethodArgumentNotValidException
    public ErrorResponse(MethodArgumentNotValidException e) {
        
        this.fieldError = new ArrayList<>();
        
        // 因為未通過基礎檢核的欄位可能不只一個
        // 所以需要呼叫 e.getBindingResult().getFieldErrors() 取得不符合基礎檢核的欄位
        // 再放入 fieldError 中
        
        e.getBindingResult().getFieldErrors().stream().forEach(m -> {
            Map<String, String> fieldMap = new HashMap<>();
            
            // 欄位名稱
            fieldMap.put("fiels", m.getField());
            
            // 錯誤類型，例 : NotNull 或是 NotBlank
            fieldMap.put("code", m.getCode());
            
            // 錯誤訊息，例 : 年齡不可為空
            fieldMap.put("message", m.getDefaultMessage());
            
            fieldError.add(fieldMap);
        });
    }
    
    // 處理 Query String 未通過基礎檢核所拋的 ConstraintViolationException
    public ErrorResponse(ConstraintViolationException e) {
        
        this.fieldError = new ArrayList<>();
        
        // 因為未通過基礎檢核的欄位可能不只一個
        // 所以需要呼叫 e.getConstraintViolations() 取得不符合基礎檢核的欄位
        // 再放入 fieldError 中
       
        e.getConstraintViolations().stream().forEach(c -> {
            
            String fieldName = null;
            
            for (Node node : c.getPropertyPath()) {
                fieldName = node.getName();
            }
            
            Map<String, String> map = new HashMap<>();
            // 錯誤類型，例 : NotNull 或是 NotBlank
            map.put("code", c.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
            
            // 錯誤訊息，例 : 年齡不可為空
            map.put("message", c.getMessage());
            
            // 欄位名稱
            map.put("field", fieldName);
            
            fieldError.add(map);
        });
    }
    
    // 處理 Exception
    public ErrorResponse(Exception e) {
        this.error = e.getMessage();
    }
    
    public List<Map<String, String>> getFieldError() {
        return fieldError;
    }
    
    public void setFieldError(List<Map<String, String>> fieldError) {
        this.fieldError = fieldError;
    }
    
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    
}

```

#### 建立 DemoExceptionHandler.java

這個 class 主要功能是捕捉 Exception 並進行處裡

* 在 class 上加上 `@RestControllerAdvice`
* 在 method 上加上 `@ExceptionHandler()`，在括號裡放入要捕捉的 Exception 類別。
    這次要捕捉的例外有 Exception、MethodArgumentNotValidException 與 ConstraintViolationException
    寫法如下 :
  * `@ExceptionHandler(Exception.class)`
  * `@ExceptionHandler(MethodArgumentNotValidException.class)`
  * `@ExceptionHandler(ConstraintViolationException.class)`
* 而回傳給前端的 class 是 ResponseEntity 使用泛型可以放入想要回傳給前端的 class 與狀態，寫法如下：
  * `new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);`

    | `HttpStatus` | Status Code | 適用情境 |
    | ------------ | ----------- | ------- |
    | `OK`          | 200 | Success |
    | `BAD_REQUEST` | 400 | Request Error（e.g. Param、Limit、Format、Size...） |
    | `UNAUTHORIZED` | 401 | 權限不足 |
    | `FORBIDDEN`    | 403 | Token 異常 |
    | `NOT_FOUND`    | 404 | 查無資料 |
    | `INTERNAL_SERVER_ERROR` | 500 | 系統異常 |

```java=
package com.example.demo;

@RestControllerAdvice
public class DemoExceptionHandler {
    
    // 捕捉 Exception
    // 因為是所有例外的父類，可以作為例外處理的最後一道防線
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handler(Exception e) {
        
        ErrorResponse error = new ErrorResponse(e);
        
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }
    
    // 捕捉 MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handler(MethodArgumentNotValidException e) {2
        
        ErrorResponse error = new ErrorResponse(e);
                                                                                     
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }
    
    // 捕捉 ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handler(ConstraintViolationException e) {
        
        ErrorResponse error = new ErrorResponse(e);
        
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }

}

```

### 測試

#### 第 1 個 API

:::info
`GET` <http://localhost:8080/test1>
:::
Response :

```json=
{
    "fieldError": null,
    "error": "Test1 error"
}
```

#### 第 2 個 API

:::info
`POST` <http://localhost:8080/test2>
:::

RequestBody :

```json=
{
    "id":"",
    "name":"",
    "age":""
}

```

Response :

```json=
{
    "fieldError": [
        {
            "code": "NotBlank",
            "fiels": "name",
            "message": "名字不可為空"
        },
        {
            "code": "NotNull",
            "fiels": "age",
            "message": "年齡不可為空"
        }
    ],
    "error": null
}
```

#### 第 3 個 API

:::info
`GET` <http://localhost:8080/test3?message=&name=>
:::
Response :

```json=
{
    "fieldError": [
        {
            "code": "NotEmpty",
            "field": "name",
            "message": "name 不可為空值"
        },
        {
            "code": "NotEmpty",
            "field": "message",
            "message": "message 不可為空值"
        }
    ],
    "error": null
}
```

## 參考資料

* [Day23 Spring MVC例外處理篇(Exception Handling in Spring MVC)上](https://ithelp.ithome.com.tw/articles/10196822)
* [Spring ExceptionHandler 設定](https://www.tpisoftware.com/tpu/articleDetails/1334)
* [SpringBoot 中使用@Valid 註解+ Exception 全局處理器優雅處理參數驗證](https://zhuanlan.zhihu.com/p/109227851)
* [Spring Boot統一處理全域性異常的實戰教學](https://www.it145.com/9/174344.html#_lab2_0_0)
* [@ControllerAdvice + @ExceptionHandler 全局處理Controller 層異常](https://blog.51cto.com/u_6346066/3282351)
* [Java 異常處理](https://www.runoob.com/java/java-exceptions.html)
* [Day 16 - Spring Boot 資料驗證](https://ithelp.ithome.com.tw/m/articles/10275699)
* [@RestControllerAdvice與@ControllerAdvice的區別](https://blog.csdn.net/hyk521/article/details/104990636)

## 撰寫紀錄

| 人員 | 日期   | 修改紀錄 |
| ---- | ------ | -------- |
| 婉瑜 | 2022/8 | 初版     |
| 佳穎 | 2022/9 | 補充各種 status code 情境 |
