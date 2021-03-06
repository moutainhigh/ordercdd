angular.module('app.controllers',[])
 //总控制器
.controller('appCtrl',function ($scope,$http) {
    var JsUrlOre =window.location.href;
    var JsUrl=JsUrlOre.split('#')[0];
    $.ajax({
        type: "get",
        url: "/wechat/wlqz/getCofigure",
        data:{'JsUrl':JsUrl},
        dataType: "json",
        success: function(res){
            console.log(res)
            wx.config({
                debug: false,
                appId:res.appId ,
                timestamp: res.timestamp,
                nonceStr: res.nonceStr,
                signature: res.signature,
                jsApiList: [
                    'closeWindow',
                    'getLocation',
                    'chooseImage',
                    'uploadImage'
                    // 所有要调用的 API 都要加到这个列表中
                ]
            });
            wx.ready(function () {

            });
            wx.error(function(res){

                // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
            });


        }
    })
})
.controller('loginCtrl',function ($scope,setTitleService,$timeout,$http,$stateParams,$state) {
    //设置微信titles
    setTitleService.setTitle('登录')
    $scope.applyMobile=null;
    $scope.smscode=null;
    $scope.source=$stateParams.source;
    console.log($scope.source)
    var time=60;  //验证码时间
    $scope.config={
        vCodeName:"获取验证码",
        disabled:false,
        css:true,
    };
    function loading() {
        $('body').loading({
            loadingWidth:120,
            title:'',
            name:'test',
            discription:'',
            direction:'column',
            type:'origin',
            // originBg:'#71EA71',
            originDivWidth:40,
            originDivHeight:40,
            originWidth:6,
            originHeight:6,
            smallLoading:false,
            loadingMaskBg:'rgba(0,0,0,0.2)'
        });

    }

    $scope.login=function () {
        var data ={
            username: $scope.applyMobile,
            smscode:$scope.smscode
        }

        //var upData=JSON.stringify(data);
        $.ajax({
            type: 'post',
            url: '/weblogin',
            data: data ,
            headers: {
                app_code:"WULIUQIANZHUANG",
                client:"wechat"
            },
            success: function (res) {
                removeLoading('test');
                if(res.ActionStatus == 'FAIL'){
                    alert(res.ErrorInfo)
                }else{
                    $.toast("登录成功");
                    $timeout(function () {
                        $state.go($scope.source)
                    },1500)
                }

            } ,
            error:function(res){
                removeLoading('test');
                $.alert(res.responseJSON.message)
            },
            dataType: 'json'
        })
    }

    // 获取验证码倒计时
    $scope.vCode=function () {
       vCodeFn();
        $.ajax({
            type: 'GET',
            url: '/smsCode' ,
            data: {"mobile":$scope.applyMobile} ,
            headers: {
                app_code:"WULIUQIANZHUANG",
                client:"wechat"
            },
            success: function (res) {
                $.alert(res.msg)
            },error:function(res){
                $.alert(res.responseJSON.message)
            },
            dataType: 'json'
        })
    };

    function vCodeFn(){
        if(time==0){
            $scope.config.vCodeName="获取验证码";
            $scope.config.disabled=false;
            $scope.config.css=true;
            time=60;
        }else{
            $scope.config.vCodeName="重新获取("+time+")";
            $scope.config.disabled=true;
            $scope.config.css=false;
            $timeout(function(){
                time=time-1;
                vCodeFn();
            },1000)
        }
    }

})
.controller('cddApplyLoginCtrl',function ($scope,$http) {
	$.ajax({
        type: 'GET',
        url: '/wechat/wlqz/state' ,
        data: {} ,
        headers: {
            app_code:"WULIUQIANZHUANG",
            client:"wechat"
        },
        success: function (res) {
            var state=res;
            var uri='http://'+window.config.location+'#/cddApply';
            uri=encodeURIComponent(uri);
            window.location.href='https://open.weixin.qq.com/connect/oauth2/authorize?appid='+window.config.appid+'&redirect_uri='+uri+'&response_type=code&scope=snsapi_base&state='+state+'#wechat_redirect';
        } ,
        dataType: 'text'
    })


})
    //申请页面
