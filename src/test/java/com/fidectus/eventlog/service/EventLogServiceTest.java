package com.fidectus.eventlog.service;

import com.fidectus.eventlog.dao.entity.Event;
import com.fidectus.eventlog.dao.entity.EventType;
import com.fidectus.eventlog.dao.repository.EventRepository;
import com.fidectus.eventlog.model.EventDTO;
import com.fidectus.eventlog.model.factory.EventDTOFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public class EventLogServiceTest {

    @InjectMocks
    private EventLogService eventLogService;

    @Mock
    private EventDTOFactory eventDTOFactory;

    @Mock
    private EventRepository eventRepository;

    @Captor
    private ArgumentCaptor<Event> captorEvent;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_getById() {
        Long id = 2l;
        Event event = new Event();
        event.setId(id);

        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(id);

        when(eventRepository.findById(id)).thenReturn(Optional.of(event));
        when(eventDTOFactory.mapToEventDTO(event)).thenReturn(eventDTO);

        EventDTO result = eventLogService.getById(id);

        verify(eventRepository, times(1)).findById(eq(id));
        verify(eventDTOFactory, times(1)).mapToEventDTO(same(event));
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void test_getById_null() {
        when(eventRepository.findById(null)).thenReturn(Optional.empty());
        EventDTO result = eventLogService.getById(null);

        verify(eventRepository, times(1)).findById(same(null));
        assertNull(result);
    }

    @Test
    public void test_getById_not_existing() {
        Long id = 8l;
        when(eventRepository.findById(id)).thenReturn(Optional.empty());
        EventDTO result = eventLogService.getById(id);

        verify(eventRepository, times(1)).findById(eq(id));
        assertNull(result);
    }

    @Test
    public void test_getByUserId() {
        Long userId = 5l;

        Event event = new Event();
        event.setUserId(userId);
        List<Event> events = Arrays.asList(event);

        EventDTO eventDTO = new EventDTO();
        eventDTO.setUserId(userId);
        List<EventDTO> eventDTOs = Arrays.asList(eventDTO);

        when(eventRepository.findAllByUserId(userId)).thenReturn(events);
        when(eventDTOFactory.mapToEventDTOs(events)).thenReturn(eventDTOs);

        List<EventDTO> result = eventLogService.getByUserId(userId);

        verify(eventRepository, times(1)).findAllByUserId(eq(userId));
        verify(eventDTOFactory, times(1)).mapToEventDTOs(eq(events));

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(1, result.size());
        assertEquals(eventDTOs.get(0).getUserId(), result.get(0).getUserId());
    }

    @Test
    public void test_getByUserId_null() {
        when(eventRepository.findAllByUserId(null)).thenReturn(Collections.EMPTY_LIST);
        List<EventDTO> result = eventLogService.getByUserId(null);

        verify(eventRepository, times(1)).findAllByUserId(same(null));
        assertTrue(CollectionUtils.isEmpty(result));
    }

    @Test
    public void test_getByUserId_not_existing() {
        Long userId = 1l;
        when(eventRepository.findAllByUserId(userId)).thenReturn(Collections.EMPTY_LIST);
        List<EventDTO> result = eventLogService.getByUserId(userId);

        verify(eventRepository, times(1)).findAllByUserId(eq(userId));
        assertTrue(CollectionUtils.isEmpty(result));
    }

    @Test
    public void test_create() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setUserId(2L);
        Date date = new Date();
        eventDTO.setCreatedDate(date);
        eventDTO.setEventType(EventType.USER_DEACTIVATED);
        eventDTO.setHash("edfg");

        Event event = new Event();
        event.setUserId(2L);
        event.setCreated(date);
        event.setEventType(EventType.USER_DEACTIVATED);
        event.setHash("edfg");

        Event updatedEvent = new Event();
        updatedEvent.setId(1l);
        updatedEvent.setUserId(2L);
        updatedEvent.setCreated(date);
        updatedEvent.setEventType(EventType.USER_DEACTIVATED);
        updatedEvent.setHash("edfg");

        EventDTO updatedEventDTO = new EventDTO();
        updatedEventDTO.setId(1l);
        updatedEventDTO.setUserId(2L);
        updatedEventDTO.setCreatedDate(date);
        updatedEventDTO.setEventType(EventType.USER_DEACTIVATED);
        updatedEventDTO.setHash("edfg");

        when(eventDTOFactory.mapToEvent(eventDTO)).thenReturn(event);
        when(eventRepository.save(captorEvent.capture())).thenReturn(updatedEvent);
        when(eventDTOFactory.mapToEventDTO(updatedEvent)).thenReturn(updatedEventDTO);

        EventDTO result = eventLogService.create(eventDTO);
        assertNotNull(result);
        Event resultCaptorEvent = captorEvent.getValue();
        assertEquals(resultCaptorEvent.getCreated(), event.getCreated());
        assertEquals(resultCaptorEvent.getEventType(), event.getEventType());
        assertEquals(resultCaptorEvent.getHash(), event.getHash());
        assertEquals(resultCaptorEvent.getUserId(), event.getUserId());
    }
}
