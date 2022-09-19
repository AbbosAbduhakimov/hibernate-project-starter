package com.abbos.entitiy;

import com.abbos.converter.BirthdayConverter;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@NamedQuery(name = "findUserByName",query = "select u from User u " +
        "join u.company c " +
        "where u.personalInfo.contact = :contact and c.name = :companyname " +
        "order by u.personalInfo.visitDate desc" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
// entity does not final for working CGLIB
// entity should be POJO
@NamedEntityGraph(
        name = "WithCompanyAndChat",
        attributeNodes = {
                @NamedAttributeNode("company"),
                @NamedAttributeNode(value = "userChats", subgraph = "chats")
        },
        subgraphs = {
                @NamedSubgraph(name = "chats", attributeNodes = @NamedAttributeNode("chat"))
        }
)

@FetchProfile(name = "withCompanyAndPayment",fetchOverrides = {
        @FetchProfile.FetchOverride(
                entity = User.class,association = "company",mode = FetchMode.JOIN
        ),
        @FetchProfile.FetchOverride(
                entity = User.class,association = "payments",mode = FetchMode.JOIN
        )
})

@Table(name = "users")
@TypeDef(name = "abbos",typeClass = JsonBinaryType.class)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public class User extends BaseEntity<Long>{
//
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //generator = "user_generator",
//    @SequenceGenerator(name = "user_generator",sequenceName = "users_id_seq",initialValue = 50)
    private Long id;

    private String username;

    private String firstname;

    private String lastname;

    // or can use configuration.addAnnotatedClass(User.class) on configuration
    @Column(name = "birth_date")
    // or can use configuration.addAttributeConverter(new BirthdayConverter(),auto=true) on configuration
    @Convert(converter = BirthdayConverter.class)
    private BirthDay birthDate;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "info_user")
    // or can use above class
//    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonBinaryType")
    @Type(type = "abbos")
    private String infoUser;


    @Embedded // not optional
    @AttributeOverride(name = "visitDate",column = @Column(name = "visit_date"))
    private PersonalInfo personalInfo;

    @ManyToOne(fetch = FetchType.LAZY,cascade = javax.persistence.CascadeType.ALL)
    @JoinColumn(name = "company_id")
//    @Fetch(FetchMode.JOIN) // don't working on ManyToOne
    private Company company;

    @Column(name = "company_id",insertable = false,updatable = false)
    private Long companyId;


    @OneToOne(mappedBy = "user",
            cascade = CascadeType.ALL,
            optional = false)
    private Profile profile;


    @Builder.Default
//    @ManyToMany
//    @JoinTable(
//            name = "users_chat",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "chat_id")
//    )
    @OneToMany
    private Set<UserChat> userChats = new HashSet<>();



    @Builder.Default
    @OneToMany(mappedBy = "receiver")
//    @BatchSize(size = 3)
    @Fetch(FetchMode.SUBSELECT)
    private List<Payment> payments = new ArrayList<>();


}
