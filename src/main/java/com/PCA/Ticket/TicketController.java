package com.PCA.Ticket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.PCA.Event.Event;
import com.PCA.Event.EventService;
import com.PCA.User.User;
import com.PCA.User.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class TicketController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;



    @GetMapping("/tickets")
    public String showTickets(Model model, @RequestParam(required = false) String name) {

        model.addAttribute("sessions", eventService.findAllList());

        if (name == null || name.equals("Todos")) {
            model.addAttribute("tickets", ticketService.findAllList());
            return "showTickets";
        }

        List<Ticket> tickets = ticketService.getByEventName(name);
        if (tickets.isEmpty()) {
            model.addAttribute("errorMessage", "No hay tickets para el evento seleccionado.");
            return "error";
        }

        model.addAttribute("tickets", tickets);
        return "showTickets";
    }


    @RequestMapping("/showMyTickets")
    public String showMyTickets(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Long ticketId = (Long) session.getAttribute("ticketId");
            List<Ticket> userTickets = ticketService.findByUserId(userId);
            if (userTickets.isEmpty()) {
                model.addAttribute("errorMessage", "No has comprado ningún ticket.");
                return "error";
            }
            model.addAttribute("userTickets", userTickets);
            model.addAttribute("ticketId", ticketId);
            model.addAttribute("sessions", eventService.findAllList());
            return "showMyTickets";
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para ver tus tickets.");
            return "redirect:/error";
        }
    }


    @GetMapping("/buyTickets")
    public String buyTickets(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            model.addAttribute("errorMessage", "Debes iniciar sesión para comprar tickets.");
            return "redirect:/error";
        }
        model.addAttribute("sessions", eventService.findAllList());
        model.addAttribute("tickets", ticketService.findAllList());

        return "buyTickets";
    }

    @PostMapping("/tickets/add")
    public String addTicket(@RequestParam("name") String name, @RequestParam("email") String email,
                            @RequestParam("phone") long phone,
                            @RequestParam("type") String type, @RequestParam("num") int num, @RequestParam("event") String event,
                            HttpSession session,
                            Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Event aux = eventService.getByName(event).stream().findFirst().orElse(null);
            if (aux == null) {
                model.addAttribute("errorMessage", "El evento no existe.");
                return "error";
            }
            float totalPrice = aux.getTicketPrice() * num;

            // Guardar los datos del ticket en la base de datos
            User user = userService.findById(userId);
            if (user != null && aux.getAvailableTickets() >= num) {
                aux.setAvailableTickets(aux.getAvailableTickets() - num);
                Ticket ticket = new Ticket(name, email, phone, num, type, aux, user);
                ticketService.save(ticket);
                session.setAttribute("ticketId", ticket.getId());
                user.addTickets(ticket);
                eventService.save(aux);
                userService.save(user);

                model.addAttribute("name", name);
                model.addAttribute("email", email);
                model.addAttribute("phone", phone);
                model.addAttribute("num", num);
                model.addAttribute("type", type);
                model.addAttribute("event", event);
                model.addAttribute("totalPrice", totalPrice);
                model.addAttribute("ticketPrice", aux.getTicketPrice());
                model.addAttribute("sessions", eventService.findAllList());
                model.addAttribute("tickets", ticketService.findAllList());

                return "ticketConfirmation";
            } else {
                model.addAttribute("errorMessage", "No hay suficientes tickets disponibles.");
                return "error";
            }
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para comprar tickets.");
            return "error";
        }
    }

    @RequestMapping("/returnTickets")
    public String returnTicket(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {

            List<Ticket> userTickets = ticketService.findByUserId(userId);

            if (userTickets.isEmpty()) {
                model.addAttribute("errorMessage", "No tienes tickets para devolver.");
                return "error";
            }

            model.addAttribute("userTickets", userTickets);

            return "returnTicket";
        } else {
            model.addAttribute("errorMessage", "Debes iniciar sesión para devolver tickets.");
            return "error";
        }
    }

    @RequestMapping("/ticket/return")
    public String deleteTicket(@RequestParam("ticketId")Long ticketId,
                                Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {

            Ticket ticketToDelete = ticketService.findById(ticketId);
            if (ticketToDelete == null ) {
                model.addAttribute("errorMessage", "El ticket que intentas devolver no existe.");
                return "error";
            }

            String name = ticketToDelete.getEvent().getName();
            Event aux = new Event(eventService.getByName(name).get(0));
            int recovered = aux.getAvailableTickets() + ticketToDelete.getNum();
            aux.setAvailableTickets(recovered);
            eventService.save(aux);
            ticketService.delete(ticketToDelete);
            List<Ticket> userTickets = ticketService.findByUserId(userId);
            model.addAttribute("userTickets", userTickets);


            return "returnTicket";
        } else {

            model.addAttribute("errorMessage", "Debes iniciar sesión para devolver tickets.");
            return "error";
        }
    }
}
