package space.xinzhu.ee.web;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import space.xinzhu.ee.vo.Student;

import java.util.ArrayList;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/22
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
@Component
@Scope("prototype")	// 作者要求每次读取都要使用新的Listener
public class StudentReadListener extends AnalysisEventListener<Student> {

    @Autowired
    private StudentService studentService;

    private final int BATCH_SAVE_NUM = 5;
    ArrayList<Student> students = new ArrayList<>();


    private int count = 0;

    // 每读一样，会调用该invoke方法一次
    @Override
    public void invoke(Student data, AnalysisContext context) {
        students.add(data);
        if (++count % BATCH_SAVE_NUM == 0) {
            studentService.save(students);
            students.clear();
        }
    }

    // 全部读完之后，会调用该方法
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // TODO......
    }
}