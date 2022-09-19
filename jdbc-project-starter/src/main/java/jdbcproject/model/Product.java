package uz.abbos.jdbcproject.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private String pid;
    private String pname;
    private String color;
    private Integer size;
    private String store;
}
