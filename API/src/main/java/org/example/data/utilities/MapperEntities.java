package org.example.data.utilities;

import org.example.data.dto.AnalyseResponse;
import org.example.data.entities.Analyse;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.core.utils.DateUtils;

public class MapperEntities {

    public static org.example.data.entities.Account AccountToEntity(Account account) {
        return org.example.data.entities.Account.builder()
            .id(account.getId())
            .openedDate(DateUtils.timestampToInstant(account.getOpenedDate()))
            .closedDate(DateUtils.timestampToInstant(account.getClosedDate()))
            .type(account.getTypeValue())
            .name(account.getName())
            .status(account.getStatusValue())
            .accessLevel(account.getAccessLevelValue())
            .build();
    }

    public static Analyse AnalyseResponseToAnalyse(AnalyseResponse analyseResponse) {
        return Analyse.builder()
            .mean(analyseResponse.getMean())
            .stdDev(analyseResponse.getStdDev())
            .coefVariation(analyseResponse.getCoefVariation())
            .coefSharp(analyseResponse.getCoefSharp())
            .coefInformation(analyseResponse.getCoefInformation())
            .coefSortino(analyseResponse.getCoefSortino())
            .build();
    }
}
