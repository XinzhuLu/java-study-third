package space.xinzhu.ee.web;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import space.xinzhu.ee.vo.Student;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/22
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
@Controller
public class WebUploadAndDownload {
    /**
     * 文件上传
     * 1. 编写excel中每一行对应的实体类
     * 2. 由于默认异步读取excel，所以需要逐行读取的回调监听器
     * 3. 开始读取Excel
     */
    @PostMapping("upload")
    @ResponseBody
    public String upload(MultipartFile file) throws IOException {
        ExcelReaderBuilder workBook = EasyExcel.read(file.getInputStream(), Student.class, new StudentReadListener());
        workBook.sheet().doRead();
        return "success";
    }

    /**
     * 文件下载
     * 1. 编写实体类并创建对象以便写入表格
     * 2. 设置响应参数：文件的ContentType和文件名，同时设置编码避免乱码
     * 3. 直接写，内部会调用finish方法自动关闭OutputStream
     */
    @GetMapping("download")
    public void download(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 防止中文乱码
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName + ".xlsx");
        ExcelWriterBuilder workBook = EasyExcel.write(response.getOutputStream(), Student.class);

        ExcelWriterSheetBuilder sheet = workBook.sheet("模板");

        sheet.doWrite(initData());
    }

    // 循环生成10个学生对象
    private static List<space.xinzhu.ee.web.Student> initData() {
        ArrayList<space.xinzhu.ee.web.Student> students = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            space.xinzhu.ee.web.Student data = new space.xinzhu.ee.web.Student();
            data.setName("杭州黑马学号0" + i);
            data.setBirthday(new Date());
            data.setGender("男");
            students.add(data);
        }
        return students;
    }
}