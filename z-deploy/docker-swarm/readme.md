1.master执行：
    1.1docker swarm init; 成为一个manager
    1.2执行  docker swarm join-token manager，用于其他机子加入新的manager
    或者执行  docker swarm join-token worker， 用于其他机子加入新的worker
    此时每台机子上docker network ls会出现docker_gwbridge和ingress两个网络。
2.slave使用上面的命令加入
    比如 docker swarm join --token SWMTKN-1-51qhuz9mcwuxxxxx 192.168.40.131:2377
3.master上可以使用
    3.1 创建service
        docker service create --name helloworld busybox:latest sh -c "while true; do echo Hello; sleep 2; done"
    3.2 扩容,创建2个副本.会自动负载到不同节点。
        docker service scale helloworld=2
    3.3 查看所有所有节点上servcie的情况
        docker service ls
    3.4 查看service的定义详情
        docker service inspect helloworld   
    3.5 更新service，包括替换掉镜像和数量。把镜像从busybox替换成nginx.注意！原来的busybox镜像知识停止并不删除。
        docker service update --image nginx:latest helloworld
    3.6 删除服务。非常干净。
        docker service rm helloword
4. swarm集群管理
    4.1 查看集群节点
        docker node ls
    4.2 从集群中删除一个节点
        docker node rm node-id
   