package org.comu;

public class Musteri {
    private String id;
    private String ad;
    private String soyad;
    private String email;
    private String telefon;

    public Musteri() {}

    public Musteri(String ad, String soyad, String email, String telefon) {
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.telefon = telefon;
    }

    public Musteri(String id, String ad, String soyad, String email, String telefon) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.telefon = telefon;
    }

    // Getter-setter'lar
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }

    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    @Override
    public String toString() {
        return id + " - " + ad + " " + soyad + " - " + email + " - " + telefon;
    }
}
