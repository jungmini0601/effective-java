# 인스턴스화를 막으려거든 private 생성자를 사용하라.

```java
public class UtilClass {
    private UtilClass() {
        throw new AssertionError();
    }
    // 생략
}
```

- 인스턴스화를 막기 위해서는 private 생성자를 이용하면 된다. (싱글톤의 대표적인 구현 방법중 하나)
- 생성자 내부에서 AssertionError를 던지는 이유는 UtilClass 내부에서 실수로 인스턴스를 생성하는 것을 방지하기 위함이다.