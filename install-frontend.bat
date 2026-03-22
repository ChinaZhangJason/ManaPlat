@echo off
REM 安装前端依赖脚本 (Windows)

cd /d "%~dp0\frontend"

echo 安装前端依赖...
npm install

if %ERRORLEVEL% EQU 0 (
    echo.
    echo 安装成功!
    echo 运行: npm run dev
) else (
    echo.
    echo 安装失败，请检查错误信息
)

pause
