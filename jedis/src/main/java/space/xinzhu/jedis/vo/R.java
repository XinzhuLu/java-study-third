package space.xinzhu.jedis.vo;

import lombok.Data;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/23
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
@Data
public class R<T> {
    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }
    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }
    public R<T> add(String key, Object value) {
        return this;
    }
}
