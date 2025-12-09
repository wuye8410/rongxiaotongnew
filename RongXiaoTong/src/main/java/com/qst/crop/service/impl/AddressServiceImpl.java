package com.qst.crop.service.impl;

import com.qst.crop.dao.AddressDao;
import com.qst.crop.entity.Address;
import com.qst.crop.service.AddressService;
import com.qst.crop.util.AddressValidationUtil;
import com.qst.crop.common.exception.BusinessException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 地址服务实现类
 * 处理地址的增删改查及默认地址管理等业务逻辑
 */
@Service
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService {

    private final AddressDao addressDao;
    private final AddressValidationUtil addressValidationUtil; // 注入校验工具

    @Autowired
    public AddressServiceImpl(AddressDao addressDao, AddressValidationUtil addressValidationUtil) {
        this.addressDao = addressDao;
        this.addressValidationUtil = addressValidationUtil; // 注入
    }

    @Override
    @Transactional
    public void add(Address address) {
        // 1. 地址有效性校验
        validateAddressParams(address);

        // 2. 获取并设置当前登录用户
        String currentUsername = getCurrentUsername();
        address.setOwnName(currentUsername);

        // 3. 处理默认地址逻辑
        handleDefaultAddress(address);

        // 4. 保存地址
        addressDao.insertSelective(address);
    }

    @Override
    public List<Address> selectByOwnName() {
        String currentUsername = getCurrentUsername();
        return addressDao.selectByExample(currentUsername);
    }

    @Override
    public Address selectDefaultByOwnName() {
        String currentUsername = getCurrentUsername();
        return addressDao.selectOneByExample(currentUsername, "1");
    }

    @Override
    @Transactional
    public void defaultAddressInfoUpdate(Address address) {
        // 1. 校验地址ID
        if (address.getId() == null) {
            throw new BusinessException("地址ID不能为空");
        }

        // 2. 校验地址参数
        validateAddressParams(address);

        // 3. 设置当前登录用户
        String currentUsername = getCurrentUsername();
        address.setOwnName(currentUsername);

        // 4. 处理默认地址逻辑
        handleDefaultAddress(address);

        // 5. 执行更新
        update(address);
    }

    @Override
    @Transactional
    public void update(Address address) {
        // 检查地址是否存在
        Address existingAddress = addressDao.selectByPrimaryKey(address.getId());
        if (existingAddress == null) {
            throw new BusinessException("地址不存在，无法更新");
        }
        // 验证所有权
        validateAddressOwnership(existingAddress);
        addressDao.updateByPrimaryKey(address);
    }

    @Override
    @Transactional
    public boolean delete(Integer id) {
        if (id == null) {
            throw new BusinessException("地址ID不能为空");
        }

        Address address = addressDao.selectByPrimaryKey(id);
        if (address == null) {
            throw new BusinessException("地址不存在，无法删除");
        }

        // 验证所有权
        validateAddressOwnership(address);

        // 默认地址不能删除
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            return false;
        }

        addressDao.deleteByPrimaryKey(id);
        return true;
    }

    /**
     * 新增：地址校验接口
     */
    @SneakyThrows
    @Override
    public boolean validateAddress(String addressDetail) {
        return addressValidationUtil.validateAddress(addressDetail);
    }

    private void setZero() {
        Address defaultAddress = selectDefaultByOwnName();
        if (defaultAddress != null) {
            defaultAddress.setIsDefault(false);
            addressDao.updateByPrimaryKey(defaultAddress);
        }
    }

    private String getCurrentUsername() {
        try {
            UserDetails principal = (UserDetails) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
            return principal.getUsername();
        } catch (Exception e) {
            throw new BusinessException("获取当前登录用户失败", e);
        }
    }

    /**
     * 校验地址参数合法性
     */
    private void validateAddressParams(Address address) {
        // 收货人校验
        if (!StringUtils.hasText(address.getConsignee())) {
            throw new BusinessException("收货人姓名不能为空");
        }
        if (address.getConsignee().length() < 2 || address.getConsignee().length() > 20) {
            throw new BusinessException("收货人姓名长度必须在2-20个字符之间");
        }

        // 手机号校验
        if (!StringUtils.hasText(address.getPhone())) {
            throw new BusinessException("手机号码不能为空");
        }
        if (!address.getPhone().matches("^1[3-9]\\d{9}$")) {
            throw new BusinessException("请输入正确的11位手机号码");
        }

        // 地址详情校验
        if (!StringUtils.hasText(address.getAddressDetail())) {
            throw new BusinessException("详细地址不能为空");
        }
        if (address.getAddressDetail().length() < 5 || address.getAddressDetail().length() > 100) {
            throw new BusinessException("详细地址长度必须在5-100个字符之间");
        }

        // 使用工具类进行地址校验
        try {
            if (!addressValidationUtil.validateAddress(address.getAddressDetail())) {
                throw new BusinessException("地址格式无效，请检查详细地址是否正确");
            }
        } catch (Exception e) {
            // 记录异常日志，这里可以根据需要记录
            e.printStackTrace();
            // 抛出业务异常，将异常信息传递给上层
            throw new BusinessException("地址校验服务异常: " + e.getMessage());
        }

        // 标签校验（可选）
        if (StringUtils.hasText(address.getTag()) && address.getTag().length() > 10) {
            throw new BusinessException("地址标签长度不能超过10个字符");
        }
    }
    private void handleDefaultAddress(Address address) {
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            setZero();
        }
    }

    private void validateAddressOwnership(Address address) {
        String currentUsername = getCurrentUsername();
        if (!currentUsername.equals(address.getOwnName())) {
            throw new BusinessException("无权操作该地址");
        }
    }
}