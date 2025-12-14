create role mono_db_user login password 'mono_db_pass';
create database mono_db
WITH 
    TEMPLATE template0
    OWNER = mono_db_user -- ご自身のPostgreSQLユーザー名を設定
    ENCODING = 'UTF8'
    LC_COLLATE = 'C'       -- ★ ロケールをCに設定
    LC_CTYPE = 'C'         -- ★ 文字種別をCに設定
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
