package com.booktree.booktreespring.Repository;

import com.booktree.booktreespring.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    User save(User entity);
    Optional<User> findByUserName(String username);
    void deleteById(Long userId);
}