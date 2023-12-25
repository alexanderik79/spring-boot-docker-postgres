package com.kaluzny.demo.service;

import com.kaluzny.demo.domain.Automobile;

import java.util.List;

public interface AutomobileService {
    Automobile saveAndSendMessage(Automobile automobile);

    List<String> getCarByColor(String color);
}
