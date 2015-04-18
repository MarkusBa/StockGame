// Compiled by ClojureScript 0.0-2411
goog.provide('core.start');
goog.require('cljs.core');
goog.require('reagent.core');
goog.require('reagent.core');
cljs.core.enable_console_print_BANG_.call(null);
core.start.greeting = (function greeting(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"h1","h1",-1896887462),"Hi"], null)], null);
});
core.start.bla = (function bla(){
return reagent.core.render_component.call(null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [core.start.greeting], null),document.body);
});
cljs.core.println.call(null,"test");

core.start.bla.call(null);
