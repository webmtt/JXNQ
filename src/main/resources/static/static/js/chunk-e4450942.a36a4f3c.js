(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-e4450942"],{"4f45":function(e,t,s){"use strict";s.r(t);var o=function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("el-form",{ref:"loginForm",staticClass:"login-form",attrs:{model:e.loginForm,rules:e.loginRules,"auto-complete":"on","label-position":"left"}},[s("div",{staticClass:"title-container"},[s("h3",{staticClass:"title"},[e._v("登 录")])]),e._v(" "),s("el-form-item",{attrs:{prop:"username"}},[s("span",{staticClass:"svg-container"},[s("svg-icon",{attrs:{"icon-class":"user"}})],1),e._v(" "),s("el-input",{ref:"username",attrs:{placeholder:"用户名",name:"username",type:"text",tabindex:"1"},model:{value:e.loginForm.username,callback:function(t){e.$set(e.loginForm,"username",t)},expression:"loginForm.username"}})],1),e._v(" "),s("el-form-item",{attrs:{prop:"password"}},[s("span",{staticClass:"svg-container"},[s("svg-icon",{attrs:{"icon-class":"password"}})],1),e._v(" "),s("el-input",{key:e.passwordType,ref:"password",attrs:{type:e.passwordType,placeholder:"密码",name:"password",tabindex:"2"},model:{value:e.loginForm.password,callback:function(t){e.$set(e.loginForm,"password",t)},expression:"loginForm.password"}}),e._v(" "),s("span",{staticClass:"show-pwd",on:{click:e.showPwd}},[s("svg-icon",{attrs:{"icon-class":"password"===e.passwordType?"eye":"eye-open"}})],1)],1),e._v(" "),s("div",{staticClass:"pwdHanle"},[s("el-checkbox",{model:{value:e.checked,callback:function(t){e.checked=t},expression:"checked"}},[e._v("记住密码")])],1),e._v(" "),s("div",{staticClass:"loginHanle"},[s("el-button",{staticStyle:{flex:"1","margin-bottom":"30px"},attrs:{loading:e.loading,type:"success"},nativeOn:{click:function(t){return t.preventDefault(),e.goToRegister(t)}}},[e._v("注册")]),e._v(" "),s("el-button",{staticStyle:{flex:"1","margin-bottom":"30px"},attrs:{loading:e.loading,type:"primary"},nativeOn:{click:function(t){return t.preventDefault(),e.handleLogin(t)}}},[e._v("登录")])],1)],1)},n=[],r=s("61f7"),a=s("5f87"),i={name:"Login",data:function(){var e=function(e,t,s){Object(r["b"])(t)?s():s(new Error("用户名不合法!"))},t=function(e,t,s){t.length<6?s(new Error("密码长度不能小于6位")):s()};return{checked:!0,loginForm:{username:"",password:""},loginRules:{username:[{required:!0,trigger:"blur",validator:e}],password:[{required:!0,trigger:"blur",validator:t}]},loading:!1,passwordType:"password",redirect:void 0}},watch:{$route:{handler:function(e){this.redirect=e.query&&e.query.redirect},immediate:!0}},mounted:function(){a["c"]()&&(this.loginForm=JSON.parse(a["c"]())),this.checked=!!a["c"]()},methods:{showPwd:function(){var e=this;"password"===this.passwordType?this.passwordType="":this.passwordType="password",this.$nextTick((function(){e.$refs.password.focus()}))},goToRegister:function(){this.$router.push({path:"/register"})},handleLogin:function(){var e=this;this.$refs.loginForm.validate((function(t){if(!t)return!1;e.loading=!0,e.checked?a["d"](e.loginForm):a["a"](),e.$store.dispatch("user/login",e.loginForm).then((function(){e.$router.push({path:e.redirect||"/"}),e.loading=!1})).catch((function(){e.loading=!1}))}))}}},l=i,c=(s("69df"),s("4023")),d=Object(c["a"])(l,o,n,!1,null,"7bd69dca",null);t["default"]=d.exports},"69df":function(e,t,s){"use strict";var o=s("b5fa"),n=s.n(o);n.a},b5fa:function(e,t,s){}}]);