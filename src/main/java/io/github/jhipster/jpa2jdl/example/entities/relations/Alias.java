package io.github.jhipster.jpa2jdl.example.entities.relations;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Alias.
 */
@Entity
@Table(name = "alias")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Alias implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @ManyToOne
    private Author identity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public Alias nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Author getIdentity() {
        return identity;
    }

    public Alias identity(Author author) {
        this.identity = author;
        return this;
    }

    public void setIdentity(Author author) {
        this.identity = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Alias alias = (Alias) o;
        if (alias.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), alias.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Alias{" +
            "id=" + getId() +
            ", nickname='" + getNickname() + "'" +
            "}";
    }
}
