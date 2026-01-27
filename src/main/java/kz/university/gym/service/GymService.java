package kz.university.gym.service;

import kz.university.gym.dto.SubscriptionInfo;
import kz.university.gym.entity.Client;
import kz.university.gym.entity.ClientSubscription;
import kz.university.gym.entity.MembershipType;
import kz.university.gym.repository.ClientRepository;
import kz.university.gym.repository.MembershipRepository;
import kz.university.gym.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class GymService {
    private final ClientRepository clientRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MembershipRepository membershipRepository;

    private final Predicate<String> isValidPhone = phone ->
            phone != null && phone.startsWith("+7") && phone.length() == 12;

    public void registerClient(String name, String phone) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (!isValidPhone.test(phone)) {
            throw new IllegalArgumentException("Invalid phone format! Example: +77011234567");
        }

        Client client = new Client();
        client.setName(name);
        client.setPhone(phone);
        clientRepository.save(client);
    }

    public List<SubscriptionInfo> getDetailedSubscriptions() {
        return subscriptionRepository.findAllWithDetails();
    }

    public List<MembershipType> findAllMembershipTypes() {
        return membershipRepository.findAll();
    }

    public String buySubscription(Long clientId, Long typeId) {
        Optional<Client> clientOptional = clientRepository.findById(clientId);
        Optional<MembershipType> membershipTypeOptional = membershipRepository.findById(typeId);

        if (clientOptional.isEmpty()) {
            return "Client not found";
        }
        if (membershipTypeOptional.isEmpty()) {
            return "MembershipType not found";
        }
        if (subscriptionRepository.findActiveByClientId(clientId).isPresent()) {
            return "Client already has an active subscription";
        }
        MembershipType membershipType = membershipTypeOptional.get();

        ClientSubscription subscription = new ClientSubscription();
        subscription.setClientId(clientId);
        subscription.setTypeId(typeId);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusDays(membershipType.getDurationDays()));
        subscription.setVisitLeft(membershipType.getVisitLimit());
        subscription.setIsActive(true);

        subscriptionRepository.save(subscription);

        return "Subscription '" + membershipType.getName() + "' successful! Valid until: " + subscription.getEndDate();
    }

    public String checkInClient(Long clientId) {
        Optional<ClientSubscription> subscriptionOptional = subscriptionRepository.findActiveByClientId(clientId);

        if (subscriptionOptional.isEmpty()){
            return "FORBIDDEN: No active subscription";
        }

        ClientSubscription subscription = subscriptionOptional.get();

        if (LocalDate.now().isAfter(subscription.getEndDate())){
            closeSubscription(subscription);
            return "FORBIDDEN: Subscription expired";
        }
        if (subscription.getVisitLeft() <= 0){
            closeSubscription(subscription);
            return "FORBIDDEN: No visits left";
        }

        subscription.setVisitLeft(subscription.getVisitLeft() - 1);

        if (subscription.getVisitLeft() == 0){
            subscription.setIsActive(false);
        }

        subscriptionRepository.update(subscription);

        return "WELCOME! Visits left: " + subscription.getVisitLeft();
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    private void closeSubscription(ClientSubscription subscription) {
        subscription.setIsActive(false);
        subscriptionRepository.update(subscription);
    }

}
