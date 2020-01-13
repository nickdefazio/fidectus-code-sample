package com.fidectus.eventlog.rest;

import com.fidectus.eventlog.model.EventDTO;
import com.fidectus.eventlog.service.EventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/event")
public class EventLogController {

    @Autowired
    private EventLogService eventLogService;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<EventDTO> getById(@PathVariable Long id){
        EventDTO eventDTO = eventLogService.getById(id);
        return eventDTO != null ? ResponseEntity.ok(eventDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/searchByUserId/{userId}", produces = "application/json")
    public ResponseEntity<List<EventDTO>> searchByUserId(@PathVariable Long userId) {
        List<EventDTO> eventDTOs = eventLogService.getByUserId(userId);
        return !CollectionUtils.isEmpty(eventDTOs) ? ResponseEntity.ok(eventDTOs) : ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity create(@Valid @RequestBody EventDTO eventDTO) {
        EventDTO updatedEventDTO = eventLogService.create(eventDTO);
        return updatedEventDTO != null ? ResponseEntity.ok(updatedEventDTO) : ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> validationErrors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach((error) -> {
            validationErrors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });
        return validationErrors;
    }
}
