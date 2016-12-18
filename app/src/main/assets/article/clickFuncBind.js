//窗体加载完成后的事件
window.onload=function(){

var itemList = document.querySelectorAll("[data-type]");

    var dataTypeList = [];
    for(var i = 0; i < itemList.length; i++ ){
        var item = itemList[i];
        var dataType = item.getAttribute("data-type");

        if( ["image", "gif", "video", "album"].indexOf(dataType) > -1 ){
            if(dataType == "image"){
                dataTypeList.push(item.getAttribute("src"));

                item.addEventListener("click", function(){
                    var index = this.getAttribute("data-imgindex");
                    window.m3WebViewInterface.clickArticleImage(index);
                },"false");
            }
        }
    }

    window.m3WebViewInterface.setArticleImageArr(dataTypeList);

};
