package edu.nesl.maxient;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

/**
 * Mapping insipred by the Maxient demographic feed
 */

//TODO potentially useful as entity for other programs

public class Student {

    private String id;

    private String authenticationToken = "";

    private String lastName = "";

    private String firstName = "";

    private String middleName = "";

    private String preferredName = "";

    private String birthDate;

    private String gender = "";

    private String ethnicity = "";

    private String housing = "Off Campus";

    private String housingRoomNumber = "";

    private String localMailingAddress = "";

    private String localCity = "";

    private String localState = "";

    private String localZip = "";

    private String localPhone = "";

    private String cellPhone = "";

    private String permanentAddress = "";

    private String permanetCity = "";

    private String permanentState = "";

    private String permanentZip = "";

    private String permanentCountry = "";

    private String permanetPhone = "";

    private String emergencyContact = "";

    private String emailAddress = "";

    private String classifcation = "";

    private String academicMajor = "";

    private String academicAdvisor = "";

    private String gpaMostRecentTerm = "";

    private String gpaCumulative = "";

    private String athleticMember = "N/A";

    private String greekMember = "N/A";

    private String honorsMember = "N/A";

    private String rotcNumber = "N/A";

    private String honorfic = "";

    private final String pronouns = "";

    private final String lastUpdate = LocalDate.now().toString();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getHousing() {
        return housing;
    }

    public void setHousing(String housing) {
        this.housing = housing;
    }

    public String getHousingRoomNumber() {
        return housingRoomNumber;
    }

    public void setHousingRoomNumber(String housingRoomNumber) {
        this.housingRoomNumber = housingRoomNumber;
    }

    public String getLocalMailingAddress() {
        return localMailingAddress;
    }

    public void setLocalMailingAddress(String localMailingAddress) {
        this.localMailingAddress = localMailingAddress;
    }

    public String getLocalCity() {
        return localCity;
    }

    public void setLocalCity(String localCity) {
        this.localCity = localCity;
    }

    public String getLocalState() {
        return localState;
    }

    public void setLocalState(String localState) {
        this.localState = localState;
    }

    public String getLocalZip() {
        return localZip;
    }

    public void setLocalZip(String localZip) {
        this.localZip = localZip;
    }

    public String getLocalPhone() {
        return localPhone;
    }

    public void setLocalPhone(String localPhone) {
        this.localPhone = localPhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPermanetCity() {
        return permanetCity;
    }

    public void setPermanetCity(String permanetCity) {
        this.permanetCity = permanetCity;
    }

    public String getPermanentState() {
        return permanentState;
    }

    public void setPermanentState(String permanentState) {
        this.permanentState = permanentState;
    }

    public String getPermanentZip() {
        return permanentZip;
    }

    public void setPermanentZip(String permanentZip) {
        this.permanentZip = permanentZip;
    }

    public String getPermanentCountry() {
        return permanentCountry;
    }

    public void setPermanentCountry(String permanentCountry) {
        this.permanentCountry = permanentCountry;
    }

    public String getPermanetPhone() {
        return permanetPhone;
    }

    public void setPermanetPhone(String permanetPhone) {
        this.permanetPhone = permanetPhone;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getClassifcation() {
        return classifcation;
    }

    public void setClassifcation(String classifcation) {
        this.classifcation = classifcation;
    }

    public String getAcademicMajor() {
        return academicMajor;
    }

    public void setAcademicMajor(String academicMajor) {
        this.academicMajor = academicMajor;
    }

    public String getAcademicAdvisor() {
        return academicAdvisor;
    }

    public void setAcademicAdvisor(String academicAdvisor) {
        this.academicAdvisor = academicAdvisor;
    }

    public String getGpaMostRecentTerm() {
        return gpaMostRecentTerm;
    }

    public void setGpaMostRecentTerm(String gpaMostRecentTerm) {
        this.gpaMostRecentTerm = gpaMostRecentTerm;
    }

    public String getGpaCumulative() {
        return gpaCumulative;
    }

    public void setGpaCumulative(String gpaCumulative) {
        this.gpaCumulative = gpaCumulative;
    }

    public String getAthleticMember() {
        return athleticMember;
    }

    public void setAthleticMember(String athleticMember) {
        this.athleticMember = athleticMember;
    }

    public String getGreekMember() {
        return greekMember;
    }

    public void setGreekMember(String greekMember) {
        this.greekMember = greekMember;
    }

    public String getHonorsMember() {
        return honorsMember;
    }

    public void setHonorsMember(String honorsMember) {
        this.honorsMember = honorsMember;
    }

    public String getRotcNumber() {
        return rotcNumber;
    }

    public void setRotcNumber(String rotcNumber) {
        this.rotcNumber = rotcNumber;
    }

    public String getHonorfic() {
        return honorfic;
    }

    public void setHonorfic(String honorfic) {
        this.honorfic = honorfic;
    }

    public String getPronouns() {
        return pronouns;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }
}
