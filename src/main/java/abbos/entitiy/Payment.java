package com.abbos.entitiy;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Payment extends BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private Integer amount;


    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id")
    private User receiver;
}
