package org.example.data.services;

import org.example.services.t_api.InstrumentsService_T_API;
import org.example.services.t_api.QuotesService_T_API;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class QuotesServiceTAPITest {
    @Autowired
    InstrumentsService_T_API instrumentsServiceTAPI;
    @Autowired
    QuotesService_T_API quotesServiceTAPI;

    @Test
    void getLastPrices() {

    }

    @Test
    void getHistoricCandles() {
    }
}