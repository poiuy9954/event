package com.kims.coreevent.eventtst;

import com.kims.coreevent.event.DomainEvent;

public class TestDTO extends DomainEvent {

    private String id;
    private String name;

    public TestDTO() {
        super();
    }

    public TestDTO(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
