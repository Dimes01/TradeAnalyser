package org.example.data.repositories;

import org.example.data.entities.Securities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecuritiesRepository extends JpaRepository<Securities, Long> {
}
