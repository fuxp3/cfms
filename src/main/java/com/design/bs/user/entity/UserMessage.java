package com.design.bs.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;
import java.util.Set;

/**
 * @description: 登陆成功响应信息封装
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserMessage {

	private String token;
	private Set<String> roleSet;
	private Set<String> permSet;
	private Map<String,Object> btnName;
	private User user;

}
