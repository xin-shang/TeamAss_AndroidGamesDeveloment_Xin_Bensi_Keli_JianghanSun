package com.mygdx.game.utils;

/**
 * @author Ke Li
 * */
public class LocationUtil {
    public static class Point<T> {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point<?> point = (Point<?>) o;

            if (!getX().equals(point.getX())) return false;
            return getY().equals(point.getY());
        }

        @Override
        public int hashCode() {
            int result = getX().hashCode();
            result = 31 * result + getY().hashCode();
            return result;
        }

        T x;
        T y;
        public Point(T x, T y) {
            this.x = x;
            this.y = y;
        }

        public T getX() {
            return x;
        }

        public T getY() {
            return y;
        }
    }


    public static Point<Float> toScreen(float x, float y) {
        return new Point<Float>(160 + x * 16, y * 16);
    }

    public static Point<Integer> toMap(float x, float y) {
        return new Point<Integer>((int)Math.floor((x - 160) / 16.0f), (int)Math.floor(y / 16.0f));
    }
}
