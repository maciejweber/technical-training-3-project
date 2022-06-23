package com.example.itadvertsapp;

public class ModelPost {
    String id, title, company, category, salary_from, salary_to, location, formatted_date;

    public ModelPost(String id, String title, String company, String category, String salary_from, String salary_to, String location, String formatted_date) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.category = category;
        this.salary_from = salary_from;
        this.salary_to = salary_to;
        this.location = location;
        this.formatted_date = formatted_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSalary_from() {
        return salary_from;
    }

    public void setSalary_from(String salary_from) {
        this.salary_from = salary_from;
    }

    public String getSalary_to() {
        return salary_to;
    }

    public void setSalary_to(String salary_to) {
        this.salary_to = salary_to;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFormatted_date() {
        return formatted_date;
    }

    public void setFormatted_date(String formatted_date) {
        this.formatted_date = formatted_date;
    }
}
