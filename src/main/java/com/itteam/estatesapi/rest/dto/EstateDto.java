package com.itteam.estatesapi.rest.dto;

public class EstateDto {
    private String id;
    private String title;
    private String poster;
    private String description;
    private String contact;
    private int price;
    private String address;
    private String createdAt;

    public EstateDto(String id, String title, String poster, String description, String contact, int price, String address, String createdAt) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.description = description;
        this.contact = contact;
        this.price = price;
        this.address = address;
        this.createdAt = createdAt;
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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
