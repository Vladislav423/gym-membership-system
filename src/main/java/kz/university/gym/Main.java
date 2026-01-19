package kz.university.gym;

import kz.university.gym.config.IDBManager;
import kz.university.gym.config.PostgresDBManager;
import kz.university.gym.controller.GymController;
import kz.university.gym.repository.SubscriptionRepository;
import kz.university.gym.repository.SubscriptionRepositoryImpl;
import kz.university.gym.service.GymService;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.util.Scanner;

@RequiredArgsConstructor
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        IDBManager idbManager = new PostgresDBManager();
        Connection connection = idbManager.getConnection();
        SubscriptionRepository subscriptionRepository = new SubscriptionRepositoryImpl(connection);
        GymService gymService = new GymService(subscriptionRepository);
        GymController gymController = new GymController(gymService);

        int input;
        while (true) {
            System.out.println("""
                    === FITNESS CLUB SYSTEM ===
                    1. Add client
                    2. Show plans
                    3. Sell subscription to another user
                    4. Cancel check-in
                    5. Exit""");

            System.out.print("Choose action: ");
            input = scanner.nextInt();

            switch (input) {
                case 1 -> System.out.println();
                case 2 -> gymController.findActiveByClientId(1L);
                case 3 -> System.out.println();
                case 4 -> System.out.println();
                case 5 -> {
                    return;
                }
                default -> System.out.println("Unknown command");
            }
        }
    }
}
