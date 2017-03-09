This repository is porting from QuantComponents by Luigi Sgro.

git init
git add README.md
git commit -m "first commit"
git remote add origin https://github.com/BrentHuang/MyRepo.git
git push -u origin master


create branch local: git branch branch1
switchover: git checkout branch1
push this branch to github: git push origin branch1

delete branch local: git branch -d branch1
push:              : git push origin :branch1



Git鼓励大量使用分支：

查看分支：git branch

创建分支：git branch <name>

切换分支：git checkout <name>

创建+切换分支：git checkout -b <name>

合并某分支到当前分支：git merge <name>

删除分支：git branch -d <name>

