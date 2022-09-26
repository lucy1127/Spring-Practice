---
tags: Java
---

# JAVA 8 語法糖

## 前言

在使用 Java 開發時，有時候覺得為什麼實作出某個功能的語法怎麼那麼醜那麼長，舉例來說要逐一比較迴圈內資料的大小，動不動就寫了 50 行，有沒有什麼辦法可以簡化程式碼呢？方法當然很多，而這個章節主要會介紹 JAVA 8 中的語法糖，語法糖可以讓工程師在未來開發上用更簡潔的語法撰寫程式，不僅可以簡化程式碼，也可以使工程師把專注力投入於更為重要的程式邏輯處理。

## 目錄

* [JAVA 8 語法糖](#JAVA-8-語法糖)
  * [前言](#前言)
  * [目錄](#目錄)
  * [介紹/基本概念](#介紹基本概念)
    * [Lambda 的基本概念](#Lambda-的基本概念)
    * [Stream 的基本概念](#Stream-的基本概念)
    * [Optional 的基本概念](#Optional-的基本概念)
  * [實作過程](#實作過程)
    * [Lambda 的實作過程](#Lambda-的實作過程)
      * [Lambda 的語法](#Lambda-的語法)
      * [parameters 的部分](#parameters-的部分)
      * [`->` 後的部分](#-gt-後的部分)
    * [Stream 的實作過程](#Stream-的實作過程)
      * [如何建立一個 Stream 物件？](#如何建立一個-Stream-物件)
      * [中繼操作（intermediate operations）](#中繼操作（intermediate-operations）)
      * [終端操作（terminal operation）](#（terminal-operation）)
    * [Optional 的實作過程](#Optional-的實作過程)
      * [如何建立 Optional 物件](#如何建立-Optional-物件)
      * [如何檢核 Optional](#如何檢核-Optional)
      * [如何取得 Optional 的值](#如何取得-Optional-的值)
  * [應用情境](#應用情境)
    * [Lambda、Stream 的應用情境](#Lambda、Stream-的應用情境)
    * [Optional 的應用情境](#Optional-的應用情境)
  * [參考資料](#參考資料)
  * [撰寫紀錄](#撰寫紀錄)

## 介紹/基本概念

語法糖（Syntactic sugar）意旨在程式語言中加入對原本程式語法沒影響，但卻能加速工程師開發速度的一種對原有語法簡化的新語法。在 Java 8 中，著名的語法糖不只一種。而在這個章節中，目前會著重於 Lambda、Stream、Optional 此三類語法糖。

### Lambda 的基本概念

Lambda 的誕生是為了簡化匿名類別（anonymous classes）程式碼語法，舉例來說只有一個方法的介面，如果要來實作它，程式碼的語法會顯得很笨重。Lambda 表示式可以讓工程師在撰寫程式時將函式視為方法參數或是將程式視為數據來實作介面內的方法。

```java=
public class Demo {

    interface OnlyOneMethod {
        public void printASentence();
    }

    public static void main(String[] args) {
//        使用匿名函示
        OnlyOneMethod onlyOneMethod = new OnlyOneMethod() {
            public void printASentence() {
                System.out.println("This is a demo of anonymous class.");
            }
        };
        onlyOneMethod.printASentence();
//             This is a demo of anonymous class.


//        使用 lambda 表示式
        OnlyOneMethod onlyOneMethod1 = () -> System.out.println("This is a demo of lambda expression.");
        onlyOneMethod1.printASentence();
//             This is a demo of lambda expression.。
    }
}
```

### Stream 的基本概念

Stream 的誕生使工程師可以更為輕鬆的讀取程式邏輯，它不僅大幅簡化程式碼，同時也可以透過 Stream 的操作快速使工程師知道這段程式的目的。以下介紹一些常用的 Stream 指令及應用情境。

Stream 可以運行一系列的管線（pipeline）操作來執行循序運算（sequential）或並行運算（parallel）。Stream 會有一個來源值（可能是 array、collectuin、I/O channel 等等），0 或多個的中繼操作（intermediate operations)，和 1 個終端操作（terminal operation）。

此外 Stream 的運行只有在終端操作被啟動時才會執行（可以簡化成回傳值不為 stream 才會真正執行），並且只會消耗這次運行所需要的來源值內的資料。

使用 Stream 時要注意不能在 Stream 內更動到來源值，否則會有不可預期的錯誤產生。另外，基本上調用過的 Stream 不能再被調用，否則會拋出 IllegalStateException 的錯誤。

### Optional 的基本概念

Optional 的誕生是為了處理資料流有 null 的狀況，有時在撰寫程式時，忘記考慮到資料流有 null 的狀況，小則在畫面直接顯示 null，大則可能導致程式因為後續邏輯處理拋出 NullPointerException 而整個壞掉。

Optional 完整的類別名稱為 java.util.Optional&lt;T&gt;，以下會介紹一些常用的 Optional 指令及應用情境。

## 實作過程

### Lambda 的實作過程

#### <span class="orange">Lambda 的語法</span>

```java=
(parameters) -> expression
or
(parameters) -> { statements; }
```

Lambda parameters 的部分跟 `->` 後的部分各有多種語法的呈現方式，都可以依不同情形在撰寫程式時混用。

#### <span class="orange">parameters 的部分</span>

1. 不輸入參數

    ```=
    ()
    ```

    ```java=
    public class Demo {

        interface OnlyOneMethod {
            public void printASentence();
        }

        public static void main(String[] args) {
            OnlyOneMethod onlyOneMethod1 = () -> System.out.println("This is a demo.");
            onlyOneMethod1.printASentence();
           // This is a demo.
        }
    }
    ```

2. 輸入一個參數

    ```=
    (param)、param
    ```

    ```java=
    public class Demo {

        interface OnlyOneMethod {
            public void printASentence(String input);
        }

        public static void main(String[] args) {
            OnlyOneMethod onlyOneMethod1 = input -> System.out.println(input);
            onlyOneMethod1.printASentence("This is a demo.");
            // This is a demo.
        }
    }
    ```

3. 輸入多個參數

    ```=
    (型別 param1, 型別 param2)、(param1, param2)
    ```

    ```java=
    public class Demo {

        interface OnlyOneMethod {
            public void printASentence(String first, String second);
        }

        public static void main(String[] args) {
            OnlyOneMethod onlyOneMethod1 = (String first, String second) -> System.out.printf("%s %s.", first, second);
            onlyOneMethod1.printASentence("This is a", "demo");
            // This is a demo.    
        }
    }
    ```

#### <span class="orange">`->` 後的部分</span>

1. 什麼都不做

    ```=
    {}
    ```

    ```java=
    public class Demo {

        interface OnlyOneMethod {
            public void printASentence();
        }

        public static void main(String[] args) {
            OnlyOneMethod onlyOneMethod1 = () -> {};
            onlyOneMethod1.printASentence();
        }
    }
    ```

2. 單行不回傳值

    ```java=
    public class Demo {

        interface OnlyOneMethod {
            public void printASentence();
        }

        public static void main(String[] args) {
            OnlyOneMethod onlyOneMethod1 = () -> System.out.println("This is a demo.");
            onlyOneMethod1.printASentence();
            // This is a demo.
        }
    }
    ```

3. 多行不回傳值

    ```java=
    public class Demo {

        interface OnlyOneMethod {
            public void printASentence();
        }

        public static void main(String[] args) {
            OnlyOneMethod onlyOneMethod1 = () -> {
                System.out.println("This is a demo1.");
                System.out.println("This is a demo2.");
            };
            onlyOneMethod1.printASentence();
            // This is a demo1.
            // This is a demo2.
        }
    }
    ```

4. 單行有回傳值

    ```java=
    public class Demo {

        interface OnlyOneMethod {
            public String printASentence();
        }

        public static void main(String[] args) {
            OnlyOneMethod onlyOneMethod1 = () -> "This is a demo.";
            System.out.println(onlyOneMethod1.printASentence());
            // This is a demo.
        }
    }
    ```

5. 多行有回傳值

    ```java=
    public class Demo {

        interface OnlyOneMethod {
            public List<String> printASentence();
        }

        public static void main(String[] args) {
            OnlyOneMethod onlyOneMethod1 = () -> Arrays.asList("This is a demo1.", "This is a demo2.");
            onlyOneMethod1.printASentence().stream().forEach(sentence -> System.out.println(sentence));
            // This is a demo1.
            // This is a demo2.
        }
    }
    ```

### Stream 的實作過程

#### <span class="orange">如何建立一個 Stream 物件？</span>

***empty()***
回傳一個空的 sequential Stream

```java=
Stream<String> testStream = Stream.empty();
```

***Collection 內的 stream()***
回傳有著特定 value 的 sequential Stream

```java=
List<String> list = Arrays.asList("1", "2", "3");
Stream<String> listStream = list.stream();
```

***of(T... values)***
回傳有著特定 value 的 sequential Stream

```java=
Stream<String> testStream = Stream.of("1", "2", "3");
```

#### <span class="orange">中繼操作（intermediate operations）</span>

***distinct()***
回傳一個 value 皆不同的 Stream

```java=
List<String> list = Arrays.asList("1", "2", "3", "1");
Stream<String> listStream = list.stream().distinct();

System.out.println( listStream.collect(Collectors.joining(",")));
// 1,2,3
```

***filter(Predicate&lt;? super T&gt; predicate)***
回傳一個 value 皆符合給定條件的 Stream

```java=
List<String> list = Arrays.asList("1", "2", "3", "1");
Stream<String> listStream = list.stream().filter(element -> Integer.valueOf(element) > 1);

System.out.println( listStream.collect(Collectors.joining(",")));
// 2,3
```

***map(Function&lt;? super T,? extends R&gt; mapper)***
回傳一個 Stream，裡面的 value 皆為給定執行 function 的回傳值

```java=
List<String> list = Arrays.asList("1", "2", "3", "1");
Stream<String> listStream = list.stream().map(element -> String.valueOf(Integer.valueOf(element) + 1));

System.out.println( listStream.collect(Collectors.joining(",")));
// 2,3,4,2
```

#### <span class="orange">終端操作（terminal operation）</span>

***void forEach(Consumer&lt;? super T&gt; action)***
遍歷整個 Stream 裡面的 element

```java=
List<String> list = Arrays.asList("1", "2", "3", "1");

list.stream().forEach(element -> {
    System.out.println(element);
});
// 1
// 2
// 3
// 1
```

***long count()***
回傳 stream 的 value 數量

```java=
List<String> list = Arrays.asList("1", "2", "3", "1");

System.out.println(list.stream().count());
// 4
```

***&lt;R,A&gt; R collect(Collector&lt;? super T,A,R&gt; collector)***
回傳一個集合

```java=
List<String> list = Arrays.asList("1", "2", "3", "1");
List<String> anotherList = list.stream()
                        .map(element -> String.valueOf(Integer.valueOf(element) + 1))
                        .collect(Collectors.toList());

anotherList.stream().forEach(element -> {
    System.out.println(element);
});
// 2
// 3
// 4
// 2
```

***Optional&lt;T&gt; findAny()***
在 Stream 中查詢任一資料而無需在意順序，有資料則回傳一個包含這筆資料的 Optional；反之則回傳一個空的 Optional。要注意這邊回傳的值具有不確定性。

```java=
Set<String> set = Set.of("1", "2", "3", "4");
Optional<String> setOptional = set.stream().findAny();

if(setOptional.isPresent())
    System.out.println(setOptional.get());
// 3
```

***Optional&lt;T&gt; findFirst()***
在 Stream 中查詢第一筆資料，有資料則回傳一個包含這筆資料的 Optional；反之則回傳一個空的 Optional。若原本資料就不具順序性，則回傳值不確定。

```java=
Set<String> set = Set.of("1", "2", "3", "4");
Optional<String> setOptional = set.stream().findFirst();

if(setOptional.isPresent())
    System.out.println(setOptional.get());
// 3
```

***T reduce(T identity, BinaryOperator&lt;T&gt; accumulator)***
reduce() 以較為簡化的方式完成了自訂運算需求；reduce() 的第一個參數為起始值，第二個參數為指定執行的 function

```java=
List<Integer> list = Arrays.asList(1, 2, 3, 4);
Integer sum = list.stream().reduce(0, Integer::sum);

System.out.println(sum);
// 10
```

### Optional 的實作過程

#### <span class="orange">如何建立 Optional 物件</span>

***of(T value)***
接收非 null 的值並回傳一個有特定 value 的 Optional 實例

```java=
Optional<String> testOptional = Optional.of("test");
```

***empty()***
回傳一個空的 Optional 實例

```java=
Optional<String> testOptional = Optional.empty();
```

***ofNullable(T value)***
可以接收 null 或非 null 的值並回傳一個 Optional 實例。若傳入為 null 值，則回傳空的 Optional 實例；反之，則為有特定 value 的 Optional 實例

```java=
Optional<String> testOptional = Optional.ofNullable(null);
Optional<String> testOptional1 = Optional.ofNullable("test");
```

#### <span class="orange">如何檢核 Optional</span>

***ifPresent(Consumer&lt;? super T&gt; consumer)***
如果 value 存在，呼叫指定的 consumer 物件並將值傳給它處理；不存在則什麼都不做。

```java=
Optional<String> testOptional = Optional.of("test");
testOptional.ifPresent(value -> {
    System.out.println(value);
});
// test
```

***boolean isPresent()***
回傳一個 boolean 值，如果 value 不為 null，則回傳 true；反之，則回傳 false

```java=
Optional<String> testOptional = Optional.of("test");
String value = testOptional.isPresent() ? 
    testOptional.get():"no value";

System.out.println(value);
// test
```

#### <span class="orange">如何取得 Optional 的值</span>

***get()***
如果 value 存在，則回傳此 value；否則拋出 NoSuchElementException

```java=
Optional<String> testOptional = Optional.ofNullable("123");
if(testOptional.isPresent()) {
    System.out.println(testOptional.get());
//  123
}
```

***orElse(T other)***
如果 value 存在，則回傳此 value；否則回傳 other

```java=
Optional<String> testOptional = Optional.ofNullable(null);
System.out.println(testOptional.orElse("no value"));
// no value
```

***orElseGet(Supplier&lt;? extends T&gt; other)***
如果 value 存在，則回傳此 value；否則調用 other 並回傳此調用的結果

```java=
Optional<String> testOptional = Optional.ofNullable(null);
System.out.println(testOptional.orElseGet(() -> "no value"));
// no value
```

***orElseThrow(Supplier&lt;? extends X&gt; exceptionSupplier)***
如果 value 存在，則回傳此 value；否則回傳方法參數內提供的 exception

```java=
Optional<String> testOptional = Optional.ofNullable(null);
System.out.println(testOptional.orElseThrow(NullPointerException::new));
// Exception in thread "main" java.lang.NullPointerException
```

## 應用情境

### Lambda、Stream 的應用情境

假設要查詢大於 25 歲的員工有幾位

***無使用 Lambda、Stream***

```java=
public class Demo {

    class Employee {
        public Integer id;
        public String name;
        public Integer age;

        public Employee(int id, String name, Integer age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }
    }

//   假設是從資料庫取值
    private List<Employee> getEmployeeList() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee(1, "Doris", 18));
        employeeList.add(new Employee(2, "Emily", 30));
        employeeList.add(new Employee(3, "Tim", 25));
        employeeList.add(new Employee(4, "Kevin", 26));

        return employeeList;
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
        List<Employee> employeeList = demo.getEmployeeList();
        Integer count = 0;
        for (Employee employee : employeeList) {
            if (employee.age > 25)
                count++;
        }
        System.out.println("The number of employees who is older than 25 is " + count + ".");
//             The number of employees who is older than 25 is 2.
    }
}
```

***有使用 Lambda、Stream***

```java=
public class Demo {

    class Employee {
        public Integer id;
        public String name;
        public Integer age;

        public Employee(int id, String name, Integer age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }
    }

//    假設是從資料庫取值
    private List<Employee> getEmployeeList() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee(1, "Doris", 18));
        employeeList.add(new Employee(2, "Emily", 30));
        employeeList.add(new Employee(3, "Tim", 25));
        employeeList.add(new Employee(4, "Kevin", 26));

        return employeeList;
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
        List<Employee> employeeList = demo.getEmployeeList();
        Integer count = Math.toIntExact(employeeList.stream().filter(employee -> employee.age > 25).count());
        System.out.println("The number of employees who is older than 25 is " + count + ".");
//             The number of employees who is older than 25 is 2.
    }
}
```

### Optional 的應用情境

假設使用者要用員工的名字去尋找員工的電話

***無使用 Optional***

```java=
public class Demo {

    class Employee {
        public Integer id;
        public String name;
        public String phone;

        public Employee(int id, String name, String phone) {
            this.id = id;
            this.name = name;
            this.phone = phone;
        }
    }

//    假設是從資料庫取值
    private List<Employee> getEmployeeList() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee(1, "Doris", "0901234567"));
        employeeList.add(new Employee(2, "Emily", "0976543210"));
        employeeList.add(new Employee(3, "Tim", null));

        return employeeList;
    }

//    用員工名字找尋員工電話    
    private String findPhoneByName(String name) {
        List<Employee> employeeMap = getEmployeeList();
        if (employeeMap != null) { // 有可能沒資料
            for (Employee employee : employeeMap) {
                if (employee.name.equals(name))
                    return employee.phone;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please type the employee's name which you want to serach:(enter 0 to exit)");
        String searchName = sc.nextLine();

        while (!searchName.equals("0")) {
            Demo demo = new Demo();
            String returnPhone = demo.findPhoneByName(searchName);
            if (returnPhone != null) // 有可能沒資料
                System.out.println(searchName + "'s phone is " + returnPhone + ".");
            else
                System.out.println(
                        "Cannot find this employee or this employee did not leave his/her phone in our database.");

            searchName = sc.nextLine();
        }
    }
}
```

***有使用 Optional***

```java=
public class Demo {

    class Employee {
        public Integer id;
        public String name;
        public String phone;

        public Employee(int id, String name, String phone) {
            this.id = id;
            this.name = name;
            this.phone = phone;
        }
    }

//    假設是從資料庫取值
    private Optional<List<Employee>> getEmployeeList() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee(1, "Doris", "0901234567"));
        employeeList.add(new Employee(2, "Emily", "0976543210"));
        employeeList.add(new Employee(3, "Tim", null));

        return Optional.ofNullable(employeeList);
    }

//    用員工名字找尋員工電話
    private Optional<String> findPhoneByName(String name) {
        Optional<List<Employee>> employeeMapOptional = getEmployeeList();
        if (employeeMapOptional.isPresent()) { // 有可能沒資料
            for (Employee employee : employeeMapOptional.get()) {
                if (employee.name.equals(name))
                    return Optional.ofNullable(employee.phone);
            }
        }

        return Optional.empty();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please type the employee's name which you want to serach:(enter 0 to exit)");
        String searchName = sc.nextLine();

        while (!searchName.equals("0")) {
            Demo demo = new Demo();
            Optional<String> returnPhoneOptional = demo.findPhoneByName(searchName);
            if (returnPhoneOptional.isPresent()) // 有可能沒資料
                System.out.println(searchName + "'s phone is " + returnPhoneOptional.get() + ".");
            else
                System.out.println(
                        "Cannot find this employee or this employee did not leave his/her phone in our database.");

            searchName = sc.nextLine();
        }
    }
}
```

## 參考資料

* [Java 8 Lambda 新語法，簡化程式，增強效能](https://magiclen.org/java-8-lambda/)
* [Stream (Java Platform SE 8 ) - Oracle Help Center](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)
* [Optional (Java Platform SE 8 ) - Oracle Help Center](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)

## 撰寫紀錄

| 人員 | 日期 | 修改紀錄 |
| - | - | - |
| 于慧 | 2022/7 | 初版 |

<style>
.orange {
    color: #f28500;
}
</style>
