package kz.university.gym.service;

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

@RequiredArgsConstructor
public class GymService {
    private final ClientRepository clientRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MembershipRepository membershipRepository;

    public void registerClient(String name, String phone) {
        Client client = new Client();
        client.setName(name);
        client.setPhone(phone);
        clientRepository.save(client);
        System.out.println("Registration successful");
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
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
            return "Client has active subscription";
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

        return "Subscription " + membershipType.getName() + "' successful! Duration to: " + subscription.getEndDate();
    }

    public String checkInClient(Long clientId) {
        Optional<ClientSubscription> subscriptionOptional = subscriptionRepository.findActiveByClientId(clientId);
        if (subscriptionOptional.isEmpty()){
            return "FORBIDDEN: None active subscription";
        }
        ClientSubscription subscription = new ClientSubscription();
        if (LocalDate.now().isAfter(subscription.getEndDate())){
            return "FORBIDDEN: The subscription experienced";
        }
        if (subscription.getVisitLeft() <= 0){
            closeSubscription(subscription);
            return "FORBIDDEN: Sessions ended";
        }
       subscription.setVisitLeft(subscription.getVisitLeft() - 1);

        if (subscription.getVisitLeft() == 0){
            subscription.setIsActive(false);
        }

        subscriptionRepository.update(subscription);

        return "WELCOME! Number of visits left: " + subscription.getVisitLeft();
    }
    private void closeSubscription(ClientSubscription subscription){
        subscription.setIsActive(false);
        subscriptionRepository.update(subscription);
    }

}
