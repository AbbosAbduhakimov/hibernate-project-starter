//package com.abbos.entitiy;
//
//import lombok.*;
//
//import javax.persistence.DiscriminatorValue;
//import javax.persistence.Entity;
//import java.util.List;
//import java.util.Set;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@DiscriminatorValue("manager")
//public class Manager extends User{
//
//    private String projectName;
//
//
//
//    @Builder
//    public Manager(String username, String firstname, String lastname, BirthDay birthDate, Integer age, Role role, String infoUser, PersonalInfo personalInfo, Company company, Long companyId, Profile profile, Set<UserChat> userChats, List<Payment> payments, String projectName) {
//        super(username, firstname, lastname, birthDate, age, role, infoUser, personalInfo, company, companyId, profile, userChats, payments);
//        this.projectName = projectName;
//    }
//}
