start redis-server.exe  redis.7001.conf
start redis-server.exe  redis.7002.conf
start redis-server.exe  redis.7003.conf
start redis-server.exe  redis.7004.conf
start redis-server.exe  redis.7005.conf
start redis-server.exe  redis.7006.conf

redis-cli --cluster create --cluster-replicas 1 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 127.0.0.1:7006

#`redis-cli --cluster`：代表集群操作命令
#`create`：代表是创建集群
#`--cluster-replicas 1` ：指定集群中每个master的副本个数为1，此时`节点总数 ÷ (副本 + 1)` 得到的就是master的数量。因此节点列表中的前n个就是master，其它节点都是slave节点，随机分配到不同master

# 尝试连接7001节点，存储一个数据：
redis-cli -c -p 7001
set a 1