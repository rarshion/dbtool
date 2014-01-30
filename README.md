dbtool
=====
比较不同数据源间表结构差异的工具（主要用于本地开发环境，测试环境和线上环境的表结构差异比较）

要点备忘
1. 根据mysql的information_schema中的信息获取数据库表结构的信息
2. 在程序中向spring的applicationContext中动态注册bean
