package com.design.bs.core.utils;

import com.design.bs.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 */
@Component
public class PasswordHelper {
	private final static String specialArr[]= {"!","@","#","$","%","^",".","*","(",")"};

    //实例化RandomNumberGenerator对象，用于生成一个随机数
    @Getter
    @Setter
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    //加密算法
    public void encryptPassword(User user) {
        if (user.getPassword() != null) {
            // Shiro会根据数据库中储存的盐值以及你注入的加密方式进行校验。
            // 对user对象设置盐：salt；这个盐值是randomNumberGenerator生成的随机数
            user.setSalt(randomNumberGenerator.nextBytes().toHex());

            //调用SimpleHash指定散列算法参数：1、算法名称；2、用户输入的密码；3、盐值（随机生成的）；4、迭代次数
            String newPassword = new SimpleHash(
                    SysConstant.ALGORITHNAME,
                    user.getPassword(),
                    ByteSource.Util.bytes(user.getSalt()),
                    SysConstant.HASHNUM).toHex();
            user.setPassword(newPassword);
        }
    }
    
    //重置密码
    public String getResetPassword(int length) {
    	Random r=new Random();
    	StringBuilder password=new StringBuilder();
    	for (int i = 0; i < length; i++) {
			int num = r.nextInt(4);
			password.append(getStr(num));
		}
    	return password.toString();
    }
    
    //单个字符
    public String getStr(int num) {
    	Random r=new Random();
    	if(num==0) {
    		//数字
    		return r.nextInt(10)+"";
    	}else if(num==1) {
    		//特殊符号
    		return specialArr[r.nextInt(10)];
    	}else if(num==2) {
    		//大写字母
    		return String.valueOf((char)(65+r.nextInt(26)));
    	}else if(num==3) {
    		//小写字母
    		return String.valueOf((char)(97+r.nextInt(26)));
    	}
    	return "";
    }
    

    /**
     * 获取加密之后的密码
     * @param password 明文密码
     * @param salt 盐值
     * @return
     */
    public String getPassword(String password,String salt) {
    	return new SimpleHash(
                SysConstant.ALGORITHNAME,
                password,
                ByteSource.Util.bytes(salt),
                SysConstant.HASHNUM).toHex();
    }
    
    
    public static void main(String[] args) {
    	String newPassword = new SimpleHash(
                SysConstant.ALGORITHNAME,
                "777",
                ByteSource.Util.bytes("754c41b6ff67f944d060bd25b507fd2e"),
                SysConstant.HASHNUM).toHex();
    	System.out.println(newPassword);
	}

}
