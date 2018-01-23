angular.module('app.services',[])

//辣鸡微信 SPA框架改变title信息，只能用这种hack方法
//设置title信息
.factory('setTitleService',function ($timeout) {
    var factory = {};
    factory.setTitle=function (titleName) {
        document.setTitle = function(t) {
            document.title = t;
            var i = document.createElement('iframe');
            i.style.display = 'none';
            i.onload = function() {
                $timeout(function(){
                    i.remove();
                }, 9)
            }
            document.body.appendChild(i);
        }

        $timeout(function(){
            document.setTitle(titleName)
        }, 100)
    }
    return factory;
})