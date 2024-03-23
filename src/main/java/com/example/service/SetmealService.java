package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.SetmealDto;
import com.example.entity.Setmeal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    @Transactional
    void saveWithDishes(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);
}
