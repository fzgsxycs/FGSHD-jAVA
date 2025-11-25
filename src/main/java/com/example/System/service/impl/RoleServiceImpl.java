package com.example.System.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.System.entity.Role;
import com.example.System.mapper.RoleMapper;
import com.example.System.service.RoleService;
import com.example.System.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        List<Long> roleIds = userRoleService.getRoleIdsByUserId(userId);
        if (roleIds.isEmpty()) {
            return List.of();
        }
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", roleIds);
        return list(queryWrapper);
    }

    @Override
    public Role getByRoleCode(String roleCode) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_code", roleCode);
        return getOne(queryWrapper);
    }
}