#!/bin/bash

# 异常处理机制测试脚本 (使用curl)
# 用于测试Spring Boot应用程序的异常处理机制

# 服务器基础URL
BASE_URL="http://localhost:8080"

echo "异常处理机制测试脚本"
echo "请确保Spring Boot应用程序正在运行在 $BASE_URL"
echo ""

# 测试函数
test_endpoint() {
    local endpoint=$1
    local description=$2
    
    echo "========================================"
    echo "测试: $description"
    echo "端点: $endpoint"
    echo "========================================"
    
    # 发送请求并格式化输出
    response=$(curl -s -w "\nHTTP_CODE:%{http_code}" "$BASE_URL$endpoint")
    http_code=$(echo "$response" | grep -o 'HTTP_CODE:[0-9]*' | cut -d: -f2)
    body=$(echo "$response" | sed -e 's/HTTP_CODE:[0-9]*$//')
    
    echo "状态码: $http_code"
    echo "响应内容:"
    
    # 尝试格式化JSON
    if command -v jq &> /dev/null; then
        echo "$body" | jq . 2>/dev/null || echo "$body"
    else
        echo "$body"
    fi
    
    echo ""
    echo ""
}

# 测试自定义异常
test_endpoint "/exception-test/business?withData=false" "业务异常(无数据)"
test_endpoint "/exception-test/business?withData=true" "业务异常(带数据)"
test_endpoint "/exception-test/auth" "认证异常"
test_endpoint "/exception-test/system" "系统异常"

# 测试Java内置异常
test_endpoint "/exception-test/nullpointer" "空指针异常"
test_endpoint "/exception-test/array" "数组越界异常"
test_endpoint "/exception-test/numberformat?number=abc" "数字格式异常"
test_endpoint "/exception-test/arithmetic" "算术异常"
test_endpoint "/exception-test/classcast" "类型转换异常"
test_endpoint "/exception-test/illegal-argument?value=" "IllegalArgumentException(空参数)"
test_endpoint "/exception-test/illegal-argument?value=test" "IllegalArgumentException(有效参数)"

echo "========================================"
echo "所有测试完成!"
echo "========================================"

echo ""
echo "异常处理机制测试总结:"
echo "1. 检查所有异常是否返回了适当的HTTP状态码"
echo "2. 检查响应格式是否一致"
echo "3. 检查是否有追踪ID(traceId)"
echo "4. 检查异常日志是否正确记录到数据库"
echo ""
echo "您可以查看数据库中的sys_exception_log表来验证异常日志记录"