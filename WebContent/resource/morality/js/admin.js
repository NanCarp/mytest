function layer_show(title,url,w,h){
	if (title == null || title == '') {
		title=false;
	};
	if (url == null || url == '') {
		url="404.html";
	};
	if (w == null || w == '') {
		w=800;
	};
	if (h == null || h == '') {
		h=($(window).height() - 50);
	};
	layer.open({
		type: 2,
		area: [w+'px', h +'px'],
		fix: false, //不固定
		maxmin: true,
		shade:0.4,
		title: title,
		content: url
	});
}
function layer_show_noshadow(title,url,w,h){
	if (title == null || title == '') {
		title=false;
	};
	if (url == null || url == '') {
		url="404.html";
	};
	if (w == null || w == '') {
		w=800;
	};
	if (h == null || h == '') {
		h=($(window).height() - 50);
	};
	layer.open({
		type: 2,
		area: [w+'px', h +'px'],
		fix: false, //不固定
		maxmin: true,
		shade:0,
		title: title,
		content: url
	});
}
/*关闭弹出框口*/
function layer_close(){
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}
function open_window(title, url, w, h){
	layer_show(title, url, w, h);
}

function open_window_weixin(title, url, w, h){
	layer_show(title, url, w, h);
}
function open_window_noshadow(title, url, w, h){
	layer_show_noshadow(title, url, w, h);
}

function refresh_iframe(){
	var iframedoc = $('.layui-tab-item iframe:visible').get(0);
	iframedoc.src = iframedoc.src;
}
function go_iframe(){
	var iframedoc = $('.layui-tab-item iframe:visible').get(0);
	iframedoc.src = 'http://'+ window.location.host +'/statistic/companylistForSearch';
}
function open_schedule(url, w, h){
	layer.open({
		type: 2,
		area: [w+'px', h +'px'],
		fix: true, //不固定
		title: false,
		closeBtn: 0,
		content: url
	});
}

//全屏询问框{
function actconfirm(title, next){
	layer.confirm(title, function(index){
		layer.close(index);
		next();
	});
}

//登录过期
function loginOut(){
	layer.confirm('登录过期，重新登录？',function(index){
		location.replace('/manage/login');
	});
}