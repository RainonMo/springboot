# myworld-backend
一个集导航、博客、商城等功能的平台。

## 运行

1.添加数据库表

2.配置application.yml

3.运行

4.打开api接口文档测试

5.运行前端

## 目录结构

- sql 数据库文件
- src 资源
    - main
        - java
            - common 公共类
            - config 配置类
            - exception 报错类
            - controller
            - mapper
            - model
            - service
            - utils
            - MainApplication.java
        - resources 资源
            - mapper
            - application.yml
    - test
- pom.xml Maven配置文件
- Dockerfile docker部署文件

## 项目模块

### 用户模块

- 获取当前登录用户
- 登录
- 注册
- 注销
- 更新个人信息
- 创建用户（仅管理员）
- 删除用户（仅管理员）
- 编辑用户（仅管理员）
- 根据 id 获取用户（仅管理员）
- 根据 id 获取包装类
- 分页获取用户列表（仅管理员）
- 分页获取用户封装列表

### 博客模块

#### 分类

#### 博客

- 仅管理员可以创建分类
- 仅管理员可以删除分类
- 仅管理员可以修改分类
- 获取所有分类数据
- 获取所属分类的文章分页数据
- 创建文章
- 删除文章
- 编辑文章
- 根据 id 获取文章
- 获取当前用户的文章列表

### bi模块

- 智能分析（同步）
- 创建
- 删除
- 更新（仅管理员）
- 根据 id 获取
- 分页获取列表（封装类）
- 分页获取当前用户创建的资源列表

### 答题模块

#### 应用

#### 题目

#### 评分

#### 答题

### 商城模块

每日爆品推荐api，https://www.dataoke.com/pmc/api-d.html?id=34

1.程序引入SDK

IDEA：file-new-project from existing sources...

2.配置api密钥
```yaml
dataoke:
  appKey: 665ed....
  appSecret: d30d....

```

## 数据删除逻辑

1.字段为 isDelete 的默认为逻辑删除

```yml
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: false
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: isDelete # 全局逻辑删除的实体字段名
      logic-delete-value: 1 # 逻辑已删除值（默认为 1）
      logic-not-delete-value: 0 # 逻辑未删除值（默认为 0）
```

比如：Chart.java，图表的删除字段是 isDelete 为逻辑删除

```Java
/**
     * 是否删除
     */
    private Integer isDelete;

```

2.添加@TableLogic 的注解为逻辑删除

比如：TCategory.java，删除字段不是默认删除字段，但字段添加了注解，分类为逻辑删除。

```Java
 /**
     * 删除标志位：0：未删除 1：已删除
     */
    @TableLogic
    private Integer is_deleted;
```


3.其他为直接删除


比如：TArticle.java，删除字段不是默认删除字段，也无注解，文章为直接删除。

```java

    /**
     * 删除标志位：0：未删除 1：已删除
     */
    private Integer is_deleted;
```



