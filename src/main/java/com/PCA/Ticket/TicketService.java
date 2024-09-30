package com.PCA.Ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public List<Ticket> getByEventName(String eventName){
        return ticketRepository.findByEventName(eventName);
    }

    public boolean save(Ticket ticket) {
        if (ticket.getName() == null || ticket.getName().isEmpty()) {
            return false;
        } else {
            ticketRepository.save(ticket);
            return true;
        }
    }

    public List<Ticket> findAllList() {
        return ticketRepository.findAll();
    }

    public void delete(Ticket ticket){
        ticketRepository.delete(ticket);
    }

    public List<Ticket> findByUserId(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    public Ticket findById(Long ticketId) {
        for (Ticket ticket: ticketRepository.findAll()){
            if (ticket.getId().equals(ticketId)){
                return ticket;
            }
        }
        return null;

    }
}

