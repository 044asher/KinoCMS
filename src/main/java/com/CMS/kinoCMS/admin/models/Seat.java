package com.CMS.kinoCMS.admin.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "row_num")
    private int rowNumber;
    private int seatNumber;
    private boolean booked;

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;
}
