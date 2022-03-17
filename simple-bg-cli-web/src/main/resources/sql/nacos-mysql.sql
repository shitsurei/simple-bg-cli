-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: 47.105.65.21    Database: nacos
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `config_info`
--

DROP TABLE IF EXISTS `config_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `c_use` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `effect` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `c_schema` text COLLATE utf8_bin,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='config_info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info`
--

LOCK TABLES `config_info` WRITE;
/*!40000 ALTER TABLE `config_info` DISABLE KEYS */;
INSERT INTO `config_info` VALUES (1,'cli-common.yaml','CLI_GROUP','server:\n  port: 17777\n  servlet:\n    context-path: /cli\n\n# springboot\nspring:\n  # 服务名\n  application:\n    name: CLI\n  main:\n    # 允许循环依赖\n    allow-circular-references: true\n  # 国际化\n  messages:\n    basename: static.i18n.message\n    cache-duration: 3600\n    encoding: utf-8\n  # mvc\n  mvc:\n    pathmatch:\n      # Springfox使用的路径匹配是基于AntPathMatcher,Spring Boot 2.6.X使用的是PathPatternMatcher\n      matching-strategy: ant_path_matcher\n  # 邮件\n  mail:\n    host: smtp.163.com\n    protocol: smtp\n    port: 465\n    username: xxx@163.com\n    password: xxx\n    default-encoding: UTF-8\n  thymeleaf:\n    enabled: true\n    mode: HTML\n    encoding: UTF-8\n    # 模板存放在资源目录的 templates/ 文件下\n    prefix: classpath:/templates/\n    suffix: .html\n    check-template-location: true\n    check-template: false\n    cache: false\n\n# 日志配置文件\nlogging:\n  config: classpath:cli-log-config.xml\n# 日志存储路径\nlog:\n  path: D:/cli/log','07e4183b5a0a5611784020a1603322c2','2022-03-09 07:24:24','2022-03-14 12:30:15','nacos','117.22.122.136','','cli-dev','','','','yaml',''),(2,'cli-data.yaml','CLI_GROUP','# springboot\r\nspring:\r\n  # JPA配置\r\n  jpa:\r\n    database: mysql\r\n    generate-ddl: true\r\n    open-in-view: true\r\n    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect\r\n    hibernate:\r\n      ddl-auto: update\r\n      use-new-id-generator-mappings: true\r\n      naming:\r\n        # 隐式名称命名策略\r\n        implicit-strategy: org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy\r\n        # 物理名称命名策略\r\n        # 表名，字段为小写，当有大写字母的时候会添加下划线分隔符号\r\n        physical-strategy: org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy\r\n    # hibernate原生配置\r\n    properties:\r\n      hibernate:\r\n        format_sql: true\r\n  # redis\r\n  redis:\r\n    host: 127.0.0.1\r\n    port: 6379\r\n    database: 7\r\n    password:\r\n  # 数据库\r\n  datasource:\r\n    url: jdbc:mysql://127.0.0.1:3306/cli?serverTimezone=UTC&useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=utf8\r\n    username: root\r\n    password: mysql\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    # Hikari 连接池配置\r\n    hikari:\r\n      # 最小空闲连接数量\r\n      minimum-idle: 5\r\n      # 空闲连接存活最大时间，默认600000（10分钟）\r\n      idle-timeout: 180000\r\n      # 连接池最大连接数，默认是10\r\n      maximum-pool-size: 10\r\n      # 此属性控制从池返回的连接的默认自动提交行为,默认值：true\r\n      auto-commit: true\r\n      # 连接池名称\r\n      pool-name: MyHikariCP\r\n      # 此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认1800000即30分钟\r\n      max-lifetime: 1800000\r\n      # 数据库连接超时时间,默认30秒，即30000\r\n      connection-timeout: 30000\r\n      connection-test-query: SELECT 1','af74e9dad96c002774f8206cd634a718','2022-03-09 07:24:24','2022-03-09 07:24:24',NULL,'124.115.229.243','','cli-dev',NULL,NULL,NULL,'yaml',NULL),(3,'cli-file.yaml','CLI_GROUP','# springboot\nspring:\n  servlet:\n    # 文件上传\n    multipart:\n      # 单文件最大限制（10mb）\n      max-file-size: 10MB\n      # 单请求最大限制（100mb）\n      max-request-size: 100MB\n      # 文件写入磁盘的阈值\n      file-size-threshold: 2\n# 自定义配置\ncustom:\n  # 文件路径\n  file:\n    # 存储路径\n    path:\n      win: D:/cli/file\n      linux: /data/cli/file\n    # 实现类\n    impl:','f40e7a75c1a9546d5a2df74a0478ad3a','2022-03-09 07:24:24','2022-03-09 07:29:53','nacos','124.115.229.243','','cli-dev','','','','yaml',''),(4,'cli-custom.yaml','CLI_GROUP','# 自定义配置\ncustom:\n  test-config: abcd\n  domain: http://127.0.0.1:17777\n  login:\n    failed:\n      top: 10\n      gap: 36000\n  admin:\n    account: admin\n    password: 123456\n  data:\n    init: true\n  jwt:\n    # 有效期1天(单位:s)\n    expire: 5184000\n    # secret: 秘钥(普通字符串) 不能太短，太短可能会导致报错\n    secret: 69c4918f239d31d6625abfa55a3763bc\n    # 签发者\n    issuer: cli\n  # 验证码\n  captcha:\n    enable: false\n    # 长度（px）\n    width: 100\n    # 高度（px）\n    height: 30\n    # 过期时间（秒）\n    expireSecond: 60\n  rsa:\n    enable: false','ed2034e95a87618e9e2260f406d85cf7','2022-03-09 07:24:24','2022-03-15 09:57:51','nacos','124.115.229.243','','cli-dev','','','','yaml',''),(5,'cli-common.yaml','CLI_GROUP','server:\n  port: 17777\n  servlet:\n    context-path: /cli\n\n# springboot\nspring:\n  # 服务名\n  application:\n    name: CLI\n  main:\n    # 允许循环依赖\n    allow-circular-references: true\n  # 国际化\n  messages:\n    basename: static.i18n.message\n    cache-duration: 3600\n    encoding: utf-8\n  # mvc\n  mvc:\n    pathmatch:\n      # Springfox使用的路径匹配是基于AntPathMatcher,Spring Boot 2.6.X使用的是PathPatternMatcher\n      matching-strategy: ant_path_matcher\n  # 邮件\n  mail:\n    host: smtp.sina.com\n    protocol: smtp\n    port: 465\n    username: clidata@sina.com\n    password: f02ccc07207c65f0\n    default-encoding: UTF-8\n  thymeleaf:\n    enabled: true\n    mode: HTML\n    encoding: UTF-8\n    # 模板存放在资源目录的 templates/ 文件下\n    prefix: classpath:/templates/\n    suffix: .html\n    check-template-location: true\n    check-template: false\n    cache: false\n\n# 日志配置文件\nlogging:\n  config: classpath:cli-log-config.xml\n# 日志存储路径\nlog:\n  path: D:/cli/log','33b0da171f0e9b92befd5e88bc7cd7a5','2022-03-09 07:26:09','2022-03-14 12:31:09','nacos','117.22.122.136','','cli-test','','','','yaml',''),(6,'cli-data.yaml','CLI_GROUP','# springboot\nspring:\n  # JPA配置\n  jpa:\n    database: mysql\n    generate-ddl: true\n    open-in-view: true\n    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect\n    hibernate:\n      ddl-auto: update\n      use-new-id-generator-mappings: true\n      naming:\n        # 隐式名称命名策略\n        implicit-strategy: org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy\n        # 物理名称命名策略\n        # 表名，字段为小写，当有大写字母的时候会添加下划线分隔符号\n        physical-strategy: org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy\n    # hibernate原生配置\n    properties:\n      hibernate:\n        format_sql: true\n  # redis\n  redis:\n    host: 47.105.65.21\n    port: 6379\n    database: 7\n    password: 1992068Zwy!\n  # 数据库\n  datasource:\n    url: jdbc:mysql://47.105.65.21:3306/cli?serverTimezone=UTC&useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=utf8\n    username: root\n    password: 1992068Zwy!\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    # Hikari 连接池配置\n    hikari:\n      # 最小空闲连接数量\n      minimum-idle: 5\n      # 空闲连接存活最大时间，默认600000（10分钟）\n      idle-timeout: 180000\n      # 连接池最大连接数，默认是10\n      maximum-pool-size: 10\n      # 此属性控制从池返回的连接的默认自动提交行为,默认值：true\n      auto-commit: true\n      # 连接池名称\n      pool-name: MyHikariCP\n      # 此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认1800000即30分钟\n      max-lifetime: 1800000\n      # 数据库连接超时时间,默认30秒，即30000\n      connection-timeout: 30000\n      connection-test-query: SELECT 1','f894711d81a2c8db7c3a19ffec6c8642','2022-03-09 07:26:09','2022-03-09 07:27:16','nacos','124.115.229.243','','cli-test','','','','yaml',''),(7,'cli-file.yaml','CLI_GROUP','# springboot\nspring:\n  servlet:\n    # 文件上传\n    multipart:\n      # 单文件最大限制（10mb）\n      max-file-size: 10MB\n      # 单请求最大限制（100mb）\n      max-request-size: 100MB\n      # 文件写入磁盘的阈值\n      file-size-threshold: 2\n# 自定义配置\ncustom:\n  # 文件路径\n  file:\n    # 存储路径\n    path:\n      win: D:/cli/file\n      linux: /data/cli/file\n    # 实现类\n    impl:','f40e7a75c1a9546d5a2df74a0478ad3a','2022-03-09 07:26:09','2022-03-09 07:28:13','nacos','124.115.229.243','','cli-test','','','','yaml',''),(8,'cli-custom.yaml','CLI_GROUP','# 自定义配置\ncustom:\n  test-config: abcd\n  domain: http://47.105.65.21:17777\n  login:\n    failed:\n      top: 10\n      gap: 36000\n  admin:\n    account: admin\n    password: 123456\n  data:\n    init: true\n  jwt:\n    # 有效期1天(单位:s)\n    expire: 5184000\n    # secret: 秘钥(普通字符串) 不能太短，太短可能会导致报错\n    secret: 69c4918f239d31d6625abfa55a3763bc\n    # 签发者\n    issuer: cli\n  # 验证码\n  captcha:\n    enable: true\n    # 长度（px）\n    width: 100\n    # 高度（px）\n    height: 30\n    # 过期时间（秒）\n    expireSecond: 60\n  rsa:\n    enable: true','f25ae75480a00b409832a4b833459cca','2022-03-09 07:26:09','2022-03-15 09:58:09','nacos','124.115.229.243','','cli-test','','','','yaml','');
/*!40000 ALTER TABLE `config_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_aggr`
--

DROP TABLE IF EXISTS `config_info_aggr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_aggr` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='增加租户字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_aggr`
--

LOCK TABLES `config_info_aggr` WRITE;
/*!40000 ALTER TABLE `config_info_aggr` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_aggr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_beta`
--

DROP TABLE IF EXISTS `config_info_beta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_beta` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='config_info_beta';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_beta`
--

LOCK TABLES `config_info_beta` WRITE;
/*!40000 ALTER TABLE `config_info_beta` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_beta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_tag`
--

DROP TABLE IF EXISTS `config_info_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='config_info_tag';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_tag`
--

LOCK TABLES `config_info_tag` WRITE;
/*!40000 ALTER TABLE `config_info_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_tags_relation`
--

DROP TABLE IF EXISTS `config_tags_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_tags_relation` (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='config_tag_relation';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_tags_relation`
--

LOCK TABLES `config_tags_relation` WRITE;
/*!40000 ALTER TABLE `config_tags_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_tags_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_capacity`
--

DROP TABLE IF EXISTS `group_capacity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_capacity` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_capacity`
--

LOCK TABLES `group_capacity` WRITE;
/*!40000 ALTER TABLE `group_capacity` DISABLE KEYS */;
/*!40000 ALTER TABLE `group_capacity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `his_config_info`
--

DROP TABLE IF EXISTS `his_config_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `his_config_info` (
  `id` bigint unsigned NOT NULL,
  `nid` bigint unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text COLLATE utf8_bin,
  `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `op_type` char(10) COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='多租户改造';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `his_config_info`
--

LOCK TABLES `his_config_info` WRITE;
/*!40000 ALTER TABLE `his_config_info` DISABLE KEYS */;
INSERT INTO `his_config_info` VALUES (0,1,'cli-common.yaml','CLI_GROUP','','server:\n  port: 17777\n  servlet:\n    context-path: /cli\n\n# springboot\nspring:\n  # 服务名\n  application:\n    name: CLI\n  main:\n    # 允许循环依赖\n    allow-circular-references: true\n  # 国际化\n  messages:\n    basename: static.i18n.message\n    cache-duration: 3600\n    encoding: utf-8\n  # mvc\n  mvc:\n    pathmatch:\n      # Springfox使用的路径匹配是基于AntPathMatcher,Spring Boot 2.6.X使用的是PathPatternMatcher\n      matching-strategy: ant_path_matcher\n  # 邮件\n  mail:\n    host: smtp.163.com\n    protocol: smtp\n    port: 25\n    username: xxx@163.com\n    password: xxx\n    default-encoding: UTF-8\n  thymeleaf:\n    enabled: true\n    mode: HTML\n    encoding: UTF-8\n    # 模板存放在资源目录的 templates/ 文件下\n    prefix: classpath:/templates/\n    suffix: .html\n    check-template-location: true\n    check-template: false\n    cache: false','be4eb470ff9b541d9bf100296bbba756','2022-03-09 15:24:23','2022-03-09 07:24:24',NULL,'124.115.229.243','I','cli-dev'),(0,2,'cli-data.yaml','CLI_GROUP','','# springboot\r\nspring:\r\n  # JPA配置\r\n  jpa:\r\n    database: mysql\r\n    generate-ddl: true\r\n    open-in-view: true\r\n    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect\r\n    hibernate:\r\n      ddl-auto: update\r\n      use-new-id-generator-mappings: true\r\n      naming:\r\n        # 隐式名称命名策略\r\n        implicit-strategy: org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy\r\n        # 物理名称命名策略\r\n        # 表名，字段为小写，当有大写字母的时候会添加下划线分隔符号\r\n        physical-strategy: org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy\r\n    # hibernate原生配置\r\n    properties:\r\n      hibernate:\r\n        format_sql: true\r\n  # redis\r\n  redis:\r\n    host: 127.0.0.1\r\n    port: 6379\r\n    database: 7\r\n    password:\r\n  # 数据库\r\n  datasource:\r\n    url: jdbc:mysql://127.0.0.1:3306/cli?serverTimezone=UTC&useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=utf8\r\n    username: root\r\n    password: mysql\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    # Hikari 连接池配置\r\n    hikari:\r\n      # 最小空闲连接数量\r\n      minimum-idle: 5\r\n      # 空闲连接存活最大时间，默认600000（10分钟）\r\n      idle-timeout: 180000\r\n      # 连接池最大连接数，默认是10\r\n      maximum-pool-size: 10\r\n      # 此属性控制从池返回的连接的默认自动提交行为,默认值：true\r\n      auto-commit: true\r\n      # 连接池名称\r\n      pool-name: MyHikariCP\r\n      # 此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认1800000即30分钟\r\n      max-lifetime: 1800000\r\n      # 数据库连接超时时间,默认30秒，即30000\r\n      connection-timeout: 30000\r\n      connection-test-query: SELECT 1','af74e9dad96c002774f8206cd634a718','2022-03-09 15:24:23','2022-03-09 07:24:24',NULL,'124.115.229.243','I','cli-dev'),(0,3,'cli-file.yaml','CLI_GROUP','','# springboot\r\nspring:\r\n  servlet:\r\n    # 文件上传\r\n    multipart:\r\n      # 单文件最大限制（10mb）\r\n      max-file-size: 10MB\r\n      # 单请求最大限制（100mb）\r\n      max-request-size: 100MB\r\n      # 文件写入磁盘的阈值\r\n      file-size-threshold: 2\r\n# 自定义配置\r\ncustom:\r\n  # 文件路径\r\n  file:\r\n    # 存储路径\r\n    path:\r\n      win: D:/cli\r\n      linux: /data/cli\r\n    # 实现类\r\n    impl:','da9c3338d00e7e7f2e59da532e1a4d16','2022-03-09 15:24:23','2022-03-09 07:24:24',NULL,'124.115.229.243','I','cli-dev'),(0,4,'cli-custom.yaml','CLI_GROUP','','# 自定义配置\ncustom:\n  test-config: abcd\n  domain: http://127.0.0.1:17777\n  admin:\n    account: admin\n    password: 123456\n  data:\n    init: true\n  jwt:\n    # 有效期1天(单位:s)\n    expire: 5184000\n    # secret: 秘钥(普通字符串) 不能太短，太短可能会导致报错\n    secret: 69c4918f239d31d6625abfa55a3763bc\n    # 签发者\n    issuer: cli\n  # 验证码\n  captcha:\n    enable: false\n    # 长度（px）\n    width: 100\n    # 高度（px）\n    height: 30\n    # 过期时间（秒）\n    expireSecond: 60\n  rsa:\n    enable: false','c7ea6da580617984f2e10853b37d109a','2022-03-09 15:24:23','2022-03-09 07:24:24',NULL,'124.115.229.243','I','cli-dev'),(0,5,'cli-common.yaml','CLI_GROUP','','server:\n  port: 17777\n  servlet:\n    context-path: /cli\n\n# springboot\nspring:\n  # 服务名\n  application:\n    name: CLI\n  main:\n    # 允许循环依赖\n    allow-circular-references: true\n  # 国际化\n  messages:\n    basename: static.i18n.message\n    cache-duration: 3600\n    encoding: utf-8\n  # mvc\n  mvc:\n    pathmatch:\n      # Springfox使用的路径匹配是基于AntPathMatcher,Spring Boot 2.6.X使用的是PathPatternMatcher\n      matching-strategy: ant_path_matcher\n  # 邮件\n  mail:\n    host: smtp.163.com\n    protocol: smtp\n    port: 25\n    username: xxx@163.com\n    password: xxx\n    default-encoding: UTF-8\n  thymeleaf:\n    enabled: true\n    mode: HTML\n    encoding: UTF-8\n    # 模板存放在资源目录的 templates/ 文件下\n    prefix: classpath:/templates/\n    suffix: .html\n    check-template-location: true\n    check-template: false\n    cache: false','be4eb470ff9b541d9bf100296bbba756','2022-03-09 15:26:09','2022-03-09 07:26:09',NULL,'124.115.229.243','I','cli-test'),(0,6,'cli-data.yaml','CLI_GROUP','','# springboot\r\nspring:\r\n  # JPA配置\r\n  jpa:\r\n    database: mysql\r\n    generate-ddl: true\r\n    open-in-view: true\r\n    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect\r\n    hibernate:\r\n      ddl-auto: update\r\n      use-new-id-generator-mappings: true\r\n      naming:\r\n        # 隐式名称命名策略\r\n        implicit-strategy: org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy\r\n        # 物理名称命名策略\r\n        # 表名，字段为小写，当有大写字母的时候会添加下划线分隔符号\r\n        physical-strategy: org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy\r\n    # hibernate原生配置\r\n    properties:\r\n      hibernate:\r\n        format_sql: true\r\n  # redis\r\n  redis:\r\n    host: 127.0.0.1\r\n    port: 6379\r\n    database: 7\r\n    password:\r\n  # 数据库\r\n  datasource:\r\n    url: jdbc:mysql://127.0.0.1:3306/cli?serverTimezone=UTC&useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=utf8\r\n    username: root\r\n    password: mysql\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    # Hikari 连接池配置\r\n    hikari:\r\n      # 最小空闲连接数量\r\n      minimum-idle: 5\r\n      # 空闲连接存活最大时间，默认600000（10分钟）\r\n      idle-timeout: 180000\r\n      # 连接池最大连接数，默认是10\r\n      maximum-pool-size: 10\r\n      # 此属性控制从池返回的连接的默认自动提交行为,默认值：true\r\n      auto-commit: true\r\n      # 连接池名称\r\n      pool-name: MyHikariCP\r\n      # 此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认1800000即30分钟\r\n      max-lifetime: 1800000\r\n      # 数据库连接超时时间,默认30秒，即30000\r\n      connection-timeout: 30000\r\n      connection-test-query: SELECT 1','af74e9dad96c002774f8206cd634a718','2022-03-09 15:26:09','2022-03-09 07:26:09',NULL,'124.115.229.243','I','cli-test'),(0,7,'cli-file.yaml','CLI_GROUP','','# springboot\r\nspring:\r\n  servlet:\r\n    # 文件上传\r\n    multipart:\r\n      # 单文件最大限制（10mb）\r\n      max-file-size: 10MB\r\n      # 单请求最大限制（100mb）\r\n      max-request-size: 100MB\r\n      # 文件写入磁盘的阈值\r\n      file-size-threshold: 2\r\n# 自定义配置\r\ncustom:\r\n  # 文件路径\r\n  file:\r\n    # 存储路径\r\n    path:\r\n      win: D:/cli\r\n      linux: /data/cli\r\n    # 实现类\r\n    impl:','da9c3338d00e7e7f2e59da532e1a4d16','2022-03-09 15:26:09','2022-03-09 07:26:09',NULL,'124.115.229.243','I','cli-test'),(0,8,'cli-custom.yaml','CLI_GROUP','','# 自定义配置\ncustom:\n  test-config: abcd\n  domain: http://127.0.0.1:17777\n  admin:\n    account: admin\n    password: 123456\n  data:\n    init: true\n  jwt:\n    # 有效期1天(单位:s)\n    expire: 5184000\n    # secret: 秘钥(普通字符串) 不能太短，太短可能会导致报错\n    secret: 69c4918f239d31d6625abfa55a3763bc\n    # 签发者\n    issuer: cli\n  # 验证码\n  captcha:\n    enable: false\n    # 长度（px）\n    width: 100\n    # 高度（px）\n    height: 30\n    # 过期时间（秒）\n    expireSecond: 60\n  rsa:\n    enable: false','c7ea6da580617984f2e10853b37d109a','2022-03-09 15:26:09','2022-03-09 07:26:09',NULL,'124.115.229.243','I','cli-test'),(6,9,'cli-data.yaml','CLI_GROUP','','# springboot\r\nspring:\r\n  # JPA配置\r\n  jpa:\r\n    database: mysql\r\n    generate-ddl: true\r\n    open-in-view: true\r\n    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect\r\n    hibernate:\r\n      ddl-auto: update\r\n      use-new-id-generator-mappings: true\r\n      naming:\r\n        # 隐式名称命名策略\r\n        implicit-strategy: org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy\r\n        # 物理名称命名策略\r\n        # 表名，字段为小写，当有大写字母的时候会添加下划线分隔符号\r\n        physical-strategy: org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy\r\n    # hibernate原生配置\r\n    properties:\r\n      hibernate:\r\n        format_sql: true\r\n  # redis\r\n  redis:\r\n    host: 127.0.0.1\r\n    port: 6379\r\n    database: 7\r\n    password:\r\n  # 数据库\r\n  datasource:\r\n    url: jdbc:mysql://127.0.0.1:3306/cli?serverTimezone=UTC&useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=utf8\r\n    username: root\r\n    password: mysql\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    # Hikari 连接池配置\r\n    hikari:\r\n      # 最小空闲连接数量\r\n      minimum-idle: 5\r\n      # 空闲连接存活最大时间，默认600000（10分钟）\r\n      idle-timeout: 180000\r\n      # 连接池最大连接数，默认是10\r\n      maximum-pool-size: 10\r\n      # 此属性控制从池返回的连接的默认自动提交行为,默认值：true\r\n      auto-commit: true\r\n      # 连接池名称\r\n      pool-name: MyHikariCP\r\n      # 此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认1800000即30分钟\r\n      max-lifetime: 1800000\r\n      # 数据库连接超时时间,默认30秒，即30000\r\n      connection-timeout: 30000\r\n      connection-test-query: SELECT 1','af74e9dad96c002774f8206cd634a718','2022-03-09 15:27:16','2022-03-09 07:27:16','nacos','124.115.229.243','U','cli-test'),(8,10,'cli-custom.yaml','CLI_GROUP','','# 自定义配置\ncustom:\n  test-config: abcd\n  domain: http://127.0.0.1:17777\n  admin:\n    account: admin\n    password: 123456\n  data:\n    init: true\n  jwt:\n    # 有效期1天(单位:s)\n    expire: 5184000\n    # secret: 秘钥(普通字符串) 不能太短，太短可能会导致报错\n    secret: 69c4918f239d31d6625abfa55a3763bc\n    # 签发者\n    issuer: cli\n  # 验证码\n  captcha:\n    enable: false\n    # 长度（px）\n    width: 100\n    # 高度（px）\n    height: 30\n    # 过期时间（秒）\n    expireSecond: 60\n  rsa:\n    enable: false','c7ea6da580617984f2e10853b37d109a','2022-03-09 15:27:44','2022-03-09 07:27:44','nacos','124.115.229.243','U','cli-test'),(7,11,'cli-file.yaml','CLI_GROUP','','# springboot\r\nspring:\r\n  servlet:\r\n    # 文件上传\r\n    multipart:\r\n      # 单文件最大限制（10mb）\r\n      max-file-size: 10MB\r\n      # 单请求最大限制（100mb）\r\n      max-request-size: 100MB\r\n      # 文件写入磁盘的阈值\r\n      file-size-threshold: 2\r\n# 自定义配置\r\ncustom:\r\n  # 文件路径\r\n  file:\r\n    # 存储路径\r\n    path:\r\n      win: D:/cli\r\n      linux: /data/cli\r\n    # 实现类\r\n    impl:','da9c3338d00e7e7f2e59da532e1a4d16','2022-03-09 15:28:12','2022-03-09 07:28:13','nacos','124.115.229.243','U','cli-test'),(3,12,'cli-file.yaml','CLI_GROUP','','# springboot\r\nspring:\r\n  servlet:\r\n    # 文件上传\r\n    multipart:\r\n      # 单文件最大限制（10mb）\r\n      max-file-size: 10MB\r\n      # 单请求最大限制（100mb）\r\n      max-request-size: 100MB\r\n      # 文件写入磁盘的阈值\r\n      file-size-threshold: 2\r\n# 自定义配置\r\ncustom:\r\n  # 文件路径\r\n  file:\r\n    # 存储路径\r\n    path:\r\n      win: D:/cli\r\n      linux: /data/cli\r\n    # 实现类\r\n    impl:','da9c3338d00e7e7f2e59da532e1a4d16','2022-03-09 15:29:52','2022-03-09 07:29:53','nacos','124.115.229.243','U','cli-dev'),(5,13,'cli-common.yaml','CLI_GROUP','','server:\n  port: 17777\n  servlet:\n    context-path: /cli\n\n# springboot\nspring:\n  # 服务名\n  application:\n    name: CLI\n  main:\n    # 允许循环依赖\n    allow-circular-references: true\n  # 国际化\n  messages:\n    basename: static.i18n.message\n    cache-duration: 3600\n    encoding: utf-8\n  # mvc\n  mvc:\n    pathmatch:\n      # Springfox使用的路径匹配是基于AntPathMatcher,Spring Boot 2.6.X使用的是PathPatternMatcher\n      matching-strategy: ant_path_matcher\n  # 邮件\n  mail:\n    host: smtp.163.com\n    protocol: smtp\n    port: 25\n    username: xxx@163.com\n    password: xxx\n    default-encoding: UTF-8\n  thymeleaf:\n    enabled: true\n    mode: HTML\n    encoding: UTF-8\n    # 模板存放在资源目录的 templates/ 文件下\n    prefix: classpath:/templates/\n    suffix: .html\n    check-template-location: true\n    check-template: false\n    cache: false','be4eb470ff9b541d9bf100296bbba756','2022-03-09 15:50:47','2022-03-09 07:50:48','nacos','124.115.229.243','U','cli-test'),(1,14,'cli-common.yaml','CLI_GROUP','','server:\n  port: 17777\n  servlet:\n    context-path: /cli\n\n# springboot\nspring:\n  # 服务名\n  application:\n    name: CLI\n  main:\n    # 允许循环依赖\n    allow-circular-references: true\n  # 国际化\n  messages:\n    basename: static.i18n.message\n    cache-duration: 3600\n    encoding: utf-8\n  # mvc\n  mvc:\n    pathmatch:\n      # Springfox使用的路径匹配是基于AntPathMatcher,Spring Boot 2.6.X使用的是PathPatternMatcher\n      matching-strategy: ant_path_matcher\n  # 邮件\n  mail:\n    host: smtp.163.com\n    protocol: smtp\n    port: 25\n    username: xxx@163.com\n    password: xxx\n    default-encoding: UTF-8\n  thymeleaf:\n    enabled: true\n    mode: HTML\n    encoding: UTF-8\n    # 模板存放在资源目录的 templates/ 文件下\n    prefix: classpath:/templates/\n    suffix: .html\n    check-template-location: true\n    check-template: false\n    cache: false','be4eb470ff9b541d9bf100296bbba756','2022-03-09 15:51:09','2022-03-09 07:51:09','nacos','124.115.229.243','U','cli-dev'),(5,15,'cli-common.yaml','CLI_GROUP','','server:\n  port: 17777\n  servlet:\n    context-path: /cli\n\n# springboot\nspring:\n  # 服务名\n  application:\n    name: CLI\n  main:\n    # 允许循环依赖\n    allow-circular-references: true\n  # 国际化\n  messages:\n    basename: static.i18n.message\n    cache-duration: 3600\n    encoding: utf-8\n  # mvc\n  mvc:\n    pathmatch:\n      # Springfox使用的路径匹配是基于AntPathMatcher,Spring Boot 2.6.X使用的是PathPatternMatcher\n      matching-strategy: ant_path_matcher\n  # 邮件\n  mail:\n    host: smtp.163.com\n    protocol: smtp\n    port: 25\n    username: xxx@163.com\n    password: xxx\n    default-encoding: UTF-8\n  thymeleaf:\n    enabled: true\n    mode: HTML\n    encoding: UTF-8\n    # 模板存放在资源目录的 templates/ 文件下\n    prefix: classpath:/templates/\n    suffix: .html\n    check-template-location: true\n    check-template: false\n    cache: false\n\n# 日志存储路径\nlog:\n  path: /data/cli/log','a29212c9839cc25474aa074c1fddd3f4','2022-03-14 16:20:20','2022-03-14 08:20:21','nacos','124.115.229.243','U','cli-test'),(8,16,'cli-custom.yaml','CLI_GROUP','','# 自定义配置\ncustom:\n  test-config: abcd\n  domain: http://47.105.65.21:17777\n  admin:\n    account: admin\n    password: 123456\n  data:\n    init: true\n  jwt:\n    # 有效期1天(单位:s)\n    expire: 5184000\n    # secret: 秘钥(普通字符串) 不能太短，太短可能会导致报错\n    secret: 69c4918f239d31d6625abfa55a3763bc\n    # 签发者\n    issuer: cli\n  # 验证码\n  captcha:\n    enable: false\n    # 长度（px）\n    width: 100\n    # 高度（px）\n    height: 30\n    # 过期时间（秒）\n    expireSecond: 60\n  rsa:\n    enable: true','7074d2edd182cadd71e26229edac43a3','2022-03-14 16:21:31','2022-03-14 08:21:31','nacos','124.115.229.243','U','cli-test'),(1,17,'cli-common.yaml','CLI_GROUP','','server:\n  port: 17777\n  servlet:\n    context-path: /cli\n\n# springboot\nspring:\n  # 服务名\n  application:\n    name: CLI\n  main:\n    # 允许循环依赖\n    allow-circular-references: true\n  # 国际化\n  messages:\n    basename: static.i18n.message\n    cache-duration: 3600\n    encoding: utf-8\n  # mvc\n  mvc:\n    pathmatch:\n      # Springfox使用的路径匹配是基于AntPathMatcher,Spring Boot 2.6.X使用的是PathPatternMatcher\n      matching-strategy: ant_path_matcher\n  # 邮件\n  mail:\n    host: smtp.163.com\n    protocol: smtp\n    port: 25\n    username: xxx@163.com\n    password: xxx\n    default-encoding: UTF-8\n  thymeleaf:\n    enabled: true\n    mode: HTML\n    encoding: UTF-8\n    # 模板存放在资源目录的 templates/ 文件下\n    prefix: classpath:/templates/\n    suffix: .html\n    check-template-location: true\n    check-template: false\n    cache: false\n\n# 日志存储路径\nlog:\n  path: D:/cli/log','c4f871d66b246701d0ee8560bd422e73','2022-03-14 18:35:05','2022-03-14 10:35:06','nacos','124.115.229.243','U','cli-dev'),(1,18,'cli-common.yaml','CLI_GROUP','','server:\n  port: 17777\n  servlet:\n    context-path: /cli\n\n# springboot\nspring:\n  # 服务名\n  application:\n    name: CLI\n  main:\n    # 允许循环依赖\n    allow-circular-references: true\n  # 国际化\n  messages:\n    basename: static.i18n.message\n    cache-duration: 3600\n    encoding: utf-8\n  # mvc\n  mvc:\n    pathmatch:\n      # Springfox使用的路径匹配是基于AntPathMatcher,Spring Boot 2.6.X使用的是PathPatternMatcher\n      matching-strategy: ant_path_matcher\n  # 邮件\n  mail:\n    host: smtp.163.com\n    protocol: smtp\n    port: 465\n    username: xxx@163.com\n    password: xxx\n    default-encoding: UTF-8\n    properties:\n      mail:\n        stmp:\n          ssl:\n            enable: true\n            trust: smtp.163.com\n          socketFactory:\n            port: 465\n          port: 465\n          auth: true\n          starttls:\n            enable: true\n            required: true\n        socketFactory:\n          class: javax.net.ssl.SSLSocketFactory\n  thymeleaf:\n    enabled: true\n    mode: HTML\n    encoding: UTF-8\n    # 模板存放在资源目录的 templates/ 文件下\n    prefix: classpath:/templates/\n    suffix: .html\n    check-template-location: true\n    check-template: false\n    cache: false\n\n# 日志存储路径\nlog:\n  path: D:/cli/log','4d95b5181e627a1173450592633318b9','2022-03-14 18:42:35','2022-03-14 10:42:35','nacos','124.115.229.243','U','cli-dev'),(5,19,'cli-common.yaml','CLI_GROUP','','server:\n  port: 17777\n  servlet:\n    context-path: /cli\n\n# springboot\nspring:\n  # 服务名\n  application:\n    name: CLI\n  main:\n    # 允许循环依赖\n    allow-circular-references: true\n  # 国际化\n  messages:\n    basename: static.i18n.message\n    cache-duration: 3600\n    encoding: utf-8\n  # mvc\n  mvc:\n    pathmatch:\n      # Springfox使用的路径匹配是基于AntPathMatcher,Spring Boot 2.6.X使用的是PathPatternMatcher\n      matching-strategy: ant_path_matcher\n  # 邮件\n  mail:\n    host: smtp.sina.com\n    protocol: smtp\n    port: 465\n    username: clidata@sina.com\n    password: f02ccc07207c65f0\n    default-encoding: UTF-8\n    properties:\n      mail:\n        stmp:\n          ssl:\n            enable: true\n            trust: smtp.sina.com\n          socketFactory:\n            port: 465\n          port: 465\n          auth: true\n          starttls:\n            enable: true\n            required: true\n        socketFactory:\n          class: javax.net.ssl.SSLSocketFactory\n  thymeleaf:\n    enabled: true\n    mode: HTML\n    encoding: UTF-8\n    # 模板存放在资源目录的 templates/ 文件下\n    prefix: classpath:/templates/\n    suffix: .html\n    check-template-location: true\n    check-template: false\n    cache: false\n\n# 日志存储路径\nlog:\n  path: /data/cli/log','c9585add6391f120c58f9009042b85e5','2022-03-14 18:44:47','2022-03-14 10:44:47','nacos','124.115.229.243','U','cli-test'),(1,20,'cli-common.yaml','CLI_GROUP','','server:\n  port: 17777\n  servlet:\n    context-path: /cli\n\n# springboot\nspring:\n  # 服务名\n  application:\n    name: CLI\n  main:\n    # 允许循环依赖\n    allow-circular-references: true\n  # 国际化\n  messages:\n    basename: static.i18n.message\n    cache-duration: 3600\n    encoding: utf-8\n  # mvc\n  mvc:\n    pathmatch:\n      # Springfox使用的路径匹配是基于AntPathMatcher,Spring Boot 2.6.X使用的是PathPatternMatcher\n      matching-strategy: ant_path_matcher\n  # 邮件\n  mail:\n    host: smtp.163.com\n    protocol: smtp\n    port: 465\n    username: xxx@163.com\n    password: xxx\n    default-encoding: UTF-8\n  thymeleaf:\n    enabled: true\n    mode: HTML\n    encoding: UTF-8\n    # 模板存放在资源目录的 templates/ 文件下\n    prefix: classpath:/templates/\n    suffix: .html\n    check-template-location: true\n    check-template: false\n    cache: false\n\n# 日志存储路径\nlog:\n  path: D:/cli/log','e7eafd8543ecd5d1047a4dfbb85a673e','2022-03-14 20:30:15','2022-03-14 12:30:15','nacos','117.22.122.136','U','cli-dev'),(5,21,'cli-common.yaml','CLI_GROUP','','server:\n  port: 17777\n  servlet:\n    context-path: /cli\n\n# springboot\nspring:\n  # 服务名\n  application:\n    name: CLI\n  main:\n    # 允许循环依赖\n    allow-circular-references: true\n  # 国际化\n  messages:\n    basename: static.i18n.message\n    cache-duration: 3600\n    encoding: utf-8\n  # mvc\n  mvc:\n    pathmatch:\n      # Springfox使用的路径匹配是基于AntPathMatcher,Spring Boot 2.6.X使用的是PathPatternMatcher\n      matching-strategy: ant_path_matcher\n  # 邮件\n  mail:\n    host: smtp.sina.com\n    protocol: smtp\n    port: 465\n    username: clidata@sina.com\n    password: f02ccc07207c65f0\n    default-encoding: UTF-8\n  thymeleaf:\n    enabled: true\n    mode: HTML\n    encoding: UTF-8\n    # 模板存放在资源目录的 templates/ 文件下\n    prefix: classpath:/templates/\n    suffix: .html\n    check-template-location: true\n    check-template: false\n    cache: false\n\n# 日志存储路径\nlog:\n  path: /data/cli/log','4eb3b81a60004fc0827206171ab2b2ef','2022-03-14 20:31:08','2022-03-14 12:31:09','nacos','117.22.122.136','U','cli-test'),(4,22,'cli-custom.yaml','CLI_GROUP','','# 自定义配置\ncustom:\n  test-config: abcd\n  domain: http://127.0.0.1:17777\n  admin:\n    account: admin\n    password: 123456\n  data:\n    init: true\n  jwt:\n    # 有效期1天(单位:s)\n    expire: 5184000\n    # secret: 秘钥(普通字符串) 不能太短，太短可能会导致报错\n    secret: 69c4918f239d31d6625abfa55a3763bc\n    # 签发者\n    issuer: cli\n  # 验证码\n  captcha:\n    enable: false\n    # 长度（px）\n    width: 100\n    # 高度（px）\n    height: 30\n    # 过期时间（秒）\n    expireSecond: 60\n  rsa:\n    enable: false','c7ea6da580617984f2e10853b37d109a','2022-03-15 17:57:51','2022-03-15 09:57:51','nacos','124.115.229.243','U','cli-dev'),(8,23,'cli-custom.yaml','CLI_GROUP','','# 自定义配置\ncustom:\n  test-config: abcd\n  domain: http://47.105.65.21:17777\n  admin:\n    account: admin\n    password: 123456\n  data:\n    init: true\n  jwt:\n    # 有效期1天(单位:s)\n    expire: 5184000\n    # secret: 秘钥(普通字符串) 不能太短，太短可能会导致报错\n    secret: 69c4918f239d31d6625abfa55a3763bc\n    # 签发者\n    issuer: cli\n  # 验证码\n  captcha:\n    enable: true\n    # 长度（px）\n    width: 100\n    # 高度（px）\n    height: 30\n    # 过期时间（秒）\n    expireSecond: 60\n  rsa:\n    enable: true','21c56996fde8920ef49d348639a600ef','2022-03-15 17:58:08','2022-03-15 09:58:09','nacos','124.115.229.243','U','cli-test');
/*!40000 ALTER TABLE `his_config_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `role` varchar(50) NOT NULL,
  `resource` varchar(255) NOT NULL,
  `action` varchar(8) NOT NULL,
  UNIQUE KEY `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `username` varchar(50) NOT NULL,
  `role` varchar(50) NOT NULL,
  UNIQUE KEY `idx_user_role` (`username`,`role`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES ('nacos','ROLE_ADMIN');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenant_capacity`
--

DROP TABLE IF EXISTS `tenant_capacity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tenant_capacity` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='租户容量信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenant_capacity`
--

LOCK TABLES `tenant_capacity` WRITE;
/*!40000 ALTER TABLE `tenant_capacity` DISABLE KEYS */;
/*!40000 ALTER TABLE `tenant_capacity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenant_info`
--

DROP TABLE IF EXISTS `tenant_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tenant_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='tenant_info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenant_info`
--

LOCK TABLES `tenant_info` WRITE;
/*!40000 ALTER TABLE `tenant_info` DISABLE KEYS */;
INSERT INTO `tenant_info` VALUES (1,'1','cli-dev','cli-dev','开发环境','nacos',1646810621290,1646810621290),(2,'1','cli-test','cli-test','测试环境','nacos',1646810633313,1646810633313);
/*!40000 ALTER TABLE `tenant_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(500) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('nacos','$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'nacos'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-03-17 18:57:02
