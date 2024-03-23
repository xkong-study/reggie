package com.example.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.BaseContext;
import com.example.common.R;
import com.example.entity.ShoppingCart;
import com.example.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FileName: ShoppingCartController
 * Date: 2023/01/19
 * Time: 21:19
 * Author: river
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    // 添加购物车
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
      log.info("购物车数据");
      //设置用户id，指定当前是哪一个
      Long currentId = BaseContext.getThreadLocal();
      shoppingCart.setUserId(currentId);
      //查询菜品或者套餐是否在购物车中
      Long dishId = shoppingCart.getDishId();
      LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
      lambdaQueryWrapper.eq(ShoppingCart::getUserId,currentId);
      if(dishId!=null){
          //如果已经存在，就在原来的数量基础上加1
          lambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);
      }
      else{
          //如果不存在就添加到购物车数量默认为1
          lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,ShoppingCart.getSetmealId());
      }
      ShoppingCart cartServiceOne = shoppingCartService.getOne(lambdaQueryWrapper);
      if(cartServiceOne!=null){
          Integer number = cartServiceOne.getNumber();
          cartServiceOne.setNumber(number+1);
          shoppingCartService.updateById(cartServiceOne);
      }
      else{
          shoppingCart.setNumber(1);
          shoppingCartService.save(shoppingCart);
          cartServiceOne = shoppingCart;
      }
      return R.success(cartServiceOne);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车...");
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getThreadLocal());
        lambdaQueryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(lambdaQueryWrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getThreadLocal());
        shoppingCartService.remove(lambdaQueryWrapper);
        return R.success("成功");
    }

}
