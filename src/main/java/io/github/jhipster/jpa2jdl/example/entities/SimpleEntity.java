package io.github.jhipster.jpa2jdl.example.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @NotNull
    @Size(max = 10)
    @Column(name = "postcode", length = 10, nullable = false)
    private String postcode;

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

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
