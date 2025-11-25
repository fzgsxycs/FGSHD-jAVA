package com.example.System.controller;

import com.example.System.annotation.RequirePermission;
import com.example.System.common.Result;
import com.example.System.entity.Permission;
import com.example.System.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@Tag(name = "权限管理", description = "权限相关接口")
@RequirePermission(roles = {"admin"})
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Operation(summary = "获取所有权限")
    @GetMapping("/list")
    public Result<List<Permission>> getAllPermissions() {
        return Result.success(permissionService.list());
    }

    @Operation(summary = "创建权限")
    @PostMapping("/create")
    public Result<String> createPermission(@RequestBody Permission permission) {
        boolean success = permissionService.save(permission);
        if (success) {
            return Result.success("权限创建成功");
        } else {
            return Result.error("权限创建失败");
        }
    }

    @Operation(summary = "更新权限")
    @PutMapping("/update/{id}")
    public Result<String> updatePermission(@PathVariable Long id, @RequestBody Permission permission) {
        permission.setId(id);
        boolean success = permissionService.updateById(permission);
        if (success) {
            return Result.success("权限更新成功");
        } else {
            return Result.error("权限更新失败");
        }
    }

    @Operation(summary = "删除权限")
    @DeleteMapping("/delete/{id}")
    public Result<String> deletePermission(@PathVariable Long id) {
        boolean success = permissionService.removeById(id);
        if (success) {
            return Result.success("权限删除成功");
        } else {
            return Result.error("权限删除失败");
        }
    }
}