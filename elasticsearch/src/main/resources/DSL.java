##### ES自带的standard分词器，对中文不友好
POST /_analyze
{
  "analyzer": "standard",
  "text": "黑马程序员学习java太棒了！"
}


##### 测试IK分词器，对中文分词比较友好，分词性能高
# ik_smart:最少切分，ik_max_word:最细切分
GET /_analyze
{
  "analyzer": "ik_smart",
  "text": "传智播客Java就业率超过95%，习大大都点赞，奥力给！"
}


##### 创建heima索引库，注意类型：text分词，keyword不分词
PUT /heima
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


##### 查看索引信息
GET /heima/_mapping



##### 更新索引信息，不支持修改，可以新增字段
PUT /heima/_mapping
{
  "properties": {
    "age": {
      "type": "long"
    }
  }
}


##### 删除索引库
DELETE /heima


##### 添加文档
POST /heima/_doc/1
{
  "info": "黑马程序员Java讲师",
  "email": "zy@itcast.cn",
  "name": {
    "firstName": "赵",
    "lastName": "云"
  }
}


##### 根据id查询文档
GET /heima/_doc/1


##### 根据id删除数据
DELETE /heima/_doc/1


##### 更新文档，先删除再添加
PUT /heima/_doc/1
{
  "info": "黑马程序员高级Java讲师",
  "email": "zy@itcast.cn",
  "name": {
    "firstName": "赵",
    "lastName": "云"
  }
}

##### 增量更新文档信息，文档必须存在
POST /heima/_update/1
{
  "doc": {
    "email": "zhaoyun@itcast.cn"
  }
}


##### 大家猜测下，如果新增文档的结构与mapping结构不一致，会报什么错误? 不会
PUT /heima/_doc/2
{
  "info": "黑马程序员Python讲师",
  "email": "zf@itcast.cn",
  "name": {
    "firstName": "张",
    "lastName": "飞"
  },
  "age": 35,
  "score": [98.5, 98.9, 97.9, 99.2],
  "isMarried": false,
  "birthday": "1988-05-20",
  "city": "上海"
}




##### 创建hotel酒店索引库
PUT /hotel
{
  "mappings": {
    "properties": {
      "id": {
        "type": "keyword"
      },
      "name":{
        "type": "text",
        "analyzer": "ik_max_word",
        "copy_to": "all"
      },
      "brand":{
        "type": "keyword",
        "copy_to": "all"
      },
      "city":{
        "type": "keyword",
        "copy_to": "all"
      },
      "address":{
        "type": "keyword",
        "index": false
      },
      "price":{
        "type": "integer"
      },
      "score":{
        "type": "integer"
      },
      "starName":{
        "type": "keyword"
      },
      "business":{
        "type": "keyword"
      },
      "location":{
        "type": "geo_point"
      },
      "pic":{
        "type": "keyword",
        "index": false
      },
      "all":{
        "type": "text",
        "analyzer": "ik_max_word"
      }
    }
  }
}


##### 查询所有文档，match_all查询
GET /hotel/_search
{
  "query": {
    "match_all": {}
  }
}


##### 基本查询，match
GET /hotel/_search
{
  "query": {
    "match": {
      "all": "如家外滩"
    }
  }
}


##### 多字段查询，multi_match
GET /hotel/_search
{
  "query": {
    "multi_match": {
      "query": "如家外滩",
      "fields": ["name", "brand", "business"]
    }
  }
}



##### term词条查询，使用的较少，不会分词
GET /hotel/_search
{
  "query": {
    "term": {
      "city": "上海"
    }
  }
}


##### range范围查询，常见的价格范围搜索
GET /hotel/_search
{
  "query": {
    "range": {
      "price": {
        "gte": 500,
        "lte": 1000
      }
    }
  }
}


##### geo_bounding_box查询，盒子模型，top_left 左上坐标，bottom_right 右下坐标
GET /hotel/_search
{
  "query": {
    "geo_bounding_box": {
      "location": {
        "top_left": {
          "lat": 31.1,
          "lon": 121.5
        },
        "bottom_right": {
          "lat": 30.9,
          "lon": 121.7
        }
      }
    }
  }
}


##### geo_distance查询，圆形模型，distance 半径，location 圆心
GET /hotel/_search
{
  "query": {
    "geo_distance": {
      "distance": "15km",
      "location": "31.21, 121.5"
    }
  }
}


GET /hotel/_search
{
  "query": {
    "match": {
      "name": "上海外滩"
    }
  }
}


