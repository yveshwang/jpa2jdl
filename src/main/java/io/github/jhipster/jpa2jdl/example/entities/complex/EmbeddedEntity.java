package io.github.jhipster.jpa2jdl.example.entities.complex;

import javax.persistence.Access;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author mrozlukasz
 */
@Entity
@Access(FIELD)
public class EmbeddedEntity {

    @GeneratedValue(strategy = SEQUENCE) @Id
    private Long id;

    @Embedded
    private SimpleEmbeddableEntity embeddedEntity;

    @Embedded
    private SimpleEmbeddableEntity embeddedEntity2;

    @Embedded
    private ComplexEmbeddableEntity complexEntity;

}
