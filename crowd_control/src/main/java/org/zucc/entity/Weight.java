package org.zucc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weight {
    /**
     * 类别
     */
    private String category;
    /**
     * 权重值
     */
    private int weight;
    /**
     * 分配数
     */
    private int num;

    private String longitude;

    private String latitude;


}
