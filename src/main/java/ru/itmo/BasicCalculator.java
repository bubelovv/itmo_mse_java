package ru.itmo;

public class BasicCalculator implements Calculator {
    @Override
    public int sum(int x, int y) {
        int result = x + y;

        if ((x > 0 && y > 0 && result < 0) || (x < 0 && y < 0 && result > 0)) {
            throw new ArithmeticException("integer overflow");
        } else {
            return result;
        }
    }

    @Override
    public int sub(int x, int y) {
        int r = x - y;
        if ((x > 0 && y < 0 && r < 0) || (x < 0 && y > 0 && r > 0)) {
            throw new ArithmeticException("integer overflow");
        } else {
            return r;
        }
    }

    @Override
    public int mul(int x, int y) {
        long r = (long) x * (long) y;
        if (r > Integer.MAX_VALUE || r < Integer.MIN_VALUE) {
            throw new ArithmeticException("integer overflow");
        } else {
            return (int) r;
        }
    }

    @Override
    public double div(int x, int y) {
        if (y == 0) {
            throw new ArithmeticException("division by zero");
        }
        return (double) x / y;
    }
}