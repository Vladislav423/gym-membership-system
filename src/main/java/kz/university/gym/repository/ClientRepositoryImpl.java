package kz.university.gym.repository;

import kz.university.gym.entity.Client;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ClientRepositoryImpl implements ClientRepository {
    private final Connection connection;

    public Client save(Client client) {
        String sql = "insert into clients(name,phone) values (?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getPhone());
            preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    client.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return client;
    }

    public Optional<Client> findById(Long id) {

    }

    public List<Client> findAll() {
        return null;
    }

    public void update(Client client) {

    }

    public void deleteById(Long id) {

    }

    private Client mapRow(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getLong("id"));
        client.setName(rs.getString("name"));
        client.setPhone(rs.getString("phone"));
        return client;
    }

}
