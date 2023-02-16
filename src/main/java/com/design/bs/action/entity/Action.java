package com.design.bs.action.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@ToString
@Table(name = "t_action")
public class Action implements Serializable {
    @Id
    private Long id;

    private String type;

    private String name;

    private String uid;
}
