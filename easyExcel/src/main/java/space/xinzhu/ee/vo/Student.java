package space.xinzhu.ee.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/22
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ColumnWidth(20)
public class Student {

    /**
     * id
     */
    //@ExcelProperty(value = "编号",index = 3)
    @ExcelIgnore
    private String id;
    /**
     * 学生姓名
     */
    @ExcelProperty(value = "学生姓名", index = 0)
    //@ColumnWidth(30)
    private String name;
    /**
     * 学生性别
     */
    @ExcelProperty(value = "学生性别", index = 2)
    private String gender;

    /**
     * 学生出生日期
     */
    @ExcelProperty(value = "学生出生日期", index = 1)
    //@ColumnWidth(20)
    private Date birthday;
}
