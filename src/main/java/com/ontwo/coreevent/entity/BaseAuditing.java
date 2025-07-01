package com.ontwo.coreevent.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditing {

        @CreatedDate
        @Column(name = "CREATED_AT"
                , nullable = false
                , updatable = false
                , columnDefinition = "TIMESTAMP")
        protected LocalDateTime createdAt;

        @LastModifiedDate
        @Column(name = "UPDATED_AT"
                , nullable = false
                , columnDefinition = "TIMESTAMP")
        protected LocalDateTime updatedAt;

        @CreatedBy
        @Column(name = "CREATED_BY"
                , nullable = false
                , updatable = false
                , length = 255)
        protected String createdBy;

        @LastModifiedBy
        @Column(name = "UPDATED_BY"
                , nullable = false
                , length = 255)
        protected String updatedBy;
}