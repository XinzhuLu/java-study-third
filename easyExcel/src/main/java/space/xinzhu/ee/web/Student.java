package space.xinzhu.ee.web;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/22
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
// @ExcelIgnoreUnannotated  标注该注解后，类中的成员变量如果没有标注`@ExcelProperty` 注解将不会参与读写。
public class Student {

    /**
     * id
     */
    @ExcelIgnore//加了这个注解会忽略该字段
    //@NumberFormat  代码中用`String类型的成员变量`去接收`excel数字格式的数据`会调用这个注解
    private String id;
    /**
     * 学生姓名
     */
    //@ExcelProperty({"学员信息表", "学生姓名"})
    @ExcelProperty("学生姓名")//标准作用在成员变量上
    //@ExcelProperty(value = "学生姓名" , index = 0 , converter = ??) converter成员变量转换器：自定义转换器需要实Converter接口
    private String name;
    /**
     * 学生性别
     */
    //@ExcelProperty({"学员信息表", "学生性别"})
    @ExcelProperty("学生性别")
    private String gender;

    /**
     * 学生出生日期
     */
    //@ExcelProperty({"学员信息表", "学生出生日期"})
    @ExcelProperty("学生出生日期")
    //@DateTimeFormat("yyyy-MM-dd")
    private Date birthday;
}

