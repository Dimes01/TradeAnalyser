package org.example.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "settings")
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Positive(message = "Risk free should be a positive number")
    private double riskFree;

    @Positive(message = "Mean of benchmark should be a positive number")
    private double meanBenchmark;
}
