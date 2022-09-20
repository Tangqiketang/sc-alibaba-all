FROM davidcaste/alpine-java-unlimited-jce:jre8

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories
RUN apk add --no-cache tzdata \
&& ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo "Asia/Shanghai" > /etc/timezone \
&&rm -rf /var/cache/apk/* /tmp/* /var/tmp/* $HOME/.cache
ENV TZ=Asia/Shanghai


ADD babycare.jar babycare.jar

CMD "tail" "-f" "/dev/null"