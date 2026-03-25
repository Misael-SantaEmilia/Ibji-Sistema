@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo ============================================
echo    IBJI - Sistema de Gestão Eclesiástica
echo    Script de Setup para Windows
echo ============================================
echo.

:: Cores não funcionam bem no CMD, então usaremos texto simples

echo === Verificando Pré-requisitos ===
echo.

:: Verificar Java
java -version >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] Java encontrado
) else (
    echo [ERRO] Java não encontrado. Instale Java 21+.
)

:: Verificar Node.js
node -v >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] Node.js encontrado
) else (
    echo [ERRO] Node.js não encontrado. Instale Node.js 18+.
)

:: Verificar npm
npm -v >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] npm encontrado
) else (
    echo [ERRO] npm não encontrado.
)

:: Verificar PostgreSQL
psql --version >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] PostgreSQL encontrado
) else (
    echo [AVISO] psql não encontrado no PATH.
)

echo.
echo === Verificando Portas ===
echo.

:: Verificar porta 8080
netstat -an | findstr ":8080" | findstr "LISTENING" >nul 2>&1
if %errorlevel% equ 0 (
    echo [AVISO] Porta 8080 já está em uso
) else (
    echo [OK] Porta 8080 disponível
)

:: Verificar porta 3000
netstat -an | findstr ":3000" | findstr "LISTENING" >nul 2>&1
if %errorlevel% equ 0 (
    echo [AVISO] Porta 3000 já está em uso
) else (
    echo [OK] Porta 3000 disponível
)

echo.
echo === Configuração do Banco de Dados ===
echo.

set /p CREATE_DB="Deseja criar o banco de dados? (S/N): "
if /i "%CREATE_DB%"=="S" (
    echo.
    set /p PG_USER="Usuário PostgreSQL [postgres]: "
    if "!PG_USER!"=="" set PG_USER=postgres
    
    set /p PG_PASS="Senha do PostgreSQL: "
    
    echo Criando banco de dados...
    
    :: Criar banco
    set PGPASSWORD=!PG_PASS!
    psql -U !PG_USER! -c "CREATE DATABASE db_ibji;" 2>nul
    
    if %errorlevel% equ 0 (
        echo [OK] Banco de dados criado!
        
        echo Executando schema...
        psql -U !PG_USER! -d db_ibji -f ibji-api\src\main\resources\schema.sql
        
        if %errorlevel% equ 0 (
            echo [OK] Tabelas criadas com sucesso!
        ) else (
            echo [ERRO] Erro ao criar tabelas.
        )
    ) else (
        echo [AVISO] Banco pode já existir ou erro na criação.
    )
)

echo.
echo === Configuração do Backend ===
echo.

set /p CONFIG_YML="Deseja configurar o application.yml? (S/N): "
if /i "%CONFIG_YML%"=="S" (
    echo Configurando application.yml...
    
    set /p DB_PASS="Senha para o backend [postgres]: "
    if "!DB_PASS!"=="" set DB_PASS=postgres
    
    :: Criar application.yml usando echo
    (
        echo server:
        echo   port: 8080
        echo.
        echo spring:
        echo   application:
        echo     name: ibji-api
        echo.
        echo   datasource:
        echo     url: jdbc:postgresql://localhost:5432/db_ibji
        echo     username: !PG_USER!
        echo     password: !DB_PASS!
        echo     driver-class-name: org.postgresql.Driver
        echo.
        echo   jpa:
        echo     hibernate:
        echo       ddl-auto: validate
        echo     show-sql: true
        echo     properties:
        echo       hibernate:
        echo         format_sql: true
        echo         dialect: org.hibernate.dialect.PostgreSQLDialect
        echo.
        echo   jackson:
        echo     default-property-inclusion: non_null
        echo     serialization:
        echo       write-dates-as-timestamps: false
        echo     deserialization:
        echo       fail-on-unknown-properties: false
        echo.
        echo logging:
        echo   level:
        echo     com.ibji.app: DEBUG
        echo     org.springframework.web: INFO
        echo.
        echo springdoc:
        echo   api-docs:
        echo     path: /api-docs
        echo   swagger-ui:
        echo     path: /swagger-ui.html
    ) > ibji-api\src\main\resources\application.yml
    
    echo [OK] application.yml configurado!
)

echo.
echo === Instalando Dependências do Frontend ===
echo.

cd ibji-web
call npm install
cd ..

echo.
echo ============================================
echo           Setup Concluído!
echo ============================================
echo.
echo Para iniciar o projeto:
echo.
echo 1. Backend (Terminal 1):
echo    cd ibji-api
echo    mvnw.cmd spring-boot:run
echo.
echo 2. Frontend (Terminal 2):
echo    cd ibji-web
echo    npm start
echo.
echo URLs:
echo   - Backend API: http://localhost:8080
echo   - Swagger UI: http://localhost:8080/swagger-ui.html
echo   - Frontend: http://localhost:3000
echo.
echo Credenciais padrão:
echo   - Email: admin@ibji.org.br
echo   - Senha: admin123
echo.
echo ============================================

pause