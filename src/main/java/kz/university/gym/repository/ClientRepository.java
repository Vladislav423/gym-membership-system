package kz.university.gym.repository;

import kz.university.gym.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);

    Optional<Client> findById(Long id);

    List<Client> findAll();

    void update(Client client);

    void deleteById(Long id);
}
