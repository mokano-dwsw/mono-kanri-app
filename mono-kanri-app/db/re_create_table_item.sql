-- 1. バックアップテーブルを作成し、既存データを退避
DROP TABLE IF EXISTS zbk_items;
CREATE TABLE zbk_items AS
SELECT
    item_id
,   item_nm
,   stor_loc
,   reg_dtm
,   upd_dtm
FROM
    items
;
-- 2. itemsテーブルを再作成（item_qtyカラムを追加）
DROP TABLE IF EXISTS items;
CREATE TABLE items (
    item_id SERIAL PRIMARY KEY
,   item_nm VARCHAR(255) NOT NULL
,   item_qty INTEGER NOT NULL DEFAULT 1
,   stor_loc VARCHAR(255) -- NULLを許可
,   reg_dtm TIMESTAMP WITHOUT TIME ZONE NOT NULL
,   upd_dtm TIMESTAMP WITHOUT TIME ZONE -- NULLを許可
);
-- 3. 退避しておいたデータをitemsテーブルに戻す（item_qtyは1で設定）
INSERT INTO items (
    item_id
,   item_nm
,   item_qty
,   stor_loc
,   reg_dtm
,   upd_dtm
)
SELECT
    item_id
,   item_nm
,   1
,   stor_loc
,   reg_dtm
,   upd_dtm
FROM
    zbk_items
;
