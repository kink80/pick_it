/*
jQWidgets v2.1.0 (2012-May-04)
Copyright (c) 2011-2012 jQWidgets.
License: http://jqwidgets.com/license/
*/

(function(a){a.jqx.jqxWidget("jqxListBox","",{});a.extend(a.jqx._jqxListBox.prototype,{defineInstance:function(){this.disabled=false;this.width=null;this.height=null;this.items=new Array();this.multiple=false;this.selectedIndex=-1;this.selectedIndexes=new Array();this.source=null;this.scrollBarSize=15;this.enableHover=true;this.enableSelection=true;this.visualItems=new Array();this.groups=new Array();this.equalItemsWidth=true;this.itemHeight=-1;this.visibleItems=new Array();this.emptyGroupText="Group";this.checkboxes=false;this.hasThreeStates=false;this.autoHeight=false;this.roundedcorners=true;this.touchMode="auto";this.displayMember="";this.valueMember="";this.searchMode="startswithignorecase";this.incrementalSearch=true;this.incrementalSearchDelay=700;this.events=["select","unselect","change","checkchange",]},createInstance:function(c){var b=this;this.host.addClass(this.toThemeProperty("jqx-listbox"));this.host.addClass(this.toThemeProperty("jqx-reset"));this.host.addClass(this.toThemeProperty("jqx-rc-all"));this.host.addClass(this.toThemeProperty("jqx-widget"));this.host.addClass(this.toThemeProperty("jqx-widget-content"));var g=false;if(this.width!=null&&this.width.toString().indexOf("%")!=-1){this.host.width(this.width);g=true}if(this.height!=null&&this.height.toString().indexOf("%")!=-1){this.host.height(this.height);if(this.host.height()==0){this.host.height(200)}g=true}if(this.width!=null&&this.width.toString().indexOf("px")!=-1){this.host.width(this.width)}else{if(this.width!=undefined&&!isNaN(this.width)){this.host.width(this.width)}}if(this.height!=null&&this.height.toString().indexOf("px")!=-1){this.host.height(this.height)}else{if(this.height!=undefined&&!isNaN(this.height)){this.host.height(this.height)}}var d=a("<div tabIndex=0 style='-webkit-appearance: none; background: transparent; outline: none; width:100%; height: 100%; align:left; border: 0px; padding: 0px; margin: 0px; left: 0px; top: 0px; valign:top; position: relative;'><div tabIndex=1 style='-webkit-appearance: none; border: none; background: transparent; outline: none; width:100%; height: 100%; padding: 0px; margin: 0px; align:left; left: 0px; top: 0px; valign:top; position: relative;'><div id='listBoxContent' tabIndex=2 style='-webkit-appearance: none; border: none; background: transparent; outline: none; border: none; padding: 0px; overflow: hidden; margin: 0px; align:left; valign:top; left: 0px; top: 0px; position: absolute;'/><div id='verticalScrollBar"+this.element.id+"' style='align:left; valign:top; left: 0px; top: 0px; position: absolute;'/><div id='horizontalScrollBar"+this.element.id+"' style='align:left; valign:top; left: 0px; top: 0px; position: absolute;'/><div id='bottomRight' style='align:left; valign:top; left: 0px; top: 0px; border: none; position: absolute;'/></div></div>");this.host.attr("tabIndex",1);this.host.append(d);var f=this.host.find("#verticalScrollBar"+this.element.id);if(!f.jqxScrollBar){alert("jqxscrollbar.js is not loaded.");return}if(this.touchMode==true){a.jqx.mobile.setMobileSimulator(this.element)}this.vScrollBar=f.jqxScrollBar({vertical:true,theme:this.theme,touchMode:this.touchMode,largestep:parseInt(this.host.height())/2});var e=this.host.find("#horizontalScrollBar"+this.element.id);this.hScrollBar=e.jqxScrollBar({vertical:false,touchMode:this.touchMode,theme:this.theme});this.content=this.host.find("#listBoxContent");this.bottomRight=this.host.find("#bottomRight").addClass(this.toThemeProperty("jqx-listbox-bottomright"));this.bottomRight[0].id="bottomRight"+this.element.id;this.vScrollBar.css("visibility","visible");this.hScrollBar.css("visibility","visible");this.vScrollInstance=a.data(this.vScrollBar[0],"jqxScrollBar").instance;this.hScrollInstance=a.data(this.hScrollBar[0],"jqxScrollBar").instance;if(this.isTouchDevice()){a.jqx.mobile.touchScroll(this.content,b.vScrollInstance.max,function(l,k){if(b.vScrollBar.css("visibility")=="visible"){var h=b.vScrollInstance.value;b.vScrollInstance.setPosition(h+k);b._lastScroll=new Date()}if(b.hScrollBar.css("visibility")=="visible"){var h=b.hScrollInstance.value;b.hScrollInstance.setPosition(h+l);b._lastScroll=new Date()}})}this.host.addClass("jqx-disableselect");if(g){a(window).resize(function(){b._updateSize()})}},isTouchDevice:function(){var b=a.jqx.mobile.isTouchDevice();if(this.touchMode==true){b=true}else{if(this.touchMode==false){b=false}}return b},beginUpdateLayout:function(){this.updating=true},resumeUpdateLayout:function(){this.updating=false;this.vScrollInstance.value=0;this._render(false)},_updateSize:function(){var c=this;var d=c.host.width();var b=c.host.height();if(!c._oldWidth){c._oldWidth=d}if(!c._oldHeight){c._oldHeight=b}setTimeout(function(){if(d!=c._oldWidth){c._updatescrollbars();c._renderItems()}if(b!=c._oldHeight){if(c.items){if(c.items.length>0&&c.virtualItemsCount*c.items[0].height<b){c._render(false)}else{c._updatescrollbars();c._renderItems()}}}c._oldWidth=d;c._oldHeight=b},1)},propertyChangedHandler:function(b,c,e,d){if(this.isInitialized==undefined||this.isInitialized==false){return}if(c=="source"||c=="scrollBarSize"||c=="equalItemsWidth"||c=="checkboxes"){b.clearSelection();b.refresh()}if(!this.updating){if(c=="width"||c=="height"){setTimeout(function(){b.vScrollInstance.value=0;if(c=="width"){if(e!=d){b.host.width(d);b._updatescrollbars();b._renderItems()}}else{if(e!=d){b.host.height(d);if(b.items){if(b.items.length>0&&b.virtualItemsCount*b.items[0].height<d){b._render(false)}else{b._updatescrollbars();b._renderItems()}}}}},1)}}if(c=="theme"){b.hScrollBar.jqxScrollBar({theme:b.theme});b.vScrollBar.jqxScrollBar({theme:b.theme});b.host.removeClass();b.host.addClass(b.toThemeProperty("jqx-listbox"));b.host.addClass(b.toThemeProperty("jqx-widget"));b.host.addClass(b.toThemeProperty("jqx-widget-content"));b.host.addClass(b.toThemeProperty("jqx-reset"));b.host.addClass(b.toThemeProperty("jqx-rc-all"));b.refresh()}if(c=="selectedIndex"){b.clearSelection();b.selectIndex(d,true)}if(c=="displayMember"||c=="valueMember"){b.refresh()}if(c=="autoHeight"){b._render()}},loadFromSelect:function(k){if(k==null){return}var c="#"+k;var f=a(c);if(f.length>0){var e=f.find("option");var b=f.find("optgroup");var d=0;var h=-1;var g=new Array();a.each(e,function(){var m=b.find(this).length>0;var o=null;if(this.text!=this.value&&(this.label==null||this.label=="")){this.label=this.text}var n={disabled:this.disabled,value:this.value,label:this.label,title:this.title,originalItem:this};var l=a.browser.msie&&a.browser.version<8;if(l){if(n.value==""&&this.text!=null&&this.text.length>0){n.value=this.text}}if(m){o=b.find(this).parent()[0].label;n.group=o}if(this.selected){h=d}g[d]=n;d++});this.source=g;this.fromSelect=true;this.clearSelection();this.selectedIndex=h;this.selectedIndexes[this.selectedIndex]=this.selectedIndex;this.refresh()}},refresh:function(){var b=this.host.find("#verticalScrollBar"+this.element.id);if(!b.jqxScrollBar){return}this.visibleItems=new Array();if(a.jqx.dataAdapter&&this.source!=null&&this.source._source){this.databind(this.source);return}this.items=this.loadItems(this.source);this._render()},_render:function(c){this._addItems();this._renderItems();var b=a.data(this.vScrollBar[0],"jqxScrollBar").instance;b.setPosition(0);if(c==undefined||c){if(this.items!=undefined&&this.items!=null){if(this.selectedIndex>=0&&this.selectedIndex<this.items.length){this.selectIndex(this.selectedIndex);this.ensureVisible(this.selectedIndex)}}}},_createID:function(){var b=Math.random()+"";b=b.replace(".","");b="99"+b;b=b/1;while(this.items[b]){b=Math.random()+"";b=b.replace(".","");b=b/1}return"listitem"+b},_hitTest:function(c,e){var d=parseInt(this.vScrollInstance.value);var b=this._searchFirstVisibleIndex(e+d,this.renderedVisibleItems);if(this.renderedVisibleItems[b]!=undefined&&this.renderedVisibleItems[b].isGroup){return null}b=this._searchFirstVisibleIndex(e+d);return this.visibleItems[b];return null},_searchFirstVisibleIndex:function(e,f){if(e==undefined){e=parseInt(this.vScrollInstance.value)}var c=0;if(f==undefined||f==null){f=this.visibleItems}var b=f.length;while(c<=b){mid=parseInt((c+b)/2);var d=f[mid];if(d==undefined){break}if(d.initialTop>e&&d.initialTop+d.height>e){b=mid-1}else{if(d.initialTop<e&&d.initialTop+d.height<e){c=mid+1}else{return mid;break}}}return 0},_renderItems:function(){if(this.items==undefined||this.items.length==0){return}var o=this.vScrollInstance;var x=this.hScrollInstance;var u=parseInt(o.value);var f=parseInt(x.value);var F=this.items.length;var z=parseInt(this.content.width())+parseInt(x.max);var d=parseInt(this.vScrollBar.outerWidth());if(this.vScrollBar.css("visibility")!="visible"){d=0}if(this.hScrollBar.css("visibility")!="visible"){z=parseInt(this.content.width())}var h=this._getVirtualItemsCount();var H=new Array();var n=0;var m=this.host.outerHeight();var D=0;var k=0;var b=0;if(o.value==0||this.visibleItems.length==0){for(indx=0;indx<this.items.length;indx++){var G=this.items[indx];if(G.visible){G.top=-u;G.initialTop=-u;if(!G.isGroup&&G.visible){this.visibleItems[k++]=G;G.visibleIndex=k-1}this.renderedVisibleItems[b++]=G;G.left=-f;var l=G.top+G.height;if(l>=0&&G.top-G.height<=m){H[n++]={index:indx,item:G}}u-=G.height}}}var c=u>0?this._searchFirstVisibleIndex(this.vScrollInstance.value,this.renderedVisibleItems):0;var A=0;n=0;var q=this.vScrollInstance.value;var B=0;while(A<100+m){var G=this.renderedVisibleItems[c];if(G==undefined){break}if(G.visible){G.left=-f;var l=G.top+G.height-q;if(l>=0&&G.initialTop-q-G.height<=2*m){H[n++]={index:c,item:G}}}c++;if(G.visible){A+=G.initialTop-q+G.height-A}B++;if(B>this.items.length-1){break}}var K=this.toThemeProperty("jqx-listitem-state-normal")+" "+this.toThemeProperty("jqx-item");var t=this.toThemeProperty("jqx-listitem-state-group");var w=this.toThemeProperty("jqx-listitem-state-disabled")+" "+this.toThemeProperty("jqx-fill-state-disabled");var J=0;var I=this;for(indx=0;indx<this.visualItems.length;indx++){var E=this.visualItems[indx];var C=function(){var y=E.find("#spanElement");y.css({visibility:"hidden"});y.removeClass();if(I.checkboxes&&I.host.jqxCheckBox){var L=E.find(".chkbox");L.css({visibility:"hidden"})}};if(indx<H.length){var G=H[indx].item;if(G.initialTop-q>=m){C();continue}var v=E.find("#spanElement");if(v.length==0){continue}v.removeClass();v.css({display:"block",visibility:"visible"});if(!G.isGroup&&!this.selectedIndexes[G.index]>=0){v.addClass(K)}else{v.addClass(t)}if(G.disabled||this.disabled){v.addClass(w)}if(this.roundedcorners){v.addClass(this.toThemeProperty("jqx-rc-all"))}if(G.html!=null&&G.html.toString().length>0){v[0].innerHTML=G.html}else{if(G.label!=null||G.value!=null){if(G.label!=null){if(v[0].innerHTML!==G.label){v[0].innerHTML=G.label}}else{if(v[0].innerHTML!==G.value){v[0].innerHTML=G.value}}}}E[0].style.left=G.left+"px";E[0].style.top=G.initialTop-q+"px";G.element=v[0];a.data(v[0],"item",G);if(G.title){v[0].title=G.title}if(this.equalItemsWidth&&!G.isGroup){if(D==0){var e=parseInt(z);var r=parseInt(v.outerWidth())-parseInt(v.width());e-=r;var p=1;if(p!=null){p=parseInt(p)}else{p=0}e-=2*p;D=e;if(this.checkboxes&&this.host.jqxCheckBox){D-=18}}v.width(D);G.width=D}if(this.checkboxes&&this.host.jqxCheckBox&&!G.isGroup){if(J==0){J=(parseInt(E.outerHeight(true))-16)/2;J++}var s=a(E.children()[0]);s[0].item=G;v.css({left:"18px"});s.css("top",J+"px");s.css({display:"block",visibility:"visible"});var g=s.jqxCheckBox("checked");if(g!=G.checked){s.jqxCheckBox({checked:G.checked,disabled:G.disabled})}}if(this.selectedIndexes[G.visibleIndex]>=0&&!G.disabled){v.addClass(this.toThemeProperty("jqx-listitem-state-selected"));v.addClass(this.toThemeProperty("jqx-fill-state-pressed"))}}else{C()}}},_calculateVirtualSize:function(){var d=0;var k=2;var g=0;var f=a("<span></span>");var e=0;var c=this.host.outerHeight();a(document.body).append(f);for(g=0;g<this.items.length;g++){var l=this.items[g];if(l.isGroup&&(l.label==""&&l.html=="")){continue}if(!l.visible){continue}if(this.itemHeight>-1){l.height=this.itemHeight;l.width=this.content.width();k+=this.itemHeight}else{if(!l.isGroup){f.addClass(this.toThemeProperty("jqx-listitem-state-normal jqx-rc-all"))}else{f.addClass(this.toThemeProperty("jqx-listitem-state-group jqx-rc-all"))}f.addClass(this.toThemeProperty("jqx-fill-state-normal"));if(this.equalItemsWidth){f.css("float","left")}if(l.html!=null&&l.html.toString().length>0){f.html(l.html)}else{if(l.label!=null||l.value!=null){if(l.label!=null){f.text(l.label)}else{f.text(l.value)}}}var b=f.outerHeight();var h=f.outerWidth();l.height=b;l.width=h;k+=b;d=Math.max(d,h)}if(k<=c){e++}}if(e<10){e=10}f.remove();return{width:d+4,height:k,itemsPerPage:e}},_getVirtualItemsCount:function(){if(this.virtualItemsCount==0){var b=parseInt(this.host.height())/5;if(b>this.items.length){b=this.items.length}return b}else{return this.virtualItemsCount}},_addItems:function(){if(this.items==undefined||this.items.length==0){this.virtualSize={width:0,height:0,itemsPerPage:0};this._updatescrollbars();this.renderedVisibleItems=new Array();if(this.itemswrapper){this.itemswrapper.children().remove()}return}var n=this;var h=0;this.visibleItems=new Array();this.renderedVisibleItems=new Array();this.content.find(".jqx-listitem-state-normal").remove();this.content.find(".jqx-listitem-state-pressed").remove();this.content.find(".jqx-listitem-state-hover").remove();this.content.find(".jqx-listitem-state-disabled").remove();this.content.find(".jqx-listitem-state-group ").remove();this._removeHandlers();this.content[0].innerHTML="";this.itemswrapper=a('<div tabIndex=1 style="outline: 0 none; overflow:hidden; width:100%; position: relative;"></div>');this.itemswrapper.height(2*this.host.height());this.content.append(this.itemswrapper);var k=this._calculateVirtualSize();var l=k.itemsPerPage*2;if(this.autoHeight){l=this.items.length}this.virtualItemsCount=Math.min(l,this.items.length);var g=this;var f=k.width;this.virtualSize=k;this.itemswrapper.width(Math.max(this.host.width(),17+k.width));for(virtualItemIndex=0;virtualItemIndex<this.virtualItemsCount;virtualItemIndex++){var m=this.items[virtualItemIndex];var b=a("<div style='border: none; tabIndex=0 width:100%; height: 100%; align:left; valign:top; position: absolute;'></div>");var c=a("<span id='spanElement'></span>");b[0].id=n._createID();c.appendTo(b);b.appendTo(this.itemswrapper);if(this.checkboxes&&this.host.jqxCheckBox){var e=a('<div tabIndex=1 style="background-color: transparent; padding: 0; margin: 0; position: absolute; float: left; width: 16px; height: 16px;" class="chkbox"/>');b.css("float","left");c.css("float","left");b.prepend(e);e.jqxCheckBox({checked:m.checked,animationShowDelay:0,animationHideDelay:0,disabled:m.disabled,enableContainerClick:false,keyboardCheck:false,hasThreeStates:this.hasThreeStates,theme:this.theme});m.checkBoxElement=e[0];var d=function(r,q){var o=r.owner.element.item;if(o!=null){var p=r.args;if(q){g.checkIndex(o.index,false)}else{if(q==false){g.uncheckIndex(o.index,false)}else{g.indeterminate(o.index,false)}}}g.focused=true};e.jqxCheckBox("updated",d)}b.height(m.height);b.css("top",h);h+=m.height;this.visualItems[virtualItemIndex]=b}this._addHandlers();this._updatescrollbars();if(a.browser.msie&&a.browser.version<8){this.host.attr("hideFocus",true);this.host.find("div").attr("hideFocus",true)}},_updatescrollbars:function(){this._arrange();var e=this.virtualSize.height;var c=this.virtualSize.width;var b=this.vScrollInstance;var d=this.hScrollInstance;if(e>this.host.outerHeight()){var f=0;if(c>this.host.outerWidth()){f=this.hScrollBar.outerHeight()+2}b.max=2+parseInt(e)+f-parseInt(this.host.height());this.vScrollBar.css("visibility","visible")}else{this.vScrollBar.css("visibility","hidden")}if(c>this.host.outerWidth()){d.max=parseInt(c)-parseInt(this.content.width())+parseInt(this.hScrollBar.height());this.hScrollBar.css("visibility","visible")}else{this.hScrollBar.css("visibility","hidden")}this.hScrollBar.jqxScrollBar("setPosition",0);this._arrange();if(this.itemswrapper){this.itemswrapper.width(Math.max(this.host.width(),17+c));this.itemswrapper.height(2*this.host.height())}},clear:function(){this.source=null;this.clearSelection();this.refresh()},clearSelection:function(b){for(indx=0;indx<this.selectedIndexes.length;indx++){this.selectedIndexes[indx]=-1}this.selectedIndex=-1;if(b==undefined||b==true){this._renderItems()}},unselectIndex:function(b,c){if(isNaN(b)){return}this.selectedIndexes[b]=-1;if(c==undefined||c==true){this._renderItems();this._raiseEvent("1",{index:b,type:type})}this._raiseEvent("2",{index:b,item:this.getItem(b)})},getItem:function(c){if(c==-1||isNaN(c)){return null}var b=null;var d=a.each(this.items,function(){if(this.index==c){b=this;return false}});return b},checkIndex:function(b,c){if(!this.checkboxes||!this.host.jqxCheckBox){return}if(isNaN(b)){return}if(b<0||b>=this.visibleItems.length){return}if(this.visibleItems[b]!=null&&this.visibleItems[b].disabled){return}if(this.disabled){return}var d=this.getItem(b);if(d!=null){var e=a(d.checkBoxElement);d.checked=true;if(c==undefined||c==true){this._renderItems()}}this._raiseEvent(3,{label:d.label,value:d.value,checked:true})},checkAll:function(){if(!this.checkboxes||!this.host.jqxCheckBox){return}if(this.disabled){return}a.each(this.items,function(){this.checked=true});this._renderItems();this._raiseEvent(3,{checked:true})},uncheckAll:function(){if(!this.checkboxes||!this.host.jqxCheckBox){return}if(this.disabled){return}a.each(this.items,function(){this.checked=false});this._renderItems();this._raiseEvent(3,{checked:false})},uncheckIndex:function(b,c){if(!this.checkboxes||!this.host.jqxCheckBox){return}if(isNaN(b)){return}if(b<0||b>=this.visibleItems.length){return}if(this.visibleItems[b]!=null&&this.visibleItems[b].disabled){return}if(this.disabled){return}var d=this.getItem(b);if(d!=null){var e=a(d.checkBoxElement);d.checked=false;if(c==undefined||c==true){this._renderItems()}}this._raiseEvent(3,{label:d.label,value:d.value,checked:false})},indeterminateIndex:function(b,c){if(!this.checkboxes||!this.host.jqxCheckBox){return}if(isNaN(b)){return}if(b<0||b>=this.visibleItems.length){return}if(this.visibleItems[b]!=null&&this.visibleItems[b].disabled){return}if(this.disabled){return}var d=this.getItem(b);if(d!=null){var e=a(d.checkBoxElement);d.checked=null;if(c==undefined||c==true){this._renderItems()}}this._raiseEvent(3,{checked:null})},getSelectedIndex:function(){return this.selectedIndex},getSelectedItem:function(){return this.getItem(this.selectedIndex)},selectIndex:function(c,e,g,d,f){if(isNaN(c)){return}if(c<-1||c>=this.visibleItems.length){return}if(this.visibleItems[c]!=null&&this.visibleItems[c].disabled){return}if(this.disabled){return}if(!this.multiple&&this.selectedIndex==c){return}this.focused=true;var b=false;if(this.selectedIndex!=c){b=true}var h=this.selectedIndex;if(this.selectedIndex==c&&!this.multiple){h=-1}if(f==undefined){f="none"}if(d!=undefined&&d){this._raiseEvent("1",{index:h,type:f,item:this.getItem(h)});this.selectedIndex=c;this.selectedIndexes[h]=-1;this.selectedIndexes[c]=c;this._raiseEvent("0",{index:c,type:f,item:this.getItem(c)})}else{if(this.multiple){if(this.selectedIndexes[c]==undefined||this.selectedIndexes[c]==-1){this.selectedIndexes[c]=c;this.selectedIndex=c;this._raiseEvent("0",{index:c,type:f,item:this.getItem(c)})}else{h=this.selectedIndexes[c];this.selectedIndexes[c]=-1;this.selectedIndex=-1;this._raiseEvent("1",{index:h,type:f,item:this.getItem(h)})}}else{this._raiseEvent("1",{index:h,type:f,item:this.getItem(h)});this.selectedIndex=c;this.selectedIndexes[h]=-1;h=c;this.selectedIndexes[c]=c;this._raiseEvent("0",{index:c,type:f,item:this.getItem(c)})}}if(g==undefined||g==true){this._renderItems()}if(e!=undefined&&e!=null&&e==true){this.ensureVisible(c)}this._raiseEvent("2",{index:c,item:this.getItem(c)});return b},isIndexInView:function(c){if(isNaN(c)){return false}if(c<0||c>=this.items.length){return false}var d=this.vScrollInstance.value;var e=this.visibleItems[this.selectedIndex];if(e==undefined){return true}var b=e.initialTop;var f=e.height;if(b-d<0||b-d+f>=this.host.outerHeight()){return false}return true},_itemsInPage:function(){var b=0;var c=this;a.each(this.items,function(){if((this.initialTop+this.height)>=c.content.height()){return false}b++});return b},_firstItemIndex:function(){if(this.visibleItems!=null){if(this.visibleItems[0].isGroup){return this._nextItemIndex(0)}else{return 0}}return -1},_lastItemIndex:function(){if(this.visibleItems!=null){if(this.visibleItems[this.visibleItems.length-1].isGroup){return this._prevItemIndex(this.visibleItems.length-1)}else{return this.visibleItems.length-1}}return -1},_nextItemIndex:function(b){for(indx=b+1;indx<this.visibleItems.length;indx++){if(!this.visibleItems[indx].disabled&&!this.visibleItems[indx].isGroup){return indx}}return -1},_prevItemIndex:function(b){for(indx=b-1;indx>=0;indx--){if(!this.visibleItems[indx].disabled&&!this.visibleItems[indx].isGroup){return indx}}return -1},_getMatches:function(g,d){if(g==undefined||g.length==0){return -1}if(d==undefined){d=0}var b=this.getItems();var f=this;var c=-1;var e=0;a.each(b,function(h){var l="";if(!this.isGroup){if(this.label){l=this.label}else{if(this.value){l=this.value}else{if(this.title){l=this.title}else{l="jqxItem"}}}var k=false;switch(f.searchMode){case"containsignorecase":k=a.jqx.string.containsIgnoreCase(l,g);break;case"contains":k=a.jqx.string.contains(l,g);break;case"equals":k=a.jqx.string.equals(l,g);break;case"equalsignorecase":k=a.jqx.string.equalsIgnoreCase(l,g);break;case"startswith":k=a.jqx.string.startsWith(l,g);break;case"startswithignorecase":k=a.jqx.string.startsWithIgnoreCase(l,g);break;case"endswith":k=a.jqx.string.endsWith(l,g);break;case"endswithignorecase":k=a.jqx.string.endsWithIgnoreCase(l,g);break}if(k&&this.visibleIndex>=d){c=this.visibleIndex;return false}}});return c},findItems:function(e){var b=this.getItems();var d=this;var c=0;var f=new Array();a.each(b,function(g){var k="";if(!this.isGroup){if(this.label){k=this.label}else{if(this.value){k=this.value}else{if(this.title){k=this.title}else{k="jqxItem"}}}var h=false;switch(d.searchMode){case"containsignorecase":h=a.jqx.string.containsIgnoreCase(k,e);break;case"contains":h=a.jqx.string.contains(k,e);break;case"equals":h=a.jqx.string.equals(k,e);break;case"equalsignorecase":h=a.jqx.string.equalsIgnoreCase(k,e);break;case"startswith":h=a.jqx.string.startsWith(k,e);break;case"startswithignorecase":h=a.jqx.string.startsWithIgnoreCase(k,e);break;case"endswith":h=a.jqx.string.endsWith(k,e);break;case"endswithignorecase":h=a.jqx.string.endsWithIgnoreCase(k,e);break}if(h){f[c++]=this}}});return f},_handleKeyDown:function(b){var p=b.keyCode;var q=this;var k=q.selectedIndex;var d=q.selectedIndex;var m=false;if(b.altKey){p=-1}if(q.incrementalSearch){var r=-1;if(!q._searchString){q._searchString=""}if((p==8||p==46)&&q._searchString.length>=1){q._searchString=q._searchString.substr(0,q._searchString.length-1)}var c=String.fromCharCode(p);var l=(!isNaN(parseInt(c)));if((p>=65&&p<=97)||l||p==8||p==32||p==46){if(!b.shiftKey){c=c.toLocaleLowerCase()}var o=1+q.selectedIndex;if(p!=8&&p!=32&&p!=46){if(q._searchString.length>0&&q._searchString.substr(0,1)==c){o=1+q.selectedIndex}else{q._searchString+=c}}if(p==32){q._searchString+=" "}var h=this._getMatches(q._searchString,o);r=h;if(r==q._lastMatchIndex||r==-1){var h=this._getMatches(q._searchString,0);r=h}q._lastMatchIndex=r;if(r>=0){if(q.multiple){q.clearSelection(false)}q.selectIndex(r,false,false,false,"keyboard");var e=q.isIndexInView(r);if(!e){q.ensureVisible(r)}else{q._renderItems()}}}if(q._searchTimer!=undefined){clearTimeout(q._searchTimer)}if(p==27||p==13){q._searchString=""}q._searchTimer=setTimeout(function(){q._searchString=""},q.incrementalSearchDelay);if(r>=0){return}}if(p==33){var g=q._itemsInPage();if(q.selectedIndex-g>=0){if(q.multiple){q.clearSelection(false)}q.selectIndex(d-g,false,false,false,"keyboard")}else{if(q.multiple){q.clearSelection(false)}q.selectIndex(q._firstItemIndex(),false,false,false,"keyboard")}q._searchString=""}if(p==32&&this.checkboxes){var f=this.getItem(k);if(f!=null){if(f.checked==true){f.checked=this.hasThreeStates?null:false}else{f.checked=f.checked!=null}switch(f.checked){case true:this.checkIndex(k);break;case false:this.uncheckIndex(k);break;default:this.indeterminateIndex(k);break}b.preventDefault()}q._searchString=""}if(p==36){if(q.multiple){q.clearSelection(false)}q.selectIndex(q._firstItemIndex(),false,false,false,"keyboard");q._searchString=""}if(p==35){if(q.multiple){q.clearSelection(false)}q.selectIndex(q._lastItemIndex(),false,false,false,"keyboard");q._searchString=""}if(p==34){var g=q._itemsInPage();if(q.selectedIndex+g<q.visibleItems.length){if(q.multiple){q.clearSelection(false)}q.selectIndex(d+g,false,false,false,"keyboard")}else{if(q.multiple){q.clearSelection(false)}q.selectIndex(q._lastItemIndex(),false,false,false,"keyboard")}q._searchString=""}if(p==38){q._searchString="";if(q.selectedIndex>0){var n=q._prevItemIndex(q.selectedIndex);if(n!=q.selectedIndex&&n!=-1){if(q.multiple){q.clearSelection(false)}q.selectIndex(n,false,false,false,"keyboard")}else{return true}}else{return false}}else{if(p==40){q._searchString="";if(q.selectedIndex+1<q.visibleItems.length){var n=q._nextItemIndex(q.selectedIndex);if(n!=q.selectedIndex&&n!=-1){if(q.multiple){q.clearSelection(false)}q.selectIndex(n,false,false,false,"keyboard")}else{return true}}else{return false}}}if(p==35||p==36||p==38||p==40||p==34||p==33){var e=q.isIndexInView(q.selectedIndex);if(!e){q.ensureVisible(q.selectedIndex)}else{q._renderItems()}return false}return true},wheel:function(d,c){if(c.autoHeight){d.returnValue=true;return true}var e=0;if(!d){d=window.event}if(d.originalEvent&&d.originalEvent.wheelDelta){d.wheelDelta=d.originalEvent.wheelDelta}if(d.wheelDelta){e=d.wheelDelta/120}else{if(d.detail){e=-d.detail/3}}if(e){var b=c._handleDelta(e);if(b){if(d.preventDefault){d.preventDefault()}if(d.originalEvent!=null){d.originalEvent.mouseHandled=true}if(d.stopPropagation!=undefined){d.stopPropagation()}}if(b){b=false;d.returnValue=b;return b}else{return false}}if(d.preventDefault){d.preventDefault()}d.returnValue=false},_handleDelta:function(d){var c=this.vScrollInstance.value;if(d<0){this.scrollDown()}else{this.scrollUp()}var b=this.vScrollInstance.value;if(c!=b){return true}return false},_removeHandlers:function(){var b=this;this.removeHandler(this.vScrollBar,"valuechanged");this.removeHandler(this.hScrollBar,"valuechanged");this.removeHandler(this.host,"mousewheel");this.removeHandler(this.host,"keydown");this.removeHandler(this.content,"mouseleave");this.removeHandler(this.content,"focus");this.removeHandler(this.content,"blur");this.removeHandler(this.content,"mouseenter");this.removeHandler(this.content,"mouseup");this.removeHandler(this.content,"mousedown");this.removeHandler(this.content,"mousemove");if(a.browser.msie){this.removeHandler(this.content,"selectstart")}},_addHandlers:function(){var k=this;this.focused=false;var l=false;var h=0;var d=null;var h=0;var b=0;var f=new Date();var c=this.isTouchDevice();if((this.width!=null&&this.width.toString().indexOf("%")!=-1)||(this.height!=null&&this.height.toString().indexOf("%")!=-1)){this.autoUpdateId=setInterval(function(){if(k.host.height()!=k._oldheight||k.host.width()!=k._oldwidth){k._oldwidth=k.host.width();k._oldheight=k.host.height();if(k.items){if(k.items.length>0&&k.virtualItemsCount*k.items[0].height<k._oldheight){k._render(false)}else{k._updatescrollbars();k._renderItems()}}}},100)}this.addHandler(this.vScrollBar,"valuechanged",function(m){k._renderItems()});this.addHandler(this.hScrollBar,"valuechanged",function(){k._renderItems()});this.addHandler(this.host,"mousewheel",function(m){k.wheel(m,k)});this.addHandler(this.host,"keydown",function(m){if(k.focused){return k._handleKeyDown(m)}});this.addHandler(this.content,"mouseleave",function(m){k.focused=false;var n=a.data(k.element,"hoveredItem");if(n!=null){a(n).removeClass(k.toThemeProperty("jqx-listitem-state-hover"));a(n).removeClass(k.toThemeProperty("jqx-fill-state-hover"))}});this.addHandler(this.content,"focus",function(m){k.focused=true});this.addHandler(this.content,"blur",function(m){k.focused=false});this.addHandler(this.content,"mouseenter",function(m){k.focused=true});if(this.enableSelection){var e=k.isTouchDevice();var g=!e?"mousedown":"touchend";this.addHandler(this.content,g,function(p){if(e){k._newScroll=new Date();if(k._newScroll-k._lastScroll<500){return false}}k.focused=true;if(!k.isTouchDevice()){k.content.focus()}if(p.target.id!="listBoxContent"&&k.itemswrapper[0]!=p.target){var q=p.target;var r=a(q).offset();var n=k.host.offset();var s=parseInt(r.top)-parseInt(n.top);var m=parseInt(r.left)-parseInt(n.left);var o=k._hitTest(m,s);if(o!=null&&!o.isGroup){if(o.html.indexOf("href")!=-1){setTimeout(function(){k.selectIndex(o.visibleIndex,false,true,false,"mouse")},100)}else{k.selectIndex(o.visibleIndex,false,true,false,"mouse")}}if(g=="mousedown"){return false}}return true});this.addHandler(this.content,"mouseup",function(m){k.vScrollInstance.handlemouseup(k,m)});if(a.browser.msie){this.addHandler(this.content,"selectstart",function(m){return false})}}var c=this.isTouchDevice();if(this.enableHover&&!c){this.addHandler(this.content,"mousemove",function(m){if(c){return true}if(!k.enableHover){return true}var n=a.browser.msie==true&&a.browser.version<9?0:1;if(m.target==null){return true}if(k.disabled){return true}k.focused=true;var p=k.vScrollInstance.isScrolling();if(!p&&m.target.id!="listBoxContent"){if(k.itemswrapper[0]!=m.target){var r=m.target;var w=a(r).offset();var q=k.host.offset();var s=parseInt(w.top)-parseInt(q.top);var t=parseInt(w.left)-parseInt(q.left);var v=k._hitTest(t,s);if(v!=null&&!v.isGroup&&!v.disabled){var o=a.data(k.element,"hoveredItem");if(o!=null){a(o).removeClass(k.toThemeProperty("jqx-listitem-state-hover"));a(o).removeClass(k.toThemeProperty("jqx-fill-state-hover"))}a.data(k.element,"hoveredItem",v.element);var u=a(v.element);u.addClass(k.toThemeProperty("jqx-listitem-state-hover"));u.addClass(k.toThemeProperty("jqx-fill-state-hover"))}}}})}},_arrange:function(){var c=null;var m=null;var h=this;var f=function(p){p=h.host.height();if(p==0){p=200;h.host.height(p)}return p};if(this.width!=null&&this.width.toString().indexOf("px")!=-1){c=this.width}else{if(this.width!=undefined&&!isNaN(this.width)){c=this.width}}if(this.height!=null&&this.height.toString().indexOf("px")!=-1){m=this.height}else{if(this.height!=undefined&&!isNaN(this.height)){m=this.height}}if(this.width!=null&&this.width.toString().indexOf("%")!=-1){this.host.width(this.width);c=this.host.width()}if(this.height!=null&&this.height.toString().indexOf("%")!=-1){this.host.height(this.height);m=f(m)}var l=this.host.css("border-width");if(l==null){l=0}if(c!=null){c=parseInt(c);this.host.width(this.width)}if(!this.autoHeight){if(m!=null){m=parseInt(m);this.host.height(this.height);f(m)}}else{if(this.virtualSize){this.host.height(this.virtualSize.height);this.height=this.virtualSize.height}}var b=this.scrollBarSize;if(isNaN(b)){b=parseInt(b);if(isNaN(b)){b="17px"}else{b=b+"px"}}b=parseInt(b);var g=4;var o=2;var k=0;if(this.vScrollBar){if(this.vScrollBar.css("visibility")=="visible"){k=b+g}else{this.vScrollBar.jqxScrollBar("setPosition",0)}}else{return}if(this.hScrollBar){if(this.hScrollBar.css("visibility")=="visible"){o=b+g}else{this.hScrollBar.jqxScrollBar("setPosition",0)}}else{return}var n=parseInt(m)-g-b;if(n<0){n=0}this.hScrollBar.height(b);this.hScrollBar.css({top:n+"px",left:"0px"});this.hScrollBar.width(c-b-g+"px");if(k==0){this.hScrollBar.width(c-2)}this.vScrollBar.width(b);this.vScrollBar.height(parseInt(m)-o+"px");this.vScrollBar.css({left:parseInt(c)-parseInt(b)-g+"px",top:"0px"});var e=this.vScrollInstance;e.disabled=this.disabled;e._arrange();var d=this.hScrollInstance;d.disabled=this.disabled;d._arrange();if((this.vScrollBar.css("visibility")=="visible")&&(this.hScrollBar.css("visibility")=="visible")){this.bottomRight.css("visibility","visible");this.bottomRight.css({left:1+parseInt(this.vScrollBar.css("left")),top:1+parseInt(this.hScrollBar.css("top"))});this.bottomRight.width(parseInt(b)+3);this.bottomRight.height(parseInt(b)+3)}else{this.bottomRight.css("visibility","hidden")}this.content.width(parseInt(c)-k);this.content.height(parseInt(m)-o)},ensureVisible:function(d){var c=this.isIndexInView(d);if(!c){if(d<0){return}if(this.autoHeight){var b=a.data(this.vScrollBar[0],"jqxScrollBar").instance;b.setPosition(0)}else{for(indx=0;indx<this.visibleItems.length;indx++){var e=this.visibleItems[indx];if(e.visibleIndex==d&&!e.isGroup){var b=a.data(this.vScrollBar[0],"jqxScrollBar").instance;b.setPosition(e.initialTop);break}}}}this._renderItems()},scrollDown:function(){if(this.vScrollBar.css("visibility")=="hidden"){return}var b=this.vScrollInstance;if(b.value+b.largestep<=b.max){b.setPosition(b.value+b.largestep)}else{b.setPosition(b.max)}},scrollUp:function(){if(this.vScrollBar.css("visibility")=="hidden"){return}var b=this.vScrollInstance;if(b.value-b.largestep>=b.min){b.setPosition(b.value-b.largestep)}else{b.setPosition(b.min)}},databind:function(h){this.records=new Array();var d=h._source?true:false;var k=new a.jqx.dataAdapter(h,{autoBind:false});if(d){k=h;h=h._source}var g=function(l){if(h.type!=undefined){k._options.type=h.type}if(h.formatdata!=undefined){k._options.formatData=h.formatdata}if(h.contenttype!=undefined){k._options.contentType=h.contenttype}if(h.async!=undefined){k._options.async=h.async}};var c=function(p){p.records=k.records;var o=p.records.length;p.items=new Array();for(i=0;i<o;i++){var l=p.records[i];var q=l[p.valueMember];var n=l[p.displayMember];var m=new a.jqx._jqxListBox.item();m.label=n;m.value=q;m.html="";m.visible=true;m.originalItem=l;m.index=i;m.group="";m.groupHtml="";m.disabled=false;p.items[i]=m}p._render();if(p.rendered){p.rendered()}};g(this);var f=this;switch(h.datatype){case"local":case"array":default:if(h.localdata!=null){k.dataBind();c(this);k.unbindBindingUpdate(this.element.id);k.bindBindingUpdate(this.element.id,function(){c(f)})}break;case"json":case"jsonp":case"xml":case"xhtml":case"script":case"text":case"csv":case"tab":if(h.localdata!=null){k.dataBind();c(this);k.unbindBindingUpdate(this.element.id);k.bindBindingUpdate(this.element.id,function(){c(f)});return}var e={};if(k._options.data){a.extend(k._options.data,e)}else{if(h.data){a.extend(e,h.data)}k._options.data=e}var b=function(){c(f)};k.unbindDownloadComplete(f.element.id);k.bindDownloadComplete(f.element.id,b);k.dataBind()}},loadItems:function(h){if(h==null){this.groups=new Array();this.items=new Array();this.visualItems=new Array();return}var o=this;var f=0;var d=0;var b=0;this.groups=new Array();this.items=new Array();this.visualItems=new Array();var e=new Array();a.map(h,function(t){if(t==undefined){return null}var q=new a.jqx._jqxListBox.item();var u=t.group;var p=t.groupHtml;var v=t.title;if(v==null||v==undefined){v=""}if(u==null||u==undefined){u=""}if(p==null||p==undefined){p=""}if(!o.groups[u]){o.groups[u]={items:new Array(),index:-1,caption:u,captionHtml:p};f++;var r=f+"jqxGroup";o.groups[r]=o.groups[u];d++}var s=o.groups[u];s.index++;s.items[s.index]=q;if(typeof t==="string"){q.label=t;q.value=t}else{if(t.label==null&&t.value==null&&t.html==null&&t.group==null&&t.groupHtml==null){q.label=t.toString();q.value=t.toString()}else{q.label=t.label||t.value;q.value=t.value||t.label}}if(typeof t!="string"){if(o.displayMember!=""){q.label=t[o.displayMember]}if(o.valueMember!=""){q.value=t[o.valueMember]}}q.originalItem=t;q.title=v;q.html=t.html||"";q.group=u;q.groupHtml=t.groupHtml||"";q.disabled=t.disabled||false;q.visible=t.visible||true;q.index=b;e[b]=q;b++;return q});var c=new Array();var l=0;if(this.fromSelect==undefined||this.fromSelect==false){for(indx=0;indx<d;indx++){var f=indx+1;var k=f+"jqxGroup";var m=this.groups[k];if(m==undefined||m==null){break}if(indx==0&&m.caption==""&&m.captionHtml==""&&d<=1){return m.items}else{var g=new a.jqx._jqxListBox.item();g.isGroup=true;g.label=m.caption;if(m.caption==""&&m.captionHtml==""){m.caption=this.emptyGroupText;g.label=m.caption}g.html=m.captionHtml;c[l]=g;l++}for(j=0;j<m.items.length;j++){c[l]=m.items[j];l++}}}else{var l=0;var n=new Array();a.each(e,function(){if(!n[this.group]){if(this.group!=""){var p=new a.jqx._jqxListBox.item();p.isGroup=true;p.label=this.group;c[l]=p;l++;n[this.group]=true}}c[l]=this;l++})}return c},_mapItem:function(c){var b=new a.jqx._jqxListBox.item();if(typeof c==="string"){b.label=c;b.value=c}else{if(typeof c==="number"){b.label=c.toString();b.value=c.toString()}else{b.label=c.label||c.value;b.value=c.value||c.label}}b.html=c.html||"";b.group=c.group||"";b.groupHtml=c.groupHtml||"";b.disabled=c.disabled||false;b.visible=c.visible||true;return b},addItem:function(b){if(this.items==undefined||this.items.length==0){this.source=new Array();this.source[0]=b;this.refresh();return}return this.insertAt(b,this.items.length)},insertAt:function(k,e){if(k==null){return false}if(this.items==undefined||this.items.length==0){this.source=new Array();this.source[0]=k;this.refresh();return false}var d=this._mapItem(k);if(e==-1||e==undefined||e==null||e>=this.items.length){d.index=this.items.length;this.items[this.items.length]=d}else{var f=new Array();var h=0;var g=false;var b=0;for(itemIndex=0;itemIndex<this.items.length;itemIndex++){if(this.items[itemIndex].isGroup==false){if(b>=e&&!g){f[h++]=d;d.index=e;b++;g=true}}f[h]=this.items[itemIndex];if(!this.items[itemIndex].isGroup){f[h].index=b;b++}h++}this.items=f}this.visibleItems=new Array();var c=a.data(this.vScrollBar[0],"jqxScrollBar").instance;c.setPosition(0);this._addItems();this._renderItems();return true},removeAt:function(c){if(c<0||c>this.items.length-1){return false}this.items.splice(c,1);var b=a.data(this.vScrollBar[0],"jqxScrollBar").instance;b.setPosition(0);this.visibleItems=new Array();this._addItems();this._renderItems();return true},getItems:function(){return this.items},disableAt:function(b){if(!this.items){return false}if(b<0||b>this.items.length-1){return false}this.items[b].disabled=true;this._renderItems();return true},enableAt:function(b){if(!this.items){return false}if(b<0||b>this.items.length-1){return false}this.items[b].disabled=false;this._renderItems();return true},destroy:function(){this._removeHandlers();this.vScrollBar.jqxScrollBar("destroy");this.hScrollBar.jqxScrollBar("destroy");this.vScrollBar.remove();this.hScrollBar.remove();this.host.removeClass("jqx-listbox jqx-rc-all");this.host.remove()},_raiseEvent:function(f,c){if(c==undefined){c={owner:null}}var d=this.events[f];args=c;args.owner=this;var e=new jQuery.Event(d);e.owner=this;if(f==0||f==1||f==2||f==3){e.args=args}if(this.host!=null){var b=this.host.trigger(e)}return b}})})(jQuery);(function(a){a.jqx._jqxListBox.item=function(){var b={group:"",groupHtml:"",selected:false,isGroup:false,highlighted:false,value:null,label:"",html:null,visible:true,disabled:false,element:null,width:null,height:null,initialTop:null,top:null,left:null,title:"",index:-1,checkBoxElement:null,originalItem:null,checked:false,visibleIndex:-1};return b}})(jQuery);