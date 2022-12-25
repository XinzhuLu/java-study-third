package space.xinzhu.es.publisher.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.xinzhu.es.constants.HotelMQConstants;
import space.xinzhu.es.publisher.service.IHotelService;
import space.xinzhu.es.vo.Hotel;
import space.xinzhu.es.vo.PageResult;

@RestController
@RequestMapping("hotel")
public class HotelController {

    @Autowired
    private IHotelService hotelService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //根据id查询
    @GetMapping("{id}")
    public Hotel queryById(@PathVariable("id") Long id) {
        return hotelService.getById(id);
    }

    //分页查询
    @GetMapping("list")
    public PageResult hotelList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "5") Integer size) {
        Page<Hotel> result = hotelService.page(new Page<>(page, size));
        //return new PageResult(result.getTotal(), result.getRecords());
        return null;
    }

    //新增酒店
    @PostMapping
    public void saveHotel(@RequestBody Hotel hotel) {
        // 新增酒店
        hotelService.save(hotel);
        // 发送MQ消息
        rabbitTemplate.convertAndSend(HotelMQConstants.EXCHANGE_NAME, HotelMQConstants.ROUTING_KEY_INSERT, hotel.getId());
    }

    //更新酒店
    @PutMapping
    public void updateById(@RequestBody Hotel hotel) {
        hotelService.updateById(hotel);
        // 发送MQ消息
        rabbitTemplate.convertAndSend(HotelMQConstants.EXCHANGE_NAME, HotelMQConstants.ROUTING_KEY_INSERT, hotel.getId());
    }

    //删除酒店
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        hotelService.removeById(id);
        // 发送MQ消息
        rabbitTemplate.convertAndSend(HotelMQConstants.EXCHANGE_NAME, HotelMQConstants.ROUTING_KEY_DELETE, id);
    }
}
