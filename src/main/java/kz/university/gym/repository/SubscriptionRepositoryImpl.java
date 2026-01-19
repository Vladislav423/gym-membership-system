package kz.university.gym.repository;


import kz.university.gym.entity.ClientSubscription;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.Optional;

@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepository {
    private final Connection connection;

    @Override
    public void save(ClientSubscription subscription) {
        String sql = """
                INSERT INTO client_subscriptions
                (client_id, type_id, start_date, end_date, visit_left, is_active)
                VALUES (?, ?, ?, ?, ?, ?)""";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                ps.setLong(1, subscription.getClientId());
                ps.setLong(2, subscription.getTypeId());
                ps.setDate(3, Date.valueOf(subscription.getStartDate()));
                ps.setDate(4, Date.valueOf(subscription.getEndDate()));
                ps.setInt(5, subscription.getVisitLeft());
                ps.setBoolean(6, subscription.getIsActive());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public Optional<ClientSubscription> findActiveByClientId(Long clientId) {
        // Ищем только тот, который is_active = true
        String sql = "SELECT * FROM client_subscriptions WHERE client_id = ? AND is_active = true";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void update(ClientSubscription sub) {
        String sql = "UPDATE client_subscriptions SET visit_left = ?, is_active = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sub.getVisitLeft());
            ps.setBoolean(2, sub.getIsActive());
            ps.setLong(3, sub.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private ClientSubscription mapRow(ResultSet rs) throws SQLException {
        ClientSubscription sub = new ClientSubscription();
        sub.setId(rs.getLong("id"));
        sub.setClientId(rs.getLong("client_id"));
        sub.setTypeId(rs.getLong("type_id"));
        sub.setStartDate(rs.getDate("start_date").toLocalDate());
        sub.setEndDate(rs.getDate("end_date").toLocalDate());
        sub.setVisitLeft(rs.getInt("visit_left"));
        sub.setIsActive(rs.getBoolean("is_active"));
        return sub;
    }

}
