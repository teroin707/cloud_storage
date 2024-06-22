$(function () {
    // 打开搜索框
    $("#layui-icon-search").click(function (e) {
        $("#search-win").toggle()
        $("#sortlist").hide();
        e.stopPropagation()
    });
    // 排序
    $("#div01").click(function (e) {
        $("#sortlist").toggle()
        $("#search-win").hide();
        e.stopPropagation()
    });
    $(document).click(function(e){
        if (!($("#sortlist").is(e.target))){
            $("#sortlist").hide();
        }
    });
    // 关闭搜索框
    $("#layui-icon-close").click(function () {
        $("#search-win").hide()
    });
    // 关闭详细信息弹窗
    $("#layui-icon-close02").click(function () {
        $("#curtain").hide()
        $("#lookinfo").hide()
    });
    // 关闭右键菜单
    $(".files").mouseleave(function(){
        $(".rightsub-list").hide();
    });
})

window.onload = function () {
    //;layui操作结果反馈
    var layer = layui.layer;
    var chooseones = document.getElementsByClassName("chooseone")
    var files = document.getElementsByClassName("files");
    var rightsub = document.getElementsByClassName("rightsub");
    for (var i=0; i<files.length; i++){
        // 鼠标右击文件弹出框
        files[i].oncontextmenu = function () {
            this.children[0].style.display = "block";
        }
        files[i].onmouseover = function () {
            if (!(this.children[1].children[0].checked)){
                this.style.backgroundColor = "#eeeeee";
                this.children[1].style.display = "block";
                this.children[2].style.display = "block";
            }
        }
        files[i].onmouseout = function () {
            if (!(this.children[1].children[0].checked)) {
                this.style.backgroundColor = "#ffffff";
                this.children[1].style.display = "none";
                this.children[2].style.display = "none";
            }
        }
    }
    for (var i=0; i<rightsub.length; i++){
        rightsub[i].onclick = function(){
            this.parentNode.children[0].style.display = "block";
        }
    }


    // 下载所有选择过的文件
    document.getElementById("downloadall").onclick = function () {
        var ids = new Array();
        for (let i = 0; i < chooseones.length; i++) {
            if (chooseones[i].checked){
                ids.push(chooseones[i].nextElementSibling.value);
            }
        }
        if (ids.length===0){
            return false;
        }
        window.location.href = "/files/downloadfiles?ids="+ids;
        layer.msg("下载准备中...");
    };

    // 批量删除
    // 删除单击提示
    document.getElementById("moveinrecycle").onclick = function () {
        layer.msg("请双击图标移除")
    };
    document.getElementById("moveinrecycle").ondblclick = function () {
        var ids = new Array();
        var fileList = new Array();
        for (let i = 0; i < chooseones.length; i++) {
            if (chooseones[i].checked){
                ids.push(chooseones[i].nextElementSibling.value);
                fileList.push(chooseones[i])
            }
        }
        $.ajax({
            type: "post",
            url: "/files/setrecycles",
            traditional: true,
            data: {"ids":ids},
            success: function (data) {
                if (data===1){
                    for (var i = 0; i < fileList.length; i++) {
                        fileList[i].parentElement.parentElement.style.display = "none"
                    }
                    layer.msg("已移动到回收站")
                }else{
                    layer.msg("服务器出现异常，咱不可删除")
                }
                document.getElementById("downlist").style.display = "none";
                if (chooseones.length===ids.length){
                    window.location.href = "/files/findallimages";
                }
            }
        })
    };
}

