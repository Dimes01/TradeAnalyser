package org.example.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "securities")
public class Securities {
    @Id
    @Column(name = "instrument_uid")
    private String id;

    @Column(name = "position_uid")
    private String positionUid;

    @Column(name = "exchange_blocked")
    private boolean exchangeBlocked;

    @Column(name = "instrument_type")
    private String instrumentType;

    private long blocked;
    private long balance;
    private String figi;
}
