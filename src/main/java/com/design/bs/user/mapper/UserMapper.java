package com.design.bs.user.mapper;

import com.design.bs.user.entity.User;
import com.design.bs.user.entity.UserWithRole;
import com.design.bs.core.basemapper.MyMapper;
import com.design.bs.menu.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserMapper extends MyMapper<User> {

    List<User> queryList(User user);

    List<UserWithRole> findById(Long id);
    
    List<String> getUserRoleNames(Long id);
    
    List<Menu> getUserMenuAndButtons(Long id);

    List<User> achievementRank(@Param("departmentId") Long departmentId, @Param("dealDateBegin") Date dealDateBegin, @Param("dealDateEnd")Date dealDateEnd);

    List<User> achievementRankAll(@Param("dealDateBegin") Date dealDateBegin, @Param("dealDateEnd")Date dealDateEnd);
}
