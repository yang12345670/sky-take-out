package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Employee;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Beans;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Transactional//保证transaction的原子性
    public void saveCombo(SetmealDTO setmealDTO) {
        //1.先保存套餐基本信息到 setmeal 表
        Setmeal setmeal = new Setmeal();
        // 如果前端没有传状态，就默认起售
        if (setmeal.getStatus() == null) {
            setmeal.setStatus(StatusConstant.ENABLE);  // 1 起售
        }
        //自动填充切面（AutoFill），这里什么都不用管；
        //createTime、updateTime、createUser
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);
        //2.再保存套餐包含的菜品到 setmeal_dish 表
        List<SetmealDish> dishes = setmealDTO.getSetmealDishes();
        if (dishes != null && !dishes.isEmpty()) {
            for (SetmealDish dish : dishes) {
                dish.setSetmealId(setmeal.getId());
            }
            setmealDishMapper.insertBatch(dishes);
        }
    }

    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        long total = page.getTotal();
        List<Setmeal> records = page.getResult();
        return new PageResult(total, records);
    }

    public void changeStatus(Integer status, Long id) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);
        setmealMapper.update(setmeal);
    }

    @Transactional
    public void deleteBatch(List<Long> ids) {
        //1.判断套餐是否可以删除，是否存在起售的套餐？如果有那么不能删除
        for(Long id:ids){
            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus()== StatusConstant.ENABLE){
                throw new RuntimeException("当前套餐正在售卖，不能删除");
            }
        }
        //2.删除套餐表中的数据 setmeal
        setmealMapper.deleteBatch(ids);
        //3.删除套餐和菜品的关联数据 setmeal_dish
        setmealDishMapper.deleteBySetmealIds(ids);
    }

    public void updateSetmeal(SetmealDTO setmealDTO) {
        //1.更新套餐的基本信息 setmeal 表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
        //2.删除原有的套餐和菜品的关联数据 setmeal_dish 表
        List<Long> ids = List.of(setmealDTO.getId());
        setmealDishMapper.deleteBySetmealIds(ids);
        //3.重新插入新的套餐和菜品的关联数据 setmeal_dish 表
        List<SetmealDish> dishes = setmealDTO.getSetmealDishes();
        if (dishes != null && !dishes.isEmpty()) {
            for (SetmealDish dish : dishes) {
                dish.setSetmealId(setmealDTO.getId());
            }
            setmealDishMapper.insertBatch(dishes);
        }
    }

    public SetmealDTO getSetmealById(Long id) {
        //1.查询套餐的基本信息 setmeal 表
        Setmeal setmeal = setmealMapper.getById(id);
        if(setmeal==null){
            throw new RuntimeException("当前套餐不存在");
        }
        //2.查询套餐和菜品的关联信息 setmeal_dish 表
        List<SetmealDish> dishes = setmealDishMapper.getBySetmealId(id);
        //3.封装成 SetmealDTO 对象并返回
        SetmealDTO setmealDTO = new SetmealDTO();
        BeanUtils.copyProperties(setmeal,setmealDTO);
        setmealDTO.setSetmealDishes(dishes);
        return setmealDTO;
    }
}
