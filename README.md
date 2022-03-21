# simple-bg-cli
a simple background cli

## introduction
This project implements a basic backend web system by Spring framework,which include basic user register and login module,and a basic RBAC authority system.You can extend development your business based on this framework easier, more config and rules will show you in follow sections.

## backend stack
Basically we use Spring buckets as system main middleware,like SpringBoot, Spring MVC, Spring Security, Spring Cloud, Spring Data JPA. We use Hibernate as system persistence framework so that we could avoid create and update database tables manually. About the database and cache we use MySQL and Redis, which was opensource, common, popular in software industry. You can write your project config statically with code, or dynamically create and update config in config center(Nacos). Meanwhile, we join Lombok and Guava to help your development more fast. 

Components edition that we use as follows:

| component | edition |
| :--: | :--: |
| Spring | 5.2.15.RELEASE |
| SpringBoot | 2.3.12.RELEASE |
| SpringSecurity | 5.3.9.RELEASE |
| SpringCloud | Hoxton.SR12 |
| Thymeleaf | 3.0.12.RELEASE |
| Swagger2 | 3.0.0 |
| Nacos-client | 2.0.2 |
| Hibernate | 5.4.32.Final |
| Querydsl | 5.0.0 |
| Guava | 31.0.1-jre |
| Gson | 2.8.9 |
| Lombok | 1.18.22 |

We suggest you use middlewares as following edition:

| middleware | edition |
| :--: | :--: |
| jdk | 1.8 |
| mysql | 8.0.28 |
| redis | 6.2.6 |
| nacos | 2.0.4 |