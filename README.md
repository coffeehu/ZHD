知乎日报，一个轻量级的应用，适合用来练手。

下载二维码：

![image](http://pan.baidu.com/share/qrcode?w=148&h=148&url=http%3A%2F%2Fpan.baidu.com%2Fs%2F1dF7IVtR%26qrfrom%3D1%26qrtype%3Dapk)






一、API 的获取：
https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90


二、实现：
1.网络请求以及图片加载：首页和内容界面使用 Android-Async-Http 和 Universal-Image-Loader；
启动页和评论页使用原生的 HttpURLConnection + Handler Message 封装。

2.离线缓存：Json 数据保存到 SQLite；启动页和评论页的图片通过 LruCache 实现内存缓存，同时保存到本地实现文件缓存。

3.Json数据解析：Gson

4、界面：RecyclerView、DrawerLayout、WebView、自定义圆形图片等



三、功能介绍：


1.启动界面，实现了缩放的动画，图片保存本地，每日更新；:

![image](https://github.com/coffeehu/ZHD/blob/master/readmeImages/splash.gif?raw=true)



2.首页，图片轮播，顶部下滑刷新，底部上滑加载更多：

![image](https://github.com/coffeehu/ZHD/blob/master/readmeImages/main.gif?raw=true)


3.侧滑栏，不同主题对应不同内容：

![image](https://github.com/coffeehu/ZHD/blob/master/readmeImages/navigation.gif?raw=true)


4.内容页,视差滚动，顶部标题栏根据滑动情况实现渐隐或突显，WebView 加载内容：

![image](https://github.com/coffeehu/ZHD/blob/master/readmeImages/content.gif?raw=true)



5.评论页：


![image](https://github.com/coffeehu/ZHD/blob/master/readmeImages/comment.gif?raw=true)
