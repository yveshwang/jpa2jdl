package com.wordpress.macyves.test.unit.entities;

import javax.persistence.Access;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by yveshwang on 22/06/2017.
 */
@Entity
@Access(FIELD)
public class SimpleEntity {
    @GeneratedValue(strategy = SEQUENCE) @Id
    private Long id;
    private int blah;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SimpleEntity(final int blah) {
        this.blah = blah;
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getBlah() {
        return blah;
    }

    public void setBlah(int blah) {
        this.blah = blah;
    }
}