##### 复合查询 - Function Score查询
# 三要素：查询条件，过滤条件，加权方式
GET /hotel/_search
{
  "query": {
    "function_score": {
      "query": {
        "match": {
          "name": "上海外滩"
        }
      },
      "functions": [
        {
          "filter": {
            "term": {
              "brand": "如家"
            }
          },
          "weight": 5
        }
      ],
      "boost_mode": "multiply"
    }
  }
}



##### 复合查询 - bool查询
GET /hotel/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "city": "上海"
          }
        }
      ],
      "should": [
        {
          "term": {
            "brand": "皇冠假日"
          }
        },
        {
          "term": {
            "brand": "华美达"
          }
        }
      ],
      "must_not": [
        {
          "range": {
            "price": {
              "lte": 500
            }
          }
        }
      ],
      "filter": [
        {
          "range": {
            "score": {
              "gte": 45
            }
          }
        }
      ]
    }
  }
}


##### 复合查询 - bool查询
GET /hotel/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "name": "如家"
          }
        }
      ],
      "must_not": [
        {
          "range": {
            "price": {
              "gt": 400
            }
          }
        }
      ],
      "filter": [
        {
          "geo_distance": {
            "distance": "10km",
            "location": {
              "lat": 31.21,
              "lon": 121.5
            }
          }
        }
      ]
    }
  }
}


##### 排序，实现对酒店数据按照用户评价降序排序，评价相同的按照价格升序排序
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "score": "desc"
    },
    {
      "price": "desc"
    }
  ]
}



##### 排序，实现对酒店数据按照到你的位置坐标的距离升序排序
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "sort": {
    "_geo_distance": {
      "location": "31.034, 121.612",
      "order": "asc",
      "unit": "km"
    }
  }
}



##### 分页
# from: 默认0，不能超过1w
# size: 默认10
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "from": 0,
  "size": 5,
  "sort": {
    "price": "asc"
  }
}



##### 高亮
GET /hotel/_search
{
  "query": {
    "match": {
      "all": "如家"
    }
  },
  "highlight": {
    "fields": {
      "name": {
        "pre_tags": "<em>",
        "post_tags": "</em>",
        "require_field_match": "false"
      }
    }
  }
}


##### 给酒店添加广告
POST /hotel/_update/1902197537
{
  "doc": {
    "isAD": true
  }
}
POST /hotel/_update/2056126831
{
  "doc": {
    "isAD": true
  }
}
POST /hotel/_update/1989806195
{
  "doc": {
    "isAD": true
  }
}
POST /hotel/_update/2056105938
{
  "doc": {
    "isAD": true
  }
}






##### Bucket分桶聚合
# size：    结果中不包含文档，只包含聚合结果
# brandAgg：聚合取得的名字，可以自定义
# terms：   聚合的类型，按词条聚合，比如城市，品牌
# field：   参与聚合的字段
# size：    获取聚合结果的条数
GET /hotel/_search
{
  "size": 0,
  "aggs": {
    "brandAgg": {
      "terms": {
        "field": "brand",
        "size": 10
      }
    },
    "cityAgg":{
      "terms": {
        "field": "city",
        "size": 10
      }
    }
  }
}

##### Bucket分桶聚合-对结果排序
# 默认情况下，会根据桶内文档数量降desc序排序
GET /hotel/_search
{
  "size": 0,
  "aggs": {
    "brandAgg": {
      "terms": {
        "field": "brand",
        "order": {
          "_count": "asc"
        },
        "size": 10
      }
    }
  }
}



##### Bucket分桶聚合-添加限定条件
# 只对200元以下的文档聚合
GET /hotel/_search
{
  "query": {
    "range": {
      "price": {
        "lte": 200
      }
    }
  },
  "size": 0,
  "aggs": {
    "brandAgg": {
      "terms": {
        "field": "brand",
        "size": 20
      }
    }
  }
}


##### Metrics聚合
# 获取每个品牌酒店的平均分
# stats
GET /hotel/_search
{
  "size": 0,
  "aggs": {
    "brandAgg": {
      "terms": {
        "field": "brand",
        "size": 10
      },
      "aggs": {
        "score_avg": {
          "stats": {
            "field": "score"
          }
        }
      }
    }
  }
}

##### 创建test1索引库，测试自动补全
PUT /test1/
{
  "mappings": {
    "properties": {
      "title": {
        "type": "completion"
      }
    }
  }
}

