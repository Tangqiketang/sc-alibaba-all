

##
maven-central：maven中央库，默认从https://repo1.maven.org/maven2/拉取jar
maven-releases：私库发行版jar，初次安装请将Deployment policy设置为Allow redeploy
maven-snapshots：私库快照（调试版本）jar
maven-public：仓库分组，把上面三个仓库组合在一起对外提供服务，在本地maven基础配置settings.xml或项目pom.xml中使用
##
hosted：本地仓库，通常我们会部署自己的构件到这一类型的仓库。比如公司的第二方库。
proxy： 代理仓库，代理远程的公共仓库，如maven中央仓库。
group： 仓库组，用来合并多个hosted/proxy仓库，当你的项目希望在多个repository使用资源时就不需要多次引用了，只需要引用一个group即可。
组资源库 = 代理资源库 + 托管资源库

在创建repository之前，还需要先指定文件存储目录，便于统一管理。就需要创建Blob Stores，不创建则使用的是default

##
Allow redeploy：允许同一个版本号下重复提交代码, nexus以时间区分
Disable redeploy：不允许同一个版本号下重复提交代码
Read-Only：不允许提交任何版本
原生的maven-releases库是Disable redeploy设置， maven-snapshots是Allow redeploy。

##
Hosted有三种方式：Releases、Snapshot、Mixed

Releases: 一般是已经发布的Jar包
Snapshot: 未发布的版本
Mixed：混合的



