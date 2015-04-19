// Compiled by ClojureScript 0.0-2411
goog.provide('core.start');
goog.require('cljs.core');
goog.require('cljs.core.async');
goog.require('reagent.core');
goog.require('reagent.core');
goog.require('cljs_http.client');
goog.require('cljs_http.client');
goog.require('cljs.core.async');
cljs.core.enable_console_print_BANG_.call(null);
core.start.init = (function init(){
return reagent.core.render_component.call(null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [core.start.list_of_stocks], null),document.body);
});
var c__5879__auto___15056 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__5879__auto___15056){
return (function (){
var f__5880__auto__ = (function (){var switch__5864__auto__ = ((function (c__5879__auto___15056){
return (function (state_15046){
var state_val_15047 = (state_15046[(1)]);
if((state_val_15047 === (1))){
var inst_15032 = [new cljs.core.Keyword(null,"edn-params","edn-params",894273052)];
var inst_15033 = [new cljs.core.Keyword(null,"query","query",-1288509510)];
var inst_15034 = ["salzgitter"];
var inst_15035 = cljs.core.PersistentHashMap.fromArrays(inst_15033,inst_15034);
var inst_15036 = [inst_15035];
var inst_15037 = cljs.core.PersistentHashMap.fromArrays(inst_15032,inst_15036);
var inst_15038 = cljs_http.client.get.call(null,"query",inst_15037);
var state_15046__$1 = state_15046;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_15046__$1,(2),inst_15038);
} else {
if((state_val_15047 === (2))){
var inst_15040 = (state_15046[(2)]);
var inst_15041 = new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(inst_15040);
var inst_15042 = cljs.core.println.call(null,inst_15041);
var inst_15043 = new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(inst_15040);
var inst_15044 = cljs.core.println.call(null,inst_15043);
var state_15046__$1 = (function (){var statearr_15048 = state_15046;
(statearr_15048[(7)] = inst_15042);

return statearr_15048;
})();
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_15046__$1,inst_15044);
} else {
return null;
}
}
});})(c__5879__auto___15056))
;
return ((function (switch__5864__auto__,c__5879__auto___15056){
return (function() {
var state_machine__5865__auto__ = null;
var state_machine__5865__auto____0 = (function (){
var statearr_15052 = [null,null,null,null,null,null,null,null];
(statearr_15052[(0)] = state_machine__5865__auto__);

(statearr_15052[(1)] = (1));

return statearr_15052;
});
var state_machine__5865__auto____1 = (function (state_15046){
while(true){
var ret_value__5866__auto__ = (function (){try{while(true){
var result__5867__auto__ = switch__5864__auto__.call(null,state_15046);
if(cljs.core.keyword_identical_QMARK_.call(null,result__5867__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__5867__auto__;
}
break;
}
}catch (e15053){if((e15053 instanceof Object)){
var ex__5868__auto__ = e15053;
var statearr_15054_15057 = state_15046;
(statearr_15054_15057[(5)] = ex__5868__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_15046);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e15053;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__5866__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__15058 = state_15046;
state_15046 = G__15058;
continue;
} else {
return ret_value__5866__auto__;
}
break;
}
});
state_machine__5865__auto__ = function(state_15046){
switch(arguments.length){
case 0:
return state_machine__5865__auto____0.call(this);
case 1:
return state_machine__5865__auto____1.call(this,state_15046);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__5865__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__5865__auto____0;
state_machine__5865__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__5865__auto____1;
return state_machine__5865__auto__;
})()
;})(switch__5864__auto__,c__5879__auto___15056))
})();
var state__5881__auto__ = (function (){var statearr_15055 = f__5880__auto__.call(null);
(statearr_15055[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__5879__auto___15056);

return statearr_15055;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__5881__auto__);
});})(c__5879__auto___15056))
);

