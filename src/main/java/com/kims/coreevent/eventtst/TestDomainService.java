package com.kims.coreevent.eventtst;

import com.kims.coreevent.publisher.DomainEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Transactional("eventTransactionManager")
@Service
public class TestDomainService {

    private final DomainEventPublisher publisher;

    public TestDomainService(DomainEventPublisher publisher) {
        this.publisher = publisher;
    }


    public void test(){
        //ApplyVO 변경 ~~~ 비즈니스 로직~~~

//       원부 생성
//        원부 history 이벤트 생성
        publisher.publish(new TestDTO("1", "ApplyVO_HIST"));
        publisher.publish(new TestDTO2("2", "몰라몰라"));
        publisher.publish(new TestDTO2("3", "ㅅㄷㄷ"));
        publisher.publish(new TestDTO("4", "fferer"));

        Stream.of(10).parallel().forEach(
            i -> IntStream.range(0, 1000).forEach(
                j -> {
                    if(j % 2 == 0)
                        publisher.publish(new TestDTO2(String.valueOf(j), "TestDomainService.test()"));
                    else
                        publisher.publish(new TestDTO(String.valueOf(j), "TestDomainService.test()"));
                }
            )
        );



    }
}
