GET _search
{
    "query": {
        "match_all": {}
    }
}

GET /_analyze
{
    "analyzer": "ik_max_word",
    "text": "黑马程序员学习叼你java太棒了习大大屏蔽词A"
}

# 分析：：：
{
    "age": 21,
    "weight": 52.1,
    "isMarried": false,
    "info": "一个人",
    "email": "xxx@qq.cn",
    "score": [99.1, 99.5, 98.9],
    "name": {
        "firstName": "云",
        "lastName": "赵"
}
}
#对应的每个字段映射（mapping）：

# - age：类型为 integer；参与搜索，因此需要index为true；无需分词器
# - weight：类型为float；参与搜索，因此需要index为true；无需分词器
# - isMarried：类型为boolean；参与搜索，因此需要index为true；无需分词器
# - info：类型为字符串，需要分词，因此是text；参与搜索，因此需要index为true；分词器可以用ik_smart
# - email：类型为字符串，但是不需要分词，因此是keyword；不参与搜索，因此需要index为false；无需分词器
# - score：虽然是数组，但是我们只看元素的类型，类型为float；参与搜索，因此需要index为true；无需分词器
# - name：类型为object，需要定义多个子属性
# - name.firstName；类型为字符串，但是不需要分词，因此是keyword；参与搜索，因此需要index为true；无需分词器
# - name.lastName；类型为字符串，但是不需要分词，因此是keyword；参与搜索，因此需要index为true；无需分词器*/
# PUT /索引库名
{
    "mappings": {
        "properties": {
            "info": {
                "type": "text",
                    "analyzer": "ik_smart"
            },
            "email": {
                "type": "keyword",
                    "index": false
            },
            "name": {
                "properties": {
                    "firstName": {
                        "type": "keyword"
                    },
                    "lastName": {
                        "type": "keyword"
                    }
                }
            }
        }
    }
}

GET /索引库名

/*
* 索引库一旦创建无法修改mapping
* 只允许添加新字段
*/
PUT /索引库名/_mapping
{
    "properties": {
        "age": {
            "type": "integer"
        }
    }
}

DELETE /索引库名

# =============================================================

POST /索引库名/_doc/1
{
"info": "一个好人",
"email": "zy@qq.cn",
"name": {
"firstName": "赵",
"lastName": "云"
}
}

GET /heima/_doc/1

DELETE /heima/_doc/1

# 全量修改
PUT /heima/_doc/1
{
"info": "一个好人个屁",
"email": "zy@qq.com",
"name": {
"firstName": "赵",
"lastName": "子龙"
}
}

# 增量修改
POST /heima/_update/1
{
"doc": {
"email": "zhaoyun@itcast.cn"
}
}

# location：地理坐标，里面包含精度、纬度
# all：一个组合字段，其目的是将多字段的值 利用copy_to合并，提供给用户搜索