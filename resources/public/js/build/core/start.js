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
var c__5879__auto___9709 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__5879__auto___9709){
return (function (){
var f__5880__auto__ = (function (){var switch__5864__auto__ = ((function (c__5879__auto___9709){
return (function (state_9699){
var state_val_9700 = (state_9699[(1)]);
if((state_val_9700 === (1))){
var inst_9685 = [new cljs.core.Keyword(null,"query-params","query-params",900640534)];
var inst_9686 = ["query"];
var inst_9687 = ["salzgitter"];
var inst_9688 = cljs.core.PersistentHashMap.fromArrays(inst_9686,inst_9687);
var inst_9689 = [inst_9688];
var inst_9690 = cljs.core.PersistentHashMap.fromArrays(inst_9685,inst_9689);
var inst_9691 = cljs_http.client.get.call(null,"query",inst_9690);
var state_9699__$1 = state_9699;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_9699__$1,(2),inst_9691);
} else {
if((state_val_9700 === (2))){
var inst_9693 = (state_9699[(2)]);
var inst_9694 = new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(inst_9693);
var inst_9695 = cljs.core.println.call(null,inst_9694);
var inst_9696 = new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(inst_9693);
var inst_9697 = cljs.core.println.call(null,inst_9696);
var state_9699__$1 = (function (){var statearr_9701 = state_9699;
(statearr_9701[(7)] = inst_9695);

return statearr_9701;
})();
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_9699__$1,inst_9697);
} else {
return null;
}
}
});})(c__5879__auto___9709))
;
return ((function (switch__5864__auto__,c__5879__auto___9709){
return (function() {
var state_machine__5865__auto__ = null;
var state_machine__5865__auto____0 = (function (){
var statearr_9705 = [null,null,null,null,null,null,null,null];
(statearr_9705[(0)] = state_machine__5865__auto__);

(statearr_9705[(1)] = (1));

return statearr_9705;
});
var state_machine__5865__auto____1 = (function (state_9699){
while(true){
var ret_value__5866__auto__ = (function (){try{while(true){
var result__5867__auto__ = switch__5864__auto__.call(null,state_9699);
if(cljs.core.keyword_identical_QMARK_.call(null,result__5867__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__5867__auto__;
}
break;
}
}catch (e9706){if((e9706 instanceof Object)){
var ex__5868__auto__ = e9706;
var statearr_9707_9710 = state_9699;
(statearr_9707_9710[(5)] = ex__5868__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_9699);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e9706;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__5866__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__9711 = state_9699;
state_9699 = G__9711;
continue;
} else {
return ret_value__5866__auto__;
}
break;
}
});
state_machine__5865__auto__ = function(state_9699){
switch(arguments.length){
case 0:
return state_machine__5865__auto____0.call(this);
case 1:
return state_machine__5865__auto____1.call(this,state_9699);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__5865__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__5865__auto____0;
state_machine__5865__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__5865__auto____1;
return state_machine__5865__auto__;
})()
;})(switch__5864__auto__,c__5879__auto___9709))
})();
var state__5881__auto__ = (function (){var statearr_9708 = f__5880__auto__.call(null);
(statearr_9708[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__5879__auto___9709);

return statearr_9708;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__5881__auto__);
});})(c__5879__auto___9709))
);

core.start.init = (function init(){
return reagent.core.render_component.call(null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [core.start.list_of_stocks], null),document.body);
});
