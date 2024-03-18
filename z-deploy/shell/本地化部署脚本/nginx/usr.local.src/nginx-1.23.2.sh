#!/bin/sh
#yum -y install gcc-c++ pcre pcre-devel zlib zlib-devel openssl openssl-devel
#ubuntu16.04
apt-get update
sudo apt-get install -y build-essential libtool libpcre3 libpcre3-dev zlib1g-dev openssl libssl-dev
useradd -s /sbin/nologin www -M
cd /usr/local/src
tar -xvf nginx-1.23.2.tar.gz
cd /usr/local/src/nginx-1.23.2
./configure --user=www --group=www --with-http_ssl_module --with-http_stub_status_module --with-stream --prefix=/usr/local/nginx-1.23.2 
make && make install
ln -sfT /usr/local/nginx-1.23.2 /usr/local/nginx
echo '##### nginx \nexport PATH=/usr/local/nginx/sbin:$PATH' >>/etc/profile
echo "[Unit]
Description=nginx - high performance web server
Documentation=http://nginx.org/en/docs/
After=network.target remote-fs.target nss-lookup.target

[Service]
Type=forking
PIDFile=/usr/local/nginx/logs/nginx.pid
ExecStartPre=/usr/local/nginx/sbin/nginx -t -c /usr/local/nginx/conf/nginx.conf
ExecStart=/usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf
ExecReload=/usr/local/nginx/sbin/nginx -s reload
ExecStop=/usr/local/nginx/sbin/nginx -s quit
PrivateTmp=true

[Install]
WantedBy=multi-user.target" > /lib/systemd/system/nginx.service
systemctl daemon-reload
systemctl enable nginx
rm -rf /usr/local/nginx/conf/nginx.conf
echo 'user www www;
worker_processes auto;
worker_limit_nofile 65535;
events {
      accept_mutex on;   #设置网路连接序列化，防止惊群现象发生，默认为on
      multi_accept on;  #设置一个进程是否同时接受多个网络连接，默认为off
      worker_connections 1024; #最大连接数，默认为512
}
http {
    include mime.types;   #文件扩展名与文件类型映射表
    default_type application/octet-stream;  #默认文件类型，默认为text/plain
    #sendfile on;
    keepalive_timeout 65;
    disable_symlinks off; # 允许nginx目录使用软链接

log_format main '\$remote_addr - \$remote_user [\$time_local] \$scheme \$http_host \$server_port \"\$request\" '
'\$status \$body_bytes_sent \"\$http_referer\" '
'\"\$http_user_agent\" \"\$upstream_addr\" '
'\$request_time \$upstream_response_time \$time_iso8601 '
'\$ssl_protocol';

    # upstream 中的地址后面不加 “/”
    #upstream test-servers {
    #   server 172.16.1.2:7000;
    #   server 172.16.1.3:7000;
    #}

    # 开启nginx缓存,keys_zone定义缓存名为cache_one,主机配置中要和此对应
    #proxy_cache_path /var/nginx/proxy_cache_path levels=1:2 keys_zone=cache_one:500m inactive=1d max_size=30g;
    #proxy_cache_path /var/nginx/proxy_cache_path levels=1:2 keys_zone=cache_one:500m inactive=1d max_size=30g;

    fastcgi_connect_timeout 3000;
    fastcgi_send_timeout 3000;
    fastcgi_read_timeout 3000;
    fastcgi_buffer_size 256k;
    fastcgi_buffers 8 256k;
    fastcgi_busy_buffers_size 256k;
    fastcgi_temp_file_write_size 256k;
    fastcgi_intercept_errors on;

    client_max_body_size 100m;
    client_body_buffer_size 256k;

    # 开启nginx压缩
    gzip on;
    gzip_min_length 1k;
    gzip_buffers 16 8k;
    gzip_http_version 1.1;
    gzip_comp_level 6;
    gzip_types text/plain text/css application/xml application/javascript application/x-javascript text/javascript;
    gzip_vary on;
	
	#限制最大连池
    limit_conn_log_level error;
    limit_conn_status 503;
    limit_conn_zone $server_name zone=perserver:20m;

    # 隐藏Nginx后端服务X-Powered-By头
    proxy_hide_header X-Powered-By;
    proxy_hide_header Server;

	
	#nginx优化----------------------
    #隐藏版本号
    server_tokens off;
 
    #优化服务器域名的散列表大小 
    server_names_hash_bucket_size 64;
    server_names_hash_max_size 2048;
 
    #开启高效文件传输模式
    sendfile on;
    #减少网络报文段数量
    tcp_nopush on;
    #提高I/O性能
    #tcp_nodelay on;
	
    #关闭access_log,可以独立配置
    access_log off;
	
    # include 必须放在最后
    include extra/*.conf;
}
#stream模块和http模块是并列级别的,ngx_stream_core_module模块由1.9.0版,提供用于tcp/UDP数据流的代理和负载均衡
stream {
    log_format basic '$remote_addr [$time_local] '
                 '$protocol $status $bytes_sent $bytes_received '
                 '$session_time';
    #关闭access_log,可以独立配置
    access_log off;
    # 引入tcpudp stream引用配置
    include extra/*.stream;
}' > /usr/local/nginx/conf/nginx.conf
mkdir /usr/local/nginx/conf/extra
systemctl start nginx
systemctl status nginx
