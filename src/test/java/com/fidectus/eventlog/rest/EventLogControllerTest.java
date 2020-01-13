package com.fidectus.eventlog.rest;

import com.fidectus.eventlog.model.EventDTO;
import com.fidectus.eventlog.service.EventLogService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.fidectus.eventlog.dao.entity.EventType.USER_REGISTRATION;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
public class EventLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private EventLogController eventLogController;

    @MockBean
    private EventLogService eventLogService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_getById() throws Exception {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setUserId(2l);
        eventDTO.setId(1l);
        eventDTO.setHash("abcde");
        eventDTO.setCreatedDate(new Date());
        eventDTO.setEventType(USER_REGISTRATION);
        when(eventLogService.getById(1l)).thenReturn(eventDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/event/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();

        verify(eventLogService, times(1)).getById(eq(1l));
    }

    @Test
    public void test_getById_404() throws Exception {
        when(eventLogService.getById(1l)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/event/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
        verify(eventLogService, times(1)).getById(eq(1l));
    }

    @Test
    public void test_searchByUserId() throws Exception {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(1l);
        eventDTO.setUserId(2l);
        eventDTO.setHash("abcde");
        eventDTO.setCreatedDate(new Date());
        eventDTO.setEventType(USER_REGISTRATION);
        List<EventDTO> eventDTOs = Arrays.asList(eventDTO);

        when(eventLogService.getByUserId(2l)).thenReturn(eventDTOs);

        mockMvc.perform(MockMvcRequestBuilders.get("/event/searchByUserId/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.[0].userId").value(2))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();

        verify(eventLogService, times(1)).getByUserId(eq(2l));
    }

    @Test
    public void test_searchByUserId_404() throws Exception {
        when(eventLogService.getByUserId(2l)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/event/searchByUserId/2"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
        verify(eventLogService, times(1)).getByUserId(eq(2l));
    }

    @Test
    public void test_create() throws Exception {
        Date date = new Date();

        EventDTO updatedEventDTO = new EventDTO();
        updatedEventDTO.setEventType(USER_REGISTRATION);
        updatedEventDTO.setCreatedDate(date);
        updatedEventDTO.setUserId(2l);
        updatedEventDTO.setId(1l);
        updatedEventDTO.setHash("abcdde");

        when(eventLogService.create(any(EventDTO.class))).thenReturn(updatedEventDTO);

        String eventDTO = "{\"createdDate\" : \"2022-02-21\", \"eventType\":\"USER_REGISTRATION\", \"userId\":\"2\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/event/create")
                .content(eventDTO)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.eventType").value(USER_REGISTRATION.name()))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andDo(print())
                .andReturn();
        verify(eventLogService, times(1)).create(any(EventDTO.class));
    }

    @Test
    public void test_create_required_field_missing() throws Exception {
        String eventDTO = "{\"createdDate\" : \"2022-02-21\", \"eventType\":\"USER_REGISTRATION\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/event/create")
                .content(eventDTO)
                .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.userId").value("User ID is required."))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andDo(print())
                .andReturn();

        verify(eventLogService, times(0)).create(any(EventDTO.class));
    }

    @Test
    public void test_create_404() throws Exception {
        when(eventLogService.create(any(EventDTO.class))).thenReturn(null);

        String eventDTO = "{\"createdDate\" : \"2022-02-21\", \"eventType\":\"USER_REGISTRATION\", \"userId\":\"2\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/event/create")
                .content(eventDTO)
                .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        verify(eventLogService, times(1)).create(any(EventDTO.class));
    }
}
