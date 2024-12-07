package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.time.Instant;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Immutable
@Table(name = "latest_analysis")
public class LatestAnalyse {
    @Id
    private int id;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "securities_uid")
    private String securitiesUid;

    @Column(name = "date_from")
    private Instant dateFrom;

    @Column(name = "date_to")
    private Instant dateTo;

    private double mean;
    private double stdDev;
    private double coefVariation;
    private double coefSharp;
    private double coefInformation;
    private double coefSortino;
}
