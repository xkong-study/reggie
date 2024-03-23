package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Category;
import com.example.entity.DishFlavor;
import com.example.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService extends IService<Category> {

    /**
     * 删除分类
     *
     * @param id 分类id
     */
    void remove(Long id);
}
