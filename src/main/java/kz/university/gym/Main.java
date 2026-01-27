package kz.university.gym;

import kz.university.gym.config.IDBManager;
import kz.university.gym.config.PostgresDBManager;
import kz.university.gym.controller.GymController;
import kz.university.gym.entity.User;
import kz.university.gym.repository.*;
import kz.university.gym.service.GymService;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

@RequiredArgsConstructor
public class Main {
    public static void main(String[] args) {
        IDBManager idbManager = PostgresDBManager.getInstance();
        Scanner scanner = new Scanner(System.in);

        try (Connection connection = idbManager.getConnection()) {
            ClientRepository clientRepository = new ClientRepositoryImpl(connection);
            SubscriptionRepository subscriptionRepository = new SubscriptionRepositoryImpl(connection);
            MembershipRepository membershipRepository = new MembershipRepositoryImpl(connection);

            UserRepository userRepository = new UserRepositoryImpl(connection);

            GymService gymService = new GymService(clientRepository, subscriptionRepository, membershipRepository);

            System.out.println("=== SYSTEM LOGIN ===");
            System.out.print("Username: ");
            String username = scanner.nextLine();

            Optional<User> userOpt = userRepository.findByUsername(username);

            if (userOpt.isEmpty()) {
                System.out.println("User not found!");
                return;
            }

            User currentUser = userOpt.get();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            if (!currentUser.getPassword().equals(password)) {
                System.out.println("Error: Wrong password!");
                return;
            }

            System.out.println("Login successful! Welcome, " + currentUser.getUsername() + " [" + currentUser.getRole() + "]");

            GymController gymController = new GymController(gymService, currentUser);
            gymController.start();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
