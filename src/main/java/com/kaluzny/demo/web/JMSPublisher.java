package com.kaluzny.demo.web;

import com.kaluzny.demo.domain.Automobile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface JMSPublisher {

    ResponseEntity<Automobile> pushMessage(Automobile automobile);

    @GetMapping(value = "/color", params = {"color"})
    @ResponseStatus(HttpStatus.OK)
    List<String> getCarByColor(@RequestParam("color") String color);
}
