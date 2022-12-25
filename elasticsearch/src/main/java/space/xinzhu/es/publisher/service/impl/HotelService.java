package space.xinzhu.es.publisher.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import space.xinzhu.es.publisher.service.IHotelService;
import space.xinzhu.es.mapper.HotelMapper;
import space.xinzhu.es.vo.Hotel;

@Service
public class HotelService extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {
}
