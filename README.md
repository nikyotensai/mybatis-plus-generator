# 描述

mybatis-plus代码生成器

# 项目地址

[Github](https://github.com/nikyotensai/mybatis-plus-generator.git)
| [Gitee](https://gitee.com/nikyotensai/mybatis-plus-generator.git)

# 使用方式

在plugins下添加：

```xml

<plugin>
    <groupId>com.github.nikyotensai</groupId>
    <artifactId>mybatis-plus-generator</artifactId>
    <version>1.0.1</version>
    <configuration>
        <configurationFile>${basedir}/src/main/resources/generatorConfig.yml</configurationFile>
    </configuration>
</plugin>
```

## plugin配置说明

- configurationFile:配置文件路径

# generatorConfig文件说明

属性|说明
---|---
fileOverride|是否覆盖文件（对entity以外文件生效）建议false
dataSource.driverName|驱动
dataSource.url|数据源url
dataSource.username|数据源用户名
dataSource.password|数据源密码
strategy.naming|生成策略
strategy.tablePrefix|生成的表前缀
strategy.idGenType|主键策略（`mybatis-plus`的IdType）
strategy.superEntityClass|Entity父类全名称（例：com.github.nikyotensai.entity.BaseEntity）
packageInfo.parent|package父包
packageInfo.dao|DAO目录
packageInfo.entity|entity目录
packageInfo.mapper|mapper目录
packageInfo.xml|xml目录

## 示例文件

[generatorConfig.yml](/src/main/resources/generatorConfig.yml)（注：建议将该文件移出git管理）

## 示例项目

具体使用方式可以参考[mybatis-plus-generator-example](https://github.com/nikyotensai/mybatis-plus-generator-example.git)