.controller('cddAppCtrl',function ($scope,setTitleService,$timeout,$state,$http) {
    //设置微信title
    setTitleService.setTitle('车抵贷申请')
    $scope.mobile=null;
    $scope.captcha=null;
    $scope.smscode=null;
    $scope.realName=null;
    $scope.region=null;
    $scope.applyMobile=null;
    $scope.referralCode=null;
    var valArr=['该地区暂无匹配的门店'];
    var departmentsData = [];
    var createdByDepartment;

    var time=60;  //验证码时间
    $scope.config={
        vCodeName:"获取验证码",
        disabled:false,
        css:true
    };
    if(window.location.search){
        var href = window.location.href;
        var href2 = href.split('?')[1];
        var state_all = href2.split('&')[1];
        var code_all = href2.split('&')[0];
        var state = state_all.split('=')[1];
        var code = code_all.split('=')[1];
        console.log(state)
        console.log(code)
    }


    $("#cddApplyCity").cityPicker({
        title: "选择服务城市",
    });

    $("#departments").picker({
        title: "选择门店",
        toolbarCloseText:'确定',
        cols: [
            {
                textAlign: 'center',
                values:valArr,
                displayValues:valArr,
            }
        ]
    });
    $scope.cddApplyCityChangeFn=function () {

        $scope.province = $scope.region.split(' ')[0]
        $scope.city = $scope.region.split(' ')[1]
        $scope.district = $scope.region.split(' ')[2]

        $.ajax({
            type: 'GET',
            url: '/rest/departments/search/findByProvinceAndCityAndDistrict' ,
            data: {
                province:$scope.province,
                city:$scope.city,
                district:$scope.district
            } ,
            headers: {
                app_code:"WULIUQIANZHUANG",
                client:"wechat"
            },
            success: function (res) {
                console.log(res._embedded.departments)
                valArr.splice(0,valArr.length)

                departmentsData = res._embedded.departments;

                var resArr = res._embedded.departments;
                 if(res._embedded.departments.length == 0){
                     $('#departments').val('该地区暂无匹配的门店')
                 }else {
                     $('#departments').val(res._embedded.departments[0].label)
                 }

                angular.forEach(resArr,function (val,key) {
                    console.log(val.label)
                    valArr.push(val.label)
                })
                
                if(valArr.length == 0){
                    valArr.push('该地区暂无匹配的门店')
                }

            } ,
            dataType: 'json'
        })

    }
    //提交表单
    var $form = $("#cddAppform");

    $form.form();

    function loading() {
        $('body').loading({
            loadingWidth:120,
            title:'',
            name:'test',
            discription:'',
            direction:'column',
            type:'origin',
            // originBg:'#71EA71',
            originDivWidth:40,
            originDivHeight:40,
            originWidth:6,
            originHeight:6,
            smallLoading:false,
            loadingMaskBg:'rgba(0,0,0,0.2)'
        });

    }
    
    $scope.submitBtn=function () {
        console.log($scope.referralCode);
        $form.validate(function(error){
            if(error){

            }else{
                var departmentsVal = $('#departments').val()
                if(departmentsVal === '该地区暂无匹配的门店'){
                    $.alert('该地区暂无门店!')
                }else {
                    loading();
                    console.log(departmentsData)
                    angular.forEach(departmentsData,function (val,key) {
                   
                        if( departmentsVal == val.label ){
                            createdByDepartment = '/rest/departments/'+val.id
                        }
                    })
                    console.log(createdByDepartment)
                    var serviceProvince = $scope.region.split(' ')[0];
                    var serviceCity = $scope.region.split(' ')[1];


                    var data ={
                        captcha:$scope.captcha,
                        smsCode:$scope.smscode,
                        realName:$scope.realName,
                        applyMobile:$scope.applyMobile,
                        referralCode:$scope.referralCode,
                        applyEnterpriseProvince:$scope.region,
                        act:'create',
                        wechatCode:code,
                        createdByDepartment:createdByDepartment
                    }
                    var updata=JSON.stringify(data);
                    $.ajax({
                        type: 'post',
                        url: '/rest/ordercdds',
                        data:updata,
                        headers: {
                            app_code:"WULIUQIANZHUANG",
                            client:"wechat",
                            "Content-Type":"application/json"
                        },
                        success: function (res) {
                            removeLoading('test');
                            if(res.ActionStatus == 'FAIL'){
                                alert(res.ErrorInfo)
                            }else{
                                removeLoading('test');
                                $.toast("提交成功");
                                $timeout(function () {
                                    wx.closeWindow();
                                },1500)
                            }

                        } ,
                        error:function(res){
                            removeLoading('test');
                            var message = (res.responseJSON.message).split(':')[1]
                            $.alert(message)
                        },
                        dataType: 'json'
                    })
                }
            }
        });
    };


    //再次获取验证码
    $scope.reGetCaptcha = function() {
        $("#cddApply_captcha").attr("src","/captcha?" + Math.random()*10);
    };

    // 获取验证码倒计时
    $scope.vCode=function () {

        if( $scope.captcha != null ){
            $.toast("请输入图形验证码");
            return false;
        }

        vCodeFn();

        $.ajax({
            type: 'GET',
            url: '/smsCode' ,
            data: {
                "mobile":$scope.applyMobile,
                'captcha':$scope.captcha
            },
            headers: {
                app_code:"WULIUQIANZHUANG",
                client:"wechat"
            },
            success: function (res) {
                $scope.reGetCaptcha();
                layer.open({
                    content: res.msg
                    ,skin: 'msg'
                    ,time: 5 //2秒后自动关闭
                });
            },error:function(res){
                $scope.reGetCaptcha();
                layer.open({
                    content: res.responseJSON.message
                    ,skin: 'msg'
                    ,time: 5 //2秒后自动关闭
                });
            },
            dataType: 'json'
        })

    };

    function vCodeFn(){
        if(time==0){
            $scope.config.vCodeName="获取验证码";
            $scope.config.disabled=false;
            $scope.config.css=true;
            time=60;
        }else{
            $scope.config.vCodeName="重新获取("+time+")";
            $scope.config.disabled=true;
            $scope.config.css=false;
            $timeout(function(){
                time=time-1;
                vCodeFn();
            },1000)
        }
    }

})


    //我的银行卡 第一步
