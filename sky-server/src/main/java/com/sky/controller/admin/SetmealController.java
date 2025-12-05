package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @ApiOperation("新增套餐")
    public Result saveCombo(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);
        setmealService.saveCombo(setmealDTO);
        return Result.success();
    }

    //套餐分页查询
    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询：{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    //套餐起售、停售
    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售、停售")
    public Result changeStatus(@PathVariable Integer status,
                               @RequestParam("id") Long id) {
        log.info("套餐状态修改，套餐id：{}，修改状态：{}", id, status);
        setmealService.changeStatus(status, id);
        return Result.success();
    }

    //套餐删除和批量删除
    @DeleteMapping
    @ApiOperation("套餐删除和批量删除")
    public Result deleteBatch(@RequestParam("ids") List<Long> ids) {
        log.info("套餐删除和批量删除，套餐ids：{}", ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改套餐")
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐：{}", setmealDTO);
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐信息")
    public Result<SetmealDTO> getSetmealById(@PathVariable Long id){
        log.info("根据id查询套餐信息，套餐id：{}", id);
        SetmealDTO setmealDTO = setmealService.getSetmealById(id);
        return Result.success(setmealDTO);
    }
}

