package com.example.System.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.System.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}