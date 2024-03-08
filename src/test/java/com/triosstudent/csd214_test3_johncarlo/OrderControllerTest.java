package com.triosstudent.csd214_test3_johncarlo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderControllerTest {

    @Test
    public void calculateTotalBill_smallSizeNoToppings_returnsCorrectTotal() {
        OrderController controller = new OrderController();
        double result = controller.calculateTotalBill("S", 0);
        assertEquals(9.2, result);
    }

    @Test
    public void calculateTotalBill_mediumSizeWithToppings_returnsCorrectTotal() {
        OrderController controller = new OrderController();
        double result = controller.calculateTotalBill("M", 2);
        assertEquals(14.95, result);
    }

    @Test
    public void calculateTotalBill_largeSizeWithToppings_returnsCorrectTotal() {
        OrderController controller = new OrderController();
        double result = controller.calculateTotalBill("L", 3);
        assertEquals(18.97, result);
    }

    @Test
    public void calculateTotalBill_extraLargeSizeNoToppings_returnsCorrectTotal() {
        OrderController controller = new OrderController();
        double result = controller.calculateTotalBill("XL", 0);
        assertEquals(17.25, result);
    }

    @Test
    public void calculateTotalBill_invalidSize_throwsIllegalArgumentException() {
        OrderController controller = new OrderController();
        assertThrows(IllegalArgumentException.class, () -> controller.calculateTotalBill("XXL", 2));
    }

    @Test
    public void calculateTotalBill_negativeToppings_throwsIllegalArgumentException() {
        OrderController controller = new OrderController();
        assertThrows(IllegalArgumentException.class, () -> controller.calculateTotalBill("S", -1));
    }
}