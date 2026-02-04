package kz.university.gym.controller;

import kz.university.gym.dto.SubscriptionInfo;
import kz.university.gym.entity.Client;
import kz.university.gym.entity.MembershipType;
import kz.university.gym.entity.User;
import kz.university.gym.service.GymService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class GymController {
    private final GymService gymService;
    private final Scanner scanner = new Scanner(System.in);
    private final User currentUser;

    public void start() {
        System.out.println("WELCOME TO THE GYM SYSTEM");

        while (true) {
            System.out.println("""
                    \n=== MENU ===
                    1. Add client (Admin only)
                    2. Show plans
                    3. Sell subscription
                    4. Check-in (Enter gym)
                    5. Show detailed report (JOIN) - Admin only
                    6. Find client by name
                    0. Exit""");

            System.out.print("> Choose action: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> handleRegisterClient();
                case "2" -> handleShowPlans();
                case "3" -> handleBuySubscription();
                case "4" -> handleCheckIn();
                case "5" -> handleShowDetailedReport();
                case "6" -> handleFindClient();
                case "0" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Unknown command");
            }
        }
    }

    private void handleRegisterClient() {
        if (!"ADMIN".equals(currentUser.getRole())) {
            System.out.println("ACCESS DENIED: Only ADMIN can register new clients.");
            return;
        }

        System.out.print("Input client's name: ");
        String name = scanner.nextLine();

        System.out.print("Input client's phone (+7...): ");
        String phone = scanner.nextLine();

        try {
            gymService.registerClient(name, phone);
            System.out.println("Client registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleShowDetailedReport() {
        if (!"ADMIN".equals(currentUser.getRole())) {
            System.out.println("ACCESS DENIED: Only ADMIN can see reports.");
            return;
        }

        System.out.println("\n--- FULL SUBSCRIPTION REPORT (JOIN) ---");
        List<SubscriptionInfo> details = gymService.getDetailedSubscriptions();

        if (details.isEmpty()) {
            System.out.println("No active subscriptions found.");
            return;
        }

        System.out.printf("%-5s | %-20s | %-20s | %-12s | %s%n", "ID", "Client Name", "Plan", "End Date", "Visits");
        System.out.println("--------------------------------------------------------------------------");

        for (SubscriptionInfo info : details) {
            System.out.printf("%-5d | %-20s | %-20s | %-12s | %d%n",
                    info.getId(),
                    info.getClientName(),
                    info.getMembershipName(),
                    info.getEndDate(),
                    info.getVisitsLeft());
        }
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
            System.out.println("\n--- SELL SUBSCRIPTION ---");
            handleShowAllClients();

            System.out.print("Input client's id: ");
            Long clientId = Long.parseLong(scanner.nextLine());

            handleShowPlans();

            System.out.print("Input membership type id: ");
            Long typeId = Long.parseLong(scanner.nextLine());

            String result = gymService.buySubscription(clientId, typeId);
            System.out.println(result);
        } catch (NumberFormatException e) {
            System.out.println("Error: Enter only numbers.");
        }
    }

    private void handleCheckIn() {
        try {
            System.out.println("--- CHECK-IN ---");
            handleShowAllClients();

            System.out.print("Input client's id: ");
            Long clientId = Long.parseLong(scanner.nextLine());

            String result = gymService.checkInClient(clientId);
            System.out.println(result);

        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a number.");
        }
    }

    private void handleShowAllClients() {
        List<Client> clients = gymService.findAllClients();
        if (clients.isEmpty()) {
            System.out.println("List of clients is empty.");
        } else {
            System.out.println("Clients list:");
            for (Client client : clients) {
                System.out.printf("[%d] %s (Phone: %s)\n", client.getId(), client.getName(), client.getPhone());
            }
        }
    }

    private void handleFindClient() {
        System.out.print("Enter name to search: ");
        String query = scanner.nextLine();

        List<Client> found = gymService.searchClientsByName(query);

        if (found.isEmpty()) {
            System.out.println("No clients found.");
        } else {
            System.out.println("--- SEARCH RESULTS ---");
            found.forEach(c -> System.out.printf("[%d] %s (Phone: %s)\n", c.getId(), c.getName(), c.getPhone()));
        }
    }

}
