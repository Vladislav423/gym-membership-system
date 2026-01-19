package kz.university.gym.controller;

import kz.university.gym.entity.ClientSubscription;
import kz.university.gym.service.GymService;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class GymController {
    private final GymService gymService;
    private final Scanner scanner = new Scanner(System.in);

    public ClientSubscription findActiveByClientId(Long clientId){
       return gymService.findActiveByClientId(clientId);
    }
}
