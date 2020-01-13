package com.fidectus.eventlog.factory;

import com.fidectus.eventlog.dao.entity.Event;
import com.fidectus.eventlog.dao.entity.EventType;
import com.fidectus.eventlog.model.EventDTO;
import com.fidectus.eventlog.model.factory.EventDTOFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MessageDigest.class)
public class EventDTOFactoryTest {

    @InjectMocks
    private EventDTOFactory eventDTOFactory;
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_mapToEventDTO() {
        Date date = new Date();
        Event event = new Event();
        event.setId(1l);
        event.setHash("abcde");
        event.setEventType(EventType.USER_DEACTIVATED);
        event.setCreated(date);
        event.setUserId(2l);

        EventDTO result = eventDTOFactory.mapToEventDTO(event);
        assertEquals(Long.valueOf(1), result.getId());
        assertEquals("abcde", result.getHash());
        assertEquals(EventType.USER_DEACTIVATED, result.getEventType());
        assertEquals(date, result.getCreatedDate());
        assertEquals(Long.valueOf(2), result.getUserId());
    }

    @Test
    public void test_mapToEventDTOs() {
        Event event = new Event();
        event.setId(1l);
        List<Event> events = Arrays.asList(event);

        List<EventDTO> result = eventDTOFactory.mapToEventDTOs(events);
        assertEquals(1, result.size());
        assertEquals(Long.valueOf(1), result.get(0).getId());
    }

    @Test
    public void test_mapToEvent() throws NoSuchAlgorithmException {
        Date date = new Date();
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventType(EventType.USER_DEACTIVATED);
        eventDTO.setCreatedDate(date);
        eventDTO.setUserId(2l);

        MessageDigest digest = mock(MessageDigest.class);
        when(digest.digest()).thenReturn("test".getBytes());
        PowerMockito.mockStatic(MessageDigest.class);
        when(MessageDigest.getInstance("SHA-256")).thenReturn(digest);

        Event event = eventDTOFactory.mapToEvent(eventDTO);
        assertEquals(EventType.USER_DEACTIVATED, event.getEventType());
        assertEquals(date, event.getCreated());
        assertEquals(Long.valueOf(2), event.getUserId());
        assertFalse(event.getHash().isEmpty());
    }

    @Test
    public void test_generateCryptoHash() throws NoSuchAlgorithmException {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventType(EventType.USER_DEACTIVATED);
        eventDTO.setCreatedDate(new Date());
        eventDTO.setUserId(2l);
        MessageDigest digest = mock(MessageDigest.class);
        when(digest.digest()).thenReturn("test".getBytes());
        PowerMockito.mockStatic(MessageDigest.class);
        when(MessageDigest.getInstance("SHA-256")).thenReturn(digest);
        String result = ReflectionTestUtils.invokeMethod(eventDTOFactory, "generateCryptoHash", eventDTO);

        assertFalse(result.isEmpty());
    }
}
