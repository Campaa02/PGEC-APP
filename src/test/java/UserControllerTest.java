import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.PCA.Event.EventService;
import com.PCA.User.User;
import com.PCA.User.UserController;
import com.PCA.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private EventService eventService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User createUser(String username, String password, Long id) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(User.getSHA256(password));
        user.setId(id);
        return user;
    }

    @Test
    public void testLoginWithValidCredentials() {
        User user = createUser("validUser", "validPassword", 1L);
        when(userService.findAllList()).thenReturn(Collections.singletonList(user));

        String viewName = userController.login("validUser", "validPassword", model, session, redirectAttributes);

        assertEquals("redirect:/user", viewName);
        verify(session).setAttribute("userId", user.getId());
        verify(redirectAttributes).addFlashAttribute("userId", user.getId());
    }

    @Test
    public void testLoginWithInvalidPassword() {
        User user = createUser("validUser", "validPassword", 1L);
        when(userService.findAllList()).thenReturn(Collections.singletonList(user));

        String viewName = userController.login("validUser", "invalidPassword", model, session, redirectAttributes);

        assertEquals("error", viewName);
        verify(model).addAttribute("errorMessage", "Contraseña incorrecta.");
    }

    @Test
    public void testLoginWithNonexistentUser() {
        when(userService.findAllList()).thenReturn(Collections.emptyList());

        String viewName = userController.login("nonexistentUser", "password", model, session, redirectAttributes);

        assertEquals("error", viewName);
        verify(model).addAttribute("errorMessage", "Nombre de usuario no encontrado.");
    }

    @Test
    public void testSubmitSignUpFormWithExistingUsername() {
        User user = createUser("existingUser", "password", 1L);
        when(userService.findAllList()).thenReturn(Collections.singletonList(user));

        String viewName = userController.submitSignUpForm("existingUser", "password", model, session, redirectAttributes);

        assertEquals("error", viewName);
        verify(model).addAttribute("errorMessage", "El nombre de usuario ya existe.");
    }

    @Test
    public void testSubmitSignUpFormWithValidData() {
        when(userService.findAllList()).thenReturn(Collections.emptyList());
        when(userService.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return true;
        });

        String viewName = userController.submitSignUpForm("newUser", "validPassword", model, session, redirectAttributes);

        assertEquals("redirect:/user", viewName);
        verify(userService).save(any(User.class));
        verify(session).setAttribute("userId", 1L);
        verify(redirectAttributes).addFlashAttribute("userId", 1L);
    }

    @Test
    public void testSubmitSignUpFormWithShortPassword() {
        String viewName = userController.submitSignUpForm("newUser", "short", model, session, redirectAttributes);
        assertEquals("error", viewName);
        verify(model).addAttribute("errorMessage", "La contraseña debe tener al menos 10 caracteres.");
    }

    @Test
    public void testChangePasswordWithValidData() {
        User user = createUser("validUser", "oldPassword", 1L);
        when(userService.findAllList()).thenReturn(Collections.singletonList(user));

        String viewName = userController.changePassword("validUser", "oldPassword", "newPassword", model, session, redirectAttributes);

        assertEquals("redirect:/user", viewName);
        verify(userService).save(user);
        verify(session).setAttribute("userId", user.getId());
        verify(redirectAttributes).addFlashAttribute("userId", user.getId());
    }

    @Test
    public void testChangePasswordWithInvalidOldPassword() {
        User user = createUser("validUser", "oldPassword", 1L);
        when(userService.findAllList()).thenReturn(Collections.singletonList(user));

        String viewName = userController.changePassword("validUser", "invalidOldPassword", "newPassword", model, session, redirectAttributes);

        assertEquals("error", viewName);
        verify(model).addAttribute("errorMessage", "Contraseña antigua incorrecta.");
    }

    @Test
    public void testChangePasswordWithNonexistentUser() {
        when(userService.findAllList()).thenReturn(Collections.emptyList());

        String viewName = userController.changePassword("nonexistentUser", "oldPassword", "newPassword", model, session, redirectAttributes);

        assertEquals("error", viewName);
        verify(model).addAttribute("errorMessage", "Usuario no encontrado.");
    }

    @Test
    public void testShowAdminOrUserPageAsAdmin() throws Exception {
        Long userId = 1L;
        User user = createUser("adminUser", "password", userId);
        user.setAdmin(true);
        when(session.getAttribute("userId")).thenReturn(userId);
        when(userService.findById(userId)).thenReturn(user);
        when(eventService.findAllList()).thenReturn(Collections.emptyList());
        when(request.getRequestURI()).thenReturn("/admin");

        String viewName = userController.showAdminOrUserPage(model, session, request);

        assertEquals("admin", viewName);
        verify(model).addAttribute("user", user.getUsername());
        verify(model).addAttribute("sessions", Collections.emptyList());
    }

    @Test
    public void testShowAdminOrUserPageAsUser() throws Exception {
        Long userId = 1L;
        User user = createUser("regularUser", "password", userId);
        user.setAdmin(false);
        when(session.getAttribute("userId")).thenReturn(userId);
        when(userService.findById(userId)).thenReturn(user);
        when(eventService.findAllList()).thenReturn(Collections.emptyList());
        when(request.getRequestURI()).thenReturn("/user");

        String viewName = userController.showAdminOrUserPage(model, session, request);

        assertEquals("user", viewName);
        verify(model).addAttribute("user", user.getUsername());
        verify(model).addAttribute("sessions", Collections.emptyList());
    }
}
