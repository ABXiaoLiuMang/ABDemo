keytool -list -printcert -jarfile

一、开发分支（dev）上的代码达到上线的标准后，要合并到 master 分支

git checkout dev
git pull
git checkout master
git merge dev
git push -u origin master

二、当master代码改动了，需要更新开发分支（dev）上的代码

git checkout master 
git pull 
git checkout dev
git merge master 
git push -u origin dev
--------------------- 
git init
git add xxx
git commit -m 'xxx'
 git remote add origin ssh://software@172.16.0.30/~/yafeng/.git
git push origin master 
git remote show origin
git clone ssh://software@172.16.0.30/~/yafeng/.git


git branch -r       #查看远程所有分支
git branch           #查看本地所有分支
git branch -a       #查看本地及远程的所有分支，如下图
git fetch   #将某个远程主机的更新，全部取回本地：
git branch -a  #查看远程分支
git branch  #查看本地分支：
git checkout 分支 #切换分支：
git push origin -d 分支名  #删除远程分支: 
git branch -d 分支名  #删除本地分支
git remote show origin  #查看远程分支和本地分支的对应关系
git remote prune origin #删除远程已经删除过的分支


yabo-bugly :3468221313    baibaidu1

https://www.cnblogs.com/meitian/p/4997310.html  fiddler
13608079349
电话号码：13187474386
电话号码：9152866347
telegram账号：9152866347
gmail: dale120160528@gmail.com
小幺鸡:http://103.41.124.222:8080/dashboard  邮箱登录：dale1@yabotiyu.net
git:http://45.118.250.116:90 用户名：dale 邮箱：dale1@yabotiyu.net  (js.Dale)
git2:http://172.16.8.12  公司邮箱登录
UI获取地址(摹客):https://www.1888yabo.com

JIRA: http://jira.hnxmny.com:8080
Confluence: http://jira.hnxmny.com:8090/
排期状态：https://docs.google.com/spreadsheets/d/18y02on9NeK1LnqQ0tl6kuR69eZJoecykbDgv36lwUkU/edit#gid=469642405

http://jira.hnxmny.com:8090/pages/viewpage.action?pageId=17533698 开发过程问题

   int statusBarHeight = Utils.statusBarHeight(getActivity());

http://jira.hnxmny.com:8080/secure/BrowseProjects.jspa?selectedCategory=all （用户名dale1,密码：Abc_123456）
http://jira.hnxmny.com:8090/pages/viewpage.action?pageId=16285755 （框架使用文档）
http://jira.hnxmny.com:8090/pages/viewpage.action?pageId=16285884  （接口文档）
https://docs.google.com/spreadsheets/d/18y02on9NeK1LnqQ0tl6kuR69eZJoecykbDgv36lwUkU/edit?usp=sharing （每天完成状态表格）
https://docs.google.com/spreadsheets/d/18y02on9NeK1LnqQ0tl6kuR69eZJoecykbDgv36lwUkU/edit#gid=1746941443 项目进度日报
https://office.itcom888.com/user.php?mod=login 内推网址，用户名：dale,密码123456


http://jira.hnxmny.com:8080/browse/YABO-1935?filter=-1（bug）

GameLobbyFragment  GunQiuGameListFragment PersonalVIPInfoActivity

1.Contract中接口继承BaseView供Ac or Fragment 实现（Presenter中功能完成后回调ui）
2.Contract中接口网络请求和逻辑类，供Presenter实现（做功能）,供ui中presenter调用发起（注意不触发的可以在生命周期中调用）

错误码包含:
请求的http状态码
自定义的状态码
自定义的状态码:
11000 数据解析异常
11001 网络异常
11002 请求超时
11003 获取数据失败
11004 非法baseUrl
11005 请求参数异常
11006 图形验证码填写错误
11007 请求结果处理异常
2814373873  13608079349
https://zzcp02.com/#/home


请求失败提示带上code码,提示code码=(真实code码+5)x2
http错误code展示: (code)
接口返回code展示:code!
体育登陆App提示
1.getpreInfo请求失败----提示:登陆信息获取失败,请重试+code
2.sport_login请求失败----提示:登陆失败,请重试+code
3.188鉴权失败----提示:登陆异常,请重试+code
4.userInfo获取失败----提示:登陆信息异常,请重试+code

存款异常提示:
获取存款连接为空----提示:暂无存款通道,请稍后再试!


聊天工具域名  chat.itcom888.com
登录时候输入用户名   dale默认密码123456（务必更改）
https://rocket.chat/install自行下载；

https://transfer_ft.2018lucky.com/app/financial/withdrawal

1.本地日志添加,跟老版本V587一致
2.getPreinfo失败日志上报.
http://api.hou2008.com/api/v1/wap/location 地理位置

