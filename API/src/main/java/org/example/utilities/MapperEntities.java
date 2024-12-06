package org.example.utilities;

import org.example.dto.AnalyseResponse;
import org.example.entities.Analyse;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.core.utils.DateUtils;

import java.time.Instant;

public class MapperEntities {

    public static org.example.entities.Account AccountToEntity(Account account) {
        return org.example.entities.Account.builder()
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

    public static Analyse AnalyseResponseToAnalyse(
        AnalyseResponse analyseResponse,
        Instant from, Instant to,
        org.example.entities.Account account,
        String securitiesId
    ) {
        return Analyse.builder()
            .mean(analyseResponse.getMean())
            .stdDev(analyseResponse.getStdDev())
            .coefVariation(analyseResponse.getCoefVariation())
            .coefSharp(analyseResponse.getCoefSharp())
            .coefInformation(analyseResponse.getCoefInformation())
            .coefSortino(analyseResponse.getCoefSortino())
            .account(account)
            .securitiesUid(securitiesId)
            .dateTo(to)
            .dateFrom(from)
            .build();
    }}
