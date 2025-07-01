package com.ontwo.coreevent.eventtst;

import com.ontwo.coreevent.listener.DomainEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional("eventTransactionManager")
@Component
public class TestDomainEventHandler implements DomainEventHandler<TestDTO> {

    private static final Logger log = LoggerFactory.getLogger(TestDomainEventHandler.class);

    @Override
    public void handle(TestDTO testDTO) {
        System.out.println("TestDomainEventHandler.handle() called with: testDTO = [" + testDTO.getEventId() + "]");
        //서비스로직 후처리 로직
        log.debug("Handling TestDTO event: {}, {}", testDTO.getId(), testDTO.getName());
        //APPLY VO HIST 저장로직~
    }

    @Override
    public Class<TestDTO> getEventType() {
        return TestDTO.class;
    }
}
