知乎日报，一个轻量级的应用，适合用来练手。

一、API 的获取：
https://github.com/iKrelve/KuaiHu/blob/master/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5API.md


二、实现：
1.网络请求以及图片加载：首页和内容界面使用 Android-Async-Http 和 Universal-Image-Loader；
启动页和评论页使用原生的 HttpURLConnection + Handler Message 封装。

2.离线缓存：Json 数据保存到 SQLite；启动页和评论页的图片通过 LruCache 实现内存缓存，同时保存到本地实现文件缓存。

3.Json数据解析：Gson

4、界面：RecyclerView、DrawerLayout 等



三、功能介绍：
1.启动界面:

![image](https://github.com/coffeehu/ZHD/blob/master/readmeImages/splash.gif?raw=true)



2.首页，顶部下滑刷新，底部上滑加载更多：

![image](https://github.com/coffeehu/ZHD/blob/master/readmeImages/main.gif?raw=true)


3.侧滑栏，不同主题对应不同内容：
![image](https://github.com/coffeehu/ZHD/blob/master/readmeImages/navigation.gif?raw=true)


4.内容页,视差滚动，顶部标题栏根据条件渐隐或突显：

![image](https://github.com/coffeehu/ZHD/blob/master/readmeImages/content.gif?raw=true)



5.评论页：


![image](https://github.com/coffeehu/ZHD/blob/master/readmeImages/comment.gif?raw=true)