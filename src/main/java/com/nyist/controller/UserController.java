package com.nyist.controller;


import com.nyist.model.User;
import com.nyist.service.UserService;

import com.nyist.utils.MailTask;
import com.nyist.utils.Md5Utils;
import com.nyist.utils.WebPageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/user")
public class UserController {

    //注入service
    @Autowired
    private UserService userService;
    //注入邮件任务
    @Autowired
    private MailTask mailTask;
    //注入md5加密
    @Autowired
    private Md5Utils md5Utils;

    /**
     * 获取验证码
     * @param mail
     * @return
     */
    @PostMapping("/getmailcode")
    @ResponseBody
    public String getMailCode(String mail,HttpSession session){
        String mailCode = this.mailTask.getMailCode(mail);
        session.setAttribute("code",mailCode);
        return "ok";
    }

    /**
     * 注册登录+验证登录
     * @param user
     * @param code
     * @param model
     * @param session
     * @return
     * @throws ParseException
     */
    @RequestMapping("/loginandreg")
    public String addUser(User user,String code,Model model,HttpSession session) throws ParseException {
        if (!(this.userService.findUserName(user)>0)) {
            //注册
            if (code.equals(session.getAttribute("code"))){
                Integer integer = this.userService.addUser(user);
                Integer id = 0;
                if (integer>0){
                    model.addAttribute("status","success");
                    id = this.userService.findId(user.getUsername());
                }else{
                    model.addAttribute("status","error");
                }
                session.setAttribute("USER_NAME",user.getUsername());
                session.setAttribute("USER_ID",id);
                return "index";
            }
            model.addAttribute("status","验证码错误");
            return "loginandreg";
        }
        if (code.equals(session.getAttribute("code"))){
            Integer id = this.userService.findId(user.getUsername());
            session.setAttribute("USER_NAME",user.getUsername());
            session.setAttribute("USER_ID",id);
            return "index";
        }
        model.addAttribute("status","验证码有误");
        return "loginandreg";
    }

