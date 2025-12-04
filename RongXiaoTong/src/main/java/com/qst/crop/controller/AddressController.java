package com.qst.crop.controller;

import com.qst.crop.common.Result;
import com.qst.crop.common.StatusCode;
import com.qst.crop.entity.Address;
import com.qst.crop.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author QST
 * @Description 地址类
 * @Date 2021-08-24
 */
@RestController
@RequestMapping("/address")
@Tag(name = "地址模块接口")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Operation(summary = "添加地址")
    @PostMapping("/add")
    public Result add(@Validated @RequestBody Address address, BindingResult bindingResult) {
        // 检查参数校验
        if (bindingResult.hasErrors()) {
            StringBuffer stringBuffer = new StringBuffer();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError : allErrors) {
                stringBuffer.append(objectError.getDefaultMessage()).append("; ");
            }
            return new Result<String>(false, StatusCode.ERROR, "添加失败", stringBuffer.toString());
        }
        try {
            addressService.add(address);
            return new Result(true, StatusCode.OK, "添加成功");
        } catch (Exception e) {
            return new Result(false, StatusCode.ERROR, "添加失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据登录用户名查询所有地址")
    @GetMapping("/selectByOwnName")
    public Result<List<Address>> selectByOwnName() {
        try {
            List<Address> addresses = addressService.selectByOwnName();
            return new Result<List<Address>>(true, StatusCode.OK, "查询成功", addresses);
        } catch (Exception e) {
            return new Result<>(false, StatusCode.ERROR, "查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据登录用户名查询默认地址")
    @GetMapping("/selectDefaultByOwnName")
    public Result<Address> selectDefaultByOwnName() {
        try {
            Address address = addressService.selectDefaultByOwnName();
            if (null == address) {
                List<Address> addresses = addressService.selectByOwnName();
                if (null != addresses && !addresses.isEmpty()) {
                    address = addresses.get(0);
                }
            }
            return new Result<Address>(true, StatusCode.OK, "查询成功", address);
        } catch (Exception e) {
            return new Result<>(false, StatusCode.ERROR, "查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "默认地址信息更新操作")
    @PostMapping("/defaultAddressInfoUpdate")
    public Result<String> defaultAddressInfoUpdate(@Validated @RequestBody Address address, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuffer stringBuffer = new StringBuffer();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError : allErrors) {
                stringBuffer.append(objectError.getDefaultMessage()).append("; ");
            }
            return new Result<String>(false, StatusCode.ERROR, "地址信息更新失败", stringBuffer.toString());
        }
        try {
            addressService.defaultAddressInfoUpdate(address);
            return new Result<String>(true, StatusCode.OK, "修改成功");
        } catch (Exception e) {
            return new Result<>(false, StatusCode.ERROR, "修改失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据id修改地址")
    @PostMapping("/update/{id}")
    public Result update(@PathVariable("id") Integer id, @Validated @RequestBody Address address, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuffer stringBuffer = new StringBuffer();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError : allErrors) {
                stringBuffer.append(objectError.getDefaultMessage()).append("; ");
            }
            return new Result<String>(false, StatusCode.ERROR, "修改失败", stringBuffer.toString());
        }
        try {
            address.setId(id);
            addressService.update(address);
            return new Result(true, StatusCode.OK, "修改成功");
        } catch (Exception e) {
            return new Result(false, StatusCode.ERROR, "修改失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据id删除地址")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        try {
            boolean delete = addressService.delete(id);
            if (!delete) {
                return new Result(false, StatusCode.ERROR, "该地址为默认地址不能删除");
            } else {
                return new Result(true, StatusCode.OK, "删除成功");
            }
        } catch (Exception e) {
            return new Result(false, StatusCode.ERROR, "删除失败：" + e.getMessage());
        }
    }
}