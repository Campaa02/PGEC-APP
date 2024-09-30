package com.PCA.Others;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.PCA.Event.Event;
import com.PCA.Event.EventService;
import com.PCA.Ticket.TicketService;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private EventService eventService;


    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<Event> events = eventService.findAllList();
        for (Event event : events) {
            if (event.getImage() != null) {
                byte[] imageBytes = event.getImage();
                String base64encodedImageData = Base64.getEncoder().encodeToString(imageBytes);
                event.setBase64Image(base64encodedImageData);
            }
        }
        model.addAttribute("sessions", events);
        return "home";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signUp")
    public String signUp() {
        return "signUp";
    }

    @GetMapping("/changePassword")
    public String changePassword() {
        return "changePassword";
    }




}
