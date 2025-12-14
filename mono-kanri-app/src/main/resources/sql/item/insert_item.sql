INSERT INTO items (
    item_nm
,   item_qty
,   stor_loc
,   reg_dtm
,   upd_dtm
) VALUES (
    /*item.itemNm*/'Sample Name'
,   /*item.itemQty*/10
,   /*item.storLoc*/'Sample Location'
,   /*item.regDtm*/CURRENT_TIMESTAMP
,   /*item.updDtm*/CURRENT_TIMESTAMP
)
RETURNING item_id -- ★ これが最重要。生成されたIDを返す。
