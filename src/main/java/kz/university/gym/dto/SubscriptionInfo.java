package kz.university.gym.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SubscriptionInfo {
    private Long id;
    private String clientName;
    private String membershipName;
    private LocalDate endDate;
    private int visitsLeft;
}
