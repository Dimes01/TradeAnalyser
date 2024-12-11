package org.example.repositories;

import org.example.entities.LatestAnalyse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LatestAnalyseRepository extends JpaRepository<LatestAnalyse, Long> {
    List<LatestAnalyse> findByAccountId(String accountId);
    LatestAnalyse findByAccountIdAndSecuritiesUid(String accountId, String securitiesUid);
}
