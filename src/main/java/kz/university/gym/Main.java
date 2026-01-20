package kz.university.gym;

import kz.university.gym.config.IDBManager;
import kz.university.gym.config.PostgresDBManager;
import kz.university.gym.controller.GymController;
import kz.university.gym.repository.*;
import kz.university.gym.service.GymService;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class Main {
    public static void main(String[] args) {
        IDBManager idbManager = new PostgresDBManager();

        try (Connection connection = idbManager.getConnection()) {
            ClientRepository clientRepository = new ClientRepositoryImpl(connection);
            SubscriptionRepository subscriptionRepository = new SubscriptionRepositoryImpl(connection);
            MembershipRepository membershipRepository = new MembershipRepositoryImpl(connection);

            GymService gymService = new GymService(clientRepository, subscriptionRepository, membershipRepository);

            GymController gymController = new GymController(gymService);

            gymController.start();

        } catch (SQLException e) {
            System.out.println("Error with connecting to database: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("Unexpected error");
            e.printStackTrace();
        }
    }
}
