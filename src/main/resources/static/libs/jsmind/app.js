window.onload = function () {

    $("body").css({
        height: window.innerHeight + "px",
        width: window.innerWidth + "px"
    });

};

window.onresize = function () {

    $("body").css({
        height: window.innerHeight + "px",
        width: window.innerWidth + "px"
    });

};

var options = {                   // options 将在下一章中详细介绍
    container: "workflow-mind", // [必选] 容器的ID
    editable: false,                // [可选] 是否启用编辑
    theme: "info",                // [可选] 主题
    layout: {
        hspace: 200          // 节点之间的水平间距
    },
    view:{
        line_width:1,       // 思维导图线条的粗细
        line_color:"#555"   // 思维导图线条的颜色
    }
};

var jm = new jsMind(options);



var EntityKey = window.location.search.split("?")[1].split("=")[1].replace(/\b(\w)(\w*)/g, function($0, $1, $2) {
    return $1.toUpperCase() + $2.toLowerCase();
});

var entityKey = window.location.search.split("?")[1].split("=")[1];

var workflows = [];

var mindData = null;

$("title").html(entityKey + "工作流配置查看");

$.get("/workflow/query?entityname=" + EntityKey).success(function (mindResResult) {

    mindData = mindResResult;

    $.get("/rest/" + entityKey + "Workflows").success(function (workflowResResult) {

        workflows = workflowResResult._embedded[entityKey + "Workflows"];

        var html = "";

        $.each(workflows, function (workflowIndex, workflow) {
            html += "<option value='" + workflow.label + "'>" + workflow.label + "</option>";
        });

        $("#workflow-select").append(html);

        getWorkflowStates();

    });

});

function getWorkflowStates() {

    var selectedWorkflow = $("#workflow-select").val();

    $.each(mindData, function (dataIndex, data) {
        if (data.topic === selectedWorkflow) {
            paintMind(data);
        }
    });

}

function paintMind(data) {

    var mind = {
        /* 元数据，定义思维导图的名称、作者、版本等信息 */
        meta: {
            name: "工作流程配置图",
            author: "小金",
            version: "0.2"
        },
        /* 数据格式声明 */
        format: "node_tree",
        /* 数据内容 */
        data: data
    };

    jm.show(mind);

}