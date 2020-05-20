package com.learn.mybatisplus.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @TableId(type = IdType.AUTO)           //主健生成器  必须是Long不能long    input必须手动输入否则为null   auto必须在数据库中设置自增，从最大值开始自增   https://mybatis.plus/guide/annotation.html#tableid
    private Long id;
    private String name;
    private int age;
    private String email;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;   //如果数据库中有默认值CURRENT_TIMESTAMP  则是数据库级别操作
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;   //LocalDateTime与Date
    @Version   //乐观琐注释
    private int version;
    //@TableLogic 局部逻辑删除
    private int deleted;

}
