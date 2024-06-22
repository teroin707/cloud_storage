window.onload = function () {
    //;layui操作结果反馈
    var layer = layui.layer;
    var chooseones = document.getElementsByClassName("chooseone")
    var files = document.getElementsByClassName("files");
    var downlist = document.getElementById("downlist");
    for (var i=0; i<files.length; i++){
        //鼠标右击文件弹出框
        files[i].onmouseover = function () {
            if (!(this.children[1].children[0].checked)){
                this.style.backgroundColor = "#eeeeee";
                this.children[1].style.display = "block";
            }
        }
        files[i].onmouseout = function () {
            if (!(this.children[1].children[0].checked)) {
                this.style.backgroundColor = "#ffffff";
                this.children[1].style.display = "none";
            }
        }
    }

    /**
     * 批量永久
     */
    document.getElementById("yesremove").onclick = function () {
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
            url: "/files/foreverrecycles",
            traditional: true,
            data: {"ids":ids},
            success: function (data) {
                window.location.href = "/files/findallrecycles";
            }
        })
    };
    // 批量恢复(回收站页面独有)
    document.getElementById("recoverall").onclick = function () {
        var ids = new Array();
        var filelist = new Array();
        for (let i = 0; i < chooseones.length; i++) {
            if (chooseones[i].checked){
                filelist.push(files[i])
                ids.push(chooseones[i].nextElementSibling.value);
            }
        }
        $.ajax({
            type: "post",
            url: "/files/recoverallfiles",
            traditional: true,
            data: {"ids":ids},
            success: function (data) {
                for (let i = 0; i < filelist.length; i++) {
                    filelist[i].style.display = "none";
                }
                downlist.style.display = "none"
                if (data==1){
                    layer.msg("回收成功")
                }else{
                    layer.msg("服务器异常")
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

// ====================================================================
// 回收站页面独有(移除提示)
function removeforever() {
    var elementsByClassName = document.getElementsByClassName("rightsub-list");
    for (let i = 0; i < elementsByClassName.length; i++) {
        elementsByClassName[i].style.display = "none"
    }
    document.getElementById("curtain").style.display = "block";
    document.getElementById("removetip").style.display = "block";
}
// 下部弹出导航栏删除按钮
function removeforever01() {
    document.getElementById("curtain").style.display = "block";
    document.getElementById("removetip").style.display = "block";
}
function cancelout() {
    document.getElementById("curtain").style.display = "none";
    document.getElementById("removetip").style.display = "none";
}

//关闭默认右键菜单
document.oncontextmenu = function (e) {
    e.preventDefault()
}

