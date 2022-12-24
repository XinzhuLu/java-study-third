package space.xinzhu.es.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/24
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
@Data
@NoArgsConstructor
public class HotelDoc {
    private Long id;
    private String name;
    private String address;
    private Integer price;
    private Integer score;
    private String brand;
    private String city;
    private String starName;
    private String business;
    private String location;
    private String pic;
    private Object distance; // 排序时的距离值
    private Boolean isAD; // 是否广告
    private List<String> suggestion = new ArrayList<>(); // 自动补全建议字段

    public HotelDoc(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.address = hotel.getAddress();
        this.price = hotel.getPrice();
        this.score = hotel.getScore();
        this.brand = hotel.getBrand();
        this.city = hotel.getCity();
        this.starName = hotel.getStarName();
        this.business = hotel.getBusiness();
        this.location = hotel.getLatitude() + ", " + hotel.getLongitude();
        this.pic = hotel.getPic();
        // 自动补全建议字段：添加品牌、城市、商圈信息。其中商圈可能包含"/"需要切分
        this.suggestion.add(this.brand);
        this.suggestion.add(this.city);
        Collections.addAll(this.suggestion, this.business.split("/"));
    }
}
