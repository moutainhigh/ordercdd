"use strict";

/**
 * Config for the router
 */
angular.module("app")

    .config(
        ["$stateProvider", "$urlRouterProvider", "JQ_CONFIG", "MODULE_CONFIG",
            function ($stateProvider, $urlRouterProvider, JQ_CONFIG, MODULE_CONFIG) {

                $urlRouterProvider.otherwise("/dashboard");

                //工作台
                $stateProvider
                    .state("exceptions", {
                        url: "/exceptions",
                        templateUrl: "tpl/exceptions/list.html"
                    })
                    .state("dashboard", {
                        url: "/dashboard",
                        templateUrl: "tpl/dashboards/welcome.html"
                    });

                //消息通知
                $stateProvider
                    .state("notices",{
                        url:"/notices",
                        templateUrl: "tpl/notices/list.html"
                    });

                $stateProvider
                    //工作流类开发
                    .state("workflowEntity", {
                        url: "/workflowEntity/workflows/{workflow_id}/{entity_key}",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "template/" + $stateParams.entity_key + "/module.html";
                        }
                    })
                    .state("workflowEntity.list", {
                        url: "/list",
                        cache: false,
                        params:{
                            entity_key:null,
                            workflow_id:null
                        },
                        templateUrl: function ($stateParams) {
                            return "template/" + $stateParams.entity_key + "/list.html";
                        }
                    })
                    .state("workflowEntity.form", {
                        url: "/{id}/form",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "template/" + $stateParams.entity_key + "/form.html";
                        }
                    })
                    .state("workflowEntity.home", {
                        url: "/{id}",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            //return "template/entities/home.html";
                            return "template/" + $stateParams.entity_key + "/home.html";
                        }
                    })
                    .state("workflowEntity.home.profile", {
                        url: "/profile",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            //return "template/entities/profile.html";
                            return "template/" + $stateParams.entity_key + "/profile.html";
                        }
                    })
                    .state("workflowEntity.home.profile.info", {
                        url: "/info",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            //return "template/entities/info.html";
                            return "template/" + $stateParams.entity_key + "/info.html";
                        }
                    })
                    .state("workflowEntity.home.profile.form", {
                        url: "/update",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "template/" + $stateParams.entity_key + "/form.html";
                        }
                    })
                    .state("workflowEntity.home.profile.page", {
                        url: "/{entity_key}/{view_type}",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "template/" + $stateParams.entity_key + "/" + $stateParams.view_type + ".html";
                            // return  "template/entities/user/list.html";
                        }
                    });

                $stateProvider
                    //审计类开发
                    .state("auditEntity", {
                        url: "/auditEntity/{entity_key}",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "template/" + $stateParams.entity_key + "/module.html";
                        }
                    })
                    .state("auditEntity.list", {
                        url: "/list",
                        cache: false,
                        params:{
                            entity_key:null
                        },
                        templateUrl: function ($stateParams) {
                            return "template/" + $stateParams.entity_key + "/list.html";
                        }
                    })
                    .state("auditEntity.form", {
                        url: "/{id}/form",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "template/" + $stateParams.entity_key + "/form.html";
                        }
                    })
                    .state("auditEntity.home", {
                        url: "/{id}",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            //return "template/entities/home.html";
                            return "template/" + $stateParams.entity_key + "/home.html";
                        }
                    })
                    .state("auditEntity.home.profile", {
                        url: "/profile",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            //return "template/entities/profile.html";
                            return "template/" + $stateParams.entity_key + "/profile.html";
                        }
                    })
                    .state("auditEntity.home.profile.info", {
                        url: "/info",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            //return "template/entities/info.html";
                            return "template/" + $stateParams.entity_key + "/info.html";
                        }
                    })
                    .state("auditEntity.home.profile.form", {
                        url: "/update",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "template/" + $stateParams.entity_key + "/form.html";
                        }
                    })
                    .state("auditEntity.home.profile.page", {
                        url: "/{entity_key}/{view_type}",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "template/" + $stateParams.entity_key + "/" + $stateParams.view_type + ".html";
                            // return  "template/entities/user/list.html";
                        }
                    });

                $stateProvider
                    //工作流配置
                    .state("workflowEntityConfig", {
                        url: "/workflowEntityConfig/{entity_key}",
                        cache: false,
                        controller: "workflowEntityConfigModuleController",
                        templateUrl: function ($stateParams) {
                            return "tpl/workflowEntityConfigs/entityConfigLayout.html";
                            //return  "template/entities/" + $stateParams.entity_type + "/module.html";
                        }
                    })
                    .state("workflowEntityConfig.acts", {
                        url: "/acts",
                        controller: "workflowEntityConfigActListController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/workflowEntityConfigs/actList.html";
                        }
                    })
                    .state("workflowEntityConfig.actCreate", {
                        url: "/act/create",
                        controller: "workflowEntityConfigActFormController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/workflowEntityConfigs/actForm.html";
                        }
                    })
                    .state("workflowEntityConfig.actEdit", {
                        url: "/act/{act_id}",
                        controller: "workflowEntityConfigActFormController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/workflowEntityConfigs/actForm.html";
                        }
                    })
                    .state("workflowEntityConfig.actRole", {
                        url: "/act/{act_id}/roles",
                        controller: "workflowEntityConfigActRoleController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/workflowEntityConfigs/actRole.html";
                        }
                    })
                    .state("workflowEntityConfig.states", {
                        url: "/states",
                        controller: "workflowEntityConfigStateListController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/workflowEntityConfigs/stateList.html";
                        }
                    })
                    .state("workflowEntityConfig.stateCreate", {
                        url: "/state/create",
                        controller: "workflowEntityConfigStateFormController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/workflowEntityConfigs/stateForm.html";
                        }
                    })
                    .state("workflowEntityConfig.stateEdit", {
                        url: "/state/{state_id}",
                        controller: "workflowEntityConfigStateFormController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/workflowEntityConfigs/stateForm.html";
                        }
                    })
                    .state("workflowEntityConfig.stateAct", {
                        url: "/state/{state_id}/acts",
                        controller: "workflowEntityConfigStateActController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/workflowEntityConfigs/stateAct.html";
                        }
                    })
                    .state("workflowEntityConfig.workflows", {
                        url: "/workflows",
                        controller: "workflowEntityConfigWorkflowListController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/workflowEntityConfigs/workflowList.html";
                        }
                    })
                    .state("workflowEntityConfig.workflowCreate", {
                        url: "/workflow/create",
                        controller: "workflowEntityConfigWorkflowFormController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/workflowEntityConfigs/workflowForm.html";
                        }
                    })
                    .state("workflowEntityConfig.workflowEdit", {
                        url: "/workflow/{workflow_id}",
                        controller: "workflowEntityConfigWorkflowFormController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/workflowEntityConfigs/workflowForm.html";
                        }
                    })
                    .state("workflowEntityConfig.workflowState", {
                        url: "/workflow/{workflow_id}/states",
                        controller: "workflowEntityConfigWorkflowStateController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/workflowEntityConfigs/workflowState.html";
                        }
                    });

                $stateProvider
                    //审计配置
                    .state("auditEntityConfig", {
                        url: "/auditEntityConfig/{entity_key}",
                        cache: false,
                        controller: "auditEntityConfigModuleController",
                        templateUrl: function ($stateParams) {
                            return "tpl/auditEntityConfigs/entityConfigLayout.html";
                            //return  "template/entities/" + $stateParams.entity_type + "/module.html";
                        }
                    })
                    .state("auditEntityConfig.acts", {
                        url: "/acts",
                        controller: "auditEntityConfigActListController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/auditEntityConfigs/actList.html";
                        }
                    })
                    .state("auditEntityConfig.actCreate", {
                        url: "/act/create",
                        controller: "auditEntityConfigActFormController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/auditEntityConfigs/actForm.html";
                        }
                    })
                    .state("auditEntityConfig.actEdit", {
                        url: "/act/{act_id}",
                        controller: "auditEntityConfigActFormController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/auditEntityConfigs/actForm.html";
                        }
                    })
                    .state("auditEntityConfig.actRole", {
                        url: "/act/{act_id}/roles",
                        controller: "auditEntityConfigActRoleController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/auditEntityConfigs/actRole.html";
                        }
                    })
                    .state("auditEntityConfig.states", {
                        url: "/states",
                        controller: "auditEntityConfigStateListController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/auditEntityConfigs/stateList.html";
                        }
                    })
                    .state("auditEntityConfig.stateCreate", {
                        url: "/state/create",
                        controller: "auditEntityConfigStateFormController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/auditEntityConfigs/stateForm.html";
                        }
                    })
                    .state("auditEntityConfig.stateEdit", {
                        url: "/state/{state_id}",
                        controller: "auditEntityConfigStateFormController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/auditEntityConfigs/stateForm.html";
                        }
                    })
                    .state("auditEntityConfig.stateAct", {
                        url: "/state/{state_id}/acts",
                        controller: "auditEntityConfigStateActController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/auditEntityConfigs/stateAct.html";
                        }
                    });

                $stateProvider
                    //菜单类审计实体配置，最主要区别是“菜单有对应的可显示角色”/还有父菜单
                    .state("menuConfig", {
                        url: "/menuConfig/{entity_key}",
                        cache: false,
                        controller: "menuConfigModuleController",
                        templateUrl: function ($stateParams) {
                            return "tpl/menuConfigs/menuConfigLayout.html";
                        },
                        resolve: load(["js/controllers/menuConfigControllers.js", "angularBootstrapNavTree"])
                    })
                    .state("menuConfig.create", {
                        url: "/create",
                        controller: "menuConfigFormController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/menuConfigs/form.html";
                        }
                    })
                    .state("menuConfig.edit", {
                        url: "/edit/{id}",
                        controller: "menuConfigFormController",
                        cache: false,
                        templateUrl: function ($stateParams) {
                            return "tpl/menuConfigs/form.html";
                        }
                    });

                function load(srcs, callback) {
                    return {
                        deps: ["$ocLazyLoad", "$q",
                            function ($ocLazyLoad, $q) {
                                var deferred = $q.defer();
                                var promise  = false;
                                srcs         = angular.isArray(srcs) ? srcs : srcs.split(/\s+/);
                                if (!promise) {
                                    promise = deferred.promise;
                                }
                                angular.forEach(srcs, function (src) {
                                    promise = promise.then(function () {
                                        if (JQ_CONFIG[src]) {
                                            return $ocLazyLoad.load(JQ_CONFIG[src]);
                                        }
                                        angular.forEach(MODULE_CONFIG, function (module) {
                                            if (module.name == src) {
                                                name = module.name;
                                            } else {
                                                name = src;
                                            }
                                        });
                                        return $ocLazyLoad.load(name);
                                    });
                                });
                                deferred.resolve();
                                return callback ? promise.then(function () {
                                    return callback();
                                }) : promise;
                            }]
                    }
                }


            }
        ]
    );
