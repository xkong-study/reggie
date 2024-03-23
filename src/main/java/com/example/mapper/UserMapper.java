package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * FileName: UserMapper
 * Date: 2023/01/16
 * Time: 20:18
 * Author: river
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
