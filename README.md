Day01:    

跟着黑马程序员的java项目《瑞吉外卖》实战，一共14天。这个用的包是mybatis-plus:    
1.所以可以直接baseMapper对数据库进行增删改查。   
2.直接用ServiceImpl对数据库进行增删改查。    
3.用LamadaQueryWrapper查询数据库的对象。    

Mapper（DAO）层：    
职责： Mapper 层主要负责与数据库的交互，包括执行 SQL、封装结果等。    
使用场景： Mapper 主要用于处理数据库相关的操作，例如 CRUD 操作、复杂查询等。    

Service 层：    
职责： Service 层主要负责业务逻辑的处理，事务管理，以及对多个 Mapper 方法的组合和调用。    
使用场景： Service 通常包含了更高层次的业务逻辑，它可能需要调用多个 Mapper 的方法，进行事务控制、组合业务逻辑等。    

3种web会话管理的方式    
