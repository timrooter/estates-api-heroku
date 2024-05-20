package com.itteam.estatesapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "estates")
public class Estate {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    private String title;
    private String poster;

    @Column(columnDefinition = "TEXT")
    private String description;
    private String contact;
    private int price;
    private String address;

    private ZonedDateTime createdAt;

    public Estate(String title, String poster, String description, String contact, int price, String address) {
        this.title = title;
        this.poster = poster;
        this.description = description;
        this.contact = contact;
        this.price = price;
        this.address = address;
    }
    @PrePersist
    public void onPrePersist() {
        createdAt = ZonedDateTime.now();
    }
}
