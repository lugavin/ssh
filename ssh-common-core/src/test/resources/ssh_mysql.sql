INSERT INTO `sys_user` (`id`,`code`,`name`,`pass`,`salt`,`status`)
VALUES (101, 'admin', 'admin', (SELECT MD5(CONCAT('10086','111111'))), '10086', '1'),
       (102, 'super', 'super', (SELECT MD5(CONCAT('10086','111111'))), '10086', '1');

INSERT INTO `sys_role` (`id`,`name`,`status`)
VALUES (1001, '用户管理员', '1'),
       (1002, '角色管理员', '1');

INSERT INTO SYS_PERMISSION (`id`, `code`, `name`, `type`, `url`, `parent_id`, `parent_ids`, `status`, `seq`)
VALUES ( 1,           '',        '系统管理', 'MENU',       '',          NULL,      '', '1',   1),
       (11, 'user:query',        '用户管理', 'MENU', 'user/list',          1,    '1/', '1',  51),
       (12, 'user:create',       '用户新增', 'FUNC', 'user/add',          11, '1/11/', '1',  52),
       (13, 'user:delete',       '用户删除', 'FUNC', 'user/delete',       11, '1/11/', '1',  53),
       (14, 'user:update',       '用户修改', 'FUNC', 'user/edit',         11, '1/11/', '1',  54),
       (15, 'user:query',        '用户查询', 'FUNC', 'user/query',        11, '1/11/', '1',  55),
       (21, 'role:query',        '角色管理', 'MENU', 'role/list',          1,    '1/', '1', 101),
       (22, 'role:create',       '角色新增', 'FUNC', 'role/add',          21, '1/21/', '1', 102),
       (23, 'role:delete',       '角色删除', 'FUNC', 'role/delete',       21, '1/21/', '1', 103),
       (24, 'role:update',       '角色修改', 'FUNC', 'role/edit',         21, '1/21/', '1', 104),
       (25, 'role:query',        '角色查询', 'FUNC', 'role/query',        21, '1/21/', '1', 105),
       (31, 'permission:query',  '权限管理', 'MENU', 'permission/list',    1,    '1/', '1', 151),
       (32, 'permission:create', '权限新增', 'FUNC', 'permission/add',    31, '1/31/', '1', 152),
       (33, 'permission:delete', '权限删除', 'FUNC', 'permission/delete', 31, '1/31/', '1', 153),
       (34, 'permission:update', '权限修改', 'FUNC', 'permission/edit',   31, '1/31/', '1', 154),
       (35, 'permission:query',  '权限查询', 'FUNC', 'permission/query',  31, '1/31/', '1', 155),
       (41, 'audit:query',       '日志管理', 'MENU', 'audit/list',         1,    '1/', '1', 201),
       (42, 'audit:query',       '日志查询', 'FUNC', 'audit/query',       41, '1/41/', '1', 202);

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
