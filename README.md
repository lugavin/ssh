# ssh
- 项目基于 [Dubbo](http://dubbo.io/) 框架来构建分布式服务，前端基于 [Thymeleaf](http://www.thymeleaf.org/)模板技术以取代JSP，并采用了 [Bootstrap](http://getbootstrap.com/) 框架，后端采用了 [SpringMVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html)、[Spring](https://spring.io/)、[JPA](https://docs.spring.io/spring-data/jpa/docs/1.6.0.RELEASE/reference/html/jpa.repositories.html)、[Shiro](https://shiro.apache.org/)、[Dubbo](http://dubbo.io/) 等技术；
- 项目已实现了权限管理、审计日志（字段级别）等基本功能；  
- 项目实现了通用的服务端参数合法性校验功能并模仿 [MyBatis](http://www.mybatis.org/mybatis-3/zh/) 实现了DAO代理开发和动态 SQL/HQL 查询功能。  

### 服务端参数校验示例
``` java
public interface UserService {
    
    String BEAN_NAME = "userService";

    void add(@NotNull @Validated({Groups.Add.class}) UserDTO dto);

    void add(@Size(min = 1) @Validated({Groups.Add.class}) Collection<UserDTO> collection);

    void update(@NotNull @Validated({Groups.Update.class}) UserDTO dto);

    void delete(@NotNull Long id);

    void delete(@Size(min = 1) Long[] ids);

    UserDTO getById(@NotNull Long id);
    
}  
```  

### DAO代理开发示例
``` java
@RepositoryBean
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    String BEAN_NAME = "userRepository";

    @Query
    UserEntity getUserByCode(@Param("code") String code);

    @Query(transformer = UserDTO.class)
    List<UserDTO> getUserList(UserDTO userDTO);

    @Query(transformer = Map.class)
    List<Map> getUserList(ModelMapDTO modelMapDTO);

    @Query(value = "getUserList", transformer = Map.class)
    Page<Map> getUserPage(Pageable pageable);

}
```

### 动态 SQL/HQL 查询示例
``` xml
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE mapper PUBLIC
        "-//hibernate.org//DTD Mapper 1.0//EN"
        "http://hibernate.org/dtd/mapper-1.0.dtd">

<mapper namespace="com.ssh.sys.core.repository.UserRepository">

    <hql id="getUserByCode">
        <![CDATA[ FROM UserEntity WHERE code = :code ]]>
    </hql>

    <sql id="getUserList">
        <![CDATA[
            SELECT id AS id,
                   code AS code,
                   name AS name,
                   pass AS pass,
                   salt AS salt,
                   status AS status
              FROM sys_user
              <@where>
                  <#if code?? && code!=''>
                   AND code = :code
                  </#if>
                  <#if name?? && name!=''>
                   AND name LIKE CONCAT('%',:name,'%')
                  </#if>
              </@where>
        ]]>
    </sql>

</mapper>
```  
