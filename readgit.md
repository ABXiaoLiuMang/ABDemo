csdn账号：woaiqin2004

keytool -list -printcert -jarfile

一、开发分支（dev）上的代码达到上线的标准后，要合并到 master 分支
git checkout -b dev创建并切换分支
git checkout dev
git pull
git checkout master
git merge dev
git push -u origin master

$ git push -u origin master -f 强制覆盖

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

git remote -v 查看远程仓库地址

yabo-bugly :3468221313    baibaidu1

https://www.cnblogs.com/meitian/p/4997310.html  fiddler
13608079349
电话号码：13187474386  http://172.16.8.12/Android-Project/Yabo/SportApp
电话号码：9152866347
telegram账号：9152866347
gmail: dale120160528@gmail.com
小幺鸡:http://103.41.124.222:8080/dashboard  邮箱登录：dale1@yabotiyu.net
git:http://45.118.250.116:90 用户名：dale 邮箱：dale1@yabotiyu.net  (js.Dale)
git2:http://172.16.8.12  公司邮箱登录
UI获取地址(摹客):https://www.1888yabo.com

JIRA: http://jira.hnxmny.com:8080
Confluence: http://jira.hnxmny.com:8090/

http://jira.hnxmny.com:8090/pages/viewpage.action?pageId=17533698 开发过程问题

http://jira.hnxmny.com:8080/secure/BrowseProjects.jspa?selectedCategory=all （用户名dale1,密码：Abc_123456）
http://jira.hnxmny.com:8090/pages/viewpage.action?pageId=16285755 （框架使用文档）
http://jira.hnxmny.com:8090/pages/viewpage.action?pageId=16285884  （接口文档）
https://office.itcom888.com/user.php?mod=login 内推网址，用户名：dale,密码123456

http://jira.hnxmny.com:8080/browse/YABO-1935?filter=-1（bug）


2814373873  13608079349
https://zzcp02.com/#/home


聊天工具域名  chat.itcom888.com
登录时候输入用户名   dale默认密码123456（务必更改）
https://rocket.chat/install自行下载；


http://api.hou2008.com/api/v1/wap/location 地理位置

https://blog.csdn.net/xiaxiayige/article/details/80636091  nexus aar