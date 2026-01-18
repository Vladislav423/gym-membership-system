package kz.university.gym.service;

import kz.university.gym.entity.ClientSubscription;
import kz.university.gym.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GymService {
    private final SubscriptionRepository subscriptionRepository;

    public ClientSubscription findActiveByClientId(Long clientId){
        return subscriptionRepository.findActiveByClientId(clientId);
    }
}
