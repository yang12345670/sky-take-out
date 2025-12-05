package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;    @Transactional
    public void save(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //往菜品表插入一条数据
        dishMapper.insert(dish);
        //往口味表插入n条数据
        long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
                for (DishFlavor flavor : flavors) {
                    flavor.setDishId(dishId);
                }
            dishFlavorMapper.insertBatch(flavors);
            }
    }

    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    public void deleteBatch(List<Long> ids) {
        //判断当前菜品是否可以删除，是否存在起售的菜品？如果有那么不能删除
        for(Long id:ids){
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus()==1){
                throw new RuntimeException("当前菜品正在售卖，不能删除");
            }
        }
        //当前菜品是否被套餐关联，如果有关联那么不能删除
        for(Long id:ids){
            Integer count = dishMapper.countSetmealByDishId(id);
            if(count>0){
                throw new RuntimeException("当前菜品被套餐关联，不能删除");
            }
        }
        //删除菜品表的菜品数据
        for(Long id:ids) {
            dishMapper.deleteBatch(ids);
            //删除菜品口味表的口味数据
            dishFlavorMapper.deleteByDishIds(ids);
        }
    }


    public DishVO getByIdWithFlavor(Long id) {
        //查询菜品基本信息
        Dish dish = dishMapper.getById(id);
        if(dish==null){
            throw new RuntimeException("当前菜品不存在");
        }
        //查询菜品口味信息
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        //封装vo对象并返回
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        //修改菜品表基本信息
        Dish dish = new Dish();
        //先复制一份
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        //删除原有口味数据
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        //添加新的口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null && flavors.size()>0){
            for(DishFlavor flavor:flavors){
                flavor.setDishId(dishDTO.getId());
            }
            dishFlavorMapper.insertBatch(flavors);
        }
        }

        public List<DishVO> list(DishDTO dishDTO) {
        List<DishVO> dishVOList = dishMapper.list(dishDTO);
        return dishVOList;
        }
}


