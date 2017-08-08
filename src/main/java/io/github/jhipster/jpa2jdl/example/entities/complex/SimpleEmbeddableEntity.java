package io.github.jhipster.jpa2jdl.example.entities.complex;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class SimpleEmbeddableEntity {

    private String label;
    private Integer number;

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
