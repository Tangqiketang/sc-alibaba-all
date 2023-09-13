# 0.安装
    
# 1.基本命令
    从镜像仓库查找可用的镜像   docker search nginx  
    进入容器                docker exec -it mysql /bin/bash 
    查找日志                docker logs -f -t --tail 200 mysql  
    设置容器不自动重启        docker update --restart=no 5d195d6251f9  
# 查看资源情况
    docker ps -a --no-trunc  查看完整command
    docker inspect id
    /var/lib/docker/containers  系统日志
    find / -name hostconfig.json   容器资源限制等配置
    docker清除日志： http://t.zoukankan.com/zhaobowen-p-13374855.html  cat /dev/null > xxxjson.log
# 资源限制
    docker container update 39dc5c20f9a8  --cpus=2  --memory="1g" --memory-swap="2g"

# 网络相关
    docker network create testnet

    run参数：加入网桥
    --network testnet --network-alias nginx-server

    docker network ls 查看所有网络
    docker network prune 清除没有容器使用的网桥
# 容器导出为镜像
    docker export 1e560fca3906 > ubuntu.tar 导出容器
