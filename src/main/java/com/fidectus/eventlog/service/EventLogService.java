package com.fidectus.eventlog.service;

import com.fidectus.eventlog.dao.entity.Event;
import com.fidectus.eventlog.dao.repository.EventRepository;
import com.fidectus.eventlog.model.EventDTO;
import com.fidectus.eventlog.model.factory.EventDTOFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class EventLogService {
    private static final Logger LOGGER = LogManager.getLogger(EventLogService.class);

    @Autowired
    private EventDTOFactory factory;

    @Autowired
    private EventRepository eventRepository;

    public EventDTO getById(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty()) {
            LOGGER.debug("No user has been found for id: {0}", id);
            return null;
        }
        return factory.mapToEventDTO(event.get());
    }

    public List<EventDTO> getByUserId(Long userId) {
        List<Event> events = eventRepository.findAllByUserId(userId);
        if (CollectionUtils.isEmpty(events)) {
            LOGGER.debug("No event has been found for user id: {0}", userId);
            return null;
        }
        return factory.mapToEventDTOs(events);
    }

    public EventDTO create(EventDTO eventDTO) {
        Event event = factory.mapToEvent(eventDTO);
        Event updatedEvent = eventRepository.save(event);
        return factory.mapToEventDTO(updatedEvent);
    }
}
