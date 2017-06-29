package io.github.jhipster.jpa2jdl.example.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.sql.Blob;

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

    @NotNull
    @Size(max = 4)
    @Column(name = "max4", nullable = false)
    private Integer max4;

    @NotNull
    @Size(max = 4)
    @Column(name = "max5", nullable = false)
    @Lob
    private Blob max5;
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

    @NotNull
    @Size(min = 4)
    @Column(name = "min5", nullable = false)
    private Integer min5;

    @NotNull
    @Size(min = 4)
    @Column(name = "min6", nullable = false)
    @Lob
    private Blob min6;
    // -------------

    // field name
    @Column(name = "fiedlname1")
    private String fieldname1;

    @Column(name = "fielname0_blahblah")
    private String fieldname2;
    // -------------

    // blobs --
    @NotNull
    @Size(min = 2)
    @Lob
    @Column(name = "cotnentblob", nullable = false)
    private byte[] contentblob;

    @Lob
    @Column(name = "imageblob")
    private byte[] imageblob;

    @Lob
    @Column(name = "textblob")
    private String textblob;
    // -------------

    //pattern --
    @NotNull
    @Size(min = 5, max = 30)
    @Pattern(regexp = "[\\\\w]*@[a-zA-Z]*.com")
    @Column(name = "email", length = 30, nullable = false)
    private String email;
    // -------------

    @NotNull
    @Size(max = 10)
    @Column(name = "postcode", length = 10, nullable = false)
    private String postcode;

    public int getBlah() {
        return blah;
    }

    public void setBlah(int blah) {
        this.blah = blah;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
