#!/bin/sh

set -e

# dirval="/deploy/wt7"
# yesno="Y"

# mkdir -p /deploy/wt7/config/settings_sample
WT_DIR="/deploy/wt7/config/settings_sample"

#old
#java -jar /deploy/wt7/wt_install.jar <<EOF  
#${dirval}
#${yesno}
#EOF
#sleep 5

mkdir -p /logs_data/wt7/logs/ai-pms &&
mv /deploy/wt7/config/settings_sample /deploy/wt7/config/ai-pms  &&
java -classpath  /deploy/wt7/lib/wsthx.jar -DAllCheck whx.wt.common.util.JDBCClassMapper /deploy/BOOT-INF/lib/postgresql-42.3.4.jar &&
sleep 5 &&
mv -f /deploy/jdbc.map  /deploy/wt7/config/ai-pms/_global/ &&
mv -f /deploy/wt7/settings.ini  /deploy/wt7/config/ai-pms/