// 全选
function allchoose(){
    var allchoose = document.getElementById("allchoose");
    var chooseones = document.getElementsByClassName("chooseone");
    if (allchoose.checked){
        for (var i=0;i<chooseones.length;i++){
            chooseones[i].checked = true
            document.getElementsByClassName("leftsub")[i].style.display = "block";
            document.getElementsByClassName("files")[i].style.backgroundColor = "#ecefff"
            document.getElementById("downlist").style.display = "block"
        }
    }else{
        for (var i=0;i<chooseones.length;i++){
            chooseones[i].checked = false
            document.getElementsByClassName("leftsub")[i].style.display = "none";
            document.getElementsByClassName("files")[i].style.backgroundColor = "#ffffff"
            document.getElementById("downlist").style.display = "none"
        }
    }
}
// 选中一个
function chooseone() {
    var chooseones = document.getElementsByClassName("chooseone")
    var j = 0;
    var k = 0;
    for (var i=0;i<chooseones.length;i++){
        if (chooseones[i].checked){
            document.getElementById("downlist").style.display = "block"
            document.getElementsByClassName("leftsub")[i].style.display = "block";
            document.getElementsByClassName("rightsub")[i].style.display = "none";
            document.getElementsByClassName("files")[i].style.backgroundColor = "#ecefff"
            j++
        }else{
            document.getElementsByClassName("leftsub")[i].style.display = "none";
            document.getElementsByClassName("files")[i].style.backgroundColor = "#ffffff"
            k++;
        }
    }
    if (k===chooseones.length){
        document.getElementById("downlist").style.display = "none"
    }
    if (j===chooseones.length){
        document.getElementById("allchoose").checked = true
    }else{
        document.getElementById("allchoose").checked = false
    }
}
// 取消所有选择(底部弹出框)
function closeallchoose() {
    document.getElementById("allchoose").checked = false
    var chooseone = document.getElementsByClassName("chooseone")
    for (var i=0;i<chooseone.length;i++){
        chooseone[i].checked = false;
        document.getElementsByClassName("leftsub")[i].style.display = "none";
        document.getElementsByClassName("files")[i].style.backgroundColor = "#ffffff"
    }
    document.getElementById("downlist").style.display = "none"
}
var curWwwPath=window.document.location.href;
var pathName=window.document.location.pathname;
var pos=curWwwPath.indexOf(pathName);
var localhostPaht=curWwwPath.substring(0,pos);
// 详细信息弹窗(图片页面独有)
function detailsdataimg(newname,name,size,path,ctime,stime,dcount) {
    document.getElementById("avatar1").src = localhostPaht+"/"+path+newname;
    document.getElementById("filename").innerText = name;
    document.getElementById("filename1").innerText = name;
    if (size<1024){
        document.getElementById("filesize1").innerText = size+"KB";
    }else if (size>=1024&&size<1048576){
        document.getElementById("filesize1").innerText = (size/1024).toFixed(2)+"MB";
    }else{
        document.getElementById("filesize1").innerText = (size/1024/1024).toFixed(2)+"GB";
    }
    document.getElementById("filepath1").innerText = path;
    document.getElementById("uploadtime1").innerText = ctime;
    document.getElementById("changetime1").innerText = stime;
    document.getElementById("downcounts1").innerText = dcount+"次";

    var elementsByClassName = document.getElementsByClassName("rightsub-list");
    for (let i = 0; i < elementsByClassName.length; i++) {
        elementsByClassName[i].style.display = "none"
    }
    document.getElementById("curtain").style.display = "block";
    document.getElementById("lookinfo").style.display = "block";
}

// 提示框
function onclick02(index){
    var elementsByClassName = document.getElementsByClassName("tip-list");
    var searchinput = document.getElementById("search-input")
    var tiplist = ["图片:","视频:","音频:","压缩文件:","文档:"]
    for (var i=0;i<elementsByClassName.length;i++){
        if (index==i){
            searchinput.value=tiplist[i];
            searchinput.focus()
            document.getElementById("tip-frame").style.display = "none";
            document.getElementById("search-win").style.height = "80px";
            break;
        }
    }
}
function oninput01(x){
    x.value = x.value.trim()
    var searchinput = document.getElementById("search-input")
    var frame = document.getElementById("tip-frame")
    var win = document.getElementById("search-win")
    if (searchinput.value.length>0){
        frame.style.display = "none";
        win.style.height = "80px";
    } else{
        frame.style.display = "block";
        win.style.height = "272px";
    }
}

// 排序
// ====================================================================
// 排序选择
function choosechecked(index) {
    document.getElementsByClassName("mycheckeds")[index].checked = true;
    if (index===0){
        document.getElementById("sortstatus").innerText = "按名称排序"
    }else if (index===1){
        document.getElementById("sortstatus").innerText = "按上传时间排序"
    }else if (index===2){
        document.getElementById("sortstatus").innerText = "按修改时间排序"
    }else if (index===3){
        document.getElementById("sortstatus").innerText = "按文件大小排序"
    }else if (index===4){
        document.getElementById("sortstatus").innerText = "按下载次数排序"
    }else{
        var i = null
    }
    document.getElementById("sortlist").style.display = "none";
}

// ====================================================================
//关闭默认右键菜单
document.oncontextmenu = function (e) {
    e.preventDefault()
}
// ============================================

