package com.nyist.controller;

import com.nyist.model.Myfile;
import com.nyist.service.MyfileService;
import com.nyist.utils.FileTypeUtils;
import com.nyist.utils.WebPageVo;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/files")
public class MyfileController {

    @Autowired
    private MyfileService myfileService;
    @Autowired
    private FileTypeUtils fileTypeUtils;

    /**
     * 文件上传
     * @param files
     * @param session
     * @return
     */
    @PostMapping("/uploadfiles")
    @ResponseBody
    public WebPageVo uploadFiles(@RequestParam("files") List<MultipartFile> files, HttpSession session) {
        //获取session中的邮箱
        Integer user_id = (Integer) session.getAttribute("USER_ID");
        //准备数据
        float useSpace = 0;
        //首先判断myfile表是否有数据（防止空指针异常）
        int idCounts = this.myfileService.findIdCounts(user_id);

        WebPageVo webPageVo = new WebPageVo();
        //循环遍历所有文件
        for (MultipartFile file : files) {
            //获取文件原始名称
            String oldfilename = file.getOriginalFilename();
            //获取文件后缀名
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            //生成新的文件名称
            String newfilename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +"_"+ UUID.randomUUID().toString().replace("-","") +"_"+ oldfilename;
            //设置文件上传路径
            String path = "D:\\yunfile\\files\\";
            //获取文件大小(单位kb)
            float size = (((float)file.getSize())/1024);
            //获取文件类型
            String type = file.getContentType();
            //创建文件对象目录
            File filepath = new File(path);
            if (!filepath.exists()) {
                filepath.mkdirs();
            }
            //上传文件
            try {
                if (idCounts>0){//大于零表示有数据，然后执行查询使用空间的sql
                    useSpace = this.myfileService.findUseSpace(user_id);
                }//否则使用默认值
                if ((6291456-useSpace)>size){
                    file.transferTo(new File(path+newfilename)); //核心上传方法
                    webPageVo.setMsg(newfilename);
                    webPageVo.setCode(0);
                }else{
                    webPageVo.setMsg("空间不足");
                    webPageVo.setCode(1);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                webPageVo.setMsg("上传失败,请重新上传");
                webPageVo.setCode(1);
            }
            //将文件信息保存数据库
            Myfile myfile = new Myfile();
            myfile.setOldfilename(oldfilename);myfile.setNewfilename(newfilename);
            myfile.setExt(ext);myfile.setPath("/yun/file/");myfile.setSize(size);
            myfile.setType(type);myfile.setUser_id(user_id);
            try {
                this.myfileService.uploadFiles(myfile);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return webPageVo;
    }
    //获取文件信息
    //执行上传
    //文件信息保存数据库

    /**
     * 获取所有文件
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/findallfiles")
    public String findAllFiles(HttpSession session, Model model) {
        session.removeAttribute("keys");
        Object userIdObject = session.getAttribute("USER_ID");
        if (userIdObject == null || !(userIdObject instanceof Integer)) {
            // 处理用户ID不存在或不是整数的情况
            // 可以返回错误页面，或者设置一个默认值
            return "error/userNotFound"; // 假设存在一个错误页面
        }
        Integer id = (Integer) userIdObject;
        List<Myfile> allFilesList = this.myfileService.findAllFiles(id);
        model.addAttribute("files", allFilesList);
        model.addAttribute("sort", "uploadtime");
        model.addAttribute("sortway", "desc");
        model.addAttribute("sorttip", "按上传时间排序");
        return "subpage/file";
    }


    /**
     * 加载图片页面
     * @param sort
     * @param sortway
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/findallimages")
    public String findAllImages(String sort,
                                   @RequestParam(required = false,defaultValue = "desc")String sortway,
                                   HttpSession session,Model model){
        //保存
        if (sort==null) {
            if (session.getAttribute("sortimg")==null){
                session.setAttribute("sortimg","uploadtime");
            }
        } else {
            session.setAttribute("sortimg",sort);
        }

        Myfile myfile = new Myfile();
        myfile.setUser_id((Integer) session.getAttribute("USER_ID"));
        myfile.setSort((String) session.getAttribute("sortimg"));
        myfile.setSortway(sortway);
        List<Myfile> allImageList = this.myfileService.findAllImages(myfile);
        String sorttip = this.fileTypeUtils.setSortTip((String) session.getAttribute("sortimg"));

        model.addAttribute("files",allImageList);
        model.addAttribute("sort",session.getAttribute("sortimg"));
        model.addAttribute("sortway",sortway);
        model.addAttribute("sorttip",sorttip);
        return "subpage/album";
    }

    /**
     * 加载回收站页面
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/findallrecycles")
    public String findAllRecycles(HttpSession session,Model model){
        List<Myfile> allRecycleList = this.myfileService.findAllRecycles((Integer) session.getAttribute("USER_ID"));
        model.addAttribute("files",allRecycleList);
        return "subpage/recycle";
    }

    /**
     * 永久删除（多文件）
     * @param ids
     * @return
     */
    @PostMapping("/foreverrecycles")
    @ResponseBody
    public void deleteAllFiles(Integer ids[]){
        for (Integer id : ids) {
            //首先删除文件
            Myfile delfile = this.myfileService.findFileById(id);
            //创建删除文件对象
            File file = new File("D:\\yunfile\\files\\",delfile.getNewfilename());
            //进行删除
            if (file.exists())file.delete();

            Myfile myfile = new Myfile();
            myfile.setId(id);
            this.myfileService.deleteAllFiles(id);
        }
    }

    /**
     * 条件模糊查询
     * @param keys
     * @param sort
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/findfilesbykey")
    public String findFilesByKey(String keys,String sort,
                                 @RequestParam(required = false,defaultValue = "asc")String sortway,
                                 HttpSession session,Model model){
        //保存前一次搜索关键字
        if (keys==null) {
            if (session.getAttribute("keys")==null) {
                session.setAttribute("keys","");
            }
        } else {
            session.setAttribute("keys",keys);
        }
        if (sort==null) {
            if (session.getAttribute("sort")==null) {
                session.setAttribute("sort","oldfilename");
            }
        } else {
            session.setAttribute("sort",sort);
        }

        //获取session
        Integer user_id = (Integer) session.getAttribute("USER_ID");

        //分析关键字
        List<String> keylist = this.fileTypeUtils.ParsingKeys((String) session.getAttribute("keys"));
        //创建文件对象并设置参数
        Myfile myfile = new Myfile();
        myfile.setType(keylist.get(0));
        myfile.setOldfilename(keylist.get(1));
        myfile.setUser_id(user_id);
        myfile.setSort((String) session.getAttribute("sort"));
        myfile.setSortway(sortway);

        //执行操作
        List<Myfile> files = this.myfileService.findFilesByKey(myfile);

        String sorttip = this.fileTypeUtils.setSortTip((String) session.getAttribute("sort"));

        model.addAttribute("files",files);
        model.addAttribute("sort",session.getAttribute("sort"));
        model.addAttribute("sortway",sortway);
        model.addAttribute("sorttip",sorttip);
        return "subpage/file";
    }

    /**
     * 单个文件 下载
     * @param fileid
     * @param response
     * @throws IOException
     */
    @GetMapping("/downloadfile")
    public void uploadFiles(String fileid, HttpServletResponse response) throws IOException {
        Integer id = Integer.valueOf(fileid);
        //获取文件信息
        Myfile myfile = this.myfileService.findFileById(id);
        //获取文件的路径
        String path = "D:\\yunfile\\files\\";
        //获取文件输入流
        FileInputStream inputStream = new FileInputStream(new File(path,myfile.getNewfilename()));
        //附件下载（不设置为默认在线打开）
        response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode(myfile.getOldfilename(),"utf-8"));
        //获取响应输出流
        ServletOutputStream outputStream = response.getOutputStream();
        //文件拷贝
        IOUtils.copy(inputStream,outputStream);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        //修改文件的下载次数
        this.myfileService.setDownLoadCounts(id);
    }

    /**
     * 多文件下载
     * @param ids
     * @param response
     * @throws IOException
     */
    @GetMapping("/downloadfiles")
    public void downloadFiles(@RequestParam("ids") Integer[] ids,HttpServletResponse response) throws IOException {
        //生成日期对象
        Date current_date = new Date();
        //设置日期格式化样式为：yyyy-MM-dd
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String zipfilename = simpleDateFormat.format(current_date);
        //设置清除缓存
        response.reset();
        // 不同类型的文件对应不同的MIME类型
        response.setContentType("application/x-msdownload");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(zipfilename,"utf-8") + ".zip");

        // ZipOutputStream类：完成文件或文件夹的压缩
        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
        byte[] buf = new byte[1024];

        for (Integer id : ids) {
            //获取文件信息
            Myfile myfile = this.myfileService.findFileById(id);
            //获取文件的路径
            String path = "D:\\yunfile\\files\\";
            //创建文件对象
            File file = new File(path,myfile.getNewfilename());

            //创建输入流
            FileInputStream inputStream = new FileInputStream(file);
            // 给列表中的文件单独命名
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                zipOutputStream.write(buf, 0, len);
            }
            zipOutputStream.closeEntry();
            inputStream.close();
            //修改文件的下载次数
            this.myfileService.setDownLoadCounts(id);
        }
        zipOutputStream.close();
    }

    /**
     * 文件回收
     * @param ids
     * @return
     */
    @PostMapping("/setrecycles")
    @ResponseBody
    public Integer setRecycles(@RequestParam("ids")Integer[] ids,HttpSession session){
        int recyclecounts = 0;
        for (Integer id : ids) {
            //执行方法
            Integer integer = this.myfileService.setRecycles(id);

            if (integer>0){
                recyclecounts++;
            }
        }
        int status = 0;
        if (recyclecounts==ids.length){
            status = 1;
        }else{
            //页面出错
            status = 2;
        }
        return status;
    }

    /**
     * 回收文件
     * @param ids
     * @return
     */
    @PostMapping("/recoverallfiles")
    @ResponseBody
    public Integer recoverAllFiles(Integer ids[]){
        int reccounts = 0;
        int status = 0;
        for (Integer id : ids) {
            Integer integer = this.myfileService.recoverAllFiles(id);
            if (integer>0){
                reccounts++;
            }
        }
        if (reccounts==ids.length){
            status = 1;
        }else{
            status = 2;
        }
        return status;
    }
}
