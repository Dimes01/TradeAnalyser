package org.example.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.auth.models.User;

@Getter @Setter
@Entity(name = "analysis")
public class Analyse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "securities_id")
    private Securities securities;

    private double mean;
    private double stdDev;
    private double coefVariation;
    private double coefSharp;
    private double coefInformation;
    private double coefSortino;
}
