package io.github.jhipster.jpa2jdl.example.entities.complex;

import javax.persistence.Embeddable;

@Embeddable
public class SimpleEmbeddableEntity {

    private String label;
    private Integer number;

    public SimpleEmbeddableEntity() {
        //HB
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getLabel() {
        return label;
    }

    public Integer getNumber() {
        return number;
    }
}
