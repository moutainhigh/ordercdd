if(window.location.href.indexOf('www.xiaojinpingtai.com') >0 ){  //正式服务器
    console.log('ok')
    window.config={
        location : 'www.xiaojinpingtai.com/wechat/ddcf/index.html',
        appid:'wxa14baecd846aab4b'
    }
}else{
    window.config={
        location : 'demo.xiaojinpingtai.com/wechat/ddcf/index.html',
        appid:'wxd5c676bb1329159c'
    }
}