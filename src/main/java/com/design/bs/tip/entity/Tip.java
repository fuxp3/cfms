package com.design.bs.tip.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@ToString
@Table(name = "t_tip")
public class Tip implements Serializable {

    @Id
    private Long id;

    private String title;

    private String tip;
}
