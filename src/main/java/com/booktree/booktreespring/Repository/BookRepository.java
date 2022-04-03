package com.booktree.booktreespring.Repository;

import com.booktree.booktreespring.Domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    Book save(Book entity);
    List<Book> findByOwnerName(String ownerName);
    @Override
    Optional<Book> findById(Long aLong);

    @Override
    void deleteById(Long aLong);
}
