package com.PCA.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.PCA.Event.Event;
import com.PCA.Event.EventService;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;

    @RequestMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
                        Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        String hashedPassword = User.getSHA256(password);

        List<User> users = userService.findAllList();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(hashedPassword)) {
                    model.addAttribute("user", username);
                    session.setAttribute("userId", user.getId());
                    redirectAttributes.addFlashAttribute("userId", user.getId());
                    model.addAttribute("sessions", eventService.findAllList());
                    if (user.isAdmin()) {
                        return "redirect:/admin";
                    } else {
                        return "redirect:/user";
                    }
                } else {
                    model.addAttribute("errorMessage", "Contraseña incorrecta.");
                    return "error";
                }
            }
        }
        model.addAttribute("errorMessage", "Nombre de usuario no encontrado.");
        return "error";
    }

    @RequestMapping("/signUp")
    public String submitSignUpForm(@RequestParam("username") String username, @RequestParam("password") String password,
                                   Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        for (User user : userService.findAllList()) {
            if (user.getUsername().equals(username)) {
                model.addAttribute("errorMessage", "El nombre de usuario ya existe.");
                return "error";
            }
        }

        String hashedPassword = User.getSHA256(password);

        if (username.startsWith("admin") && password.length() >= 10) {
            User user = new User(username, hashedPassword, true, new ArrayList<>());
            userService.save(user);
            redirectAttributes.addFlashAttribute("userId", user.getId());
            model.addAttribute("user", username);
            model.addAttribute("sessions", eventService.findAllList());
            session.setAttribute("userId", user.getId());
            return "redirect:/admin";
        } else if (password.length() >= 10) {
            User user = new User(username, hashedPassword, false, new ArrayList<>());
            userService.save(user);
            redirectAttributes.addFlashAttribute("userId", user.getId());
            model.addAttribute("user", username);
            model.addAttribute("sessions", eventService.findAllList());
            session.setAttribute("userId", user.getId());
            return "redirect:/user";
        } else {
            model.addAttribute("errorMessage", "La contraseña debe tener al menos 10 caracteres.");
            return "error";
        }
    }


    @RequestMapping("/changePassword")
    public String changePassword(@RequestParam("username") String username,
                                 @RequestParam("password") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        for (User user : userService.findAllList()) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(User.getSHA256(oldPassword))) {
                    user.setPassword(User.getSHA256(newPassword));
                    userService.save(user);
                    session.setAttribute("userId", user.getId());
                    redirectAttributes.addFlashAttribute("userId", user.getId());
                    model.addAttribute("user", username);
                    model.addAttribute("sessions", eventService.findAllList());

                    if (user.isAdmin()) {
                        return "redirect:/admin";
                    } else {
                        return "redirect:/user";
                    }
                } else {
                    model.addAttribute("errorMessage", "Contraseña antigua incorrecta.");
                    return "error";
                }
            }
        }

        model.addAttribute("errorMessage", "Usuario no encontrado.");
        return "error";
    }

    @GetMapping({"/admin", "/user"})
    public String showAdminOrUserPage(Model model, HttpSession session, HttpServletRequest request) throws IOException {

        Long userId = (Long) session.getAttribute("userId");
        List<Event> events = eventService.findAllList();

        for (Event event : events) {
            if (event.getImage() != null) {
                byte[] imageBytes = event.getImage();
                String base64encodedImageData = Base64.getEncoder().encodeToString(imageBytes);
                event.setBase64Image(base64encodedImageData);
            }
        }

        model.addAttribute("user", userService.findById(userId).getUsername());
        model.addAttribute("sessions", events);

        String requestURI = request.getRequestURI();
        if (requestURI.equals("/admin")) {
            return "admin";
        } else {
            return "user";
        }
    }



}

