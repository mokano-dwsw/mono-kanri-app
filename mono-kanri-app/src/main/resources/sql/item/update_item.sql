UPDATE
    items
SET
    item_nm = /*itemNm*/'モノの名前',
    item_qty = /*itemQty*/1,
    stor_loc = /*storLoc*/'保管場所',
    upd_dtm = CURRENT_TIMESTAMP
WHERE
    item_id = /*itemId*/1
