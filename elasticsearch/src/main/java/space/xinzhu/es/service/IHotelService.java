package space.xinzhu.es.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.xinzhu.es.vo.Hotel;
import space.xinzhu.es.vo.PageResult;
import space.xinzhu.es.vo.RequestParams;

import java.util.List;
import java.util.Map;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/24
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
public interface IHotelService extends IService<Hotel> {

    //酒店搜索
    PageResult search(RequestParams params) throws Exception;

    //获取过滤条件
    Map<String, List<String>> filters(RequestParams params) throws Exception;

    //自动补全
    List<String> suggestion(String key) throws Exception;
}
