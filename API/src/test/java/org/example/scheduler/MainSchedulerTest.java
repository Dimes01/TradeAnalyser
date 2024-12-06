package org.example.scheduler;

import org.example.repositories.UserRepository;
import org.example.repositories.AnalyseRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
public class MainSchedulerTest {
    private Logger logger = LoggerFactory.getLogger(MainSchedulerTest.class);
    @Autowired private MainScheduler scheduler;
    @Autowired private UserRepository userRepository;
    @Autowired private AnalyseRepository analyseRepository;

    @Test
    public void mainTest() {
//        scheduler.updateAll();
//        assertFalse(userRepository.findAll().isEmpty());
//        assertFalse(analyseRepository.findAll().isEmpty());
//        analyseRepository.findAll().forEach((analyse -> {
//            logger.info(analyse.toString());
//        }));
    }
}
