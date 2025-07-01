package com.ontwo.coreevent.eventtst;

import com.ontwo.coreevent.listener.DomainEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional("eventTransactionManager")
@Component
public class TestDomainEventHandler2 implements DomainEventHandler<TestDTO2> {

    private static final Logger log = LoggerFactory.getLogger(TestDomainEventHandler2.class);

    @Override
    public void handle(TestDTO2 testDTO2) {
        System.out.println("TestDomainEventHandler2.handle() called with: testDTO2 = [" + testDTO2.getEventId() + "]");
        //서비스로직 후처리 로직
        log.debug("Handling TestDTO event: {}, {}", testDTO2.getId(), testDTO2.getName());
        //APPLY VO HIST 저장로직~
    }

    @Override
    public Class<TestDTO2> getEventType() {
        return TestDTO2.class;
    }
}
