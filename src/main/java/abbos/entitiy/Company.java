package com.abbos.entitiy;


import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
// when used SET
//@EqualsAndHashCode(exclude = "users")
//@ToString(exclude = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Company extends BaseEntity<Long>{



    private String name;

    @Builder.Default // for adding users from builder
    @OneToMany(mappedBy = "company",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();




    // util method for oneToMany method to save
    public void addUser(User user){
        users.add(user);
        user.setCompany(this);
    }

}
