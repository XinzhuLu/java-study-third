package space.xinzhu.ee.example.tax;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import space.xinzhu.ee.vo.Student;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: ???
 * Created by 馨竹 on 2023/03/22
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
public class TaxWhiteDemo {

    public static void main(String[] args) {

        List<TaxFormat> taxs = initData();

        ExcelWriterBuilder workBook = EasyExcel.write("d:\\报税表3.xlsx", TaxFormat.class);

        workBook.sheet().doWrite(taxs);


        //List<TaxFormat> taxFormats = initData();

    }

    private static List<TaxFormat> initData() {
        ArrayList<TaxFormat> taxs = new ArrayList<>();

        int year = 2019;
        int month = 1;

        Float seasonIncome = 0.0f;
        Float seasonCost = 0.0f;
        Float seasonProfit = 0.0f;
        Float yearIncome = 0.0f;
        Float yearCost = 0.0f;
        Float yearProfit = 0.0f;

        for (int i = 0; i < 54; i++) {

            String date = "";

            if (month ==13){
                month = 1;
                year ++;
                date = date + year + "年" + month + "月";
            }else {
                date = date + year + "年" + month + "月";
            }

            DecimalFormat df = new DecimalFormat("0.0");
            double temp =  Math.random()*10000 + 2000;
            String a = df.format(temp);
            Float income = Float.parseFloat(a);

            double temp2 =  Math.random()*5000 + 1000;
            String b = df.format(temp2);
            Float cost = Float.parseFloat(b);

            Float profit = income - cost;

            TaxFormat tax = new TaxFormat(date , income , cost , profit);
            taxs.add(tax);

            System.out.println(cost);
            System.out.println(income);
            System.out.println(profit);
            System.out.println("---------");

            seasonIncome += income;
            seasonCost += cost;

            if(month == 3 || month == 6 || month == 9 || month == 12){

                seasonProfit = seasonIncome - seasonCost;
                String dateSum = "sum";
                TaxFormat taxSum = new TaxFormat(dateSum , seasonIncome , seasonCost , seasonProfit);
                taxs.add(taxSum);

                yearIncome += seasonIncome;
                yearCost += seasonCost;

                seasonIncome = 0.0f;
                seasonCost = 0.0f;

            }

            if (month == 12){

                yearProfit = yearIncome - yearCost;
                String dateSum = "yearSum";
                TaxFormat taxSum = new TaxFormat(dateSum , yearIncome , yearCost , yearProfit);
                taxs.add(taxSum);
                yearIncome = 0.0f;
                yearCost = 0.0f;
            }

            month++;

        }
        return taxs;
    }

}
