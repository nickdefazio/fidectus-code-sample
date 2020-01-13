package com.fidectus.eventlog.model.factory;

import com.fidectus.eventlog.dao.entity.Event;
import com.fidectus.eventlog.model.EventDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventDTOFactory {
    private static final Logger LOGGER = LogManager.getLogger(EventDTOFactory.class);

    public static final String SHA_256 = "SHA-256";

    public EventDTO mapToEventDTO(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setEventType(event.getEventType());
        eventDTO.setUserId(event.getUserId());
        eventDTO.setHash(event.getHash());
        eventDTO.setCreatedDate(event.getCreated());
        return eventDTO;
    }

    public List<EventDTO> mapToEventDTOs(List<Event> events) {
        List<EventDTO> eventDTOs = new ArrayList<>();
        events.forEach(event -> {
            EventDTO eventDTO = mapToEventDTO(event);
            eventDTOs.add(eventDTO);
        });
        return eventDTOs;
    }

    public Event mapToEvent(EventDTO eventDTO) {
        Event event = new Event();
        event.setEventType(eventDTO.getEventType());
        event.setCreated(eventDTO.getCreatedDate());
        event.setHash(generateCryptoHash(eventDTO));
        event.setUserId(eventDTO.getUserId());
        return event;
    }

    private String generateCryptoHash(EventDTO eventDTO) {
        String sha256hex = StringUtils.EMPTY;
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256);
            digest.update(eventDTO.getEventType().name().getBytes());
            digest.update(eventDTO.getUserId().byteValue());
            digest.update(eventDTO.getCreatedDate().toString().getBytes());
            byte[] hashedBytes = digest.digest();

            sha256hex = new String(Hex.encode(hashedBytes));
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("SHA_256 algorithm instance was not found: {0}", e);
        }

        return sha256hex;
    }
}
