# 생성자 대신 정적 팩터리 메서드를 고려하라.

## 정적 팩토리 메소드란?
```java
public static Boolean valueOf(boolean b) {
    return b ? Boolean.TRUE : Boolean.FALSE
}
```
여기서 말하는 정적 팩토리 메소드란 `클래스의 인스턴스를 반환`하는 `정적 메소드`를 말한다.  

### 장점 1. 이름을 가질 수 있다.
```java
public static BigInteger probablePrime(int bitLength, Random rnd) 
    
BigInteger(int bitLength, Random rnd)
```
생성자보다 더 명확한 이름을 줄 수 있어서 어떤 인스턴스가 생길지 설명할 기회가 생긴다.  

### 장점 2. 시그니처가 같은 객체 생성방식 지원 가능
```java
// 예시 1. 불가능
BigInteger(int bitLength, Random rnd)
BigInteger(int bitLength, Random rnd)

// 가능
public static BigInteger probablePrime(int bitLength, Random rnd)
public static BigInteger probablePrime2(int bitLength, Random rnd)
```

### 장점 3. 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.
```java
public static Boolean valueOf(boolean b) {
    return (b ? TRUE : FALSE);
}
```
Boolean 클래스를 살펴보면 인스턴스를 캐싱하여 재활용하는 방식을 사용한다.  
불필요한 객체 생성을 줄일 수 있어서 성능을 상당히 끌어 올려준다.

### 장점 4. 반환 타입의 하위 타입 객체를 반환할 수 있다.
```java
static <E> List<E> of() {
    return (List<E>) ImmutableCollections.EMPTY_LIST;
}


static final class ListN<E> extends AbstractImmutableList<E> implements Serializable 
```
대표적으로 List 인터페이스의 정적 팩토리 메소드를 살펴보자.  
정적 팩토리의 of 메소드를 통해서 생성된 인스턴스는 ImmutableCollections의 EMPTY_LIST를 반환한다.  
하지만 of의 시그니처가 List로 되어 있기 때문에 개발자는 내부 구현에 상관없이 인스턴스를 사용할 수 있다.  
API 제공자는 버전을 업데이트 하면서 새로운 인스턴스를 제공하여 교체 할 수 있다.

### 단점 1. 정적 팩토리 메소드만 제공하면 상속을 하기가 애매하다.
상속을 하기위해서는 protected이상의 생성자가 필요하다.  
정적 팩터리 메소드만 제공하면 상속이 불가능하다.

### 단점 2. 정적 팩토리 메소드는 프로그래머가 찾기 어렵다.
정적 메소드는 다양한 이름을 가질 수 있다.  
이 정적 메소드가 정적 팩토리 메소드인지 파악한느 품이든다.  
일반적으로 위와 같은 단점은 널리 알려진 이름을 사용하는 방법으로 해결한다.