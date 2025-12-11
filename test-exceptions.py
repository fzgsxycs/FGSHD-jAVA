#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
异常处理机制测试脚本
用于测试Spring Boot应用程序的异常处理机制
"""

import requests
import json
import sys

# 服务器基础URL
BASE_URL = "http://localhost:8080"

def test_exception_endpoint(endpoint, description):
    """测试异常端点"""
    print(f"\n{'='*50}")
    print(f"测试: {description}")
    print(f"端点: {endpoint}")
    print(f"{'='*50}")
    
    try:
        response = requests.get(f"{BASE_URL}{endpoint}")
        
        print(f"状态码: {response.status_code}")
        
        try:
            data = response.json()
            print(f"响应内容:\n{json.dumps(data, indent=2, ensure_ascii=False)}")
            
            # 检查是否有traceId
            if 'traceId' in data:
                print(f"\n追踪ID: {data['traceId']}")
                
                # 可以在数据库中查询此traceId的异常日志
                print("提示: 您可以在数据库中查询此traceId的异常日志记录")
        except json.JSONDecodeError:
            print(f"响应内容 (非JSON): {response.text}")
            
    except requests.exceptions.ConnectionError:
        print(f"错误: 无法连接到服务器 {BASE_URL}")
        print("请确保Spring Boot应用程序正在运行")
        return False
    
    return True

def main():
    print("异常处理机制测试脚本")
    print("请确保Spring Boot应用程序正在运行在 http://localhost:8080")
    
    # 测试自定义异常
    test_cases = [
        ("/exception-test/business?withData=false", "业务异常(无数据)"),
        ("/exception-test/business?withData=true", "业务异常(带数据)"),
        ("/exception-test/auth", "认证异常"),
        ("/exception-test/system", "系统异常"),
        ("/exception-test/nullpointer", "空指针异常"),
        ("/exception-test/array", "数组越界异常"),
        ("/exception-test/numberformat?number=abc", "数字格式异常"),
        ("/exception-test/arithmetic", "算术异常"),
        ("/exception-test/classcast", "类型转换异常"),
        ("/exception-test/illegal-argument?value=", "IllegalArgumentException(空参数)"),
        ("/exception-test/illegal-argument?value=test", "IllegalArgumentException(有效参数)"),
    ]
    
    server_connected = False
    
    for endpoint, description in test_cases:
        if not server_connected:
            # 第一次连接时检查服务器是否可用
            if not test_exception_endpoint(endpoint, description):
                print("\n无法连接到服务器，退出测试")
                sys.exit(1)
            server_connected = True
        else:
            test_exception_endpoint(endpoint, description)
    
    print(f"\n{'='*50}")
    print("所有测试完成!")
    print(f"{'='*50}")
    
    print("\n异常处理机制测试总结:")
    print("1. 检查所有异常是否返回了适当的HTTP状态码")
    print("2. 检查响应格式是否一致")
    print("3. 检查是否有追踪ID(traceId)")
    print("4. 检查异常日志是否正确记录到数据库")
    print("\n您可以查看数据库中的sys_exception_log表来验证异常日志记录")

if __name__ == "__main__":
    main()