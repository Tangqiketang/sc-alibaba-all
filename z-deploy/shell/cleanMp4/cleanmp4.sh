#!/bin/bash

# 检查磁盘剩余空间
disk_space=$(df -h | grep '/dev/sda1' | awk '{print $4}')
# 要清理的目录路径
directory="/box"
# 当前日期
current_date=$(date +%s)
# 如果磁盘剩余空间小于100G，则执行清理操作
if [ $(echo "$disk_space" | tr -d 'G') -lt 100 ]; then
    echo "Disk space is less than 100G. Starting cleanup..."
    # 递归清理半年前的mp4文件
    find "$directory" -type f -name '*.mp4' -mtime +180 -exec rm -f {} \;
    # 再次检查磁盘剩余空间
    disk_space=$(df -h | grep '/dev/sda1' | awk '{print $4}')
    # 如果磁盘剩余空间仍然小于100G，则继续清理
    if [ $(echo "$disk_space" | tr -d 'G') -lt 100 ]; then
        echo "Disk space is still less than 100G after cleanup. Continuing cleanup..."
        # 清理3个月前的mp4文件
        find "$directory" -type f -name '*.mp4' -mtime +90 -exec rm -f {} \;
        # 再次检查磁盘剩余空间
        disk_space=$(df -h | grep '/dev/sda1' | awk '{print $4}')
        # 如果磁盘剩余空间仍然小于100G，则继续清理
        if [ $(echo "$disk_space" | tr -d 'G') -lt 100 ]; then
            echo "Disk space is still less than 100G after cleanup. Continuing cleanup..."

            # 清理1个月前的mp4文件
            find "$directory" -type f -name '*.mp4' -mtime +30 -exec rm -f {} \;
        fi
    fi
    echo "Cleanup completed."
else
    echo "Disk space is sufficient."
fi
