package io.github.jhipster.jpa2jdl.example.entities.blob;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A BFG. Generated entity that does not pass our findbugs. so suppressing.
 *
 * yh
 */
@Entity
@Table(name = "bfg")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SuppressFBWarnings
public class BFG implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Lob
    @Column(name = "jhi_blob", nullable = false)
    private byte[] blob;

    @Column(name = "jhi_blob_content_type", nullable = false)
    private String blobContentType;

    @NotNull
    @Size(max = 3)
    @Lob
    @Column(name = "any_blob", nullable = false)
    private byte[] anyBlob;

    @Column(name = "any_blob_content_type", nullable = false)
    private String anyBlobContentType;

    @Lob
    @Column(name = "image_blob")
    private byte[] imageBlob;

    @Column(name = "image_blob_content_type")
    private String imageBlobContentType;

    @Lob
    @Column(name = "text_blob")
    private String textBlob;

    @Column(name = "instant")
    private Instant instant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getBlob() {
        return blob;
    }

    public BFG blob(byte[] blob) {
        this.blob = blob;
        return this;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }

    public String getBlobContentType() {
        return blobContentType;
    }

    public BFG blobContentType(String blobContentType) {
        this.blobContentType = blobContentType;
        return this;
    }

    public void setBlobContentType(String blobContentType) {
        this.blobContentType = blobContentType;
    }

    public byte[] getAnyBlob() {
        return anyBlob;
    }

    public BFG anyBlob(byte[] anyBlob) {
        this.anyBlob = anyBlob;
        return this;
    }

    public void setAnyBlob(byte[] anyBlob) {
        this.anyBlob = anyBlob;
    }

    public String getAnyBlobContentType() {
        return anyBlobContentType;
    }

    public BFG anyBlobContentType(String anyBlobContentType) {
        this.anyBlobContentType = anyBlobContentType;
        return this;
    }

    public void setAnyBlobContentType(String anyBlobContentType) {
        this.anyBlobContentType = anyBlobContentType;
    }

    public byte[] getImageBlob() {
        return imageBlob;
    }

    public BFG imageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
        return this;
    }

    public void setImageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
    }

    public String getImageBlobContentType() {
        return imageBlobContentType;
    }

    public BFG imageBlobContentType(String imageBlobContentType) {
        this.imageBlobContentType = imageBlobContentType;
        return this;
    }

    public void setImageBlobContentType(String imageBlobContentType) {
        this.imageBlobContentType = imageBlobContentType;
    }

    public String getTextBlob() {
        return textBlob;
    }

    public BFG textBlob(String textBlob) {
        this.textBlob = textBlob;
        return this;
    }

    public void setTextBlob(String textBlob) {
        this.textBlob = textBlob;
    }

    public Instant getInstant() {
        return instant;
    }

    public BFG instant(Instant instant) {
        this.instant = instant;
        return this;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BFG bFG = (BFG) o;
        if (bFG.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bFG.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BFG{" +
            "id=" + getId() +
            ", blob='" + getBlob() + "'" +
            ", blobContentType='" + blobContentType + "'" +
            ", anyBlob='" + getAnyBlob() + "'" +
            ", anyBlobContentType='" + anyBlobContentType + "'" +
            ", imageBlob='" + getImageBlob() + "'" +
            ", imageBlobContentType='" + imageBlobContentType + "'" +
            ", textBlob='" + getTextBlob() + "'" +
            ", instant='" + getInstant() + "'" +
            "}";
    }
}
