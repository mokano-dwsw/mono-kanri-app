SELECT
    item_id
,   item_nm
,   item_qty
,   stor_loc
,   reg_dtm
,   upd_dtm
FROM
    items
/*IF keyword != null*/
WHERE
    1=1
    -- LIKE検索（部分一致）を行う
AND item_nm LIKE /*keyword*/'頭文字' || '%' ESCAPE '$'
/*END*/
ORDER BY
    item_id