.controller('myBankCardOneCtrl',function ($scope,setTitleService,$state) {
    //设置微信title
    setTitleService.setTitle('绑定银行卡')

    $scope.bankName=null;
    $scope.bankNumber=null;

    //跳转到下一页面一起提交
    $scope.toNext=function () {
        //替换所有空格
        $scope.bankNumber=$scope.bankNumber.replace(/\s/ig,'');
        $state.go('myBankCardTwo',{
            bankName: $scope.bankName,
            bankNumber:$scope.bankNumber
        })

    }
})


//我的银行卡 第二步
.controller('myBankCardTwoCtrl',function ($scope,setTitleService,$timeout,$stateParams) {
    //设置微信title
    setTitleService.setTitle('绑定银行卡')
    $scope.bankName=$stateParams.bankName;
    $scope.bankNumber=$stateParams.bankNumber;
    $scope.mobile=null;
    $scope.vCode=null;
    var time=60;  //验证码时间
    $scope.config={
        vCodeName:"获取验证码",
        disabled:false,
        css:true
    };

    console.log($scope.bankName,$scope.bankNumber)

    // 获取验证码倒计时
    $scope.vCodeBtn=function () {
        vCodeFn();
    };

    function vCodeFn(){
        if(time==0){
            $scope.config.vCodeName="获取验证码";
            $scope.config.disabled=false;
            $scope.config.css=true;
            time=60;
        }else{
            $scope.config.vCodeName="重新获取("+time+")";
            $scope.config.disabled=true;
            $scope.config.css=false;
            $timeout(function(){
                time=time-1;
                vCodeFn();
            },1000)
        }
    }

})
    //进度查询登录
.controller('scheduleQueryLoginCtrl',function () {
    $.ajax({
        type: 'GET',
        url: '/wechat/wlqz/state' ,
        data: {} ,
        headers: {
            app_code:"WULIUQIANZHUANG",
            client:"wechat"
        },
        success: function (res) {
            var state=res;
            var uri='http://'+window.config.location+'#/scheduleQuery';
            uri=encodeURIComponent(uri);
            window.location.href='https://open.weixin.qq.com/connect/oauth2/authorize?appid='+window.config.appid+'&redirect_uri='+uri+'&response_type=code&scope=snsapi_base&state='+state+'#wechat_redirect';
        } ,
        dataType: 'text'
    })
})
    //进度查询
