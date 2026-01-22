package kz.university.gym.controller;

import kz.university.gym.entity.Client;
import kz.university.gym.entity.MembershipType;
import kz.university.gym.service.GymService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class GymController {
    private final GymService gymService;
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("WELCOME TO THE GYM SYSTEM");

        while (true) {
            System.out.println("""
                    === FITNESS CLUB SYSTEM ===
                    1. Add client
                    2. Show plans
                    3. Sell subscription to user
                    4. Check-in
                    5. Exit""");

            System.out.print("Choose action: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> handleRegisterClient();
                case "2" -> handleShowPlans();
                case "3" -> handleBuySubscription();
                case "4" -> handleCheckIn();
                case "5" -> {
                    System.out.println("Exit...");
                    return;
                }
                default -> System.out.println("Unknown command");
            }
        }
    }

    private void handleRegisterClient() {
        System.out.println("Input client's name");
        String name = scanner.nextLine();

        System.out.println("Input client's phone");
        String phone = scanner.nextLine();

        gymService.registerClient(name, phone);
    }

    private void handleShowPlans() {
        List<MembershipType> membershipTypes = gymService.findAllMembershipTypes();
        for (MembershipType membershipType : membershipTypes) {
            System.out.printf("[%d] %s - %.2f ₸ (Days: %d, Visits: %d)",
                    membershipType.getId(), membershipType.getName(), membershipType.getPrice(),
                    membershipType.getDurationDays(), membershipType.getVisitLimit());
            System.out.println();
        }
    }

    private void handleBuySubscription() {
        try {
            System.out.println("Input client's id from the list:");
            handleShowAllClients();

            System.out.print("Input client's id: ");
            Long clientId = Long.parseLong(scanner.nextLine());

            handleShowPlans();

            System.out.print("Input membership type id: ");
            Long typeId = Long.parseLong(scanner.nextLine());

            String result = gymService.buySubscription(clientId, typeId);
            System.out.println(result);
        } catch (NumberFormatException e) {
            System.out.println("Enter only numbers for the ID");
        }

    }

    private void handleCheckIn() {
        try {
            System.out.println("--- ENTRANCE TO THE HALL ---");
            handleShowAllClients();

            System.out.println("Input client's id");
            Long clientId = Long.parseLong(scanner.nextLine());

            String result = gymService.checkInClient(clientId);
            System.out.println(result);

        } catch (NumberFormatException e) {
            System.out.println("The ID must be a number.");
        }
    }


    private void handleShowAllClients() {
        List<Client> clients = gymService.findAllClients();
        if (clients.isEmpty()) {
            System.out.println("List of clients is empty.");
        } else {
            for (Client client : clients) {
                System.out.printf("[%d] %s (Phone: %s)\n", client.getId(), client.getName(), client.getPhone());
            }
        }
    }

}
