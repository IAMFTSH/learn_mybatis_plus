package com.learn.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.mybatisplus.mapper.UserMapper;
import com.learn.mybatisplus.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Test
    void contextLoads() {
        //wrapper是条件构造器
        List<User> userList=userMapper.selectList(null);
        System.out.println(userList);
        System.out.println();
        userList.forEach(System.out::println);

     }
     @Test  //测试插入
    public void testInsert(){
        User user=new User();
        user.setAge(5);
        user.setName("ls");
        user.setEmail("111@qq.com");
        int result=userMapper.insert(user);
        System.out.println(result);
    }
    @Test//测试更新
    public void testUpdate(){
        User user=new User();
        user.setId(7L);
        user.setAge(5);
        user.setName("zzzzzzzzzzzzz");
        user.setEmail("111@qq.com");
        int result=userMapper.updateById(user);
        System.out.println(result);
    }

    @Test //乐观琐测试
    public void testOptimisticLocker(){
        User user=userMapper.selectById(1L);
        System.out.println(user);
        user.setName("ggggg");
        System.out.println(userMapper.updateById(user));;
        user=userMapper.selectById(1L);
        System.out.println(user);
    }

    @Test //乐观琐插入失败测试
    public void testFailureUpdateOptimisticLocker(){
        User user=userMapper.selectById(1L);
        user.setName("ttttt");

        User user2=userMapper.selectById(1L);
        user2.setName("yyyyy");
        userMapper.updateById(user2);
        userMapper.updateById(user);
        //user更新失败  user2更新后，version+1。user更新失败，因为他获取的version是老版本的version，如果需要让user更新上去 需要使用自旋琐解决（什么东西）
    }

    @Test          //单个查询
    public void testSelect(){
        User user=userMapper.selectById(1L);
        System.out.println(user);
    }
    @Test          //sql中id in （1，2，3）查询
    public void testSelectByBeachId(){
        List<User> user=userMapper.selectBatchIds(Arrays.asList(1L,2L,3L));
        //相当与的sql语句：SELECT id,name,age,email,create_time,update_time,version FROM user WHERE id IN ( ? , ? , ? )
        user.forEach(System.out::println);
    }
    @Test          //多条件查询查询  简陋版   name=？  只能用等号  不能使用大于小于符号
    public void testSelectByMapId(){
        Map<String,Object> map=new HashMap<>();
        map.put("name","ls");
        map.put("age",5);
        //
        List<User> user=userMapper.selectByMap(map);
        //相当与的sql语句：SELECT id,name,age,email,create_time,update_time,version FROM user WHERE name = ? AND age = ?
        //SELECT id,name,age,email,create_time,update_time,version FROM user 如果map没有值，就相当于查询全部selectLists
        user.forEach(System.out::println);
    }

    @Test //分页查询
    public void testPage(){
        Page<User> userPage=new Page<>(1,5); //arg1  当前页    arg2   页大小
        userMapper.selectPage(userPage,null);
        //相当与的sql语句：SELECT id,name,age,email,create_time,update_time,version FROM user LIMIT ?,?
        userPage.getRecords().forEach(System.out::println);
        System.out.println("返回总数据数"+ userPage.getTotal());
        System.out.println("是否还有下一页"+ userPage.hasNext());

        //问题 如何在分页操作中添加条件查询
    }

    @Test //测试删除  其实和select差不多       需要逻辑删除需要配置
    public void testDelete(){
        //UPDATE user SET deleted=1 WHERE id=? AND deleted=0
        System.out.println("删除单个"+userMapper.deleteById(1L));
        //UPDATE user SET deleted=1 WHERE id IN ( ? , ? ) AND deleted=0
        System.out.println("删除多个"+userMapper.deleteBatchIds(Arrays.asList(3L,2L)));

        //问题 如何解决版本指向
    }
}
