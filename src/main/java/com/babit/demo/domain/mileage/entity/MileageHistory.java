package com.babit.demo.domain.mileage.entity;

import com.babit.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mileage_histories")
@NoArgsConstructor
@AllArgsConstructor
public class MileageHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "target_date", nullable = false)
    private LocalDate targetDate;

    @Column(name = "granted_at", nullable = false)
    private LocalDateTime grantedAt;

    @PrePersist
    protected void onCreate(){
        this.grantedAt = LocalDateTime.now();
    }

}
