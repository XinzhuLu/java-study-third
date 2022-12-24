# 查询所有
GET /hotel/_search
{
"query": {
"match_all": { }
}
}

# match查询
GET /hotel/_search
{
"query":{
"match":{
"all":"如家外滩"
}
}
}

# mulit_match查询
GET /hotel/_search
{
"query": {
"multi_match": {
"query": "如家外滩",
"fields": ["name"," brand", "business"]
}
}
}

# 精确查找精确查询一般是查找keyword、数值、日期、boolean等类型字段。
# term：根据词条精确值查询
# range：根据值的范围查询
# term查询
GET /hotel/_search
{
"query": {
"term": {
"city": "上海"
}
}
}

# range查询
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

# geo_bounding_box查询
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

# geo_distance 查询，distance半径，location圆心
GET /hotel/_search
{
"query": {
"geo_distance": {
"distance": "15km",
"location": "31.21, 121.5"
}
}
}

# ---------------------------------------------------------
# 复合查询
# function score 查询中包含四部分内容：
#  **原始查询** 条件：query部分，基于这个条件搜索文档，并且基于BM25算法给文档打分，**原始算分**（query score)
#  **过滤条件**：filter部分，符合该条件的文档才会重新算分
#  **算分函数**：符合filter条件的文档要根据这个函数做运算，得到的**函数算分**（function score），有四种函数
#    - weight：函数结果是常量
#    - field_value_factor：以文档中的某个字段值作为函数结果
#    - random_score：以随机数作为函数结果
#    - script_score：自定义算分函数算法
#  **运算模式**：算分函数的结果、原始查询的相关性算分，两者之间的运算方式，包括：
#    - multiply：相乘
#    - replace：用function score替换query score
#    - 其它，例如：sum、avg、max、min

# 未添加算分函数时，"如家" 的得分
GET /hotel/_search
{
"query": {
"function_score": {
"query": {
"match": {
"all": "外滩"
}
}
}
}
}

# 添加算分函数后，"如家" 的得分
GET /hotel/_search
{
"query": {
"function_score": {
"query": {
"match": {
"all": "外滩"
}
},
"functions": [
{
"filter": {
"term": {
"brand": "如家"
}
},
"weight": 2
}
],
"boost_mode": "sum"
}
}
}

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