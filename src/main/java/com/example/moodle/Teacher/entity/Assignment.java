package com.example.moodle.Teacher.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Assignment<Cours> {
    
    private String assignmentId;
    private Cours cours;
    private String enonce;
    private Date dateLimite;
    private String etat;
    private String remoteId;
    private Date createdAt;
    private Date updatedAt;

    //Modification made by Stefy237
    private String assignName;
    private String courseName;
    private LocalDate openDate;
    private LocalDate dueDate;
    private Integer note = null; // la note obtenue
    private Integer noteMax = null; // sur combien est noté le devoir
    private Byte noteVue = 0; // l'étudiant a vu sa note (1) ou pas (0)
    private Byte ignored = 0; // l'étudiant a ignoré la notification de devoir (1) ou pas (0)
    private Set ressourceDevoirs = new HashSet(0);
    private Set assignmentSubmissions = new HashSet(0);
    private Integer id;

    public Assignment() {
    }

    public void Assignment(String remoteId) {
        this.remoteId = remoteId;
    }

    public void Assignment(Cours cours, String enonce, Date dateLimite, String etat, String remoteId, Date createdAt, Date updatedAt, Set ressourceDevoirs, Set assignmentSubmissions) {
       this.cours = cours;
       this.enonce = enonce;
       this.dateLimite = dateLimite;
       this.etat = etat;
       this.remoteId = remoteId;
       this.createdAt = createdAt;
       this.updatedAt = updatedAt;
       this.ressourceDevoirs = ressourceDevoirs;
       this.assignmentSubmissions = assignmentSubmissions;
    }

    public Assignment(String assignName, String courseName, LocalDate openDate, LocalDate dueDate) {
        this.assignName = assignName;
        this.courseName = courseName;
        this.openDate = openDate;
        this.dueDate = dueDate;
    }

    public String getAssignName() {
        return this.assignName;
    }
    public String getCourseName() {
        return this.courseName;
    }
    public LocalDate getOpenDate() {
        return this.openDate;
    }
    public LocalDate getDueDate() {
        return this.dueDate;
    }
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cours getCours() {
        return this.cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public String getEnonce() {
        return this.enonce;
    }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    public Date getDateLimite() {
        return this.dateLimite;
    }

    public void setDateLimite(Date dateLimite) {
        this.dateLimite = dateLimite;
    }

    public String getEtat() {
        return this.etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getRemoteId() {
        return this.remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set getRessourceDevoirs() {
        return this.ressourceDevoirs;
    }

    public void setRessourceDevoirs(Set ressourceDevoirs) {
        this.ressourceDevoirs = ressourceDevoirs;
    }

    public Set getAssignmentSubmissions() {
        return this.assignmentSubmissions;
    }

    public void setAssignmentSubmissions(Set assignmentSubmissions) {
        this.assignmentSubmissions = assignmentSubmissions;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public Integer getNoteMax() {
        return noteMax;
    }

    public void setNoteMax(Integer noteMax) {
        this.noteMax = noteMax;
    }

    public Byte getNoteVue() {
        return noteVue;
    }

    public void setNoteVue(Byte noteVue) {
        this.noteVue = noteVue;
    }

    public Byte getIgnored() {
        return ignored;
    }

    public void setIgnored(Byte ignored) {
        this.ignored = ignored;
    }
}
