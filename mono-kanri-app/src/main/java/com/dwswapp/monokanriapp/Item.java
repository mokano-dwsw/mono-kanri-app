package com.dwswapp.monokanriapp;

import java.time.LocalDateTime;

import jp.co.future.uroborosql.mapping.annotations.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * items テーブルに対応するエンティティクラス
 */
@Data // Getter, Setter, equals/hashCode, toString を自動生成
@NoArgsConstructor // 引数なしコンストラクタを自動生成
@AllArgsConstructor // 全引数付きコンストラクタを自動生成
@Table(name = "items")
public class Item {

    // 論物辞書: モノID (item_id)
    private Long itemId;

    // 論物辞書: モノ名称 (item_nm)
    private String itemNm;

    // 論物辞書: モノ数量 (item_qty)
    private Integer itemQty;

    // 論物辞書: 保管場所 (stor_loc) - NULL許容のため、String型
    private String storLoc;

    // 論物辞書: 登録日時 (reg_dtm)
    private LocalDateTime regDtm;

    // 論物辞書: 更新日時 (upd_dtm) - NULL許容のため、LocalDateTime型
    private LocalDateTime updDtm;
}
