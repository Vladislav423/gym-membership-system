package kz.university.gym.repository;

import kz.university.gym.entity.ClientSubscription;

import java.util.List;

public interface SubscriptionRepository {
    ClientSubscription findActiveByClientId(Long clientId);
}
