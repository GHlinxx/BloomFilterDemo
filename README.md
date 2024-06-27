# BloomFilterDemo
个人写的玩的一个小Demo
 主要涉及到通过Lua脚本批量插入数据到布隆过滤器，PIP管道批量插入（暂时还有问题）

## <font color=#FFD700>一、项目概述</font>

```
SpringBoot2.5.4 + Maven3.8.4 +Redis +lombok(插件)+Stopwatch(计时工具)
```
## <font color=#FFD700>二、项目实现功能</font>

#### 1对以下方法进行性能测试比较

   1）、List的 contains 方法

   2）、Map的 containsKey 方法

   3）、Google布隆过滤器 mightContain 方法

#### 2、SpringBoot整合Redis布隆过滤器

实现通过Lua脚本批量插入数据到redis布隆过滤器

并判断当前key值在redis布隆过滤器中是否存在。

实际开发过程中，如果涉及到批量插入数据到Redis服务器，那么就可以考虑通过Lua脚本实现。
