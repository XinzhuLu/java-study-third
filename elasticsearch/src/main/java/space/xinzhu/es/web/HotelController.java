package space.xinzhu.es.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.xinzhu.es.service.IHotelService;
import space.xinzhu.es.vo.PageResult;
import space.xinzhu.es.vo.RequestParams;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("hotel")
public class HotelController {

    @Autowired
    private IHotelService hotelService;

    /**
     * 搜索酒店
     *
     * @param params 请求参数
     * @return 结果对象
     * @throws Exception 抛出异常
     */
    @PostMapping("list")
    public PageResult search(@RequestBody RequestParams params) throws Exception {
        return hotelService.search(params);
    }

    /**
     * 获取过滤条件
     *
     * @param params 请求参数
     * @return Map格式：{"city": ["上海", "北京"], "brand": ["如家", "希尔顿"], "starName": ["二星", "三星"]}
     * @throws Exception 抛出异常
     */
    @PostMapping("filters")
    public Map<String, List<String>> filters(@RequestBody RequestParams params) throws Exception {
        return hotelService.filters(params);
    }


    /**
     * 自动补全
     *
     * @param key 关键词前缀
     * @return 词汇集合
     * @throws Exception 抛出异常
     */
    @GetMapping("suggestion")
    public List<String> suggestion(@RequestParam("key") String key) throws Exception {
        return hotelService.suggestion(key);
    }
}