##### 添加测试数据
POST /test1/_doc
{
  "title": ["Sony",  "WH-1000XM3"]
}
POST /test1/_doc
{
  "title": ["SK-II", "PITERA"]
}
POST /test1/_doc
{
  "title": ["Nintendo", "switch"]
}


##### 测试自动补全查询
GET /test1/_search
{
  "suggest": {
    "title_suggest": {
      "text": "s",
      "completion": {
        "field": "title",
        "skip_duplicates": true,
        "size": 10
      }
    }
  }
}


##### 自动补全，重新创建酒店索引库，不带拼音分词
DELETE /hotel

PUT /hotel
{
  "mappings": {
    "properties": {
      "id":{
        "type": "keyword"
      },
      "name":{
        "type": "text",
        "copy_to": "all"
      },
      "business":{
        "type": "keyword",
        "copy_to": "all"
      },  
      "brand":{
        "type": "keyword",
        "copy_to": "all"
      },
      "all":{
        "type": "text"
      },
      "suggestion":{
          "type": "completion"
      },
      "starName":{
        "type": "keyword"
      },
      "city":{
        "type": "keyword"
      },
      "address":{
        "type": "keyword",
        "index": false
      },
      "pic":{
        "type": "keyword",
        "index": false
      },
      "price":{
        "type": "integer"
      },
      "score":{
        "type": "integer"
      },
      "location":{
        "type": "geo_point"
      }
    }
  }
}

##### 自动补全，测试酒店索引库，不带拼音分词
GET /hotel/_search
{
  "suggest": {
    "title_suggest": {
      "text": "如",
      "completion": {
        "field": "suggestion",
        "skip_duplicates": true,
        "size": 10
      }
    }
  }
}


##### 安装拼音分词器后，测试拼音分词器效果，不能满足我们的要求
POST /_analyze
{
  "text": "黑马程序员",
  "analyzer": "pinyin"
}


##### 创建test2索引库，测试自定义分词器效果

#limit_first_letter_length: 设置拼音的长度，默认值：16
#keep_full_pinyin: 例如：刘德华 > [ liu, de, hua]，默认值：true
#keep_joined_full_pinyin: 例如：刘德华 > [ liudehua]，默认值：false
#keep_original: 是否保留原始内容，默认值：false
#remove_duplicated_term: 删除重复项以保存索引，默认值：false

DELETE /test2

PUT /test2
{
  "settings": {
    "analysis": {
      "analyzer": {
        "my_analyzer": {
          "tokenizer": "ik_max_word",
          "filter": "py"
        }
      },
      "filter": {
        "py": {
          "type": "pinyin",
          "keep_full_pinyin": false,
          "keep_joined_full_pinyin": false,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": false,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "name": {
        "type": "text",
        "analyzer": "my_analyzer",
        "search_analyzer": "ik_smart"
      }
    }
  }
}
##### 测试自定义分词器效果
GET /test2/_analyze
{
  "text": "黑马程序员",
  "analyzer": "my_analyzer"
}



##### 重新创建酒店索引库，带拼音分词
DELETE /hotel

PUT /hotel
{
  "settings": {
    "analysis": {
      "analyzer": {
        "text_anlyzer": {
          "tokenizer": "ik_max_word",
          "filter": "py"
        },
        "completion_analyzer": {
          "tokenizer": "keyword",
          "filter": "py"
        }
      },
      "filter": {
        "py": {
          "type": "pinyin",
          "keep_full_pinyin": false,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  
  "mappings": {
    "properties": {
      "id":{
        "type": "keyword"
      },
      "name":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart",
        "copy_to": "all"
      },
      "address":{
        "type": "keyword",
        "index": false
      },
      "price":{
        "type": "integer"
      },
      "score":{
        "type": "integer"
      },
      "brand":{
        "type": "keyword",
        "copy_to": "all"
      },
      "city":{
        "type": "keyword"
      },
      "starName":{
        "type": "keyword"
      },
      "business":{
        "type": "keyword",
        "copy_to": "all"
      },
      "location":{
        "type": "geo_point"
      },
      "pic":{
        "type": "keyword",
        "index": false
      },
      "all":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart"
      },
      "suggestion":{
          "type": "completion",
          "analyzer": "completion_analyzer"
      }
    }
  }
}
##### 测试，以"r"作为前缀，获取补全信息
GET /hotel/_search
{
  "suggest": {
    "title_suggest": {
      "text": "r",
      "completion": {
        "field": "suggestion",
        "skip_duplicates": true,
        "size": 10
      }
    }
  }
}
