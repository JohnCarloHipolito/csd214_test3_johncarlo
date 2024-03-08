package com.triosstudent.csd214_test3_johncarlo;

public class PizzaOrder {
    private Long id;
    private String customerName;
    private String mobileNumber;
    private String pizzaSize;
    private Integer noOfToppings;
    private Double totalBill;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPizzaSize() {
        return pizzaSize;
    }

    public void setPizzaSize(String pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    public Integer getNoOfToppings() {
        return noOfToppings;
    }

    public void setNoOfToppings(Integer noOfToppings) {
        this.noOfToppings = noOfToppings;
    }

    public Double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(Double totalBill) {
        this.totalBill = totalBill;
    }
}