.controller('scheduleQueryCtrl',function ($scope,setTitleService,$state,$http) {
    //设置微信title
    setTitleService.setTitle('进度查询')
    //导航
    $('#tab1').tab({defaultIndex:0,activeClass:"tab-green"});

    //隐藏默认页面
    $scope.isLogin = false;
    $scope.applyAmount=null; //申请金额
    $scope.applyPeriodNumber=null; //  分期数
    $scope.createdAt = null;
    $scope.repaymentMethods=null; //还款方式
    //操作时间 logs
    $scope.showLogsTimes={
        adopt:null,
        loanAdopt:null,
        loanArrival:null,
        loan:null
    };
    //不同状态不同进度 css
    $scope.css={
        CONTRACT : true,
        LOANING : false,
        LOANARRIVAL : false
    }

    if(window.location.search){
        var href = window.location.href;
        var href2 = href.split('?')[1];
        var state_all = href2.split('&')[1];
        var code_all = href2.split('&')[0];
        var state = state_all.split('=')[1];
        var code = code_all.split('=')[1];
         state = state.split('#')[0];
    }
    $.ajax({
        type: 'GET',
        url: '/wechat/wlqz/callback' ,
        //data: {'code':code} ,
        headers: {
            app_code:"WULIUQIANZHUANG",
            client:"wechat"
        },
        success: function (res) {
            if(res.message == 'error'){
                $.ajax({
                    type: 'GET',
                    url: '/wechatLogin' ,
                    data: {'code':code,'state':state} ,
                    headers: {
                        app_code:"WULIUQIANZHUANG",
                        client:"wechat"
                    },
                    success: function (res) {
                        if(res.ActionStatus == "FAIL"){
                            $state.go('login',{
                                source:'scheduleQuery'
                            })
                            //用户没注册
                        }else{
                            window.userid=res.message;
                            renderingFn()
                            $scope.isLogin = true;
                        }

                    } ,
                    dataType: 'json'
                })
                $scope.isLogin = false;

            }else{
                window.userid=res.message;
                renderingFn();
                $scope.isLogin = true;
            }
        } ,
        dataType: 'json'
    });

    function renderingFn() {
        $http({
            method: 'get',
            url: '/rest/ordercdds/search/getOrdercddByUser',
            data: {},
        }).success(function (res) {
            $scope.data = res._embedded.ordercdds[0];
            $scope.applyAmount=$scope.data.applyAmount.toFixed(2);
            $scope.applyPeriodNumber=$scope.data.applyPeriodNumber;
            $scope.createdAtOld=$scope.data.createdAt.split('T')[0];
            $scope.createdAt = $scope.createdAtOld.split('-')[0]+'年'+$scope.createdAtOld.split('-')[1]+'月'+$scope.createdAtOld.split('-')[2]+'日';

            //还款方式
            if($scope.data.repaymentMethodCode == 'LEND_REPAY_ON_DEMAND'){
                $scope.repaymentMethods = '随借随还';
            }else if($scope.data.repaymentMethodCode == 'BEFORE_INTEREST_AFTER_PRINCIPAL'){
                $scope.repaymentMethods = '先息后本';
            }else if($scope.data.repaymentMethodCode == 'AVERAGE_CAPITAL_PLUS_INTEREST'){
                $scope.repaymentMethods = '等额本息';
            }else if($scope.data.repaymentMethodCode == 'AVERAGE_CAPITAL'){
                $scope.repaymentMethods = '等额本金';
            }

            // $http({
            //     method: 'get',
            //     url: '/rest/ordercdds/'+$scope.data.id+'/product',
            //     data: {},
            // }).success(function (res) {
            //     $http({
            //         method: 'get',
            //         url: '/rest/products/'+res.id+'/capitalproduct',
            //         data: {},
            //     }).success(function (res) {
            //         //还款方式
            //         if(res.repaymentMethodCode == 'LEND_REPAY_ON_DEMAND'){
            //             $scope.repaymentMethods = '随借随还';
            //         }else if(res.repaymentMethodCode == 'BEFORE_INTEREST_AFTER_PRINCIPAL'){
            //             $scope.repaymentMethods = '先息后本';
            //         }else if(res.repaymentMethodCode == 'AVERAGE_CAPITAL_PLUS_INTEREST'){
            //             $scope.repaymentMethods = '等额本息';
            //         }else if(res.repaymentMethodCode == 'AVERAGE_CAPITAL'){
            //             $scope.repaymentMethods = '等额本金';
            //         }
            //
            //     }).error(function (res) {
            //
            //     });
            // }).error(function (res) {
            //
            // });
            //查询操作时间
            $http({
                method: 'get',
                url: '/rest/ordercdds/'+$scope.data.id+'/logs',
                data: {},
            }).success(function (res) {
                $scope.logs=res._embedded.ordercddLogs;
                angular.forEach($scope.logs,function (val,key) {
                    if(val.label === '一审通过'){
                        var adopt = (val.lastModifiedAt).split('T')[0];
                        $scope.showLogsTimes.adopt = adopt;
                    }
                    if(val.label === '二审通过'){
                        var loanAdopt = (val.lastModifiedAt).split('T')[0];
                        $scope.showLogsTimes.loanAdopt = loanAdopt;
                    }
                    if(val.label === '贷款到账'){
                        var loanArrival = (val.lastModifiedAt).split('T')[0];
                        $scope.showLogsTimes.loanArrival = loanArrival;
                    }
                })
            }).error(function (res) {

            });

            //审核状态
            $http({
                method: 'get',
                url: '/rest/ordercdds/'+$scope.data.id+'/state',
                data: {},
            }).success(function (res) {
                //等待签约
                if(res.stateCode == 'CONTRACT' ){
                    $scope.css={
                        CONTRACT : true,
                        LOANING : false,
                        LOANARRIVAL : false
                    }
                    //等待放款
                }else if(res.stateCode == 'ENABLED'){
                    $scope.css={
                        CONTRACT : true,
                        LOANING : true,
                        LOANARRIVAL : false
                    }
                    //放款成功
                }else if(res.stateCode == 'LOANARRIVAL'){
                    $scope.css={
                        CONTRACT : true,
                        LOANING : true,
                        LOANARRIVAL : true
                    }
                }

                $http({
                    method: 'get',
                    url: '/rest/ordercdds/'+$scope.data.id+'/loan',
                    data: {},
                }).success(function (res) {
                    $http({
                        method: 'get',
                        url: '/rest/loans/'+res.id+'/logs',
                        data: {},
                    }).success(function (res) {
                        var data =res._embedded.loanLogs;
                        angular.forEach(data,function (val,key) {
                            if(val.label =='放贷'){
                                var loan = (val.lastModifiedAt).split('T')[0];
                                $scope.showLogsTimes.loan = loan;
                                $scope.css={
                                    CONTRACT : true,
                                    LOANING : true,
                                    LOANARRIVAL : true
                                }
                            }
                        })
                    }).error(function (res) {

                    });
                }).error(function (res) {

                });

            }).error(function (res) {

            });
        }).error(function (res) {

        });
    }

})
.controller('myLoanLoginCtrl',function ($scope) {
    $.ajax({
        type: 'GET',
        url: '/wechat/wlqz/state' ,
        data: {} ,
        headers: {
            app_code:"WULIUQIANZHUANG",
            client:"wechat"
        },
        success: function (res) {
            var state=res;
            var uri='http://'+window.config.location+'#/myLoan';
            uri=encodeURIComponent(uri);
            window.location.href='https://open.weixin.qq.com/connect/oauth2/authorize?appid='+window.config.appid+'&redirect_uri='+uri+'&response_type=code&scope=snsapi_base&state='+state+'#wechat_redirect';
        } ,
        dataType: 'text'
    })
})
//我的借款
.controller('myLoanCtrl',function ($scope,setTitleService,$state,$http) {
    //设置微信title
    setTitleService.setTitle('我的借款')
    $scope.loanShow=false;
    $scope.plan=null;//
    $scope.ob=null;
    $scope.currentTimeSub=null;
    $scope.currentTimeSubOB=null;
    $scope.planDate=null;
    $scope.overDate=false; //逾期天数显示
    $scope.allPrincipal=null; //剩余本金
    $scope.overDueLeftAmount=0; //以前分期没结清的金额
    $scope.overdueAmount=0; //逾期金额
    $scope.loanId=null;
    $scope.loading=false;

    if(window.location.search){
        var href = window.location.href;
        var href2 = href.split('?')[1];
        var state_all = href2.split('&')[1];
        var code_all = href2.split('&')[0];
        var state = state_all.split('=')[1];
        var code = code_all.split('=')[1];
        state = state.split('#')[0];
    }
    $.ajax({
        type: 'GET',
        url: '/wechat/wlqz/callback' ,
        //data: {'code':code} ,
        headers: {
            app_code:"WULIUQIANZHUANG",
            client:"wechat"
        },
        success: function (res) {
            if(res.message == 'error'){
                $.ajax({
                    type: 'GET',
                    url: '/wechatLogin' ,
                    data: {'code':code,'state':state} ,
                    headers: {
                        app_code:"WULIUQIANZHUANG",
                        client:"wechat"
                    },
                    success: function (res) {
                        if(res.ActionStatus == "FAIL"){
                            $state.go('login',{
                                source:'myLoan'
                            })
                            //用户没注册
                        }else{
                            window.userid=res.message;
                            $scope.isLogin = true;
                            initFn();
                        }

                    } ,
                    dataType: 'json'
                })
                $scope.isLogin = false;

            }else{
                window.userid=res.message;
                $scope.isLogin = true;
                initFn();
            }
        } ,
        dataType: 'json'
    });

    function initFn() {
        $http({
            method: 'get',
            url: '/rest/ordercdds/search/getOrdercddByUser',
            data: {},
        }).success(function (res) {
            $http({
                method: 'get',
                url: '/rest/ordercdds/'+res._embedded.ordercdds[0].id+'/loan',
                data: {},
            }).success(function (res) {
                $scope.loanId=res.id;
                    $http({
                        method: 'get',
                        url: '/rest/loans/'+res.id+'/loanoverdues',
                        data: {},
                    }).success(function (res) {
                        var list =res._embedded.loanoverdues;
                        if(list.length == 0){
                            $scope.overdueAmount = 0;
                        }else {
                            angular.forEach(list,function (val,key) {
                                //未还完
                                if(val.status == 0){
                                    $scope.overdueAmount = val.overdueAmount;
                                }
                            })
                        }
                    }).error(function (res) {
                        
                    });



                    $http({
                        method: 'get',
                        url: '/rest/loans/'+res.id+'/creditrepayplans',
                        data: {},
                    }).success(function (res) {
                        $scope.loading=true;
                        if(res._embedded.creditrepayplans.length == 0){
                            $scope.loanShow=false;
                        }else {
                            $scope.loanShow=true;
                            var timestamp = Date.parse(new Date());
                            var plans=res._embedded.creditrepayplans;

                            angular.forEach(plans,function (val,key) {
                                //剩余本金
                                if( val.principal !== 0 ){
                                    $scope.allPrincipal =  val.leftAmount - val.interest
                                }

                                //未结清的逾期计划金额
                                if(val.overdueDays !== 0 && val.leftAmount !== 0){
                                    $scope.overDueLeftAmount += val.leftAmount
                                }
                            })

                            //console.log(Date.parse(new Date("2018/03/04")))
                            for (var i in plans){

                                var val =plans[i];
                                //ios 只支持 2019/01/01 这种格式，不兼容2010-01-01 这种格式，一定要转化
                                var newTime = (val.debtorRepaymentDate.split('T')[0]).replace(/-/g, '/');
                                var sub = (Date.parse(new Date(newTime))-timestamp);

                                // 取和当前时间最近的那期
                                if(timestamp  <= Date.parse(new Date(newTime))){
                                    //判断是不是第一次进来或者时间相差值最小的
                                    if($scope.currentTimeSub===null||sub<$scope.currentTimeSub){
                                        $scope.currentTimeSub=sub;
                                        $scope.plan=val;
                                        console.log('$scope.plan'+$scope.plan)
                                    }
                                }
                                // //如果存在逾期并且没还清
                                // if(val.isOverdue !== null && val.leftAmount !==0)
                                // {
                                // 	console.log('b')
                                //     //获取最久没还清的那期
                                //     if($scope.currentTimeSubOB===null||sub>$scope.currentTimeSubOB){
                                //         $scope.currentTimeSubOB=sub;
                                //         $scope.ob=val;
                                //         console.log('$scope.plan'+$scope.plan)
                                //     }
                                // }
                            }
                            //如果有逾期就取逾期，没有就取最近的那期
                            // if($scope.ob){
                            //     $scope.planDate=$scope.ob;
                            // }else{
                            //     $scope.planDate=$scope.plan;
                            // }

                            $scope.planDate=$scope.plan;
                            if($scope.planDate.overdueDays !==0){
                                $scope.overDate = true;
                            }
                        }

                    }).error(function (res) {

                    });
            }).error(function (res) {

            });
        }).error(function (res) {

        });
    }

    $scope.toRepayMentPlan=function () {
        $state.go('repaymentPlan')
    }

    $scope.loanFn=function () {
        var repaymentAmount=$('#repaymentAmount').text();
        console.log($scope.planDate.loanSn)
        if ( Number($scope.planDate.leftAmount + $scope.overDueLeftAmount + $scope.overdueAmount)  == 0 ){
            alert('本月账单已还清，无需还款');
        }else {
            $state.go('repayMentDetail',{
                loanSn:$scope.loanId,
                repaymentAmount:repaymentAmount
            })
        }
    }

    $scope.earlyRepayFn=function () {
        var repaymentAmount = $('#repaymentAmount').text();
        $http({
            method: "get",
            url: "/loan/settle?loanId=" + $scope.loanId,//这里是关联的实体
        }).success(function (data) {
            if( data == 0){
                alert('此订单已经结清，无需还款');
            }else{
                $state.go('earlyRepayMent',{
                    loanSn:$scope.loanId,
                    repaymentAmount:repaymentAmount
                })
            }
        });
    }
    $scope.toloanLogs = function () {
        $state.go('loanLogs',{
            id:$scope.loanId
        })
    }

})
.controller('repaymentPlanCtrl',function ($scope,setTitleService,$http) {
    //设置微信title
    setTitleService.setTitle('还款计划')
    console.log($scope.planSn);
    initFn();
    function initFn() {
        $http({
            method: 'get',
            url: '/rest/ordercdds/search/getOrdercddByUser',
            data: {},
        }).success(function (res) {
            $http({
                method: 'get',
                url: '/rest/ordercdds/'+res._embedded.ordercdds[0].id+'/loan',
                data: {},
            }).success(function (res) {
                console.log(res.id)
                $scope.debtorInterest=res.debtorInterest;
                $scope.principal=res.principal;
                $http({
                    method: 'get',
                    url: '/rest/loans/'+res.id+'/creditrepayplans',
                    data: {},
                }).success(function (res) {
                    $scope.plans=res._embedded.creditrepayplans;

                }).error(function (res) {

                });
            }).error(function (res) {

            });
        }).error(function (res) {

        });
    }

})
.controller('loanLogsCtrl',function ($scope,setTitleService,$stateParams,$http) {
    //设置微信title
    setTitleService.setTitle('还款记录');
    $scope.loanId=$stateParams.id;
    $http({
        method: 'get',
        url: '/rest/loans/'+$scope.loanId+'/creditrepayments',
        data: {},
    }).success(function (res) {
        $scope.repamentDatas=res._embedded.creditrepayments;
        angular.forEach($scope.repamentDatas,function (val,key) {
            $http({
                method: 'get',
                url: '/rest/creditrepayments/'+val.id+'/state',
                data: {},
            }).success(function (res) {
                    if(res.stateCode == 'ENABLED'){
                        val.state = true;
                    }else {
                        val.state = false;
                    }
            }).error(function (res) {
                alert(res.message)
            });
        })

        // if($scope.repamentDatas.length == 0){
        //     $scope.logsShow=false;
        // }else {
        //     $scope.logsShow=true;
        // }
    }).error(function (res) {
        alert(res.message)
    });
})
    //还款详细
