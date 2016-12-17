window.webview.system.log("测试js的加载");

//窗体加载事件
window.onload=function(){

window.webview.system.log("测试js的加载222");

var itemList = document.querySelectorAll("[data-type]");


    window.webview.system.log("测试js的加载，有dataType的长度="+ itemList.length);

    var dataTypeList = [];
    for(var i = 0; i < itemList.length; i++ ){
        var item = itemList[i];
        var dataType = item.getAttribute("data-type");

        window.webview.system.log("测试js的加载dataType="+ dataType);

        if( ["image", "gif", "video", "album"].indexOf(dataType) > -1 ){
            if(dataType == "image"){

                dataTypeList.push(item.getAttribute("src"));

                item.addEventListener("click", function(){
                    var index = this.getAttribute("data-imgindex");
                    window.webview.system.log("内部图片被点击了，index＝"+index);

                    window.m3WebViewInterface.clickArticleImage(index);
                },"false");
            }
        }
    }

    window.m3WebViewInterface.setArticleImageArr(dataTypeList);

};
