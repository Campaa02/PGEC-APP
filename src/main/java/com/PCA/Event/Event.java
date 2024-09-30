package com.PCA.Event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Event {

    @Id
    private String name;
    private String description;
    private String date;
    private String address;
    private float ticketPrice;
    private int availableTickets;

    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image; // o String para la ruta del archivo
    @Transient
    private String base64Image;


    public Event(String name, String description, String date, String address, float ticketPrice, int availableTickets) {
        super();
        this.name = name;
        this.description = description;
        this.date = date;
        this.address = address;
        this.ticketPrice = ticketPrice;
        this.availableTickets = availableTickets;
    }

    public Event(Event event) {
        super();
        this.name = event.getName();
        this.description = event.getDescription();
        this.date = event.getDate();
        this.address = event.getAddress();
        this.ticketPrice = event.getTicketPrice();
        this.availableTickets = event.getAvailableTickets();
        this.image = event.getImage();
        this.base64Image = event.getBase64Image();
    }


}

