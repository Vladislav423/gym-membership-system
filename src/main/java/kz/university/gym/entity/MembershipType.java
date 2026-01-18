package kz.university.gym.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipType {
    private Long id;
    private String name;
    private double price;
    private int durationDays;
    private int visitLimit;
}
