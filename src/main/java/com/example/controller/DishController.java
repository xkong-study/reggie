package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.R;
import com.example.dto.DishDto;
import com.example.entity.Category;
import com.example.entity.Dish;
import com.example.entity.DishFlavor;
import com.example.service.CategoryService;
import com.example.service.DishFlavorService;
import com.example.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Dish> page1 = new Page<>(page,pageSize);
        Page<DishDto> pageDto = new Page<>(page,pageSize);
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdate_time);
        dishService.page(page1,lambdaQueryWrapper);
        BeanUtils.copyProperties(page1,pageDto,"records");
        List<Dish> records = page1.getRecords();
        List<DishDto> list = records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        pageDto.setRecords(list);
        log.info("page1:{}",list);
        return R.success(pageDto);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
           DishDto dishDto = dishService.getByIdWithFlavor(id);
           return R.success(dishDto);
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdate_time);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtoList  = list.stream().map((item)->{
             DishDto dishDto = new DishDto();
             BeanUtils.copyProperties(item,dishDto);
             Long categoryId = item.getCategoryId();
             Category category  = categoryService.getById(categoryId);
             if(category!=null){
                 String categoryName = category.getName();
                 dishDto.setCategoryName(categoryName);
             }
             Long dishId = item.getId();
             LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
             return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }
}
