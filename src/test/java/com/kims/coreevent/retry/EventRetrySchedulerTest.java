package com.kims.coreevent.retry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kims.coreevent.entity.EventHist;
import com.kims.coreevent.eventtst.TestDTO;
import com.kims.coreevent.publisher.DomainEventPublisher;
import com.kims.coreevent.service.EventHistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRetrySchedulerTest {

    @Mock
    private EventHistService eventHistService;

    @Mock
    private DomainEventPublisher publisher;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EventRetryScheduler eventRetryScheduler;

    @Test
    void run_정상적으로_이벤트를_재처리한다() throws Exception {
        EventHist eventHist = EventHist.builder()
                .id("1")
                .eventType("com.ontwo.coreevent.eventtst.TestDTO")
                .payload("{\"id\":\"1\",\"name\":\"test\"}")
                .eventCreateTime(LocalDateTime.now().minusMinutes(11))
                .eventStatus(EventHist.EventStatus.PENDING)
                .processYn(false)
                .build();
        TestDTO testDTO = new TestDTO("1", "test");

        when(eventHistService.findEventHistBy10MinBefore()).thenReturn(List.of(eventHist));
        when(objectMapper.readValue(eventHist.getPayload(), Class.forName(eventHist.getEventType()))).thenReturn(testDTO);

        eventRetryScheduler.run();

        verify(publisher).publish(testDTO);
    }

    @Test
    void run_미처리_이벤트가_없으면_아무것도_하지_않는다() throws Exception {
        when(eventHistService.findEventHistBy10MinBefore()).thenReturn(List.of());

        eventRetryScheduler.run();

        verifyNoInteractions(publisher);
    }

    @Test
    void run_역직렬화_실패시_예외를_던지지_않고_처리한다() throws Exception {
        EventHist eventHist = EventHist.builder()
                .id("2")
                .eventType("com.ontwo.coreevent.eventtst.TestDTO")
                .payload("{\"id\":\"2\",\"name\":\"fail\"}")
                .build();

        when(eventHistService.findEventHistBy10MinBefore()).thenReturn(List.of(eventHist));
        when(objectMapper.readValue(anyString(), any(Class.class))).thenThrow(JsonProcessingException.class);

        assertDoesNotThrow(() -> eventRetryScheduler.run());
        verifyNoInteractions(publisher);
    }

    @Test
    void run_이벤트_클래스가_존재하지_않으면_예외를_던지지_않고_처리한다() throws Exception {
        EventHist eventHist = EventHist.builder()
                .id("3")
                .eventType("com.ontwo.coreevent.eventtst.NotExistDTO")
                .payload("{\"id\":\"3\",\"name\":\"notfound\"}")
                .build();

        when(eventHistService.findEventHistBy10MinBefore()).thenReturn(List.of(eventHist));

        assertDoesNotThrow(() -> eventRetryScheduler.run());
        verifyNoInteractions(publisher);
    }
}