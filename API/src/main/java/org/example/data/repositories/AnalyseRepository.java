package org.example.data.repositories;

import org.example.data.entities.Analyse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyseRepository extends JpaRepository<Analyse, Long> {
}
