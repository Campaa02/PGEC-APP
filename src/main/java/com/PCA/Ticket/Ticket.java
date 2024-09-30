package com.PCA.Ticket;

import com.PCA.Event.Event;
import com.PCA.User.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(name = "email")
    private String email;
    private Long phone;
    private String type;
    private int num;
    @ManyToOne
    @JoinColumn(name = "event", referencedColumnName = "name")
    private Event event;
    private float ticketPrice;
    private float totalPrice;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    public Ticket(String name, String email, Long phone, int num, String type, Event event, User user) {
        super();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.num = num;
        this.type = type;
        this.event = event;
        this.ticketPrice = event.getTicketPrice();
        this.totalPrice = ticketPrice * num;
        this.user = user;
    }

}
