package kz.university.gym.repository;

import kz.university.gym.entity.ClientSubscription;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepository {
    private final Connection connection;

    @Override
    public ClientSubscription findActiveByClientId(Long clientId) {
        String sql = "select * from client_subscriptions where client_id = ? and is_active = true";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1,clientId);
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    ClientSubscription subscription = new ClientSubscription();
                    subscription.setId(rs.getLong("id"));
                    subscription.setClientId(rs.getLong("client_id"));
                    subscription.setTypeId(rs.getLong("type_id"));
                    subscription.setVisitLeft(rs.getInt("visit_left"));
                    subscription.setEndDate(rs.getDate("end_date").toLocalDate());
                    subscription.setIsActive(rs.getBoolean("is_active"));
                    return subscription;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
