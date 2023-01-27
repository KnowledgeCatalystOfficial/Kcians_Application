package com.vishugahlot.kcians;

public class Community_data {
    String CName,CImage;
    public Community_data()
    {

    }
    public Community_data(String CName, String CImage) {
        this.CName = CName;
        this.CImage = CImage;
    }

    public String getCName() {
        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }

    public String getCImage() {
        return CImage;
    }

    public void setCImage(String CImage) {
        this.CImage = CImage;
    }
}
