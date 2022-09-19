package com.abbos.entitiy;

import lombok.*;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PersonalInfo {

    private String contact;

    private String email;

    private LocalDate visitDate;


}
