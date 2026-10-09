package com.skytrip;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Trip {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String destination;
    private String startDate;
    private String endDate;
    private int travelers;
    @ElementCollection
    private List<String> activities = new ArrayList<>();
    @ElementCollection
    private List<Expense> expenses = new ArrayList<>();

    public Trip() {}
    public Long getId() { return id; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public int getTravelers() { return travelers; }
    public void setTravelers(int travelers) { this.travelers = travelers; }
    public List<String> getActivities() { return activities; }
    public void setActivities(List<String> activities) { this.activities = activities == null ? new ArrayList<>() : activities; }
    public List<Expense> getExpenses() { return expenses; }
    public void setExpenses(List<Expense> expenses) { this.expenses = expenses == null ? new ArrayList<>() : expenses; }
}
