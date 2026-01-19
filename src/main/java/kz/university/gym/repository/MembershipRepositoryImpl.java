package kz.university.gym.repository;

import kz.university.gym.entity.MembershipType;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MembershipRepositoryImpl implements MembershipRepository {
    private final Connection connection;

    @Override
    public List<MembershipType> findAll() {
        List<MembershipType> membershipTypes = new ArrayList<>();
        String sql = "select * from membership_types";
        try (Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    membershipTypes.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return membershipTypes;
    }

    @Override
    public Optional<MembershipType> findById(Long id) {
        String sql = "select * from membership_types where id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
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

    private MembershipType mapRow(ResultSet rs) throws SQLException {
        MembershipType membershipType = new MembershipType();
        membershipType.setId(rs.getLong("id"));
        membershipType.setName(rs.getString("name"));
        membershipType.setPrice(rs.getDouble("price"));
        membershipType.setDurationDays(rs.getInt("duration_days"));
        membershipType.setVisitLimit(rs.getInt("visit_limit"));
        return membershipType;
    }
}
