$(function () {
    var layer = layui.layer;
    var form01 = $("#form01")
    var form02 = $("#form02")
    var form03 = $("#form03")
    var login01 = $("#login01")
    var login02 = $("#login02")
    var reg01 = $("#reg01")
    $("#login01").click(function () {
        form01.show()
        form02.hide()
        form03.hide()
        $("#form-reg-login").css("height","346px")
        login01.css({'background-color':'#ffffff','color':'#25262B'})
        login02.css({'background-color':'#F5F5F6','color':'#AAABAD'})
        reg01.css({'background-color':'#F5F5F6','color':'#AAABAD'})
    });
    $("#login02").click(function () {
        form01.hide()
        form02.show()
        form03.hide()
        $("#form-reg-login").css('height','320px');
        login01.css({'background-color':'#F5F5F6','color':'#AAABAD'})
        login02.css({'background-color':'#ffffff','color':'#25262B'})
        reg01.css({'background-color':'#F5F5F6','color':'#AAABAD'})
    });
    $("#reg01").click(function () {
        form01.hide()
        form02.hide()
        form03.show()
        $("#form-reg-login").css('height','346px')
        login01.css({'background-color':'#F5F5F6','color':'#AAABAD'})
        login02.css({'background-color':'#F5F5F6','color':'#AAABAD'})
        reg01.css({'background-color':'#ffffff','color':'#25262B'})
    });

    $("#email").focus(function () {
        $("#email").css({
            'border':'1px solid #B4C1FF',
            'background-color':'#ffffff',
            'height':'44px',
            'width':'302px'
        })
    });
    $("#email").blur(function () {
        $("#email").css({
            'border':'none',
            'background-color':'#F5F5F6',
            'height':'46px',
            'width':'304px'
        })
    });
    $("#code").focus(function () {
        $("#code").css({
            'border':'1px solid #B4C1FF',
            'background-color':'#ffffff',
            'height':'44px',
            'width':'179px'
        })
    });
    $("#code").blur(function () {
        $("#code").css({
            'border':'none',
            'background-color':'#F5F5F6',
            'height':'46px',
            'width':'180px'
        })
    });
    $("#checkboxyes").click(function () {
        if ($("#checkboxyes").is(':checked')){
            $("#loginsub").css({
                'background-color':'#677AE8',
                'cursor':'pointer'
            })
        }else{
            $("#loginsub").css({
                'background-color':'#AFBFFF',
                'cursor':'no-drop'
            })
        }
    });

    $("#username").focus(function () {
        $("#username").css({
            'border':'1px solid #B4C1FF',
            'background-color':'#ffffff',
            'height':'44px',
            'width':'302px'
        })
    });
    $("#username").blur(function () {
        $("#username").css({
            'border':'none',
            'background-color':'#F5F5F6',
            'height':'46px',
            'width':'304px'
        })
    });
    $("#password").focus(function () {
        $("#username").css({
            'border':'1px solid #B4C1FF',
            'background-color':'#ffffff',
            'height':'44px',
            'width':'302px'
        })
    });

    $("#password").blur(function () {
        $("#password").css({
            'border':'none',
            'background-color':'#F5F5F6',
            'height':'46px',
            'width':'304px'
        })
    });


    $("#getcode").click(function () {
        if (!$("#email").val().length>0) {
            $("#email").css({
                'border':'1px solid #B4C1FF',
                'background-color':'#ffffff',
                'height':'44px',
                'width':'302px'
            })
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
            data : {"mail":$("#email").val()},
            success : function (data) {
                layer.msg("验证码已发送，请注意查收");
            }
        });
    })
})


function submit01() {
    if ($("#checkboxyes").is(':checked')){
        if ($("#email").val().length>0 && $("#code").val().length>0){
            return true;
        }
        if (!$("#email").val().length>0){
            $("#email").css({
                'border':'1px solid #B4C1FF',
                'height':'44px',
                'width':'302px'
            })
        }
        if (!$("#code").val().length>0){
            $("#code").css({
                'border':'1px solid #B4C1FF',
                'height':'44px',
                'width':'179px'
            })
        }
        return false
    }
    return false;
}
function submit03() {
    if ($("#username").val().length>0 && $("#password").val().length>0){
        return true;
    }
    if (!($("#username").val().length>0)){
        $("#username").css({
            'border':'1px solid #B4C1FF',
            'height':'44px',
            'width':'302px'
        })
    }
    if (!($("#password").val().length>0)){
        $("#password").css({
            'border':'1px solid #B4C1FF',
            'height':'44px',
            'width':'302px'
        })
    }
    return false
}
function replace(x) {
    x.value = x.value.trim()
}


