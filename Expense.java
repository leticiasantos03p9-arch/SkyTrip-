package com.skytrip;

import jakarta.persistence.Embeddable;

@Embeddable
public class Expense {
    private String description;
    private double amount;
    public Expense() {}
    public Expense(String description, double amount) { this.description = description; this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
