package com.ontwo.coreevent.eventtst;

import com.ontwo.coreevent.repository.EventHistRepository;
import com.ontwo.coreevent.service.EventHistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("sta")
@TestPropertySource(properties = {"spring.config.name=coreEventApp"})
@SpringBootTest
class DomainSaveServiceTest {

    @Autowired
    public TestDomainService service;

    @Autowired
    public EventHistRepository eventHistRepository;

    @Autowired
    public EventHistService eventHistService;

    @Test
    public void testDomainPublisher() throws InterruptedException {

        service.test();

        Thread.sleep(1000); // Wait for events to be processed

        eventHistRepository.findAll().forEach(System.out::println);
    }

    @Test
    public void testee() {
        eventHistService.findEventHistBy10MinBefore()
                .forEach(System.out::println);
        ;
    }

}