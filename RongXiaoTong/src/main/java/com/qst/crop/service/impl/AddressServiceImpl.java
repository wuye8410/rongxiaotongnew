package com.qst.crop.service.impl;

import com.qst.crop.dao.AddressDao;
import com.qst.crop.entity.Address;
import com.qst.crop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.qst.crop.util.AddressValidationUtil;
import com.qst.crop.common.exception.BusinessException;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressDao addressDao;

    @Override
    public void add(Address address) {
        // 1. 地址有效性校验
        if (!AddressValidationUtil.validateAddress(address.getAddressDetail())) {
            throw new BusinessException("地址格式无效，请检查详细地址是否正确");
        }
        // 2. 校验收货人信息
        if (address.getConsignee() == null || address.getConsignee().trim().isEmpty()) {
            throw new BusinessException("收货人姓名不能为空");
        }
        if (address.getPhone() == null || !address.getPhone().matches("^1[3-9]\\d{9}$")) {
            throw new BusinessException("请输入正确的手机号码");
        }

        // 3. 获取登录用户名
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = principal.getUsername();
        address.setOwnName(name);

        // 4. 如果设为默认地址，清除其他默认
        if (address.getIsDefault()) {
            setZero();
        }

        // 5. 保存地址
        addressDao.insertSelective(address);
    }

    @Override
    public List<Address> selectByOwnName() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String ownName = principal.getUsername();
        return addressDao.selectByExample(ownName);
    }

    @Override
    public Address selectDefaultByOwnName() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = principal.getUsername();
        return addressDao.selectOneByExample(name, "1");
    }

    @Override
    public void defaultAddressInfoUpdate(Address address) {
        // 1. 校验参数
        if (address.getId() == null) {
            throw new BusinessException("地址ID不能为空");
        }
        if (!AddressValidationUtil.validateAddress(address.getAddressDetail())) {
            throw new BusinessException("地址格式无效，请检查详细地址是否正确");
        }
        if (address.getConsignee() == null || address.getConsignee().trim().isEmpty()) {
            throw new BusinessException("收货人姓名不能为空");
        }
        if (address.getPhone() == null || !address.getPhone().matches("^1[3-9]\\d{9}$")) {
            throw new BusinessException("请输入正确的手机号码");
        }

        // 2. 设置当前登录用户
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = principal.getUsername();
        address.setOwnName(name);

        // 3. 如果设为默认，清除其他默认
        if (address.getIsDefault()) {
            setZero();
        }

        // 4. 更新地址
        update(address);
    }

    @Override
    public void update(Address address) {
        // 检查地址是否存在
        Address existing = addressDao.selectByPrimaryKey(address.getId());
        if (existing == null) {
            throw new BusinessException("地址不存在，无法更新");
        }
        addressDao.updateByPrimaryKey(address);
    }

    @Override
    public boolean delete(Integer id) {
        if (id == null) {
            throw new BusinessException("地址ID不能为空");
        }
        Address address = addressDao.selectByPrimaryKey(id);
        if (address == null) {
            throw new BusinessException("地址不存在，无法删除");
        }
        if (address.getIsDefault()) {
            return false;
        }
        addressDao.deleteByPrimaryKey(id);
        return true;
    }

    /**
     * 将当前用户的所有地址默认值设为false
     */
    public void setZero() {
        Address defaultAddress = selectDefaultByOwnName();
        if (defaultAddress != null) {
            defaultAddress.setIsDefault(false);
            addressDao.updateByPrimaryKey(defaultAddress);
        }
    }
}