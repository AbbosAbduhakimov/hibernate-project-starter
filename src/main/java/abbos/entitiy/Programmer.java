//package com.abbos.entitiy;
//
//import lombok.*;
//
//import javax.persistence.DiscriminatorValue;
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import java.util.List;
//import java.util.Set;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@DiscriminatorValue("programmer")
//public class Programmer extends User{
//
//    @Enumerated(EnumType.STRING)
//    private Language language;
//
//
//    @Builder
//    public Programmer(String username, String firstname, String lastname, BirthDay birthDate, Integer age, Role role, String infoUser, PersonalInfo personalInfo, Company company, Long companyId, Profile profile, Set<UserChat> userChats, List<Payment> payments, Language language) {
//        super(username, firstname, lastname, birthDate, age, role, infoUser, personalInfo, company, companyId, profile, userChats, payments);
//        this.language = language;
//    }
//}
