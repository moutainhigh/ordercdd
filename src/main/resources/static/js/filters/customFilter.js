'use strict';
/* Filters */
// need load the moment.js to use this filter.
app.filter("fromNow", function () {
    return function (date) {
        return moment(date).fromNow();
    }
})
    .filter('datetime', function () {
        return function (date) {
            if (parseInt(date) > 1400000) {
                date = (date + '000') * 1;
            }
            return moment(date).format('YYYY') + "年 " + moment(date).format('MMMDo') + " " + moment(date).format('HH:mm');
        };
    })
    .filter('toFixed', function () {
        return function (date) {
            return date.toFinite(2);
        };
    })
    .filter('toLocalDate', function () {
        return function (date) {
            if (date) {
                if (parseInt(date) > 1400000) {
                    date = (date + '000') * 1;
                }
                return moment(date).format('YYYY') + "年 " + moment(date).format('MMMDo');

            } else {
                return "未填写或未知";
            }
        };
    })
    .filter('toLocalMonth', function () {
        return function (date) {
            if (date) {
                if (parseInt(date) > 1400000) {
                    date = (date + '000') * 1;
                }
                return moment(date).format('YYYY') + "年 " + moment(date).format('MMM');

            } else {
                return "未填写或未知";
            }
        };
    })
    .filter("myJson", function () {
        return function (json) {
            if (json) {
                return JSON.parse(json);
            } else {
                return "无";
            }
        }
    })
    .filter("nullToString", function () {
        return function (string) {
            if (string) {
                return string;
            } else {
                return "暂无";
            }
        }
    })
    .filter("nullToMoney", function () {
        return function (string) {
            if (string) {
                return string + " 元";
            } else {
                return "无";
            }
        }
    })
    .filter("nullToNumber", function () {
        return function (string) {
            if (string) {
                return string;
            } else {
                return "0";
            }
        }
    })
    .filter("returnEmpty", function () {
        return function (string) {
            if (string) {
                return string;
            } else {
                return "";
            }
        }
    })
    .filter("gpsStatus", function () {
        return function (status) {
            var output = "无";
            switch (status) {
                case "PENDING":
                    output = "待确认";
                    break;
                case "NORMAL":
                    output = "正常";
                    break;
                case "ABNORMAL":
                    output = "异常";
                    break;
            }
            return output;
        }
    })
    .filter("gpsLogToString",[
        "$filter",
        function ($filter) {
            return function (gpsStatus) {
                var gpsStatus = JSON.parse(gpsStatus);
                return "状态由" + $filter("gpsStatus")(gpsStatus["old"]) + "变更为" + $filter("gpsStatus")(gpsStatus["new"]);
            }
        }
    ])
    .filter("formatNoticeTextEnter",function(){
        return function(notice){
            var notice = JSON.parse(notice).MsgBody[0].MsgContent.Data;
            return notice.replace(/\r\n/g,"<br>");
        }
    })
    .filter("formatNoticeEntity",function(){
        return function(notice){
            var entity = JSON.parse(JSON.parse(notice).MsgBody[0].MsgContent.Ext);
            return entity;
        }
    })