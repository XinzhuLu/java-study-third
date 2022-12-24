package space.xinzhu.ee;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import space.xinzhu.ee.vo.Student;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/22
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
public class StudentWriteDemo {

    public static void main(String[] args) {

        List<Student> students = initData();
        /*
            String pathName 写入文件的路径
            Class head      写入文件的对象类型
            默认写入到07的xlsx中，如果想要写入xls，可以指定类型（待验证）
         */
        ExcelWriterBuilder workBook = EasyExcel.write("d:\\学生信息.xlsx", Student.class);

        // sheet方法参数： 工作表的顺序号（从0开始）或者工作表的名字
        // 会覆盖旧信息
        //ExcelWriterSheetBuilder sheet1 = workBook.sheet(0);
        workBook.sheet().doWrite(students);
    }

    private static List<Student> initData() {
        ArrayList<Student> students = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Student data = new Student();
            data.setName("学生" + i);
            data.setBirthday(new Date());
            data.setGender("男");
            students.add(data);
        }
        return students;
    }

}
