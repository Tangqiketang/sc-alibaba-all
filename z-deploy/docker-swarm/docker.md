# 0.安装
    
# 1.基本命令
    从镜像仓库查找可用的镜像   docker search nginx  
    进入容器                docker exec -it mysql /bin/bash 
    查找日志                docker logs -f -t --tail 200 mysql  
    设置容器不自动重启        docker update --restart=no 5d195d6251f9  
    查日志  docker logs -f -t --tail 200  ssssss
    容器重启  docker restart  `docker ps | grep 容器名称 | awk '{print $1}' `
# 查看资源情况
    docker ps -a --no-trunc  查看完整command
    docker inspect id |grep "" -C 3   查看容器详情
    /var/lib/docker/containers  系统日志
    find / -name hostconfig.json   容器资源限制等配置
    docker清除日志： http://t.zoukankan.com/zhaobowen-p-13374855.html  cat /dev/null > xxxjson.log
# 资源限制
    ## docker container update 39dc5c20f9a8  --cpus=2  --memory="1g" --memory-swap="2g"
    
    ## docker-compose中设置，且需要用兼容模式才会生效docker-compose --compatibility up -d
    deploy:
      resources:
         limits:
            cpus: "2.00"
            memory: 5G
         reservations:
            memory: 200M
# 网络相关
    网络有 网桥模式-默认、
          host：容器和宿主机共享network namespace,相当于本地网络
          container： –net=container:NAME_or_ID
    networks:
    - "host"


    docker network create testnet

    run参数：加入网桥
    --network testnet --network-alias nginx-server

    docker network ls 查看所有网络
    docker network prune 清除没有容器使用的网桥
# 容器导出为镜像
    docker export 1e560fca3906 > ubuntu.tar 导出容器
