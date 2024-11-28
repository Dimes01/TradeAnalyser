package org.example.data.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InstrumentsServiceTest {
    @Autowired private InstrumentsService instrumentsService;

    @Test
    void getShares() {
        instrumentsService.getTradableShares().forEach(System.out::println);
    }
}