package com.example.entity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class DishFlavor implements Serializable {
   private static final long serialVersionUID = 1L;
   private Long id;
   private Long dishId;
   private String name;
   private String value;
   @TableField(fill= FieldFill.INSERT)
   private LocalDateTime create_time;
   @TableField(fill=FieldFill.INSERT_UPDATE)
   private LocalDateTime update_time;
   @TableField(fill=FieldFill.INSERT)
   private Long create_user;
   @TableField(fill=FieldFill.INSERT)
   private Long update_user;
   private Integer isDeleted;
}
