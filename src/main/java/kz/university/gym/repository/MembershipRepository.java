package kz.university.gym.repository;

import kz.university.gym.entity.MembershipType;

import java.util.List;
import java.util.Optional;

public interface MembershipRepository {
    List<MembershipType> findAll();

    Optional<MembershipType> findById(Long id);
}
