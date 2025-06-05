package com.example.webstorydemo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLevel {
    private Integer currentExp;
    private Level level;

    public int getProgressToNextLevel() {
        int prev = level.getPrevExp();
        int next = level.getNextExp();
        return (int) (((double) (currentExp - prev) / (next - prev)) * 100);
    }
    public static Level calculateLevel(int exp) {
        for (Level level : Level.values()) {
            if (exp >= level.getPrevExp() && exp < level.getNextExp()) {
                return level;
            }
        }
        return Level.LEVEL9; // fallback
    }


    @Getter
    public enum Level {
        LEVEL1(0, 100),
        LEVEL2(100, 300),
        LEVEL3(300, 600),
        LEVEL4(600, 1000),
        LEVEL5(1000, 1500),
        LEVEL6(1500, 2100),
        LEVEL7(2100, 2800),
        LEVEL8(2800, 3600),
        LEVEL9(3600, Integer.MAX_VALUE);
        private final Integer prevExp;
        private final Integer nextExp;
        Level(Integer prevExp, Integer nextExp) {
            this.prevExp = prevExp;
            this.nextExp = nextExp;
        }
    }

}
