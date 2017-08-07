package io.github.jhipster.jpa2jdl.example.entities.relations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Author.
 */
@Entity
@Table(name = "author")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(unique = true)
    private AuthorHome home;

    @OneToOne
    @JoinColumn(unique = true)
    private AuthorAccess access;

    @OneToMany(mappedBy = "identity")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Alias> aliases = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Author name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuthorHome getHome() {
        return home;
    }

    public Author home(AuthorHome authorHome) {
        this.home = authorHome;
        return this;
    }

    public void setHome(AuthorHome authorHome) {
        this.home = authorHome;
    }

    public AuthorAccess getAccess() {
        return access;
    }

    public Author access(AuthorAccess authorAccess) {
        this.access = authorAccess;
        return this;
    }

    public void setAccess(AuthorAccess authorAccess) {
        this.access = authorAccess;
    }

    public Set<Alias> getAliases() {
        return aliases;
    }

    public Author aliases(Set<Alias> aliases) {
        this.aliases = aliases;
        return this;
    }

    public Author addAlias(Alias alias) {
        this.aliases.add(alias);
        alias.setIdentity(this);
        return this;
    }

    public Author removeAlias(Alias alias) {
        this.aliases.remove(alias);
        alias.setIdentity(null);
        return this;
    }

    public void setAliases(Set<Alias> aliases) {
        this.aliases = aliases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Author author = (Author) o;
        if (author.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), author.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Author{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
