package com.example.System.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.System.entity.ExceptionLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 异常日志Mapper
 * 
 * @author System
 */
@Mapper
public interface ExceptionLogMapper extends BaseMapper<ExceptionLog> {
}