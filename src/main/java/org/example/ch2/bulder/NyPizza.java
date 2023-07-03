package org.example.ch2.bulder;

import java.util.Objects;

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
