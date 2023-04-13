package com.example.pastebin.repository;

import com.example.pastebin.model.Paste;
import com.example.pastebin.repository.projection.PasteProjection;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PasteRepository extends JpaRepository<Paste, Long>, JpaSpecificationExecutor<Paste> {
    @Query(value = "SELECT name,text\n" +
            "FROM paste\n" +
            "WHERE paste.access like 'PUBLIC'\n" +
            "ORDER BY created_date DESC\n" +
            "limit 10", nativeQuery = true)
    List<PasteProjection> getLast10Pastes();

    List<Paste> findAll(Specification<Paste> spec);

    void deleteAllByExpiredDateIsBefore(Instant now);

}
