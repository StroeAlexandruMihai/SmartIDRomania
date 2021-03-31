package com.example.smartidromania;

public class Person {

    public String firstName, lastName, address, medical, finance, police, gender, bDay, driveLicence, idKey, studies,mImageUrl;

    public Person(){}



    public Person(String firstName, String lastName, String address, String medical, String finance, String police, String gender, String bDay, String driveLicence, String idKey, String studies, String mImageUrl){

        this.firstName = firstName;
        this.lastName = lastName;
        this.medical = medical;
        this.finance = finance;
        this.police = police;
        this.gender = gender;
        this.bDay = bDay;
        this.driveLicence = driveLicence;
        this.idKey = idKey;
        this.studies = studies;
        this.mImageUrl = mImageUrl;
        this.address = address;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMedical() {
        return medical;
    }

    public void setMedical(String medical) {
        this.medical = medical;
    }

    public String getFinance() {
        return finance;
    }

    public void setFinance(String finance) {
        this.finance = finance;
    }

    public String getPolice() {
        return police;
    }

    public void setPolice(String police) {
        this.police = police;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getbDay() {
        return bDay;
    }

    public void setbDay(String bDay) {
        this.bDay = bDay;
    }

    public String getDriveLicence() {
        return driveLicence;
    }

    public void setDriveLicence(String driveLicence) {
        this.driveLicence = driveLicence;
    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public String getStudies() {
        return studies;
    }

    public void setStudies(String studies) {
        this.studies = studies;
    }


}


