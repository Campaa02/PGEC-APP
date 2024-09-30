package com.PCA.Event;

import com.PCA.Others.DateValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.PCA.Ticket.TicketService;



@Controller
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private TicketService ticketService;


    @GetMapping("/createEvent")
    public String createEvent(Model model) {
        return "createEvent";
    }

    @PostMapping("/events/add")
    public String addEvent(@RequestParam("name") String name, @RequestParam("description") String description,
                            @RequestParam("date") String date, @RequestParam("address") String address,
                            @RequestParam("ticketPrice") float ticketPrice, @RequestParam("image") MultipartFile image,
                           @RequestParam("availableTickets") int availableTickets,
                           Model model) throws IOException {
        if (!DateValidator.isDateValid(date)) {
            model.addAttribute("errorMessage", "La fecha del evento debe ser igual o posterior a la fecha actual.");
            return "error";
        }
        Event event = new Event(name, description, date, address, ticketPrice, availableTickets);

        if (imageNotValid(image, model, event)) {
            model.addAttribute("errorMessage", "La imagen no es válida (jpg/jpeg/png) o está vacía.");
            return "error";
        }
        model.addAttribute("name", name);
        model.addAttribute("description", description);
        model.addAttribute("date", date);
        model.addAttribute("address", address);
        model.addAttribute("ticketPrice", ticketPrice);
        model.addAttribute("availableTickets", availableTickets);
        return "eventConfirmation";
    }

    private boolean imageNotValid(@RequestParam("image") MultipartFile image, Model model, Event event) throws IOException {
        if (image != null || !image.isEmpty()){
            String contentType = image.getContentType();
        if (contentType == null || (contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/jpg"))) {
            byte[] imageBytes = image.getBytes();
            event.setImage(imageBytes);
            String base64encodedImageData = Base64.getEncoder().encodeToString(imageBytes);
            model.addAttribute("image", base64encodedImageData);

        }
        else{
            return true;
        }

        eventService.save(event);
        return false;
    }
        return true;
    }

    @GetMapping("/preEditEvent")
    public String preEditEvent(Model model) {
        model.addAttribute("sessions", eventService.findAllList());
        return "preEditEvent";
    }

    @GetMapping("/editEvent")
    public String editEvent(@RequestParam String name, Model model) {
        model.addAttribute("sessions", eventService.getByName(name));
        model.addAttribute("tickets", ticketService.getByEventName(name));
        return "editEvent";
    }

    @RequestMapping("/events/edit")
    @PutMapping("/events/edit")
    public String editEvent(@RequestParam("description") String description,
                            @RequestParam("date") String date, @RequestParam("address") String address,
                            @RequestParam("ticketPrice") float ticketPrice,
                            @RequestParam("name") String event_name,
                            @RequestParam("image") MultipartFile image,
                            @RequestParam("availableTickets") int availableTickets,
                            Model model) throws IOException {

        Event event = eventService.getByName(event_name).get(0);
        event.setDescription(description);
        if (!DateValidator.isDateValid(date)) {
            model.addAttribute("errorMessage", "La fecha del evento debe ser igual o posterior a la fecha actual.");
            return "error";
        }
        event.setDate(date);
        event.setAddress(address);
        event.setTicketPrice(ticketPrice);
        event.setAvailableTickets(availableTickets);
        if (imageNotValid(image, model, event)) {
            model.addAttribute("errorMessage", "La imagen no es válida (jpg/jpeg/png) o está vacía.");
            return "error";
        }

        model.addAttribute("name", event_name);
        model.addAttribute("description", description);
        model.addAttribute("date", date);
        model.addAttribute("address", address);
        model.addAttribute("ticketPrice", ticketPrice);
        model.addAttribute("availableTickets", event.getAvailableTickets());
        return "eventConfirmation";

    }

    @GetMapping("/deleteEvent")
    public String deleteEventPage(Model model) {
        model.addAttribute("sessions", eventService.findAllList());
        return "deleteEvent";
    }

    @DeleteMapping("/events/delete")
    public String deleteEvent(@RequestParam("name") String eventName, Model model) {

        Event event = eventService.findByName(eventName);

        if (event == null) {
            model.addAttribute("errorMessage", "El evento no existe.");
            return "error";
        }

        if (ticketService.getByEventName(event.getName()).isEmpty()) {
            eventService.deleteEvent(event);
            return "eventDeleted";
        } else {
            model.addAttribute("errorMessage", "No se puede eliminar el evento porque tiene tickets asociados.");
            return "error";
        }
    }

}

