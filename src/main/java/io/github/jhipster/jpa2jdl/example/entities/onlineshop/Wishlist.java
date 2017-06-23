package io.github.jhipster.jpa2jdl.example.entities.onlineshop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Wishlist.
 */
@Entity
@Table(name = "wishlist")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Wishlist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "jhi_restricted")
    private Boolean restricted;

    @OneToMany(mappedBy = "wishlist")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Wishlist title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isRestricted() {
        return restricted;
    }

    public Wishlist restricted(Boolean restricted) {
        this.restricted = restricted;
        return this;
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Wishlist products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Wishlist addProduct(Product product) {
        this.products.add(product);
        product.setWishlist(this);
        return this;
    }

    public Wishlist removeProduct(Product product) {
        this.products.remove(product);
        product.setWishlist(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Wishlist customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Wishlist wishlist = (Wishlist) o;
        if (wishlist.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wishlist.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Wishlist{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", restricted='" + isRestricted() + "'" +
            "}";
    }
}
