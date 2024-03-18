#!/bin/sh
cd /usr/local/src
tar -zxvf jdk-8u161-linux-x64.tar.gz
mv jdk1.8.0_161 /usr/local/
ln -s /usr/local/jdk1.8.0_161 /usr/local/jdk
ln -sfT /usr/local/jdk/bin/java /usr/bin/java
echo -e '##### jdk \nexport JAVA_HOME=/usr/local/jdk\nexport PATH=$JAVA_HOME/bin:$JAVA_HOME/jre/bin:$PATH\nexport CLASSPATH=.$CALSSPATH:$JAVA_HOME/lib:$JAVA_HOME/jre/lib:$JAVA_HOME/lib/tools.jar' >>/etc/profile
source /etc/profile
java -version
