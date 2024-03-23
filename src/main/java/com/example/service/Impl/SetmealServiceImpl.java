package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.CustomException;
import com.example.dto.SetmealDto;
import com.example.entity.Setmeal;
import com.example.entity.SetmealDish;
import com.example.mapper.SetmealMapper;
import com.example.service.SetmealDishService;
import com.example.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Override
    @Transactional
    public void saveWithDishes(SetmealDto setmealDto) {
        // 保存套餐基本信息
        this.save(setmealDto);
        // 获取setmeal的List<SetmealDish>信息，！需要注意的是，该list中的setmealId为null，前端未传入，所以需要手动遍历，通过SetmealDto继承的Setmeal获取id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().peek(item -> item.setSetmealId(setmealDto.getId())).collect(Collectors.toList()); // peek()方法为中间操作，不会改变原有的list，collect()方法为终止操作，会改变原有的list
        // 保存套餐和菜品的关联关系
        setmealDishService.saveBatch(setmealDishes);
        // 清理redis缓存
        Set<Object> keys = redisTemplate.keys("setmeal_*");    // 获取所有以dish_开头的key
        assert keys != null;
        redisTemplate.delete(keys);
    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        //如果不能删除，跑出一个业务异常
        int count = this.count(queryWrapper);
        if(count>0){
            throw new CustomException("套餐正在售卖中不能删除");
        }
        //如果可以删除，先删除套餐表中的数据
        this.removeByIds(ids);
        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
