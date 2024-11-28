package org.example.data.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class QuotesServiceTest {
    @Autowired InstrumentsService instrumentsService;
    @Autowired QuotesService quotesService;

    @Test
    void getLastPrices() {

    }

    @Test
    void getHistoricCandles() {
    }
}