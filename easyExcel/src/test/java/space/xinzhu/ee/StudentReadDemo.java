package space.xinzhu.ee;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import space.xinzhu.ee.listener.StudentReadListener;
import space.xinzhu.ee.vo.Student;

import java.io.FileNotFoundException;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/22
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
@SpringBootTest
public class StudentReadDemo {

//● EasyExcel 入口类，用于构建开始各种操作
//● ExcelReaderBuilder ExcelWriterBuilder 构建出一个 ReadWorkbook WriteWorkbook，可以理解成一个excel对象，一个excel只要构建一个
//● ExcelReaderSheetBuilder ExcelWriterSheetBuilder 构建出一个 ReadSheet WriteSheet对象，可以理解成excel里面的一页,每一页都要构建一个
//● ReadListener 在每一行读取完毕后都会调用ReadListener来处理数据
//● WriteHandler 在每一个操作包括创建单元格、创建表格等都会调用WriteHandler来处理数据
//● 所有配置都是继承的，Workbook的配置会被Sheet继承，所以在用EasyExcel设置参数的时候，在EasyExcel...sheet()方法之前作用域是整个sheet,之后针对单个sheet

    @Test
    public void readTest() throws FileNotFoundException {
        // 读取文件，读取完之后会自动关闭
        /*
        	pathName  		文件路径；"d:\\学生信息.xls"
        	head			每行数据对应的实体；Student.class
        	readListener	读监听器，每读一样就会调用一次该监听器的invoke方法

        	sheet方法参数： 工作表的顺序号（从0开始）或者工作表的名字，不传默认为0
        */
        // 封装工作簿对象
        ExcelReaderBuilder workBook = EasyExcel.read
                ("d:\\学生信息.xlsx", Student.class, new StudentReadListener());

        // 封装工作表
        ExcelReaderSheetBuilder sheet1 = workBook.sheet(0);
        // 读取
        sheet1.doRead();
    }
}
