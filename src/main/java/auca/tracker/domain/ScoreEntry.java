package auca.tracker.domain;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "score_entry")
public class ScoreEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "component")
    private String component;

    @Column(name = "comment")
    private String comment;

    @Column(name = "score")
    private int score;

    @Column(name = "entered_date")
    private LocalDate enteredDate;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    public ScoreEntry() {
    }

    public ScoreEntry(String component, String comment, int score, LocalDate enteredDate, Assessment assessment) {
        this.component = component;
        this.comment = comment;
        this.score = score;
        this.enteredDate = enteredDate;
        this.assessment = assessment;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDate getEnteredDate() {
        return enteredDate;
    }

    public void setEnteredDate(LocalDate enteredDate) {
        this.enteredDate = enteredDate;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }
}
