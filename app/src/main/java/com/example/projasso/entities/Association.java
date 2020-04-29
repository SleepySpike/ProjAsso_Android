package com.example.projasso.entities;

public class Association {

    private int idAssociation;
    private String nom;
    private double montantCotisation ;
    private String activite ;
    private String telephone ;
    private String email ;
    private boolean status;

    public int getIdAssociation() {
        return idAssociation;
    }

    public void setIdAssociation(int idAssociation) {
        this.idAssociation = idAssociation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getMontantCotisation() {
        return montantCotisation;
    }

    public void setMontantCotisation(double montantCotisation) {
        this.montantCotisation = montantCotisation;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Association(int idAssociation, String nom, double montantCotisation, String activite, String telephone, String email, boolean status) {
        this.idAssociation = idAssociation;
        this.nom = nom;
        this.montantCotisation = montantCotisation;
        this.activite = activite;
        this.telephone = telephone;
        this.email = email;
        this.status = status;
    }
}
