/**
 * Created by win7 on 2017/9/13.
 */
angular.module('app.routes',[])
    .config(function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise('/myLoan');
        $stateProvider
            .state('login', {
                url: '/login',
                templateUrl:'templates/login.html',
                controller:'loginCtrl',
                params:{
                    source:null
                }
            })
            .state('cddApply', {
                url: '/cddApply',
                //abstract: true,
                templateUrl:'templates/cddApply/cddApply.html',
                controller:'cddAppCtrl'
            })
            .state('cddApplyLogin', {
                url: '/cddApplyLogin',
                //abstract: true,
                templateUrl:'templates/cddApply/cddApplyLogin.html',
                controller:'cddApplyLoginCtrl'
            })
            .state('myBankCardOne', {
                url: '/myBankCardOne',
                templateUrl:'templates/customerService/myBankCardOne.html',
                controller:'myBankCardOneCtrl'
            })
            .state('myBankCardTwo', {
                url: '/myBankCardTwo',
                templateUrl:'templates/customerService/myBankCardTwo.html',
                controller:'myBankCardTwoCtrl',
                params:{
                    bankName:null,
                    bankNumber:null
                }
            })
            .state('scheduleQueryLogin', {
                url: '/scheduleQueryLogin',
                templateUrl:'templates/customerService/scheduleQueryLogin.html',
                controller:'scheduleQueryLoginCtrl'
            })
            .state('scheduleQuery', {
                url: '/scheduleQuery',
                templateUrl:'templates/customerService/scheduleQuery.html',
                controller:'scheduleQueryCtrl'
            })
            .state('repaymentPlan', {
                url: '/repaymentPlan',
                templateUrl:'templates/customerService/repaymentPlan.html',
                controller:'repaymentPlanCtrl'
            })
            .state('myLoanLogin', {
                url: '/myLoanLogin',
                templateUrl:'templates/customerService/myLoanLogin.html',
                controller:'myLoanLoginCtrl',
            })
            .state('myLoan', {
                url: '/myLoan',
                templateUrl:'templates/customerService/myLoan.html',
                controller:'myLoanCtrl',
                cache:false
            })
            .state('loanDetailed', {
                url: '/loanDetailed',
                templateUrl:'templates/customerService/loanDetailed.html',
                controller:'loanDetailedCtrl'
            })
            .state('loanLogs', {
                url: '/loanLogs',
                templateUrl:'templates/customerService/loanLogs.html',
                controller:'loanLogsCtrl',
                params:{
                    id:null
                }
            })
            .state('repayMentDetail', {
                url: '/repayMentDetail',
                templateUrl:'templates/customerService/repayMentDetail.html',
                controller:'repayMentDetailCtrl',
                params:{
                    loanSn:null,
                    repaymentAmount:null
                }
            })
            .state('earlyRepayMent',{
                url: '/earlyRepayMent',
                templateUrl:'templates/customerService/earlyRepayMent.html',
                controller:'earlyRepayMentCtrl',
                params:{
                    loanSn:null,
                    repaymentAmount:null
                }
            })
    })
