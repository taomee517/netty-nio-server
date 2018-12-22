package com.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date  2018/12/22
 * @time  11:14
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Worker {
    private Integer id;
    private String dept;
    private String name;
    private String position;

    public static final String LNG_SIGN = "N";
    public static final String LAT_SIGN = "E";
}

