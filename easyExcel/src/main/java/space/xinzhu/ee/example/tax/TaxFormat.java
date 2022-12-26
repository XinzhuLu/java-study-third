package space.xinzhu.ee.example.tax;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description: ???
 * Created by 馨竹 on 2023/03/22
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
// @ExcelIgnoreUnannotated  标注该注解后，类中的成员变量如果没有标注`@ExcelProperty` 注解将不会参与读写。
public class TaxFormat {

    /**
     * 日期
     */
    @ExcelProperty("日期")//标准作用在成员变量上
    private String date;
    /**
     * 收入
     */
    @ExcelProperty("收入")//标准作用在成员变量上
    private float income;
    /**
     * 成本
     */
    @ExcelProperty("成本")
    private float cost;

    /**
     * 利润
     */
    @ExcelProperty("利润")
    private float profit;
}
