package io.github.jhipster.jpa2jdl.example.entities.complex;

import javax.persistence.Access;
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
public class MoneyEntity {

    @GeneratedValue(strategy = SEQUENCE) @Id
    private Long id;
}
