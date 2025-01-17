/*20200224*/
var jsBridge = function() {
    var n = !1
      , e = {
        isReady: function() {
            return !!window.WebViewJavascriptBridge
        },
        ready: function(n) {
            p(n)
        },
        txVideo: {
            play: function(n) {
                o("7.play", "string" == typeof n ? {
                    url: n
                } : n)
            },
            playList: function(n) {
                o("7.playList", n)
            },
            addPlayer: function(n, e) {
                n.e = "function" == typeof n.onEvent,
                o("7.addPlayer", n, function(t, o) {
                    if (/^\d+$/.test(o))
                        e && e(parseInt(o));
                    else if (n.e) {
                        var i = JSON.parse(o);
                        n.onEvent(i.p, i.e, i.d ? i.d : null)
                    }
                })
            },
            removePlayer: function(n) {
                o("7.removePlayer", {
                    p: n
                })
            }
        },
        network: {
            wifiInfo: function(n) {
                r("6.wifiInfo", {}, n)
            },
            active: function(n) {
                r("6.active", {}, n)
            },
            setConnectionListener: function(n) {
                u("6.scl", {}, n)
            }
        },
        ble: {
            getState: function(n) {
                a("5.getState", {}, n)
            },
            getPeripheral: function(n) {
                u("5.getPeripheral", {}, n)
            },
            isScanning: function(n) {
                s("5.isScanning", {}, n)
            },
            isConnected: function(n, e) {
                s("5.isConnected", n, e)
            },
            scan: function(n) {
                r("5.scan", {}, n)
            },
            stopScan: function(n) {
                s("5.stopScan", {}, n)
            },
            connect: function(n, e) {
                o("5.connect", n, e)
            },
            disconnect: function(n, e) {
                o("5.disconnect", n, e)
            },
            discoverServices: function(n, e) {
                r("5.discoverServices", n, e)
            },
            discoverCharacteristics: function(n, e) {
                r("5.discoverCharacteristics", n, e)
            },
            discoverDescriptors: function(n, e) {
                r("5.discoverDescriptors", n, e)
            },
            setNotify: function(n, e) {
                r("5.setNotify", n, e)
            },
            read: function(n, e) {
                r("5.read", n, e)
            },
            write: function(n, e) {
                r("5.write", n, e)
            }
        },
        fs: {
            exist: function(n, e) {
                o("3.exist", {
                    s: n
                }, e)
            },
            mkdir: function(n, e) {
                o("3.mkdir", {
                    s: n
                }, e)
            },
            list: function(n, e) {
                r("3.list", {
                    s: n
                }, e)
            },
            size: function(n, e) {
                o("3.size", {
                    s: n
                }, function(n, t) {
                    "function" == typeof e && e(n, n ? parseInt(t) : t)
                })
            },
            delete: function(n, e) {
                o("3.delete", {
                    s: n
                }, e)
            },
            copy: function(n, e, t) {
                o("3.copy", {
                    s: n,
                    d: e
                }, t)
            },
            writeText: function(n, e, t) {
                o("3.writeText", {
                    s: n,
                    a: e
                }, t)
            },
            appendText: function(n, e, t) {
                o("3.appendText", {
                    s: n,
                    a: e
                }, t)
            },
            readText: function(n, e) {
                o("3.readText", {
                    s: n
                }, e)
            },
            writeBinary: function(n, e, t) {
                o("3.writeBinary", {
                    s: n,
                    a: e
                }, t)
            },
            appendBinary: function(n, e, t) {
                o("3.appendBinary", {
                    s: n,
                    a: e
                }, t)
            },
            readBinary: function(n, e) {
                o("3.readBinary", {
                    s: n
                }, e)
            },
            toUri: function(n, e) {
                o("3.toUri", {
                    s: n
                }, e)
            },
            toAbsolute: function(n, e) {
                o("3.toAbsolute", {
                    s: n
                }, e)
            },
            share: function(n, e) {
                o("3.share", {
                    s: n
                }, e)
            },
            open: function(n, e) {
                o("3.open", {
                    s: n
                }, e)
            },
            unzip: function(n, e, t) {
                o("3.unzip", {
                    s: n,
                    d: e
                }, t)
            },
            md5: function(n, e) {
                o("3.md5", {
                    s: n
                }, e)
            },
            sha1: function(n, e) {
                o("3.sha1", {
                    s: n
                }, e)
            },
            sha256: function(n, e) {
                o("3.sha256", {
                    s: n
                }, e)
            },
            download: function(n, e) {
                i("3.download", n, function(t) {
                    var o = JSON.parse(t);
                    0 == o.a ? "function" == typeof n.progress && n.progress(o.t, o.d) : "function" == typeof e && e(1 == o.a, o.m)
                })
            }
        },
        db: {
            tables: function(n) {
                r("4.tables", {}, n)
            },
            execSQL: function(n, e, t) {
                r("4.execSQL", {
                    s: n,
                    a: e
                }, t)
            },
            query: function(n, e, t) {
                r("4.query", {
                    s: n,
                    a: e
                }, t)
            }
        },
        rc: {
            init: function(n, e) {
                s("s.init", n, e)
            },
            setUserInfoListener: function(n) {
                i("s.setUserInfoListener", {}, n)
            },
            setUserInfo: function(n) {
                o("s.setUserInfo", n, null)
            },
            connect: function(n, e) {
                o("s.connect", n, e)
            },
            disconnect: function(n) {
                s("s.disconnect", {}, n)
            },
            logout: function(n) {
                s("s.logout", {}, n)
            }
        },
        rcIM: {
            startConversationList: function(n, e) {
                s("q.startConversationList", n, e)
            },
            startConversation: function(n, e) {
                s("q.startConversation", n, e)
            },
            startSubConversationList: function(n, e) {
                s("q.startSubConversationList", n, e)
            },
            unreadMessageCount: function(n) {
                a("q.unreadMessageCount", {}, n)
            }
        },
        rcCall: {
            startSingleCall: function(n, e) {
                s("r.startSingleCall", n, e)
            },
            startMultiCall: function(n, e) {
                s("r.startMultiCall", n, e)
            }
        },
        video: {
            play: function(n) {
                o("x.play", "string" == typeof n ? {
                    url: n
                } : n)
            },
            addPlayer: function(n, e) {
                n.e = "function" == typeof n.onEvent,
                o("x.addPlayer", n, function(t, o) {
                    if (/^\d+$/.test(o))
                        e && e(parseInt(o));
                    else if (n.e) {
                        var i = JSON.parse(o);
                        n.onEvent(i.p, i.e, i.d ? i.d : null)
                    }
                })
            },
            removePlayer: function(n) {
                o("x.removePlayer", {
                    p: n
                })
            },
            resource: function(n) {
                o("x.resource", n)
            },
            start: function(n) {
                o("x.start", {
                    p: n
                })
            },
            pause: function(n) {
                o("x.pause", {
                    p: n
                })
            },
            stop: function(n) {
                o("x.stop", {
                    p: n
                })
            },
            fullScreen: function(n) {
                o("x.fullScreen", {
                    p: n
                })
            }
        },
        displays: {
            getDisplays: function(n) {
                u("z.getDisplays", {}, n)
            },
            show: function(n, e) {
                s("z.show", n, e)
            },
            dismiss: function(n) {
                o("z.dismiss", {
                    x: n
                })
            }
        },
        iap: {
            canMakePayments: function(n) {
                s("i.canMakePayments", {}, n)
            },
            getProducts: function(n, e) {
                r("i.getProducts", n, e)
            },
            setTransanctionListener: function(n) {
                r("i.setTransanctionListener", {}, n)
            },
            getUnfinishedTransactions: function() {
                r("i.getUnfinishedTransactions", {})
            },
            purchase: function(n) {
                r("i.purchase", n)
            },
            restoreTransactions: function(n) {
                r("i.restoreTransactions", n)
            },
            finishTransaction: function(n) {
                o("i.finishTransaction", n)
            },
            setDownloadListener: function(n) {
                r("i.setDownloadListener", {}, n)
            },
            startDownloads: function(n) {
                r("i.startDownloads", n)
            },
            pauseDownloads: function(n) {
                o("i.pauseDownloads", n)
            },
            resumeDownloads: function(n) {
                o("i.resumeDownloads", n)
            },
            cancelDownloads: function(n) {
                o("i.cancelDownloads", n)
            }
        },
        doc: {
            canOpen: function(n, e) {
                s("c.canOpen", {
                    url: n
                }, e)
            },
            open: function(n) {
                o("c.open", n)
            }
        },
        bdloc: {
            getCurrentPosition: function(n, e) {
                u("a.getCurrentPosition", n, e)
            },
            stop: function() {
                o("a.stop")
            }
        },
        bdface: {
            config: function(n) {
                o("y.config", n)
            },
            detect: function(n, e) {
                u("y.detect", n, e)
            },
            liveness: function(n, e) {
                u("y.liveness", n, e)
            }
        },
        bc: {
            login: function(n) {
                o("b.login", {}, n)
            },
            logout: function(n) {
                o("b.logout", {}, n)
            },
            setShouldUseAlipay: function(n) {
                o("b.setShouldUseAlipay", {
                    b: n
                })
            },
            setSyncForTaoke: function(n) {
                o("b.setSyncForTaoke", {
                    b: n
                })
            },
            setForceH5: function(n) {
                o("b.setForceH5", {
                    b: n
                })
            },
            setTaokeParams: function(n) {
                o("b.setTaokeParams", n)
            },
            setChannel: function(n) {
                o("b.setChannel", n)
            },
            setISVCode: function(n) {
                o("b.setISVCode", {
                    s: n
                })
            },
            setISVVersion: function(n) {
                o("b.setISVVersion", {
                    s: n
                })
            },
            detail: function(n, e) {
                o("b.detail", n, e)
            },
            shop: function(n, e) {
                o("b.shop", n, e)
            },
            url: function(n, e) {
                o("b.url", n, e)
            },
            addCart: function(n, e) {
                o("b.addCart", n, e)
            },
            cart: function(n, e) {
                o("b.cart", n, e)
            },
            order: function(n, e) {
                o("b.order", n, e)
            }
        },
        getui: {
            setListener: function(n) {
                u("g.setListener", {}, n)
            },
            turnOffPush: function() {
                o("g.turnOffPush")
            },
            turnOnPush: function() {
                o("g.turnOnPush")
            },
            isPushTurnedOn: function(n) {
                s("g.isPushTurnedOn", {}, n)
            },
            getClientid: function(n) {
                i("g.getClientid", {}, n)
            },
            bindAlias: function(n, e) {
                s("g.bindAlias", {
                    alias: n
                }, e)
            },
            unBindAlias: function(n, e) {
                s("g.unBindAlias", n, e)
            },
            setTag: function(n, e) {
                s("g.setTag", {
                    tags: n
                }, e)
            },
            setSilentTime: function(n, e) {
                s("g.setSilentTime", n, e)
            }
        },
        jiguang: {
            setListener: function(n) {
                u("j.setListener", {}, n)
            },
            stopPush: function() {
                o("j.stopPush")
            },
            resumePush: function() {
                o("j.resumePush")
            },
            isPushStopped: function(n) {
                s("j.isPushStopped", {}, n)
            },
            getRegistrationID: function(n) {
                i("j.getRegistrationID", {}, n)
            },
            setAlias: function(n, e) {
                i("j.setAlias", {
                    alias: n
                }, e)
            },
            deleteAlias: function(n) {
                i("j.deleteAlias", {}, n)
            },
            getAlias: function(n) {
                i("j.getAlias", {}, n)
            },
            setTags: function(n, e) {
                u("j.setTags", {
                    tags: n
                }, e)
            },
            addTags: function(n, e) {
                u("j.addTags", {
                    tags: n
                }, e)
            },
            deleteTags: function(n, e) {
                u("j.deleteTags", {
                    tags: n
                }, e)
            },
            cleanTags: function(n) {
                u("j.cleanTags", {}, n)
            },
            getAllTags: function(n) {
                u("j.getAllTags", {}, n)
            }
        },
        notification: {
            getBadge: function(n) {
                i("n.getBadge", {}, function(e) {
                    n && n(parseInt(e))
                })
            },
            setBadge: function(n) {
                o("n.setBadge", {
                    badge: n
                })
            },
            requestAuth: function(n) {
                s("n.requestAuth", {}, n)
            },
            notify: function(n, e) {
                r("n.notify", n, e)
            },
            cancelAll: function() {
                o("n.cancelAll")
            }
        },
        x5: {
            videoCacheSize: function(n) {
                e.ios ? n && n(0) : o("x5.videoCacheSize", {}, function(e, t) {
                    n && n(e && t ? parseInt(t) : 0)
                })
            },
            clearVideoCache: function(n) {
                e.ios ? n && n() : i("x5.clearVideoCache", {}, n)
            },
            playVideo: function(n, t) {
                e.ios ? t && t(!1) : s("x5.playVideo", {
                    url: n
                }, t)
            }
        },
        weibo: {
            login: function(n) {
                o("w.login", {}, function(e, t) {
                    var o = t ? JSON.parse(t) : null;
                    if ("function" == typeof n)
                        n(e, o);
                    else if (e && "string" == typeof n) {
                        var i = n + (n.indexOf("?") >= 0 ? "&" : "?") + "uid=" + encodeURIComponent(o.uid) + "&expiresTime=" + encodeURIComponent(o.expiresTime) + "&phoneNum=" + encodeURIComponent(o.phoneNum) + "&refreshToken=" + encodeURIComponent(o.refreshToken) + "&token=" + encodeURIComponent(o.token);
                        location.href = i
                    }
                })
            },
            installed: function(n) {
                s("w.installed", {}, n)
            },
            shareText: function(n, e) {
                t(n, e)
            },
            shareImage: function(n, e) {
                t(n, e, "i")
            },
            shareVideo: function(n, e) {
                t(n, e, "v")
            },
            shareWebPage: function(n, e) {
                t(n, e, "w")
            },
            shareImages: function(n, e) {
                t(n, e, "j")
            },
            shareStory: function(n, e) {
                t(n, e, "s")
            }
        },
        accelerometer: {
            support: function(n) {
                s("sa.support", {}, n)
            },
            start: function(n) {
                o("sa.start", {}, n ? function(e, t) {
                    if (e && t) {
                        var o = JSON.parse(t);
                        n(o[0], o[1], o[2])
                    }
                }
                : null)
            },
            stop: function(n) {
                o("sa.stop")
            }
        },
        gyroscope: {
            support: function(n) {
                s("sg.support", {}, n)
            },
            start: function(n) {
                o("sg.start", {}, n ? function(e, t) {
                    if (e && t) {
                        var o = JSON.parse(t);
                        n(o[0], o[1], o[2])
                    }
                }
                : null)
            },
            stop: function(n) {
                o("sg.stop")
            }
        },
        sidebarIsOpen: function(n) {
            s("sidebarIsOpen", {}, n)
        },
        sidebarOpen: function() {
            o("sidebarOpen")
        },
        sidebarClose: function() {
            o("sidebarClose")
        },
        sidebarHeader: function(n) {
            o("sidebarHeader", n)
        },
        captureWebPage: function(n) {
            o("captureWebPage", n)
        },
        checkCamera: function(n) {
            e.ios ? n && n({
                count: 2,
                front: !0,
                back: !0
            }) : u("checkCamera", {}, n)
        },
        appSettings: function() {
            o("appSettings")
        },
        appDownloads: function() {
            e.ios || o("appDownloads")
        },
        appInfo: function(n) {
            u("appInfo", {}, n)
        },
        openSetting: function(n) {
            o("openSetting", {
                w: n
            })
        },
        getSettingState: function(n, e) {
            s("getSettingState", {
                w: n
            }, e)
        },
        getIMEI: function(n) {
            e.ios ? n && n("unknown") : i("1.imei", {}, n)
        },
        getOAID: function(n) {
            e.ios ? n && n("unknown") : i("1.oaid", {}, n)
        },
        getIDFA: function(n) {
            e.ios ? i("1.idfa", {}, n) : n && n("unknown")
        },
        userAgreement: function(n) {
            o("2.userAgreement")
        },
        requestPermissions: function(n, e) {
            u("requestPermissions", {
                p: n
            }, e)
        },
        action: function(n) {
            o("action", n ? {
                btns: n
            } : {}, null)
        },
        uiNavigation: function(n) {
            o("uiNavigation", {
                b: n
            })
        },
        uiShare: function(n) {
            o("uiShare", {
                b: n
            })
        },
        uiActions: function(n) {
            o("uiActions", {
                b: n
            })
        },
        uiRefresh: function(n) {
            o("uiRefresh", {
                b: n
            })
        },
        showImages: function(n) {
            o("showImages", n)
        },
        saveImageToAlbum: function(n, e) {
            s("saveImageToAlbum", {
                i: n
            }, e)
        },
        saveImagesToAlbum: function(n, e) {
            s("saveImagesToAlbum", {
                i: n
            }, e)
        },
        saveScreenshotToAlbum: function(n) {
            s("saveScreenshotToAlbum", {}, n)
        },
        canGoBack: function(n) {
            s("canGoBack", {}, n)
        },
        canGoForward: function(n) {
            s("canGoForward", {}, n)
        },
        backToHome: function(n) {
            o("backToHome", {
                loadHomePage: !!n
            })
        },
        contactOne: function(n) {
            u("contactOne", {}, n)
        },
        contactAll: function(n) {
            u("contactAll", {}, n)
        },
        setClipboardText: function(n) {
            o("setClipboardText", {
                text: n
            })
        },
        getClipboardText: function(n) {
            i("getClipboardText", {}, n)
        },
        debug: function() {
            o("debug", {}),
            n = !0
        },
        qqLogin: function(n) {
            n ? o("qqLogin", {}, function(e, t) {
                if ("function" == typeof n)
                    n(e, t ? JSON.parse(t) : null);
                else if (e && "string" == typeof n) {
                    var o = JSON.parse(t)
                      , i = n + (n.indexOf("?") >= 0 ? "&" : "?") + "openid=" + encodeURIComponent(o.openid) + "&access_token=" + encodeURIComponent(o.access_token);
                    o.userinfo && (i += "&userinfo=" + JSON.stringify(o.userinfo)),
                    location.href = i
                }
            }) : alert("Missing Parameter")
        },
        wxLogin: function(n) {
            n ? o("wxLogin", {}, function(e, t) {
                if ("function" == typeof n)
                    n(e, t ? JSON.parse(t) : null);
                else if (e && "string" == typeof n) {
                    var o = JSON.parse(t)
                      , i = n + (n.indexOf("?") >= 0 ? "&" : "?") + "code=" + encodeURIComponent(o.code);
                    o.openid && (i += "&openid=" + encodeURIComponent(o.openid)),
                    o.access_token && (i += "&access_token=" + encodeURIComponent(o.access_token)),
                    o.userinfo && (i += "&userinfo=" + JSON.stringify(o.userinfo)),
                    location.href = i
                }
            }) : alert("Missing Parameter")
        },
        wxSubscribeMsg: function(n, e) {
            r("wxSubscribeMsg", n, e)
        },
        wxLaunchMiniProgram: function(n, e) {
            r("wxLaunchMiniProgram", n, e)
        },
        wxAppInstalled: function(n) {
            s("wxAppInstalled", {}, n)
        },
        pay: function(n, e) {
            o("pay", n, e)
        },
        wxPay: function(n, e) {
            o("wxPay", n, e)
        },
        aliPay: function(n, e) {
            o("aliPay", n, e)
        },
        unionPay: function(n, e) {
            o("v.pay", n, e)
        },
        unionSeInfo: function(n) {
            e.ios ? "function" == typeof n && n(!1, "not supported") : o("v.seInfo", {}, n)
        },
        unionPayAppInstalled: function(n) {
            s("v.unionPayAppInstalled", {}, n)
        },
        abcPay: function(n, e) {
            o("u.abcPay", n, e)
        },
        abcPayAppInstalled: function(n) {
            s("u.abcPayAppInstalled", {}, n)
        },
        icbcPay: function(n, e) {
            o("0.icbcPay", n, e)
        },
        net: function(n, e) {
            o("net", n, function(n, t) {
                var o = JSON.parse(t);
                e && "function" == typeof e && e(1 == o.a, o.b)
            })
        },
        netUploadFile: function(n) {
            o("netUploadFile", n, function(e, t) {
                var o = JSON.parse(t);
                0 == o.a && "function" == typeof n.onProgress && n.onProgress(o.b, o, c),
                1 == o.a && "function" == typeof n.onSuccess && n.onSuccess(o.b),
                2 == o.a && "function" == typeof n.onFail && n.onFail(o.b)
            })
        },
        http: {
            get: function(n, t, o) {
                var i = {
                    url: n,
                    method: "GET"
                };
                t && "object" == typeof t && (i.params = t),
                e.net(i, function(n, e) {
                    if (n) {
                        var i = "function" == typeof t ? t : "function" == typeof o ? o : null;
                        i && i(JSON.parse(e))
                    }
                })
            },
            post: function(n, t, o) {
                var i = {
                    url: n,
                    method: "POST"
                };
                t && "object" == typeof t && (i.params = t),
                e.net(i, function(n, e) {
                    if (n) {
                        var i = "function" == typeof t ? t : "function" == typeof o ? o : null;
                        i && i(JSON.parse(e))
                    }
                })
            }
        },
        share: function(n, e) {
            o("share", n, e)
        },
        shareText: function(n, e) {
            o("shareText", n, e)
        },
        shareImage: function(n, e) {
            o("shareImage", n, e)
        },
        shareMusic: function(n, e) {
            o("shareMusic", n, e)
        },
        shareVideo: function(n, e) {
            o("shareVideo", n, e)
        },
        shareImages: function(n, e) {
            o("shareImages", n, e)
        },
        scan: function(n, e) {
            i("scan", n, e)
        },
        scanFromAlbum: function(n, e) {
            i("scanFromAlbum", n, e)
        },
        scanFromUrl: function(n, e) {
            i("scanFromUrl", n, e)
        },
        cacheSize: function(n) {
            o("cacheSize", {}, function(e, t) {
                n && n(e && t ? parseInt(t) : 0)
            })
        },
        clearCache: function(n) {
            i("clearCache", {}, n)
        },
        clearCookie: function() {
            o("clearCookie")
        },
        vibrate: function() {
            o("vibrate")
        },
        onAppEnterBackground: function(n) {
            o("onAppEnterBackground", {}, n)
        },
        onAppEnterForeground: function(n) {
            o("onAppEnterForeground", {}, n)
        },
        toast: function(n) {
            o("toast", {
                s: n
            })
        },
        exit: function() {
            o("exit")
        },
        home: function() {
            e.ios ? e.exit() : o("home")
        },
        openInBrowser: function(n) {
            o("openInBrowser", {
                s: n
            })
        },
        present: function(n) {
            u("present", n, function(e) {
                e && e.l && n.onLeft && n.onLeft(),
                e && e.r && n.onRight && n.onRight()
            })
        },
        open: function(n) {
            o("open", n)
        },
        setOptions: function(n) {
            o("setOptions", n)
        },
        close: function(n) {
            e.version > 35 && e.isRoot || o("close", n ? {
                f: n
            } : {})
        },
        evalInNavbar: function(n) {
            o("evalInNavbar", {
                j: n
            })
        },
        evalInNavbarAction: function(n, t) {
            e.evalInNavbar("action(" + JSON.stringify({
                action: n,
                data: t
            }) + ")")
        },
        evalInToolbar: function(n) {
            o("evalInToolbar", {
                j: n
            })
        },
        launch: function(n, e) {
            "string" == typeof n ? s("launch", {
                a: n
            }, e) : a("launch", {
                b: n
            }, e)
        },
        deviceOwnerAuth: function(n, e) {
            s("d.deviceOwnerAuth", n, e)
        },
        deviceOwnerAuthAvailable: function(n) {
            s("d.deviceOwnerAuthAvailable", {}, n)
        },
        getInstallId: function(n) {
            i("getInstallId", {}, n)
        },
        getDeviceId: function(n) {
            i("getDeviceId", {}, n)
        },
        onMenuAction: function(n) {
            f("onMenuAction", function(t) {
                e.action(n)
            })
        },
        onClose: function(n) {
            f("onClose", n)
        },
        onBackPressed: function(n) {
            f("onBackPressed", n)
        },
        onMenuShareTimeline: function(n) {
            d("onMenuShareTimeline", 0, n)
        },
        onMenuShareFriend: function(n) {
            d("onMenuShareFriend", 1, n)
        },
        onMenuShareQQ: function(n) {
            d("onMenuShareQQ", 2, n)
        },
        onMenuShareQZone: function(n) {
            d("onMenuShareQZone", 3, n)
        }
    };
    function t(n, e, t) {
        n = n || {};
        var o = {};
        t && (o[t] = n),
        n.text && (o.text = n.text),
        i("w.share", o, e)
    }
    function o(e, t, i) {
        l() ? (n && alert("js call\n\nname:" + e + "\noptions:" + JSON.stringify(t)),
        window.WebViewJavascriptBridge.callHandler(e, t || {}, function(e) {
            if (n && alert("js back\n\n" + e),
            i && "function" == typeof i) {
                var t = JSON.parse(e);
                i(!!t.success, t.text)
            }
        })) : p(function() {
            o(e, t, i)
        })
    }
    function i(n, e, t) {
        o(n, e, t ? function(n, e) {
            t && t(e)
        }
        : null)
    }
    function a(n, e, t) {
        o(n, e, t ? function(n, e) {
            t && t(parseInt(e))
        }
        : null)
    }
    function s(n, e, t) {
        o(n, e, t ? function(n, e) {
            t && t("true" == e)
        }
        : null)
    }
    function r(n, e, t) {
        o(n, e, t ? function(n, e) {
            t && t(n, e ? JSON.parse(e) : null)
        }
        : null)
    }
    function u(n, e, t) {
        o(n, e, t ? function(n, e) {
            t && t(e ? JSON.parse(e) : null)
        }
        : null)
    }
    function f(n, e) {
        l() ? window.WebViewJavascriptBridge.registerHandler(n, function(n, t) {
            var o = {
                ok: !0
            };
            if (e) {
                var i = e(n);
                null != i && (o.data = i)
            }
            t(o)
        }) : p(function() {
            f(n, e)
        })
    }
    function l() {
        return e.inApp || console.log("jsBridge can only used in App"),
        !!e.isReady() || (console.log("jsBridge is not ready\nUsage:\njsBridge.ready(function(){\n  //do something\n});"),
        !1)
    }
    function d(n, t, o) {
        f(n, function(n) {
            "function" == typeof o ? o() : (o = o || {},
            e.share({
                to: t,
                title: o.title || document.title,
                link: o.link || location.href,
                imgUrl: o.imgUrl || "",
                desc: o.desc || ""
            }, function(n) {
                n && o.success && o.success(),
                n || o.cancel && o.cancel()
            }))
        })
    }
    function p(n) {
        if (window.WebViewJavascriptBridge)
            return n(WebViewJavascriptBridge);
        if (window.WVJBCallbacks)
            return window.WVJBCallbacks.push(n);
        window.WVJBCallbacks = [n];
        var e = document.createElement("iframe");
        e.style.display = "none",
        e.src = "wvjbscheme://__BRIDGE_LOADED__",
        document.documentElement.appendChild(e),
        setTimeout(function() {
            document.documentElement.removeChild(e)
        }, 300)
    }
    Object.defineProperty(e, "zqprinter", {
        configurable: !1,
        writable: !1,
        value: function() {
            for (var n = "SDK_Version|Prn_GetPortList|Prn_Connect|Prn_Disconnect|Prn_PrinterInit|Prn_Status|Prn_PowerStatus|Prn_PrintText|Prn_PrintEscText|Prn_PrintString|Prn_PrintBarcode|Prn_PrintQRCode|Prn_PrintBitmap|Prn_PrintBmp|Prn_CutPaper|Prn_OpenCashbox|Prn_LineFeed|Prn_MarkFeed|Prn_SetCharacterSet|Prn_SetInterCharacterSet|Prn_SetLineSpacing|Prn_SetFontStyle|Prn_SetFontSize|Prn_SetAlignment|Prn_SendData|Prn_ReadData|Prn_BeginTransaction|Prn_EndTransaction|Prn_GetMsrTrack".split("|"), e = {}, t = 0; t < n.length; t++) {
                var o = n[t];
                e[o] = function(n) {
                    return function(e, t) {
                        i("zq." + n, e, t)
                    }
                }(o)
            }
            return e
        }()
    }),
    Object.defineProperty(e, "bdocr", {
        configurable: !1,
        writable: !1,
        value: function() {
            for (var n = "general|generalBasic|accurate|accurateBasic|generalEnhanced|webImage|idCardFront|idCardFrontAuto|idCardBack|idCardBackAuto|bankCard|vehicleLicense|drivingLicense|licensePlate|businessLicense|receipt|passport|vatInvoice|qrcode|numbers|lottery|businessCard|handWriting".split("|"), e = {}, t = 0; t < n.length; t++) {
                var o = n[t];
                e[o] = function(n) {
                    return function(e) {
                        u("t." + n, {}, e)
                    }
                }(o)
            }
            return e.custom = function(n, e) {
                u("t.custom", n, e)
            }
            ,
            e
        }()
    });
    var g = /LT-APP\/(\d+)/
      , b = navigator.userAgent;
    Object.defineProperty(e, "inApp", {
        configurable: !1,
        writable: !1,
        value: g.test(b)
    }),
    Object.defineProperty(e, "version", {
        configurable: !1,
        writable: !1,
        value: e.inApp ? parseInt(g.exec(b)[1]) : 0
    });
    var h = /LT-APP\/(\d+)\/(\d+)/;
    Object.defineProperty(e, "appVersion", {
        configurable: !1,
        writable: !1,
        value: h.test(b) ? parseInt(h.exec(b)[2]) : 0
    }),
    Object.defineProperty(e, "ios", {
        configurable: !1,
        writable: !1,
        value: /(iPhone|iPad|iPod|iOS)/i.test(b)
    });
    var v = /YM-RT/.test(b);
    return Object.defineProperty(e, "isRoot", {
        configurable: !1,
        get: function() {
            return v
        }
    }),
    e.inApp && e.ready(function() {
        e.version > 35 && e.version < 41 && s("_isRoot", {}, function(n) {
            v = n
        })
    }),
    e
}();
