#!/bin/bash

# 定义 Harbor 登录凭据
HARBOR_USERNAME="admin"
HARBOR_PASSWORD="Harbor12345"
HARBOR_REGISTRY="192.168.40.131:8090"

# 等待 Docker 服务启动完成
until docker info &>/dev/null; do
    echo "Waiting for Docker to start..."
    sleep 1
done

# 执行 Docker 登录命令以自动登录到 Harbor
echo "$HARBOR_PASSWORD" | docker login -u "$HARBOR_USERNAME" --password-stdin "$HARBOR_REGISTRY"

