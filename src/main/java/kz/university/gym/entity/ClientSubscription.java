package kz.university.gym.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientSubscription {
    private Long id;
    private Long clientId;
    private Long typeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer visitLeft;
    private Boolean isActive;
}
