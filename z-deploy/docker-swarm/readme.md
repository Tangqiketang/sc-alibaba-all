一、Docker Swarm：
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
4.swarm集群管理
    4.1 查看集群节点
        docker node ls
    4.2 从集群中删除一个节点
        管理节点：  docker node update --availability drain [id]
        work节点：  docker swarm leave
        管理节点：  docker node rm node-id
====================================================================
二、Docker Stack
    1.0 与swarm的命令docker service的区别：service属于swarm更底层的命令。运行stack之前必须先有swarm初始化
                                         stack由1个或多个service组成。
    1.1 与docker-compose的区别：stack属于swarm mode的一部分，必须是现成的镜像，docker-compose版本至少为3
    1.2 部署
        docker stack deploy --with-registry-auth -c /unicloud/smart-work-site-compose.yml unicloud 
        -c:指定文件路径
        --with-registry-auth: 向swarm代理发送Registry认证详细信息(私有仓库需要携带)
    1.3 查看，有哪些服务组。每个yml独立。
        docker stack ls
    1.4 查看下面有哪些服务(服务id)。
        docker service ls     yml中每个service为单位
        docker stack services test  云效yml指定的名称
    1.5 查看下面有哪些任务(任务id、name、运行在哪个节点、状态)
        docker stack ps unicloud
    1.6 删除
        docker stack rm unicloud
    1.7 扩容,依赖service层的扩容,得具体到某个service
        docker service scale unicloud_gateway=2
    1.8 滚动更新
        docker service update --image registry.unicloud.com:9990/digibird/minio:1.0  unicloud_recording-minio
    1.9 
        docker service update unicloud_gateway --force