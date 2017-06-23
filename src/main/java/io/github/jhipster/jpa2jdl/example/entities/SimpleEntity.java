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

    // not null, all following fields must be "required"
    @NotNull
    @Column(name = "notnull1")
    private Integer notnull1;

    @Column(name = "notnull2", nullable = false)
    private Integer notnull2;

    @NotNull
    @Column(name = "notnull3", nullable = true)
    private Integer notnull3;

    @NotNull
    @Column(name = "notnull4", nullable = false)
    private Integer notnull4;
    // -------------

    // nulls are ok below
    @Column(name = "null1", nullable = true)
    private Double null1;

    @Column(name = "null2")
    private Double null2;
    // -------------

    // max
    @Size(max = 2)
    @Column(name = "max1", length = 2)
    private String max1;

    @Column(name = "max2", length = 2, nullable = false)
    private String max2;

    @Size(max = 2)
    @Column(name = "max3", nullable = false)
    private String max3;
    // -------------

    // min
    @Size(min = 1)
    @Column(name = "min1", length = 2)
    private String min1;

    @Size(min = 6)
    @Column(name = "min2", length = 2, nullable = false)
    private String min2;

    @Size(min = 1, max = 10)
    @Column(name = "min3", nullable = false)
    private String min3;

    @NotNull
    @Size(min = 4)
    @Column(name = "min4", nullable = false)
    private String min4;
    // -------------

    @NotNull
    @Size(max = 10)
    @Column(name = "postcode", length = 10, nullable = false)
    private String postcode;
}
