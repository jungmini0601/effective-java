package org.example.ch2.bulder;

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
