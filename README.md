# EcholeafFrame,小型应用快速开发帮助类框架
#### 本框架主要包含
* 常用工具类
* 基于android-async-http实现的android端VC分离框架
* 基础缓存模块
* 基础垃圾管理模块 
## VC分离框架
主要将一次页面渲染从网络接口调用到接口响应、响应结果处理以及相应事件派发、response字符串类型转换(json/xml等)，再到缓存、UI渲染，最终到二次接口调用对缓存的读取等步骤拆解为多个可拼装的模块，实现各个业务层次的低耦合，高可读性等。
## 基础垃圾管理模块
通过基于实现了TrashRecycler的类中成员，对手动添加了注解@TrashMonitor的成员变量进行自动监管，并在对应的条件(onFinish、onDestory等)下主动对变量进行回收。如果注解描述对象实现了Trash接口，则同时会调用其.recyle()方法，进行更胜层次的回收工作。
## 更新日志
##### 1.0.3 
1.修正RecylerViewAdapter的OnItemClickListener事件与OnItemViewClickListener事件冲突的问题；
2.增加了WebViewBuider，用于对WebView进行快速化常规配置。


