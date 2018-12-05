package com.felixpy.codetrial.wishtodo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.felixpy.codetrial.wishtodo.domain.enumeration.TodoStatus;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * A Todo.
 */
@Entity
@Table(name = "todo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Todo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 512)
    @Column(name = "title", length = 512, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TodoStatus status;

    @ManyToOne(optional = false)
    @NotNull
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnoreProperties("todos")
    private Wisher wisher;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Todo title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public Todo status(TodoStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(TodoStatus status) {
        this.status = status;
    }

    public Wisher getWisher() {
        return wisher;
    }

    public Todo wisher(Wisher wisher) {
        this.wisher = wisher;
        return this;
    }

    public void setWisher(Wisher wisher) {
        this.wisher = wisher;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Todo todo = (Todo) o;
        if (todo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), todo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Todo{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
