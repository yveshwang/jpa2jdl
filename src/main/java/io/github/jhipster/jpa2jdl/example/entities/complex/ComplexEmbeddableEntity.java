package io.github.jhipster.jpa2jdl.example.entities.complex;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class ComplexEmbeddableEntity {

    @Transient
    private String transientString;

    private BigDecimal amount;

    @Embedded
    private SimpleEmbeddableEntity innerEntity;

    private String amountLabel;

}
