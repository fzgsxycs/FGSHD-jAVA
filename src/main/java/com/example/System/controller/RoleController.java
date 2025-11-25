package com.example.System.controller;

import com.example.System.annotation.RequirePermission;
import com.example.System.common.Result;
import com.example.System.entity.Role;
import com.example.System.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@Tag(name = "角色管理", description = "角色相关接口")
@RequirePermission(roles = {"admin"})
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Operation(summary = "获取所有角色")
    @GetMapping("/list")
    public Result<List<Role>> getAllRoles() {
        return Result.success(roleService.list());
    }

    @Operation(summary = "创建角色")
    @PostMapping("/create")
    public Result<String> createRole(@RequestBody Role role) {
        boolean success = roleService.save(role);
        if (success) {
            return Result.success("角色创建成功");
        } else {
            return Result.error("角色创建失败");
        }
    }

    @Operation(summary = "更新角色")
    @PutMapping("/update/{id}")
    public Result<String> updateRole(@PathVariable Long id, @RequestBody Role role) {
        role.setId(id);
        boolean success = roleService.updateById(role);
        if (success) {
            return Result.success("角色更新成功");
        } else {
            return Result.error("角色更新失败");
        }
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteRole(@PathVariable Long id) {
        boolean success = roleService.removeById(id);
        if (success) {
            return Result.success("角色删除成功");
        } else {
            return Result.error("角色删除失败");
        }
    }
}