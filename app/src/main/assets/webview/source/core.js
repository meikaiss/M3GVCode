
!function (window, undefined) {

    //var core_version = "core_1.0";
    //document.write(core_version + "<br>");


    window.webview = window.webview || {};
    window.webview.debug = false;

    window.webview.core = {
        execute: function (group, methods, params) {
            return execute(group, methods, params, false);
        }
    }


    var execute = function (group, methods, userParams, async) {

        var url = buildUrl(group, methods, userParams);
        var params = parseParams(userParams);
        var callback = getCallback(userParams);
        var callbackName = getCallbackName(userParams);

            switch (getSystem()) {
                case 'iphone':
                    return iosExecute(url, params, callback, async, callbackName);
                    break;
                case 'android':
                    return androidExecute(url, params, callback, async, callbackName);
                    break;

                default:
                    break;
            }

        if (window.webview.debug) {
            log(group, methods, params, url);
        }
    }

    var buildUrl = function (group, methods) {
        if (getSystem() === 'iphone') {
            return '/!m3-web/' + group + '/' + methods;
        } else if (getSystem() === 'android'){
            return 'm3-web://' + group + '/' + methods;
        }
    }

    var parseParams = function (params) {
        var paramString = [];

        if (params) {
            for (var e in params) {
                switch (e) {
                    case 'config':
                        paramString = paramString.concat(objectToParams(params[e]));
                        break;
                    case 'callback':
                    case 'callbackName':
                        break;
                    default:
                        if (typeof params[e] === 'object') {
                            paramString.push(e + '=' + encodeURIComponent(JSON.stringify(params[e])));
                        } else {
                            paramString.push(e + '=' + encodeURIComponent(params[e]));
                        }
                    }
                }
                return paramString.join('&');
            } else {
                return '';
            }
        }

    var getCallback = function (params) {
        if (params) {
            for (var e in params) {
                if (e === 'callback') {
                    return params[e];
                }
            }
        }
        return null;
    }

    var getCallbackName = function (params) {
        if (params) {
            for (var e in params) {
                if (e === 'callbackName') {
                    return params[e];
                }
            }
        }
        return null;
    }

    var androidExecute = function (url, params, callback, async, callbackName) {

        if (async) {
            return window.m3WebViewInterface.getM3WebViewData(url + '?' + params, buildCallback(callback, callbackName));
        } else {
            return window.m3WebViewInterface.getM3WebViewData(url + '?' + params);
        }
    }


    var getSystem = function () {
        var ua = window.navigator.userAgent.toLocaleLowerCase();
        var system = ['iphone', 'android', 'windows', 'mac'];

        for (var i = 0; i < system.length; i++) {
            if (ua.indexOf(system[i]) > -1) {
                return system[i];
            }
        }

        return 'other';
    }



}(window, undefined);