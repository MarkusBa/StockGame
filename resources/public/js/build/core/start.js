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
var c__5879__auto___15110 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__5879__auto___15110){
return (function (){
var f__5880__auto__ = (function (){var switch__5864__auto__ = ((function (c__5879__auto___15110){
return (function (state_15100){
var state_val_15101 = (state_15100[(1)]);
if((state_val_15101 === (1))){
var inst_15086 = [new cljs.core.Keyword(null,"edn-params","edn-params",894273052)];
var inst_15087 = [new cljs.core.Keyword(null,"query","query",-1288509510)];
var inst_15088 = ["salzgitter"];
var inst_15089 = cljs.core.PersistentHashMap.fromArrays(inst_15087,inst_15088);
var inst_15090 = [inst_15089];
var inst_15091 = cljs.core.PersistentHashMap.fromArrays(inst_15086,inst_15090);
var inst_15092 = cljs_http.client.get.call(null,"query",inst_15091);
var state_15100__$1 = state_15100;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_15100__$1,(2),inst_15092);
} else {
if((state_val_15101 === (2))){
var inst_15094 = (state_15100[(2)]);
var inst_15095 = new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(inst_15094);
var inst_15096 = cljs.core.println.call(null,inst_15095);
var inst_15097 = new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(inst_15094);
var inst_15098 = cljs.core.println.call(null,inst_15097);
var state_15100__$1 = (function (){var statearr_15102 = state_15100;
(statearr_15102[(7)] = inst_15096);

return statearr_15102;
})();
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_15100__$1,inst_15098);
} else {
return null;
}
}
});})(c__5879__auto___15110))
;
return ((function (switch__5864__auto__,c__5879__auto___15110){
return (function() {
var state_machine__5865__auto__ = null;
var state_machine__5865__auto____0 = (function (){
var statearr_15106 = [null,null,null,null,null,null,null,null];
(statearr_15106[(0)] = state_machine__5865__auto__);

(statearr_15106[(1)] = (1));

return statearr_15106;
});
var state_machine__5865__auto____1 = (function (state_15100){
while(true){
var ret_value__5866__auto__ = (function (){try{while(true){
var result__5867__auto__ = switch__5864__auto__.call(null,state_15100);
if(cljs.core.keyword_identical_QMARK_.call(null,result__5867__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__5867__auto__;
}
break;
}
}catch (e15107){if((e15107 instanceof Object)){
var ex__5868__auto__ = e15107;
var statearr_15108_15111 = state_15100;
(statearr_15108_15111[(5)] = ex__5868__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_15100);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e15107;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__5866__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__15112 = state_15100;
state_15100 = G__15112;
continue;
} else {
return ret_value__5866__auto__;
}
break;
}
});
state_machine__5865__auto__ = function(state_15100){
switch(arguments.length){
case 0:
return state_machine__5865__auto____0.call(this);
case 1:
return state_machine__5865__auto____1.call(this,state_15100);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__5865__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__5865__auto____0;
state_machine__5865__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__5865__auto____1;
return state_machine__5865__auto__;
})()
;})(switch__5864__auto__,c__5879__auto___15110))
})();
var state__5881__auto__ = (function (){var statearr_15109 = f__5880__auto__.call(null);
(statearr_15109[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__5879__auto___15110);

return statearr_15109;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__5881__auto__);
});})(c__5879__auto___15110))
);

