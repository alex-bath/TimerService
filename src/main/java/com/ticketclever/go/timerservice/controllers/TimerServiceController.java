package com.ticketclever.go.timerservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketclever.go.timerservice.api.Activation;
import com.ticketclever.go.timerservice.services.TimerRequestBroker;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping(path = "/timers")
public class TimerServiceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerServiceController.class);

    private final TimerRequestBroker timerRequestBroker;

    @Autowired
    @Qualifier("objectMapper")
    private ObjectMapper objectMapper;

    @Inject
    public TimerServiceController(final TimerRequestBroker broker) {
        this.timerRequestBroker = broker;
    }

    @PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
    public DeferredResult<ResponseEntity<String>> submitAll(@RequestBody final Activation activation) {
        DeferredResult<ResponseEntity<String>> defResult = new DeferredResult<>();
        try {
            defResult.setResult(ResponseEntity.ok(objectMapper.writeValueAsString(timerRequestBroker.receiveEvent(activation))));
        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return defResult;
    }

}