package org.example.data.services;

import org.example.services.t_api.InstrumentsService_T_API;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InstrumentsServiceTAPITest {
    @Autowired private InstrumentsService_T_API instrumentsServiceTAPI;

    @Test
    void getShares() {
        instrumentsServiceTAPI.getTradableShares().forEach(System.out::println);
    }
}