package com.nyist.dao;

import com.nyist.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserMapper {

    /**
     * 用户注册
     * @param user
     * @return
     */
    public Integer addUser(User user);

    /**
     * 查重
     * @param user
     * @return
     */
    public Integer findUserName(User user);

    /**
     * 获取id
     * @param username
     * @return
     */
    public Integer findId(String username);

    /**
     * 账号密码登录
     * @param user
     * @return
     */
    public List<Map<String,String>> findUserByUserName(User user);

    /**
     * 查询空间和头像
     * @param id
     * @return
     */
    public User findIndexInfo(Integer id);

    /**
     * 获取文件数量
     * @param user_id
     * @return
     */
    public int findUserFileCounts(Integer user_id);

    /**
     * 上传头像
     * @param user
     * @return
     */
    public Integer uploadAvatar(User user);

    /**
     * 查询原来的头像信息进行后续删除
     * @param id
     * @return
     */
    public User findOldAvatar(Integer id);

    /**
     * 查询个人信息
     * @param id
     * @return
     */
    public User findInfoById(Integer id);

    /**
     * 修改个人信息
     * @param user
     * @return
     */
    public Integer setPersonInfo(User user);
}
