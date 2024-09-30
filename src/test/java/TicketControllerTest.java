
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.PCA.Event.Event;
import com.PCA.Event.EventService;
import com.PCA.Ticket.Ticket;
import com.PCA.Ticket.TicketController;
import com.PCA.Ticket.TicketService;
import com.PCA.User.User;
import com.PCA.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TicketControllerTest {

    @InjectMocks
    private TicketController ticketController;

    @Mock
    private TicketService ticketService;

    @Mock
    private EventService eventService;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowTicketsWithName() {
        String name = "EventName";
        List<Ticket> tickets = Collections.singletonList(new Ticket());
        when(eventService.findAllList()).thenReturn(Collections.emptyList());
        when(ticketService.getByEventName(name)).thenReturn(tickets);

        String viewName = ticketController.showTickets(model, name);

        assertEquals("showTickets", viewName);
        verify(model).addAttribute("sessions", Collections.emptyList());
        verify(model).addAttribute("tickets", tickets);
    }

    @Test
    public void testShowTicketsWithoutName() {
        when(eventService.findAllList()).thenReturn(Collections.emptyList());
        when(ticketService.findAllList()).thenReturn(Collections.emptyList());

        String viewName = ticketController.showTickets(model, null);

        assertEquals("showTickets", viewName);
        verify(model).addAttribute("sessions", Collections.emptyList());
        verify(model).addAttribute("tickets", Collections.emptyList());
    }

    @Test
    public void testShowMyTicketsWithUserId() {
        Long userId = 1L;
        when(session.getAttribute("userId")).thenReturn(userId);
        when(ticketService.findByUserId(userId)).thenReturn(Collections.emptyList());

        String viewName = ticketController.showMyTickets(model, session);

        assertEquals("error", viewName);
        verify(model).addAttribute("errorMessage", "No has comprado ningún ticket.");
    }

    @Test
    public void testShowMyTicketsWithoutUserId() {
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = ticketController.showMyTickets(model, session);

        assertEquals("redirect:/error", viewName);
        verify(model).addAttribute("errorMessage", "Debes iniciar sesión para ver tus tickets.");
    }

    @Test
    public void testBuyTicketsWithUserId() {
        Long userId = 1L;
        when(session.getAttribute("userId")).thenReturn(userId);
        when(eventService.findAllList()).thenReturn(Collections.emptyList());
        when(ticketService.findAllList()).thenReturn(Collections.emptyList());

        String viewName = ticketController.buyTickets(model, session);

        assertEquals("buyTickets", viewName);
        verify(model).addAttribute("sessions", Collections.emptyList());
        verify(model).addAttribute("tickets", Collections.emptyList());
    }

    @Test
    public void testBuyTicketsWithoutUserId() {
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = ticketController.buyTickets(model, session);

        assertEquals("redirect:/error", viewName);
        verify(model).addAttribute("errorMessage", "Debes iniciar sesión para comprar tickets.");
    }

    @Test
    public void testAddTicketWithValidUser() {
        Long userId = 1L;
        String eventName = "EventName";
        Event event = new Event();
        event.setName(eventName);
        event.setTicketPrice(100);
        event.setAvailableTickets(10);
        User user = new User();
        when(session.getAttribute("userId")).thenReturn(userId);
        when(eventService.getByName(eventName)).thenReturn(Collections.singletonList(event));
        when(userService.findById(userId)).thenReturn(user);

        String viewName = ticketController.addTicket("Name", "email@example.com", 1234567890L, "Type", 2, eventName, session, model);

        assertEquals("ticketConfirmation", viewName);
        verify(ticketService).save(any(Ticket.class));
        verify(eventService).save(event);
        verify(userService).save(user);
    }

    @Test
    public void testAddTicketWithInvalidUser() {
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = ticketController.addTicket("Name", "email@example.com", 1234567890L, "Type", 2, "EventName", session, model);

        assertEquals("error", viewName);
        verify(model).addAttribute("errorMessage", "Debes iniciar sesión para comprar tickets.");
    }

    @Test
    public void testReturnTicketWithUserId() {
        Long userId = 1L;
        when(session.getAttribute("userId")).thenReturn(userId);
        when(ticketService.findByUserId(userId)).thenReturn(Collections.emptyList());

        String viewName = ticketController.returnTicket(model, session);

        assertEquals("error", viewName);
        verify(model).addAttribute("errorMessage", "No tienes tickets para devolver.");
    }

    @Test
    public void testReturnTicketWithoutUserId() {
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = ticketController.returnTicket(model, session);

        assertEquals("error", viewName);
        verify(model).addAttribute("errorMessage", "Debes iniciar sesión para devolver tickets.");
    }

    @Test
    public void testDeleteTicketWithValidUser() {
        Long userId = 1L;
        Long ticketId = 1L;
        Event event = new Event();
        event.setName("Evento de prueba");
        when(eventService.getByName("Evento de prueba")).thenReturn(Arrays.asList(event));
        event.setAvailableTickets(10);
        User user = new User();
        user.setId(userId);
        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setNum(2);
        ticket.setUser(user);
        when(session.getAttribute("userId")).thenReturn(userId);
        when(ticketService.findById(ticketId)).thenReturn(ticket);
        String viewName = ticketController.deleteTicket(ticketId, model, session);

        assertEquals("returnTicket", viewName);
        verify(ticketService).delete(ticket);
        verify(eventService).save(argThat(savedEvent -> savedEvent.getAvailableTickets() == 12));
    }

    @Test
    public void testDeleteTicketWithInvalidUser() {
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = ticketController.deleteTicket(1L, model, session);

        assertEquals("error", viewName);
        verify(model).addAttribute("errorMessage", "Debes iniciar sesión para devolver tickets.");
    }
}