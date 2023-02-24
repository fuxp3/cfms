package com.design.bs.action.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@ToString
@Table(name = "t_action_collect")
public class ActionCollect implements Serializable {
    @Id
    private Long id;

    private String usercode;

    private Long actionId;

}
