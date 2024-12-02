package org.example.data.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.auth.models.User;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "analysis")
public class Analyse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "securities_uid")
    private String securitiesUid;
    private double mean;
    private double stdDev;
    private double coefVariation;
    private double coefSharp;
    private double coefInformation;
    private double coefSortino;
}
