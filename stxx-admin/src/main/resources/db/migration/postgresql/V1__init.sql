-- ----------------------------
-- PostgreSQL版本 - 基于MySQL脚本转换
-- 注意：此脚本为简化版本，包含核心表结构
-- 如需完整功能，请根据MySQL版本进行相应调整
-- ----------------------------

-- ----------------------------
-- 1、部门表
-- ----------------------------
drop table if exists sys_dept;
create table sys_dept (
  dept_id           bigint         not null generated always as identity    primary key,
  parent_id         bigint         default 0                  ,
  ancestors         varchar(50)    default ''                 ,
  dept_name         varchar(30)    default ''                 ,
  order_num         integer        default 0                  ,
  leader            varchar(20)    default null               ,
  phone             varchar(11)    default null               ,
  email             varchar(50)    default null               ,
  status            char(1)        default '0'                ,
  del_flag          char(1)        default '0'                ,
  create_by         varchar(64)    default ''                 ,
  create_time 	    timestamp                                 ,
  update_by         varchar(64)    default ''                 ,
  update_time       timestamp
);

-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------
insert into sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time) values(100,  0,   '0',          '若依科技',   0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', now());
insert into sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time) values(101,  100, '0,100',      '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', now());
insert into sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time) values(102,  100, '0,100',      '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', now());
insert into sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time) values(103,  101, '0,100,101',  '研发部门',   1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', now());
insert into sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time) values(104,  101, '0,100,101',  '市场部门',   2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', now());
insert into sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time) values(105,  101, '0,100,101',  '测试部门',   3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', now());
insert into sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time) values(106,  101, '0,100,101',  '财务部门',   4, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', now());
insert into sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time) values(107,  101, '0,100,101',  '运维部门',   5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', now());
insert into sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time) values(108,  102, '0,100,102',  '市场部门',   1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', now());
insert into sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time) values(109,  102, '0,100,102',  '财务部门',   2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', now());

-- ----------------------------
-- 2、用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user (
  user_id           bigint         not null generated always as identity    primary key,
  dept_id           bigint         default null               ,
  user_name         varchar(30)    not null                   ,
  nick_name         varchar(30)    not null                   ,
  user_type         varchar(2)     default '00'               ,
  email             varchar(50)    default ''                 ,
  phonenumber       varchar(11)    default ''                 ,
  sex               char(1)        default '0'                ,
  avatar            varchar(100)   default ''                 ,
  password          varchar(100)   default ''                 ,
  status            char(1)        default '0'                ,
  del_flag          char(1)        default '0'                ,
  login_ip          varchar(128)   default ''                 ,
  login_date        timestamp                                 ,
  pwd_update_date   timestamp                                 ,
  create_by         varchar(64)    default ''                 ,
  create_time       timestamp                                 ,
  update_by         varchar(64)    default ''                 ,
  update_time       timestamp                                 ,
  remark            varchar(500)   default null
);

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, create_by, create_time, remark) values(1,  103, 'admin', '若依', '00', 'ry@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', 'admin', now(), '管理员');
insert into sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, create_by, create_time, remark) values(2,  105, 'ry',    '若依', '00', 'ry@qq.com',  '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', 'admin', now(), '测试员');

-- ----------------------------
-- 其他核心表结构（简化版本）
-- 请根据实际需要添加更多表和数据
-- 可以参考 mysql/V1__init.sql 文件进行完整转换
-- ----------------------------
