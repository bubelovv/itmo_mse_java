package ru.itmo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new BasicCalculator();
    }

    @Test
    public void testSum() {
        assertEquals(5, calculator.sum(2, 3));
    }

    @Test
    public void testSub() {
        assertEquals(-1, calculator.sub(2, 3));
    }

    @Test
    public void testMul() {
        assertEquals(6, calculator.mul(2, 3));
    }

    @Test
    public void testDiv() {
        assertEquals(2.0, calculator.div(6, 3));
    }

    @Test
    public void testDivByZero() {
        assertThrows(ArithmeticException.class, () -> calculator.div(6, 0));
    }

    @Test
    public void testOverflowSum() {
        assertThrows(ArithmeticException.class, () -> calculator.sum(Integer.MAX_VALUE, 1));
    }

    @Test
    public void testOverflowSub() {
        assertThrows(ArithmeticException.class, () -> calculator.sub(Integer.MIN_VALUE, 1));
    }

    @Test
    public void testOverflowMul() {
        assertThrows(ArithmeticException.class, () -> calculator.mul(Integer.MAX_VALUE, 2));
    }
}