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
        nodeport: 在每个node上打开一个端口供外部访问,网关。 master1:port  slave1:port slave2:port
        loadbalancer: 引入外部负载均衡器来访问。
##0.5   
    label用键值对形式附加到各种对象、如pod、service、rc、node,用于管理选择
##0.6   
    在为对象定义好label后，其他对象可以使用label selector来定义其作用的对象
   
##0.7在manager节点打印join命令
    kubeadm token create --print-join-command
##0.8在worker节点加入
    kubeadm reset
    kubeadm join 192.168.40.131:6443 --token ws3vhc.9mb1dawjnr1mivbg --discovery-token-ca-cert-hash sha256:8cd0dafab


##0.9版本等信息
    kubectl version  查看版本
    kubectl cluster-info  查看
    kubectl config current-context

#1.=========================================查看pod/service==============
kubectl get pods       -A               查看所有命名空间中的pod,-A表示所有命名空间
kubectl get pods       -A -o wide       查看所有命名空间中的pod,-A表示所有命名空间,-o wide显示所在ip和node
kubectl get pods,deployments,svc  -A -o wide       查看所有命名空间中的pod、deployments、service,-A表示所有命名空间,-o wide显示所在ip和node
kubectl describe pod nginx
kubectl get endpoints -A  获取
kubectl get configmaps  获取配置service
#2.=========================================进入pod==============
kubectl exec -it pod/nginx-6799fc88d8-98cc4  -- /bin/bash


#3.===========================创建和删除==============================
kubectl create -f xxxx.yaml   创建pod/service/deployment。如果已经存在会报错。 推荐使用apply会创建或更新
kubectl apply -f 文件夹名称   将文件夹下所有yml文件都创建
kubectl delete -f xxxx.yaml   删除

kubectl create deployment nginx --image=nginx
kubectl expose deployment nginx --port=80 --``type``=NodePort

kubectl delete deployment nginx
kubectl delete pods nginx
kubectl delete service nginx
kubectl delete pod "podName" --grace-period=0 --force  强制删除Pod，例如僵尸pod
kubectl delete configmaps --all 
#4.========================扩容======================
4.1改成3   apply