.controller('loanDetailedCtrl',function ($scope,setTitleService) {
    //设置微信title
    setTitleService.setTitle('还款明细')

    //切换按钮
    $scope.showTabs={
        tab1:true,
        tab2:false
    }

    //明细显示隐藏
    $scope.hideFn=function () {
        if($('#showDetail').css('display') == 'block'){
            $('#showDetail').slideUp()
            $('#showDetailIcon').removeClass('icon-74').addClass('icon-35')
        }else {
            $('#showDetail').slideDown()
            $('#showDetailIcon').removeClass('icon-35').addClass('icon-74')
        }

    }

})
.controller('repayMentDetailCtrl',function ($scope,setTitleService,$stateParams,$http,$timeout) {
    //设置微信title
    setTitleService.setTitle('确认还款')
    $scope.loanSn=$stateParams.loanSn;
    $scope.repaymentAmount=$stateParams.repaymentAmount;
    $scope.fees=null;
    $scope.data={};
    $scope.wechatFiles=[];
    $scope.imgshow=false;
    $scope.imgSrc=null;
    console.log($scope.repaymentAmount)
    $scope.confirmFn=function () {
        console.log($scope.fees)
        console.log($scope.repaymentAmount)

        if( parseFloat($scope.fees) > parseFloat($scope.repaymentAmount) ){
            $.alert('还款金额大于'+$scope.repaymentAmount+'!')
        }if( $scope.wechatFiles.length == 0){
            $.alert('请上传还款凭证')
        }else {
            $scope.btnFlag = true;
            $.showLoading();
            $http({
                method: 'post',
                url: '/rest/creditrepayments',
                data: {
                    act:'create',
                    loan:'/rest/loans/'+$scope.loanSn,
                    repaymentType:'0',
                    fees:$scope.fees,
                    wechatFiles:$scope.wechatFiles,
                    createdByDepartment:'/rest/departments/3',
                    comeFrom:'wechat'
                },
            }).success(function (res) {
                $.hideLoading();
                if(res.ActionStatus=='FAIL'){
                    alert(res.ErrorInfo);
                }else{
                    $.toast("操作成功");
                    $timeout(function () {
                        wx.closeWindow();
                    },1000)
                }
            }).error(function (res) {
                $.hideLoading();
                console.log(res)
                alert(res.message)
            });
        }
    }

    $scope.upimageFn=function () {
        wx.chooseImage({
            count: 1, // 默认9
            sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
            sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
            success: function (res) {
                if(res.errMsg == 'chooseImage:ok'){
                    var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片

                    var img = '<img class="imgBtn" src="'+localIds[0]+'" style="width: 67px; height: 67px;margin: 0 5px;">'
                    $('#grclBtn').parent().prepend(img);

                    wx.uploadImage({
                        localId: localIds[0], // 需要上传的图片的本地ID，由chooseImage接口获得
                        isShowProgressTips: 1, // 默认为1，显示进度提示
                        success: function (res) {
                            //console.log(res)
                            if(res.errMsg == 'uploadImage:ok'){
                                    $scope.wechatFiles.push(res.serverId)
                            }
                        }
                    });
                }
            }
        })
    }

})
.controller('earlyRepayMentCtrl',function ($scope,setTitleService,$stateParams,$http,$timeout) {
        //设置微信title
        setTitleService.setTitle('提前结清')
        $scope.loanSn=$stateParams.loanSn;
        $scope.repaymentAmount=$stateParams.repaymentAmount;

        $scope.fees=null;
        $scope.data={};
        $scope.wechatFiles=[];

        $scope.imgshow=false;
        $scope.imgSrc=null;

        $scope.conFlag = false;

        //获取还款金额 设置为$scope.fees
        $http({
            method: "get",
            url: "/loan/settle?loanId=" + $scope.loanSn,//这里是关联的实体
        }).success(function (data) {
            console.log(data)
            $scope.fees = data;
        });

        $scope.confirmFn=function () {
            console.log($scope.fees)
            console.log($scope.repaymentAmount)

            if( $scope.wechatFiles.length == 0){
                $.alert('请上传还款凭证')
            }else {
                $.showLoading();
                $scope.conFlag = true;
                $http({
                    method: 'post',
                    url: '/rest/creditrepayments',
                    data: {
                        act:'create',
                        loan:'/rest/loans/'+$scope.loanSn,
                        fees:$scope.fees,
                        repaymentType:'2',
                        wechatFiles:$scope.wechatFiles,
                        createdByDepartment:'/rest/departments/3',
                        comeFrom:'wechat'
                    },
                }).success(function (res) {
                    $.hideLoading();
                    if(res.ActionStatus=='FAIL'){
                        alert(res.ErrorInfo);
                    }else{
                        $.toast("操作成功");
                        $timeout(function () {
                            wx.closeWindow();
                        },1000)
                    }
                }).error(function (res) {
                    $.hideLoading();
                    console.log(res);
                    alert(res.message);
                    $scope.conFlag = false;
                });
            }
        }

        $scope.upimageFn=function () {
            wx.chooseImage({
                count: 1, // 默认9
                sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
                sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                success: function (res) {
                    if(res.errMsg == 'chooseImage:ok'){
                        var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片

                        var img = '<img class="imgBtn" src="'+localIds[0]+'" style="width: 67px; height: 67px;margin: 0 5px;">'
                        $('#grclBtn').parent().prepend(img);

                        wx.uploadImage({
                            localId: localIds[0], // 需要上传的图片的本地ID，由chooseImage接口获得
                            isShowProgressTips: 1, // 默认为1，显示进度提示
                            success: function (res) {
                                //console.log(res)
                                if(res.errMsg == 'uploadImage:ok'){
                                    $scope.wechatFiles.push(res.serverId)
                                }
                            }
                        });
                    }
                }
            })
        }

    })