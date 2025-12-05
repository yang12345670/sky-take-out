package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface SetmealService {
    void saveCombo(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void changeStatus(Integer status, Long id);

    void deleteBatch(List<Long> ids);

    void updateSetmeal(SetmealDTO setmealDTO);

    SetmealDTO getSetmealById(Long id);
}
