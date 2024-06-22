var curWwwPath=window.document.location.href;
var pathName=window.document.location.pathname;
var pos=curWwwPath.indexOf(pathName);
var localhostPaht=curWwwPath.substring(0,pos);
$(document).ready(function() {
    $.ajax({
        type: "get",
        async: true,            //异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
        url: "/user/findindexinfo",    //请求发送到dataActiont处
        data: {},
        success: function (data) {
            $("#avatar").attr("src",localhostPaht+data[0]);
            $("#avatar1").attr("src",localhostPaht+data[0]);
            $("#usespace").text(data[1]);
            $("#space").text(data[2]+"GB");
            $("#nickname").text(data[3]);
            $("#nickname1").text(data[3]);
            $("#usepoint").css('width',data[4])
        }
    })
});
