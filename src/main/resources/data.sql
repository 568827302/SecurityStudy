insert into mooc_users(id, username, `name`, mobile, password_hash, enabled, account_non_expired, account_non_locked, credentials_non_expired, email)
            values (1, 'user', 'Zhang San', '13012341234', '{bcrypt}$2a$10$jhS817qUHgOR4uQSoEBRxO58.rZ1dBCmCTjG8PeuQAX4eISf.zowm', 1, 1, 1, 1, 'zhangsan@local.dev'),
                   (2, 'old_user', 'Li Si', '13812341234', '{SHA-1}{6ML3CMgV/JcfIKUPjsp1Dug0DOIMsl4ALQU8QnPqyAs=}882fb280c22b0efc9ded641c71c1b8e2a35b54d7', 1, 1, 1, 1, 'lisi@local.dev');
insert into mooc_roles(id, role_name) values (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');
insert into mooc_users_roles(user_id, role_id) values (1, 1), (1, 2), (2, 1);
