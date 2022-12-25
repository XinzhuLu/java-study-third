# size设置为0，结果中不包含文档，只包含聚合结果
GET /hotel/_search
{
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

# 更新索引库，自动补全的索引字段
PUT /test1/_mapping
{
    "properties": {
        "title": {
            "type": "completion"
        }
    }
}

# 自动补全查询
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

# 酒店数据索引库
DELETE /hotel

# 酒店数据索引库
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

# keep_first_letter                       | 启用此选项时，例如：刘德华> ldh，默认值：true
# keep_separate_first_letter              | 启用该选项时，将保留第一个字母分开，例如：刘德华> l，d，h，默认：false。<br />注意：查询结果也许是太模糊，由于长期过频 |
# **limit_first_letter_length**           | 设置first_letter结果的最大长度，默认值：16                 |
# **keep_full_pinyin**                    | 当启用该选项，例如：刘德华 > [ liu，de，hua]，默认值：true   |
# **keep_joined_full_pinyin**             | 当启用此选项时，例如：刘德华 > [ liudehua]，默认值：false    |
# keep_none_chinese                       | 在结果中保留非中文字母或数字，默认值：true                   |
# keep_none_chinese_in_first_letter       | 第一个字母保持非中文字母，例如：刘德华AT2016- > ldhat2016，默认值：true |
# keep_none_chinese_in_joined_full_pinyin | 保留非中文字母加入完整拼音，例如：刘德华2016- > liudehua2016，默认：false |
# **keep_original**                       | 当启用此选项时，也会保留原始输入，默认值：false              |
# lowercase                               | 小写非中文字母，默认值：true                              |
# trim_whitespace                         | 默认值：true                                           |
# **remove_duplicated_term**              | 当启用此选项时，将删除重复项以保存索引，例如：de的 > de，默认值：false。<br />注意：位置相关查询可能受影响 |
# 创建索引库
PUT /test2/
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
            "name": {
                "type": "text",
                "analyzer": "my_analyzer",
                "search_analyzer": "ik_smart"
            }
        }
    }
}