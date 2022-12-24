package space.xinzhu.ee.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import space.xinzhu.ee.vo.Student;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/22
 * --------------------------------------------
 *  ReadListener 在每一行读取完毕后都会调用ReadListener来处理数据
 **/
@Slf4j
public class StudentReadListener extends AnalysisEventListener<Student> {

    //数据入库
    //@Autowired
    //private SomeDto dto;

    // 每读一样，会调用该invoke方法一次
    @Override
    public void invoke(Student data, AnalysisContext context) {
        System.out.println("data = " + data);
        log.info(data + "保存成功");
    }

    // 全部读完之后，会调用该方法
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // TODO......
        log.info("读取完毕~~~~~~~~~~~~");
    }
}