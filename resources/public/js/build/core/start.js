// Compiled by ClojureScript 0.0-2411
goog.provide('core.start');
goog.require('cljs.core');
goog.require('cljs.core.async');
goog.require('reagent.core');
goog.require('reagent.core');
goog.require('reagent.core');
goog.require('cognitect.transit');
goog.require('cognitect.transit');
goog.require('cljs_http.client');
goog.require('cljs_http.client');
goog.require('cljs.core.async');
cljs.core.enable_console_print_BANG_.call(null);
core.start.rdr = cognitect.transit.reader.call(null,new cljs.core.Keyword(null,"json","json",1279968570));
core.start.read_response = (function read_response(response){
var temp1 = new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(response);
var nthg = cljs.core.println.call(null,temp1);
var temp = cognitect.transit.read.call(null,core.start.rdr,temp1);
var text = cljs.core.get.call(null,cljs.core.get.call(null,temp,"ResultSet"),"Result");
cljs.core.println.call(null,text);

return text;
});
core.start.state = reagent.core.atom.call(null,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"query","query",-1288509510),null,new cljs.core.Keyword(null,"stocks","stocks",-617352902),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 6, ["symbol","SZG.SG","name","SALZGITTER","exch","STU","type","S","exchDisp","Stuttgart","typeDisp","Equity"], null),new cljs.core.PersistentArrayMap(null, 6, ["symbol","SZG.MU","name","SALZGITTER","exch","MUN","type","S","exchDisp","Munich","typeDisp","Equity"], null)], null)], null));
core.start.yahooquery = (function yahooquery(param){
var c__5879__auto__ = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__5879__auto__){
return (function (){
var f__5880__auto__ = (function (){var switch__5864__auto__ = ((function (c__5879__auto__){
return (function (state_9718){
var state_val_9719 = (state_9718[(1)]);
if((state_val_9719 === (1))){
var inst_9706 = [new cljs.core.Keyword(null,"query-params","query-params",900640534)];
var inst_9707 = ["query"];
var inst_9708 = [param];
var inst_9709 = cljs.core.PersistentHashMap.fromArrays(inst_9707,inst_9708);
var inst_9710 = [inst_9709];
var inst_9711 = cljs.core.PersistentHashMap.fromArrays(inst_9706,inst_9710);
var inst_9712 = cljs_http.client.get.call(null,"query",inst_9711);
var state_9718__$1 = state_9718;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_9718__$1,(2),inst_9712);
} else {
if((state_val_9719 === (2))){
var inst_9714 = (state_9718[(2)]);
var inst_9715 = core.start.read_response.call(null,inst_9714);
var inst_9716 = cljs.core.swap_BANG_.call(null,core.start.state,cljs.core.assoc,new cljs.core.Keyword(null,"stocks","stocks",-617352902),inst_9715);
var state_9718__$1 = state_9718;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_9718__$1,inst_9716);
} else {
return null;
}
}
});})(c__5879__auto__))
;
return ((function (switch__5864__auto__,c__5879__auto__){
return (function() {
var state_machine__5865__auto__ = null;
var state_machine__5865__auto____0 = (function (){
var statearr_9723 = [null,null,null,null,null,null,null];
(statearr_9723[(0)] = state_machine__5865__auto__);

(statearr_9723[(1)] = (1));

return statearr_9723;
});
var state_machine__5865__auto____1 = (function (state_9718){
while(true){
var ret_value__5866__auto__ = (function (){try{while(true){
var result__5867__auto__ = switch__5864__auto__.call(null,state_9718);
if(cljs.core.keyword_identical_QMARK_.call(null,result__5867__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__5867__auto__;
}
break;
}
}catch (e9724){if((e9724 instanceof Object)){
var ex__5868__auto__ = e9724;
var statearr_9725_9727 = state_9718;
(statearr_9725_9727[(5)] = ex__5868__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_9718);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e9724;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__5866__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__9728 = state_9718;
state_9718 = G__9728;
continue;
} else {
return ret_value__5866__auto__;
}
break;
}
});
state_machine__5865__auto__ = function(state_9718){
switch(arguments.length){
case 0:
return state_machine__5865__auto____0.call(this);
case 1:
return state_machine__5865__auto____1.call(this,state_9718);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__5865__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__5865__auto____0;
state_machine__5865__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__5865__auto____1;
return state_machine__5865__auto__;
})()
;})(switch__5864__auto__,c__5879__auto__))
})();
var state__5881__auto__ = (function (){var statearr_9726 = f__5880__auto__.call(null);
(statearr_9726[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__5879__auto__);

return statearr_9726;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__5881__auto__);
});})(c__5879__auto__))
);

return c__5879__auto__;
});
core.start.lister = (function lister(items){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ul","ul",-1349521403),(function (){var iter__4429__auto__ = (function iter__9733(s__9734){
return (new cljs.core.LazySeq(null,(function (){
var s__9734__$1 = s__9734;
while(true){
var temp__4403__auto__ = cljs.core.seq.call(null,s__9734__$1);
if(temp__4403__auto__){
var s__9734__$2 = temp__4403__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,s__9734__$2)){
var c__4427__auto__ = cljs.core.chunk_first.call(null,s__9734__$2);
var size__4428__auto__ = cljs.core.count.call(null,c__4427__auto__);
var b__9736 = cljs.core.chunk_buffer.call(null,size__4428__auto__);
if((function (){var i__9735 = (0);
while(true){
if((i__9735 < size__4428__auto__)){
var item = cljs.core._nth.call(null,c__4427__auto__,i__9735);
cljs.core.chunk_append.call(null,b__9736,cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ul","ul",-1349521403),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),"Symbol:",cljs.core.get.call(null,item,"symbol")], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),"Name:",cljs.core.get.call(null,item,"name")], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),"Exchange:",cljs.core.get.call(null,item,"exchDisp")], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),"Type:",cljs.core.get.call(null,item,"typeDisp")], null)], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),item], null)));

var G__9737 = (i__9735 + (1));
i__9735 = G__9737;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__9736),iter__9733.call(null,cljs.core.chunk_rest.call(null,s__9734__$2)));
} else {
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__9736),null);
}
} else {
var item = cljs.core.first.call(null,s__9734__$2);
return cljs.core.cons.call(null,cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ul","ul",-1349521403),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),"Symbol:",cljs.core.get.call(null,item,"symbol")], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),"Name:",cljs.core.get.call(null,item,"name")], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),"Exchange:",cljs.core.get.call(null,item,"exchDisp")], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),"Type:",cljs.core.get.call(null,item,"typeDisp")], null)], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),item], null)),iter__9733.call(null,cljs.core.rest.call(null,s__9734__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4429__auto__.call(null,items);
})()], null);
});
core.start.list_of_stocks = (function list_of_stocks(){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"h1","h1",-1896887462),"List of stocks"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [core.start.lister,new cljs.core.Keyword(null,"stocks","stocks",-617352902).cljs$core$IFn$_invoke$arity$1(cljs.core.deref.call(null,core.start.state))], null)], null);
});
core.start.atom_input = (function atom_input(state){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"type","type",1174270348),"text",new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"query","query",-1288509510).cljs$core$IFn$_invoke$arity$1(cljs.core.deref.call(null,state)),new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (p1__9738_SHARP_){
var text = p1__9738_SHARP_.target.value;
cljs.core.swap_BANG_.call(null,state,cljs.core.assoc,new cljs.core.Keyword(null,"query","query",-1288509510),text);

return core.start.yahooquery.call(null,text);
})], null)], null);
});
core.start.init = (function init(){
return reagent.core.render_component.call(null,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [core.start.atom_input,core.start.state], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [core.start.list_of_stocks], null)], null),document.body);
});
core.start.init.call(null);
core.start.yahooquery.call(null,"basf");
