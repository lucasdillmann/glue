package glue.test.test;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
public class TestEntity {

    @Id
    private UUID id;
    private String name;
    private String description;
    @JsonIgnore
    private OffsetDateTime createdAt;
    @JsonIgnore
    private OffsetDateTime updatedAt;

    public TestEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    private void updateCreatedAt() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    private void updateUpdatedAt() {
        this.updatedAt = OffsetDateTime.now();
    }
}
