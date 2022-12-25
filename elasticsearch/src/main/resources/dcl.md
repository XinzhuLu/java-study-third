# 高亮查询，条件一定要使用全文检索查询
GET /hotel/_search
{
    # 查询条件
    "query": {
        "match": {
            "all": "如家"
            }
    },
    # 分页条件
    "from": 0,
    "size": 20,
    # 排序条件
    "sort":[
        {
            "price":"asc" ,
        },
        {
            "_geo_distance" :
                {
                    "location" : "31.1233 , 121,09876" ,
                    "order" : "asc" ,
                    "unit" : "km"
                }
        }
    ],
    # 高亮条件
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

