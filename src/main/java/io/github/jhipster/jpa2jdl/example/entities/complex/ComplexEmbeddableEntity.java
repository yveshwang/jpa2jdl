package io.github.jhipster.jpa2jdl.example.entities.complex;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Embeddable
public class ComplexEmbeddableEntity {

    @Transient
    private String transientString;

    private BigDecimal amount;

    @Embedded
    private SimpleEmbeddableEntity innerEntity;

    private String amountLabel;

    public ComplexEmbeddableEntity() {
        //HB
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public SimpleEmbeddableEntity getInnerEntity() {
        return innerEntity;
    }

    public String getAmountLabel() {
        return amountLabel;
    }
}
