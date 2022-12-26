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
public class TaxVO {

    private int id;

    private String date;

    private float income;

    private float cost;

    private float profit;
}