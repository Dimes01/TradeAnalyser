package org.example.data.repositories;

import org.example.data.entities.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
    Settings findByAccountId(String accountId);
}
