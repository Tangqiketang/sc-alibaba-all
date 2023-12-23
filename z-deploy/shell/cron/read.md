https://baijiahao.baidu.com/s?id=1779658503155315125&wfr=spider&for=pc
1.系统的定时任务在
    vim /etc/crontab 
  用户命令行创建的定时任务会自动生成在文件
    vim /var/spool/cron/crontabs/创建人用户名
    vim /var/spool/cron/crontabs/root
2.查看定时任务列表
    crontab -l
3.新增或编辑任务，会进入vim编辑界面
    crontab -e
4.语法
    */10 * * * * /server/scripts/clean-log.sh >/dev/null 2>&1