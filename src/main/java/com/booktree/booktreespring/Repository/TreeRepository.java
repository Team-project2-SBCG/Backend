package com.booktree.booktreespring.Repository;

import com.booktree.booktreespring.Domain.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TreeRepository extends JpaRepository<Tree, String> {
    @Override
    Tree save(Tree entity);
    Optional<Tree> findByUserName(String username);

}
