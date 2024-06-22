package com.nyist.service.Impl;

import com.nyist.dao.UserMapper;
import com.nyist.model.User;
import com.nyist.service.UserService;
import com.nyist.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Md5Utils md5Utils;
    /**
     * 注册
     * @param user
     * @return
     */
    @Override
    public Integer addUser(User user) throws ParseException {
        //获取服务端自动生成的盐
        String salt = md5Utils.getRandomSalt();
        //获取系统当前时间
        //生成日期对象
        Date current_date = new Date();
        //设置日期格式化样式为：yyyy-MM-dd
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String regdate = simpleDateFormat.format(current_date);
        //格式化当前日期
        //输出测试一下
        System.out.println("当前的系统日期为：" + regdate);
        //设置默认密码
        user.setPassword(md5Utils.toMD5(user.getUsername()+salt));
        //设置默认空间大小
        user.setSpace(6);
        //设置盐
        user.setSalt(salt);
        //设置默认头像路径
        user.setAvatar("/yun/avatar/");
        //设置默认头像
        user.setNewavatarname("0");
        //设置默认昵称
        user.setNickname(user.getUsername());
        //设置注册时间
        user.setRegdate(simpleDateFormat.parse(regdate));
        return this.userMapper.addUser(user);
    }

    /**
     * 查重
     * @param user
     * @return
     */
    @Override
    public Integer findUserName(User user) {
        return this.userMapper.findUserName(user);
    }

    /**
     * 获取id
     * @param username
     * @return
     */
    @Override
    public Integer findId(String username) {
        return this.userMapper.findId(username);
    }

    /**
     * 账号密码登录
     * @param user
     * @return
     */
    @Override
    public List<Map<String, String>> findUserByUserName(User user) {
        return this.userMapper.findUserByUserName(user);
    }

    /**
     * 查询空间和头像
     * @param id
     * @return
     */
    @Override
    public User findIndexInfo(Integer id) {
        return this.userMapper.findIndexInfo(id);
    }

    @Override
    public int findUserFileCounts(Integer user_id) {
        return this.userMapper.findUserFileCounts(user_id);
    }

    /**
     * 上传头像
     * @param user
     * @return
     */
    @Override
    public Integer uploadAvatar(User user) {
        return this.userMapper.uploadAvatar(user);
    }

    /**
     * 查询原来的头像信息，进行后续删除
     * @param id
     * @return
     */
    @Override
    public User findOldAvatar(Integer id) {
        return this.userMapper.findOldAvatar(id);
    }

    /**
     * 查询个人信息
     * @param id
     * @return
     */
    @Override
    public User findInfoById(Integer id) {
        return this.userMapper.findInfoById(id);
    }

    /**
     * 修改个人信息
     * @param user
     * @return
     */
    @Override
    public Integer setPersonInfo(User user) {
        if (!("".equals(user.getPassword()))){
            //获取服务端自动生成的盐
            String salt = md5Utils.getRandomSalt();
            //设置密码
            user.setPassword(md5Utils.toMD5(user.getPassword()+salt));
            //更新盐
            user.setSalt(salt);
        }
        return this.userMapper.setPersonInfo(user);
    }
}
