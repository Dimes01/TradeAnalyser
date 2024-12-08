package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
