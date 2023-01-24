package org.example.app.repository;

import org.example.app.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAllByNameContaining(String name, Pageable pageable);
    User findByEmail(String email);
    boolean deleteById(int userId);
}
