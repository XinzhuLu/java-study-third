package space.xinzhu.es.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestParams {
    private String key;
    private Integer page;
    private Integer size;
    private String sortBy;
    // 过滤条件参数
    private String brand;
    private String city;
    private String starName;
    private Integer minPrice;
    private Integer maxPrice;
    // 我当前的地理坐标
    private String location;
}
