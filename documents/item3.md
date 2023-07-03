# private 생성자나 열거 타입으로 싱글톤임을 보장하라.

# 싱글톤 패턴

## 사용하는 목적

- 인스턴스를 오직 하나만 만들기 위해서 사용
- 시스템 런타임, 환경 세팅 같은 정보 처럼 하나만 존재하는 인스턴스를 위해서

## 구현방법1. Private 생성자, static 필드 이용
```java
public class SingletonSettings implements Serializable {

    private static SingletonSettings instance;

    // 생성자로 인스턴스를 생성하지 못하도록 막는다.
    private SingletonSettings() {}

    public static SingletonSettings getInstance() {
        if (instance == null) { // 이 지점에 1번 스레드가 들어 왔을때 ContextSwithcing이 된다면 깨진다.
            instance = new SingletonSettings();
        }
        return instance;
    }
}
```
- 멀티스레드 상황에서 2개 이상의 인스턴스가 생길 수 있음.
- 리플렉션 직렬화를 이용하여 싱글톤이 아닌 인스턴스를 만들 수 있음.

<br>

## 구현방법2. synchronzied

```java
public class SingletonSettings2 {

    private static SingletonSettings2 instance;

    private SingletonSettings2() {}

    // 멀티스레드에서 싱글톤을 보장하기 위해서 synchronized 키워드를 붙인다.
    public static synchronized SingletonSettings2 getInstance() {
        if (instance == null) { // 이 지점에 1번 스레드가 들어 왔을때 ContextSwithcing이 된다면 깨진다.
            instance = new SingletonSettings2();
        }
        return instance;
    }
}
```

- 멀티스레드 상황에서 인스턴스가 여러개 생기는 것을 방지하기 위하여 `synchronzied` 키워드 사용
- getInstance메소드는 하나의 스레드만 접근 가능하기 때문에 성능이 조금 안좋아짐

## 구현방법3. eager loading

```java
public class SingletonSettings3 {

    // 이른 초기화 사용 하는 방법
    private static final SingletonSettings3 INSTANCE = new SingletonSettings3();

    // 생성자로 인스턴스를 생성하지 못하도록 막는다.
    private SingletonSettings3() {}

    public static synchronized SingletonSettings3 getInstance() {
        return INSTANCE;
    }
}
```
- 필드에 바로 인스턴스를 생성하는 방식
- 지연 초기화 불가능
- 스레드 안전

## 구현방법4. DoubleCheckedLock
```java
public class SingletonSettings4 {
    
    // 변수가 항상 메인 메모리에서 읽고 쓰도록 하는 키워드
    // CPU Cache에 있는 데이터가 메인 메모리에 무조건 동기화 하기 위하여 사용
    private static volatile SingletonSettings4 instance;

    // 생성자로 인스턴스를 생성하지 못하도록 막는다.
    private SingletonSettings4() {}

    
    public static SingletonSettings4 getInstance() {
        if (instance == null) {
            // 이 공간에 동시에 2개의 스레드가 들어 왔다 하더라도 생성에는 스레드가 1개만 들어간다.
            synchronized (SingletonSettings4.class ) {
                if (instance == null) { // 이 공간에는 무조건 한 개의 스레드가 도착
                    instance = new SingletonSettings4();
                }
            }
        }

        return instance;
    }
}
```
- `volatile`과 `synchronized`를 이용한 더블 잠금
- 인스턴스가 생성되기 전에만 함수 내부에 동기화 블록을 사용하므로 일반적인 `synchronized`기법 보다 빠름
- [voliatile 참고](https://nesoy.github.io/articles/2018-06/Java-volatile)

## 구현방법5. 중첩 클래스를 이용한 싱글톤 구현
```java
public class SingletonSettings5 {

    private SingletonSettings5() {}

    private static class SettingsHolder {
        private static final SingletonSettings5 INSTANCE = new SingletonSettings5();
    }

    // Lazy Loading 가능함.
    public static SingletonSettings5 getInstance() {
        return SettingsHolder.INSTANCE;
    }
}
```
- 중첩 클래스를 이용하여 구현
- getInstance가 호출되면 SettingsHolder가 클래스로더에 의하여 로딩됨
- 동기화 보장 가능

## 구현방법6. Enum을 이용한 구현
```java
public enum SingletonSettings6 {
    INSTANCE;
}
```
- `Enum`을 이용한 구현 방법
- 지연초기화 불가능
- 리플렉션, 직렬화, 역직렬화에도 안전하다고 함

## 싱글톤을 깨는 방법

- 리플랙션
- 직렬화 역직렬화

```java
public class BreakSingleTonTest {

    @Test
    void 리플랙션을_싱글톤_깨기() throws Exception {
        SingletonSettings settings1 = SingletonSettings.getInstance();
        
        // 리플랙션을 이용하여 생성자를 호출한다.
        Constructor<SingletonSettings> constructor = SingletonSettings.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        SingletonSettings settings2 = constructor.newInstance();

        Assertions.assertNotSame(settings1, settings2);
        System.out.printf("settings1 == settings2: %b%n", settings1 == settings2);
    }

    @Test
    void 직렬화_역직렬화_싱글톤_깨기 () throws Exception {
        SingletonSettings settings1 = SingletonSettings.getInstance();
        SingletonSettings settings2 = null;

        // 직렬화를 이용하여 객체를 생성한다.
        try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream("settings.obj"))) {
            out.writeObject(settings1);
        }

        try (ObjectInput in = new ObjectInputStream(new FileInputStream("settings.obj"))){
            settings2 = (SingletonSettings) in.readObject();
        }

        Assertions.assertNotSame(settings1, settings2);
        System.out.printf("settings1 == settings2: %b%n", settings1 == settings2);
    }
}
```