    /**
     * 账号密码登录
     * @param user
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/login")
    public String loginIn(User user,Model model,HttpSession session){
        //不正常退出再次打开默认登录（原理：session未超时默认登录）且仅对当前浏览器有效
        if (session.getAttribute("USER_ID")!=null){
            return "index";
        }
        List<Map<String, String>> userList = this.userService.findUserByUserName(user);
        if (userList.size() == 1){
            String salt = userList.get(0).get("salt");
            String pwd = userList.get(0).get("password");
            Integer id = this.userService.findId(user.getUsername());
            if (pwd.equals(md5Utils.toMD5(user.getPassword()+salt))){
                session.setAttribute("USER_NAME",user.getUsername());
                session.setAttribute("USER_ID",id);
                return "index";
            }
        }
        model.addAttribute("status","用户名或密码有误");
        return "loginandreg";
    }

    /**
     * 获取空间信息（主页展示）
     * @param session
     * @return
     */
    @GetMapping("/findindexinfo")
    @ResponseBody
    public List<String> findIndexInfo(HttpSession session){
        int filecounts = this.userService.findUserFileCounts((Integer) session.getAttribute("USER_ID"));
        User user = this.userService.findIndexInfo((Integer) session.getAttribute("USER_ID"));
        List<String> list = new LinkedList<>();
        //获取使用空间
        float usespace = 0;
        if (filecounts!=0){
            usespace = user.getUsespace();
        }
        //获取总空间
        float space = user.getSpace();
        //获取头像地址
        String avatar = user.getAvatar();
        //获取头像名称
        String newavatarname = user.getNewavatarname();
        //获取昵称
        String nickname = user.getNickname();
        //得到百分比
        String sp = (usespace/(space*1024*1024))*100+"%";

        //设置头像
        if ("0".equals(newavatarname)){
            list.add("/img/tou.jpg");
        }else{
            list.add(avatar+newavatarname);
        }
        //计算结果保留两位小数
        if (usespace<1024){
            list.add(usespace+"KB");
        }else if (usespace>=1024&&usespace<1048576){
            BigDecimal b= new BigDecimal(usespace/1024);
            float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            list.add(f1+"MB");
        }else{
            BigDecimal b= new BigDecimal(usespace/1024/1024);
            float f2 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            list.add(f2+"GB");
        }
        list.add(String.valueOf((long)space));
        list.add(nickname);
        list.add(sp);
        return list;
    }
    /**
     * 获取账号信息
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/personinfo")
    public String findInfoById(HttpSession session,Model model){
        int filecounts = this.userService.findUserFileCounts((Integer) session.getAttribute("USER_ID"));
        User userinfo = this.userService.findInfoById((Integer) session.getAttribute("USER_ID"));
        float usespace = 0;
        if (filecounts!=0){
            usespace = userinfo.getUsespace();
        }

        //计算结果保留两位小数
        if (usespace<1024){
            model.addAttribute("usespace",usespace+"KB");
        }else if (usespace>=1024&&usespace<1048576){
            BigDecimal b= new BigDecimal(usespace/1024);
            float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            model.addAttribute("usespace",f1+"MB");
        }else{
            BigDecimal b= new BigDecimal(usespace/1024/1024);
            float f2 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            model.addAttribute("usespace",f2+"GB");
        }
        model.addAttribute("userInfo",userinfo);
        return "subpage/person";
    }

    /**
     * 上传头像
     * @param avatar
     * @param session
     * @return
     */
    @PostMapping("/uploadavatar")
    @ResponseBody
    public WebPageVo uploadAvatar(@RequestParam("avatar") MultipartFile avatar, HttpSession session){
        WebPageVo webPageVo = new WebPageVo();
        //获取session中的邮箱
        Integer user_id = (Integer) session.getAttribute("USER_ID");
        //获取文件原始名称
        String oldavatarname = avatar.getOriginalFilename();
        //生成新的文件名称
        String newavatarname = user_id +"_"+ UUID.randomUUID().toString().replace("-","") +"_"+ oldavatarname;
        //设置文件上传路径
        String path = "D:\\yunfile\\avatar\\";
        //创建文件对象目录
        File filepath = new File(path);
        if (!filepath.exists()) {
            filepath.mkdirs();
        }
        //首先删除原来的头像
        User oldAvatar = this.userService.findOldAvatar(user_id);
        //创建删除文件对象
        File oldavatarfile = new File("D:\\yunfile\\avatar\\",oldAvatar.getNewavatarname());
        //进行删除
        if (oldavatarfile.exists())oldavatarfile.delete();
        //上传文件
        try {
            avatar.transferTo(new File(path+newavatarname));
            webPageVo.setMsg(newavatarname);
            webPageVo.setCode(0);
        } catch (IOException e) {
            e.printStackTrace();
            webPageVo.setMsg("上传失败,请重新上传");
            webPageVo.setCode(1);
        }
        //将文件信息保存数据库
        User user = new User();
        user.setId(user_id);
        user.setNewavatarname(newavatarname);
        this.userService.uploadAvatar(user);
        return webPageVo;
    }


    /**
     * 修改账号信息
     * @param user
     * @param code
     * @param session
     * @return
     */
    @PostMapping("/setpersoninfo")
    @ResponseBody
    public String setPersonInfo(User user,String code, HttpSession session){
        //获取session中保存的id
        Integer user_id = (Integer) session.getAttribute("USER_ID");
        user.setId(user_id);
        String status = "";
        //如果修改了密码
        if (user.getPassword()!=null && !("".equals(user.getPassword()))){
            if (!(code.equals(session.getAttribute("code")))){
                status = "请检查验证码是否有误";
            }else{
                this.userService.setPersonInfo(user);
                status = "修改成功";
            }
            return status;
        }
        this.userService.setPersonInfo(user);
        status = "修改成功";
        return status;
    }

    /**
     * 登出
     * @param session
     * @return
     */
    @PostMapping("/signout")
    public String signOut(HttpSession session){
        session.removeAttribute("USER_ID");
        session.removeAttribute("USER_NAME");
        session.invalidate();
        return "redirect:/loginandreg";
    }
}