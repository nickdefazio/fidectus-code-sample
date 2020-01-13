package com.fidectus.eventlog.model;

import com.fidectus.eventlog.dao.entity.EventType;

import javax.validation.constraints.NotNull;
import java.util.Date;


public class EventDTO {

    private Long id;

    @NotNull(message = "Event type is required.")
    private EventType eventType;

    @NotNull(message = "User ID is required.")
    private Long userId;

    private String hash;

    @NotNull(message = "Date is required.")
    private Date createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
