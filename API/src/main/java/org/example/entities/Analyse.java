package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "analysis")
public class Analyse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

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
