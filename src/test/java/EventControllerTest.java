
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.PCA.Event.Event;
import com.PCA.Event.EventController;
import com.PCA.Event.EventService;
import com.PCA.Ticket.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    @Mock
    private TicketService ticketService;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateEvent() {
        String viewName = eventController.createEvent(model);
        assertEquals("createEvent", viewName);
    }

    @Test
    public void testAddEvent() throws IOException {
        MultipartFile image = mock(MultipartFile.class);

        when(image.getContentType()).thenReturn("image/jpeg");

        when(image.getBytes()).thenReturn("dummyImageData".getBytes());

        when(image.getOriginalFilename()).thenReturn("testImage.jpg");

        when(image.isEmpty()).thenReturn(false);

        when(image.getSize()).thenReturn(1024L);

        when(eventService.findAllList()).thenReturn(Collections.emptyList());

        String viewName = eventController.addEvent(
                "EventName", "EventDescription", "2025-12-31", "Calle del León, 5", 10.0f, image, 100, model
        );

        assertEquals("eventConfirmation", viewName);

        verify(eventService).save(any(Event.class));
    }


    @Test
    public void testPreEditEvent() {
        when(eventService.findAllList()).thenReturn(Collections.emptyList());

        String viewName = eventController.preEditEvent(model);

        assertEquals("preEditEvent", viewName);
        verify(model).addAttribute("sessions", Collections.emptyList());
    }


    @Test
    public void testEditEvent() {
        Model model = mock(Model.class);
        Event mockEvent = new Event();
        mockEvent.setName("EventName");

        when(eventService.getByName(anyString())).thenReturn(Collections.singletonList(mockEvent));

        when(ticketService.getByEventName(anyString())).thenReturn(Collections.emptyList());

        String viewName = eventController.editEvent("EventName", model);

        assertEquals("editEvent", viewName);

        ArgumentCaptor<List> sessionsCaptor = ArgumentCaptor.forClass(List.class);
        verify(model).addAttribute(eq("sessions"), sessionsCaptor.capture());

        assertEquals(Collections.singletonList(mockEvent), sessionsCaptor.getValue());

        verify(model).addAttribute(eq("tickets"), eq(Collections.emptyList()));
    }



    @Test
    public void testEditEventPost() throws IOException {

        MultipartFile image = mock(MultipartFile.class);

        when(image.getContentType()).thenReturn("image/jpeg");

        when(image.getBytes()).thenReturn("dummyImageData".getBytes());

        when(image.getOriginalFilename()).thenReturn("testImage.jpg");

        when(image.isEmpty()).thenReturn(false);

        Event existingEvent = new Event();

        when(eventService.getByName(anyString())).thenReturn(Collections.singletonList(existingEvent));

        String viewName = eventController.editEvent(
                "EventDescription", "2025-12-31", "Calle de la Serpiente, 10", 10.0f, "EventName", image, 100, model
        );

        assertEquals("eventConfirmation", viewName);

        verify(eventService).save(any(Event.class));
    }



    @Test
    public void testDeleteEventPage() {
        when(eventService.findAllList()).thenReturn(Collections.emptyList());

        String viewName = eventController.deleteEventPage(model);

        assertEquals("deleteEvent", viewName);
        verify(model).addAttribute("sessions", Collections.emptyList());
    }

    @Test
    public void testDeleteEvent() {
        Event event = new Event();
        when(eventService.findByName(anyString())).thenReturn(event);
        when(ticketService.getByEventName(anyString())).thenReturn(Collections.emptyList());

        String viewName = eventController.deleteEvent("EventName", model);

        assertEquals("eventDeleted", viewName);
        verify(eventService).deleteEvent(event);
    }
}