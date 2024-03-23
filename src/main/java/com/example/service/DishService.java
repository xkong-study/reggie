package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.DishDto;
import com.example.entity.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品并加入口味数据。需要操纵两张表:dish,dish_flavor
    public void saveWithFlavor(DishDto dishDto);
    public DishDto getByIdWithFlavor(Long id);
}
