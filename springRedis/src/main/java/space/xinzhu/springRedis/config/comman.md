# 通用操作：
keys *   #查看当前redis服务器中所有的key    # 谨慎执行
keys *er
keys xx*

del key   # 删除一个
type  key

flushdb   # 清空当前库   # 谨慎执行

# string
set name lisi
get name

# hash
hset hash k1 v1
hset hash k2 v2
hget hash k1
hgetall hash

# list
lpush list aaa
rpush list bbb

lpop list
rpop list

lrange list 0 -1

# set
sadd set1 aaa
sadd set1 bbb
sadd set2 bbb

sdiff set1 set2
sunion set1 set2
sinter set1 set2