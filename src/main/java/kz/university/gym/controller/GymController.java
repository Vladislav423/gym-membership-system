package kz.university.gym.controller;

import kz.university.gym.entity.ClientSubscription;
import kz.university.gym.service.GymService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GymController {
    private final GymService gymService;

    public ClientSubscription findActiveByClientId(Long clientId){
       return gymService.findActiveByClientId(clientId);
    }
}
