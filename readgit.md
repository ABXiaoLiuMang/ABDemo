csdn账号：woaiqin2004
qq：2814373873 admin123456

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

https://www.draw.io/  画图软件

2814373873  13608079349
https://zzcp02.com/#/home

获取应用签名
keytool -list -v -keystore C:\Users\Dell\Desktop\release.keystore.jks
android签名 ->SHA256就是

http://api.hou2008.com/api/v1/wap/location 地理位置

https://blog.csdn.net/xiaxiayige/article/details/80636091  nexus aar