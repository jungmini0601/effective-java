# 생성자에 매개변수가 많다면 빌더를 고려하라.

## 정적 팩토리 메소드와 생성자의 단점

- 선택적 매개변수가 많을 때 적절히 대응하기 어렵다.

## 점층적 생성자 패턴
```java
public class NutrionFacts {
    private final int servingSize;  // 필수 
    private final int servings;     // 필수
    private final int calories;     // 선택
    private final int fat;          // 선택
    private final int sodium;       // 선택
    private final int carbohydrate; // 선택
    
    public NutrionFacts(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }
    
    public NutrionFacts(int servingSize, int servings, int calories) {
        this(servingSize, servings, calories, 0);
    }
    
    public NutrionFacts(int servingSize, int servings, int calories, int fat) {
        this(servingSize, servings, calories, fat, 0);
    }
    
    public NutrionFacts(int servingSize, int servings, int calories, int fat, int sodium) {
        this(servingSize, servings, calories, fat, sodium, 0);
    }

    public NutrionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }
}
```
<br>

위 처럼 생성자를 여러개 생성하는 방식을 `점층정 생성자 패턴`이라고 부른다.  
매개변수가 많을수록 사용하기 까다롭다.

## 자바 빈즈 패턴

```java
public class NutrionFacts {
    private int servingSize;
    private int servings;
    private int calories;
    private int fat;
    private int sodium;
    private int carbohydrate;

    public int getServingSize() {
        return servingSize;
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getSodium() {
        return sodium;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public int getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public static void main(String[] args) {
        NutrionFacts coke = new NutrionFacts();
        // 객체 세팅중
        coke.setServings(240);
        coke.setServings(8);
        coke.setServings(100);
        coke.setServings(35);
        coke.setServings(27);
        // 여기까지 객체는 불안전한 상태를 가지게 된다.
    }
}

```
자바빈즈 패턴을 사용하면 어떤 생성자를 골라야 하는지 복잡한 어려움을 줄일 수 있다.  
다만, 객체 하나를 만들면서 여러 메소드를 호출하며 객체가 불안전한 상태가 될 수 있다는 점이 문제가 된다.  
자바빈즈 패턴을 사용하면 클래스를 불변으로 만들 수도 없다.

## 빌더 패턴
```java
package org.example.ch2;

public class NutrionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;
    
    private NutrionFacts(Builder builder) {
        this.servings     = builder.servings;
        this.servingSize  = builder.servingSize;
        this.calories     = builder.calories;
        this.fat          = builder.fat;
        this.sodium       = builder.sodium;
        this.carbohydrate = builder.carbohvdrate;
    }
    
    static class Builder {
        private final int servingSize;
        private final int servings;
        
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohvdrate = 0;
        
        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings    = servings;
        }
        
        public Builder calories(int calories) {
            this.calories = calories;
            return this;
        }

        public Builder fat(int fat) {
            this.fat = fat;
            return this;
        }

        public Builder sodium(int sodium) {
            this.sodium = sodium;
            return this;
        }

        public Builder carbohvdrate(int carbohvdrate) {
            this.carbohvdrate = carbohvdrate;
            return this;
        }
        
        public NutrionFacts build() {
            return new NutrionFacts(this);
        }
    }

    public static void main(String[] args) {
        NutrionFacts n = new Builder(24, 2)
                .calories(12)
                .sodium(144)
                .build();
    }
}

```

- Builder 패턴을 이용하면 메소드 체이닝을 이용하여 캡슐화가 잘 지켜진 객체 생성 방식을 사용할 수 있다.
- 추가적으로 Java에서는 lombok라이브러리를 사용하면 손쉽게 빌더 패턴을 구현 가능하다.
<br>

# 계층 구조에서 빌더 패턴
```java


public abstract class Pizza {
    public enum Topping {HAM, MUSHROOM, ONION, PEPPER, SAUSAGE}
    final Set<Topping> toppings;

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);
        public T addTopping(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }

        abstract Pizza build();

        protected abstract T self();
    }

    Pizza(Builder<?> builder) {
        toppings = builder.toppings.clone();
    }
}

public class NyPizza extends Pizza {
    public enum Size {SMALL, MEDIUM, LARGE}
    private final Size size;

    public static class Builder extends Pizza.Builder<Builder> {
        private final Size size;

        public Builder(Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override
        public NyPizza build() {
            return new NyPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private NyPizza(Builder builder) {
        super(builder); // 부모 생성자의 빌더를 이용하여 초기화 할 수 있음.
        size = builder.size;
    }

    public static void main(String[] args) {
        NyPizza build = new Builder(Size.SMALL).addTopping(Topping.PEPPER).addTopping(Topping.ONION).build();
    }
}


```

- 추상 클래스를 이용하여 빌더를 재사용 가능하다.
- 상위 클래스 Builder를 하위 클래스 Builder에서 상속을 받는다.
- 상위 클래스에 들어있는 필드는 상위 클래스의 생성자에 builder를 넘겨 초기화 한다.
- 하위 클래스에서 나머지 필드를 초기화 하는 방식으로 빌더를 재사용 가능하다.