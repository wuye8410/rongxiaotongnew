package com.qst.crop.service.impl;

import com.qst.crop.dao.AddressDao;
import com.qst.crop.entity.Address;
import com.qst.crop.service.AddressService;
import com.qst.crop.util.AddressValidationUtil;
import com.qst.crop.common.exception.BusinessException;
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
@Transactional(readOnly = true) // 查询操作默认只读事务
public class AddressServiceImpl implements AddressService {

    private final AddressDao addressDao;

    // 构造器注入，替代字段注入，更符合Spring最佳实践
    @Autowired
    public AddressServiceImpl(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    /**
     * 添加新地址
     * 包含地址校验、用户信息绑定及默认地址处理
     */
    @Override
    @Transactional // 写操作开启事务
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

    /**
     * 根据当前登录用户查询所有地址
     */
    @Override
    public List<Address> selectByOwnName() {
        String currentUsername = getCurrentUsername();
        return addressDao.selectByExample(currentUsername);
    }

    /**
     * 查询当前用户的默认地址
     */
    @Override
    public Address selectDefaultByOwnName() {
        String currentUsername = getCurrentUsername();
        return addressDao.selectOneByExample(currentUsername, "1");
    }

    /**
     * 更新地址信息（包含默认地址更新）
     */
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

    /**
     * 通用地址更新方法
     */
    @Override
    @Transactional
    public void update(Address address) {
        // 检查地址是否存在
        Address existingAddress = addressDao.selectByPrimaryKey(address.getId());
        if (existingAddress == null) {
            throw new BusinessException("地址不存在，无法更新");
        }
        // 验证所有权（确保用户只能修改自己的地址）
        validateAddressOwnership(existingAddress);
        addressDao.updateByPrimaryKey(address);
    }

    /**
     * 删除地址（默认地址不可删除）
     */
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
        if (address.getIsDefault()) {
            return false;
        }

        addressDao.deleteByPrimaryKey(id);
        return true;
    }

    /**
     * 将当前用户的所有地址默认值设为false
     */
    private void setZero() {
        Address defaultAddress = selectDefaultByOwnName();
        if (defaultAddress != null) {
            defaultAddress.setIsDefault(false);
            addressDao.updateByPrimaryKey(defaultAddress);
        }
    }

    /**
     * 获取当前登录用户名
     */
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
        // 地址详情校验
        if (!AddressValidationUtil.validateAddress(address.getAddressDetail())) {
            throw new BusinessException("地址格式无效，请检查详细地址是否正确");
        }

        // 收货人校验
        if (address.getConsignee() == null || StringUtils.trimWhitespace(address.getConsignee()).isEmpty()) {
            throw new BusinessException("收货人姓名不能为空");
        }

        // 手机号校验
        if (address.getPhone() == null || !address.getPhone().matches("^1[3-9]\\d{9}$")) {
            throw new BusinessException("请输入正确的手机号码");
        }
    }

    /**
     * 处理默认地址逻辑（如果设为默认，先清除其他默认地址）
     */
    private void handleDefaultAddress(Address address) {
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            setZero();
        }
    }

    /**
     * 验证地址所有权（确保当前用户只能操作自己的地址）
     */
    private void validateAddressOwnership(Address address) {
        String currentUsername = getCurrentUsername();
        if (!currentUsername.equals(address.getOwnName())) {
            throw new BusinessException("无权操作该地址");
        }
    }
}