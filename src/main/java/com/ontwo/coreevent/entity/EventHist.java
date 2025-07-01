package com.ontwo.coreevent.entity;


import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "EVENT_HIST")
@EntityListeners(AuditingEntityListener.class)
public class EventHist extends BaseAuditing{

    @Id
    @Column(name = "ID", length = 36, nullable = false)
    @Comment("이벤트 ID")
    private String id;

    @Column(name = "EVENT_TYPE", length = 255, nullable = false)
    @Comment("이벤트 타입(payload 객체타입)")
    private String eventType;

    @Lob
    @Column(name = "PAYLOAD", nullable = false)
    @Comment("이벤트 페이로드")
    private String payload;

    @Column(name = "EVENT_CREATE_TIME", nullable = false)
    @Comment("이벤트 생성 시간")
    private LocalDateTime eventCreateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "EVENT_STATUS", length = 50, nullable = false)
    @Comment("이벤트 상태")
    private EventStatus eventStatus;

    @Column(name = "RESULT_MSG", length = 255)
    @Comment("이벤트 처리 결과 메시지")
    private String resultMsg;

    @Column(name = "PROCESS_YN", nullable = false)
    @Comment("이벤트 처리 여부 (Y/N)")
    private boolean processYn;

    public enum EventStatus {
        PENDING, // 대기 - 이벤트가 생성되었지만 아직 listener에 의해 처리되지 않은 상태
        PROCESSING, // 처리중 - 이벤트가 listener에 의해 처리중인 상태
        COMPLETED, // 완료 - 이벤트가 listener에 의해 성공적으로 처리된 상태
        FAILED // 실패
    }


    public static Builder builder(){
        return new Builder();
    }
    public Builder toBuilder() {
        return new Builder()
                .id(this.id)
                .eventType(this.eventType)
                .payload(this.payload)
                .eventCreateTime(this.eventCreateTime)
                .eventStatus(this.eventStatus)
                .resultMsg(this.resultMsg)
                .processYn(this.processYn);
    }

    public static class Builder {
        private final EventHist eventHist;

        public Builder() {
            eventHist = new EventHist();
        }

        public Builder id(String id) {
            eventHist.id = id;
            return this;
        }

        public Builder eventCreateTime(LocalDateTime eventCreateTime) {
            eventHist.eventCreateTime = eventCreateTime;
            return this;
        }

        public Builder eventType(String eventType) {
            eventHist.eventType = eventType;
            return this;
        }

        public Builder payload(String payload) {
            eventHist.payload = payload;
            return this;
        }

        public Builder eventStatus(EventStatus eventStatus) {
            eventHist.eventStatus = eventStatus;
            return this;
        }

        public Builder resultMsg(String resultMsg) {
            eventHist.resultMsg = resultMsg;
            return this;
        }

        public Builder processYn(boolean processYn) {
            eventHist.processYn = processYn;
            return this;
        }

        public EventHist build() {
            return eventHist;
        }
    }

    public String getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPayload() {
        return payload;
    }

    public LocalDateTime getEventCreateTime() {
        return eventCreateTime;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public boolean isProcessYn() {
        return processYn;
    }

    @Override
    public String toString() {
        return "EventHist{" +
                "id='" + id + '\'' +
                ", eventType='" + eventType + '\'' +
                ", payload='" + payload + '\'' +
                ", eventCreateTime=" + eventCreateTime +
                ", eventStatus=" + eventStatus +
                ", resultMsg='" + resultMsg + '\'' +
                ", processYn=" + processYn +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
