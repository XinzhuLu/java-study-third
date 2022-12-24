package space.xinzhu.ee.web;

import org.springframework.stereotype.Service;
import space.xinzhu.ee.vo.Student;

import java.util.ArrayList;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/22
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
public interface StudentService {
    void save(ArrayList<Student> students);
}
