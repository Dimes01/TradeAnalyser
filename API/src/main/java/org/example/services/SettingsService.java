package org.example.services;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.CreateSettings;
import org.example.entities.Account;
import org.example.entities.Settings;
import org.example.repositories.SettingsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingsService {
    private final SettingsRepository settingsRepository;

    public List<Settings> findAll() {
        return settingsRepository.findAll();
    }

    public Settings findByAccountId(String accountId) {
        return settingsRepository.findByAccountId(accountId);
    }

    public void createSettings(CreateSettings createSettings, Account account) {
        if (settingsRepository.findByAccountId(createSettings.getAccountId()) != null)
            throw new EntityExistsException("Settings already exists");
        var settings = new Settings(account, createSettings.getRiskFree(), createSettings.getMeanBenchmark());
        settingsRepository.save(settings);
    }

    public boolean updateRiskFree(String accountId, double riskFree) {
        var settings = settingsRepository.findByAccountId(accountId);
        if (settings == null) return false;

        settings.setRiskFree(riskFree);
        settingsRepository.save(settings);
        return true;
    }

    public boolean updateMeanBenchmark(String accountId, double meanBenchmark) {
        var settings = settingsRepository.findByAccountId(accountId);
        if (settings == null) return false;

        settings.setMeanBenchmark(meanBenchmark);
        settingsRepository.save(settings);
        return true;
    }

    public boolean deleteSettings(Long id) {
        var settings = settingsRepository.getReferenceById(id);
        if (settings == null) return false;

        settingsRepository.delete(settings);
        return true;
    }
}
