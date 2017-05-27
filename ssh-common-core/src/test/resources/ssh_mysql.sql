INSERT INTO `sys_user` (`id`,`code`,`name`,`pass`,`salt`,`status`)
VALUES (101, 'admin', '张三', (SELECT MD5(CONCAT('10086','111111'))), '10086', '1'),
       (102, 'super', '李四', (SELECT MD5(CONCAT('10086','111111'))), '10086', '1');

INSERT INTO `sys_role` (`id`,`name`,`status`)
VALUES (1001, '用户管理员', '1'),
       (1002, '角色管理员', '1');

INSERT INTO SYS_PERMISSION (`id`, `code`, `name`, `type`, `url`, `parent_id`, `parent_ids`, `status`, `seq`)
VALUES ( 1,           '',        '系统管理', 'MENU', '',                    NULL,      '', '1',   1),
       (11, 'user:query',        '用户管理', 'MENU', 'user/list.htm',          1,    '1/', '1', 101),
       (12, 'user:create',       '用户新增', 'FUNC', 'user/add.htm',          11, '1/11/', '1', 102),
       (13, 'user:delete',       '用户删除', 'FUNC', 'user/delete.htm',       11, '1/11/', '1', 103),
       (14, 'user:update',       '用户修改', 'FUNC', 'user/edit.htm',         11, '1/11/', '1', 104),
       (15, 'user:query',        '用户查询', 'FUNC', 'user/query.htm',        11, '1/11/', '1', 105),
       (21, 'role:query',        '角色管理', 'MENU', 'role/list.htm',          1,    '1/', '1', 201),
       (22, 'role:create',       '角色新增', 'FUNC', 'role/add.htm',          21, '1/21/', '1', 202),
       (23, 'role:delete',       '角色删除', 'FUNC', 'role/delete.htm',       21, '1/21/', '1', 203),
       (24, 'role:update',       '角色修改', 'FUNC', 'role/edit.htm',         21, '1/21/', '1', 204),
       (25, 'role:query',        '角色查询', 'FUNC', 'role/query.htm',        21, '1/21/', '1', 205),
       (31, 'permission:query',  '权限管理', 'MENU', 'permission/list.htm',    1,    '1/', '1', 301),
       (32, 'permission:create', '权限新增', 'FUNC', 'permission/add.htm',    31, '1/31/', '1', 302),
       (33, 'permission:delete', '权限删除', 'FUNC', 'permission/delete.htm', 31, '1/31/', '1', 303),
       (34, 'permission:update', '权限修改', 'FUNC', 'permission/edit.htm',   31, '1/31/', '1', 304),
       (35, 'permission:query',  '权限查询', 'FUNC', 'permission/query.htm',  31, '1/31/', '1', 305),
       (41, 'audit:query',       '日志管理', 'MENU', 'audit/list.htm',         1,    '1/', '1', 401),
       (42, 'audit:query',       '日志查询', 'FUNC', 'audit/query.htm',       41, '1/41/', '1', 402);


INSERT INTO `sys_user_role` (`user_id`,`role_id`)
VALUES (101, 1001),
       (102, 1002);

INSERT INTO `sys_permission_role` (`permission_id`,`role_id`)
VALUES ( 1, 1001),
       (11, 1001),
       (12, 1001),
       (13, 1001),
       (14, 1001),
       (15, 1001),
       (21, 1001),
       (22, 1001),
       (23, 1001),
       (24, 1001),
       (25, 1001),
       (31, 1001),
       (32, 1001),
       (33, 1001),
       (34, 1001),
       (35, 1001),
       (41, 1001),
       (42, 1001);
