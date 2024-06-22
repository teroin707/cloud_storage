$(function () {
    //;layui操作结果反馈
    var layer = layui.layer;


    // 获取验证码
    $("#getcode").click(function () {
        if (!$("#password").val().length>0) {
            layer.msg("您还未填写新密码");
            return false
        }

        var btn = $("#getcode");
        var time = 30;//定义时间变量。用于倒计时用
        var timer = null;//定义一个定时器；
        timer = setInterval(function(){///开启定时器。函数内执行
            btn.disabled = true;
            btn.text(time+"秒后重新获取");    //点击发生后，按钮的文本内容变成之前定义好的时间值。
            time--;//时间值自减
            if(time === 0){     //判断,当时间值小于等于0的时候
                btn.text('重新获取验证码'); //其文本内容变成……点击重新发送……
                btn.disabled = false;
                clearInterval(timer); //清除定时器
            }
        },1000)

        $.post({
            url : "/user/getmailcode",
            data : {"mail":$("#email").text()},
            success : function (data) {
                if (data=="ok"){
                    layer.msg("验证码已发送，请注意查收");
                }
            }
        });
    })

    //修改信息
    $("#subinfo").click(function () {
        $.ajax({
            type: "post",
            async: true,
            url: "/user/setpersoninfo",
            data: {'nickname':$("#nickname").val(),'password':$("#password").val(),'code':$("#code").val()},
            success: function (data) {
                layer.msg(data);
            }
        })
    })
})
function replace(x) {
    x.value = x.value.trim()
}
