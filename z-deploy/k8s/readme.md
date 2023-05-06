#0.基础概念 
##0.1
    k8s中最小的操作单元pod,pod由N个container和pause组成,pause最先启动实现了pod之间的网络
##0.2   
    replicaSet用于维护一组完全相同的pod副本，replicaSet是replication Controller升级版
##0.3   
    Deployment用于管理pod、replicaSet,实现滚动升级和回滚应用、缩容扩容
##0.4
    service定义了replicaSet的访问入口。请求先发送到service再转发给pod。
    service的类型分三种：
        clusterIp:提供一个集群内部的虚拟ip供pod访问。  
        nodeport: 在每个node上打开一个端口供外部访问。 master1:port  slave1:port slave2:port
        loadbalancer: 引入外部负载均衡器来访问。
##0.5   
    label用键值对形式附加到各种对象、如pod、service、rc、node,用于管理选择
##0.6   
    在为对象定义好label后，其他对象可以使用label selector来定义其作用的对象
   

    
   

#1.=========================================查看pod/service==============
kubectl get pods       -A               查看所有命名空间中的pod,-A表示所有命名空间
kubectl get pods       -A -o wide       查看所有命名空间中的pod,-A表示所有命名空间,-o wide显示所在ip和node
kubectl get pods,deployments,svc  -A -o wide       查看所有命名空间中的pod、deployments、service,-A表示所有命名空间,-o wide显示所在ip和node
kubectl describe pod nginx
kubectl get endpoints -A  获取
#2.=========================================进入pod==============
kubectl exec -it pod/nginx-6799fc88d8-98cc4  -- /bin/bash


#3.===========================创建和删除==============================
kubectl create -f xxxx.yaml   创建
kubectl delete -f xxxx.yaml   删除

kubectl create deployment nginx --image=nginx
kubectl expose deployment nginx --port=80 --``type``=NodePort
kubectl delete deployment nginx
kubectl delete pods nginx
kubectl delete service nginx
kubectl delete pod "podName" --grace-period=0 --force  强制删除Pod，例如僵尸pod
