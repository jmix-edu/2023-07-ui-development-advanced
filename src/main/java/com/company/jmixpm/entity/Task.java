package com.company.jmixpm.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@JmixEntity
@Table(name = "TASK_", indexes = {
        @Index(name = "IDX_TASK__ON_ASSIGNEE", columnList = "ASSIGNEE_ID"),
        @Index(name = "IDX_TASK__ON_PROJECT", columnList = "PROJECT_ID")
})
@Entity(name = "Task_")
public class Task {

    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @InstanceName
    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @JoinColumn(name = "ASSIGNEE_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User assignee;

    @Column(name = "START_DATE")
    private LocalDateTime startDate;

    @Column(name = "ESTIMATED_EFFORTS")
    private Integer estimatedEfforts;

    @JoinColumn(name = "PROJECT_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Project project;

    @DependsOnProperties({"project", "name"})
    @JmixProperty
    public String getCaption() {
        StringBuilder stringBuilder = new StringBuilder();
        if (project != null) {
            stringBuilder.append("[").append(project.getName()).append("] ");
        }

        return stringBuilder.append(name).toString();
    }

    @DependsOnProperties({"estimatedEfforts", "startDate"})
    @JmixProperty
    public LocalDateTime getEndDate() {
        if (startDate == null) {
            return null;
        }

        if (estimatedEfforts == null) {
            return startDate.plus(1, ChronoUnit.HOURS);
        }

        return startDate.plus(estimatedEfforts, ChronoUnit.HOURS);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Integer getEstimatedEfforts() {
        return estimatedEfforts;
    }

    public void setEstimatedEfforts(Integer estimatedEfforts) {
        this.estimatedEfforts = estimatedEfforts;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}