package com.booktree.booktreespring.Repository;

import com.booktree.booktreespring.Domain.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreeRepository extends JpaRepository<Tree, Long> {
    @Override
    Tree save(Tree entity);
}
