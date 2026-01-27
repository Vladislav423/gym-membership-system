package kz.university.gym.repository;

import kz.university.gym.dto.SubscriptionInfo;
import kz.university.gym.entity.ClientSubscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    void save(ClientSubscription subscription);

    Optional<ClientSubscription> findActiveByClientId(Long clientId);

    void update(ClientSubscription subscription);

    List<SubscriptionInfo> findAllWithDetails();
}
