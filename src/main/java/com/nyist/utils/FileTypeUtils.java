package com.nyist.utils;

import com.nyist.model.Myfile;

import java.util.ArrayList;
import java.util.List;

public class FileTypeUtils {
    public List<Myfile> IconFilesList(List<Myfile> list){
        String[] exts = new String[]{"css","doc","html","js","json","md","mp3","pdf","psd","ppt","txt","xml","xls"};

        for (Myfile file : list) {
            if (file.getIsimg()==1){
                file.setIcon("img");
                continue;
            }
            if (file.getType().startsWith("audio")){
                file.setIcon("mp3");
                continue;
            }
            if (file.getType().startsWith("video")){
                file.setIcon("mp4");
                continue;
            }
            if (file.getExt().startsWith("zip")||file.getExt().startsWith("rar")||file.getExt().startsWith("7z")||file.getExt().startsWith("gz")){
                file.setIcon("zip");
                continue;
            }

            int i = 0;
            while(i < exts.length) {
                if (file.getExt().startsWith(exts[i])){
                    file.setIcon(exts[i]);
                    break;
                }
                i++;
            }
            if (i==exts.length){
                file.setIcon("oth");
            }
        }
        return list;
    }

    public List<String> ParsingKeys(String keys){
        List<String> ketList = new ArrayList<>();
        if (keys.startsWith("图片")){
            ketList.add("image");
            ketList.add(keys.replace("图片:","").replaceAll(" ",""));
        } else if (keys.startsWith("视频")){
            ketList.add("video");
            ketList.add(keys.replace("视频:","").replaceAll(" ",""));
        } else if (keys.startsWith("音频")){
            ketList.add("audio");
            ketList.add(keys.replace("音频:","").replaceAll(" ",""));
        } else if (keys.startsWith("压缩文件")){
            ketList.add("zip");
            ketList.add(keys.replace("压缩文件:","").replaceAll(" ",""));
        } else if (keys.startsWith("文档")){
            ketList.add("application");
            ketList.add(keys.replace("文档:","").replaceAll(" ",""));
        }else{
            ketList.add("");
            ketList.add(keys);
        }
        return ketList;
    }


    public String setSortTip(String sort){
        String sorttip = "";
        if ("oldfilename".equals(sort)){
            sorttip = "按名称排序";
        }else if("uploadtime".equals(sort)){
            sorttip = "按上传时间排序";
        }else if ("changetime".equals(sort)){
            sorttip = "按修改时间排序";
        }else if ("size".equals(sort)){
            sorttip = "按文件大小排序";
        }else{
            sorttip = "按下载次数排序";
        }
        return sorttip;
    }


}
