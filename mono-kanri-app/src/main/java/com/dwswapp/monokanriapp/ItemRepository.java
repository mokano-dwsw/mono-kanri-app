package com.dwswapp.monokanriapp;

import java.util.List;
import org.springframework.stereotype.Repository;
import jp.co.future.uroborosql.SqlAgent;

/**
 * items テーブルのデータアクセスを行うリポジトリ
 */
@Repository
public class ItemRepository {

    // uroborosql の中心となるインターフェース
    private final SqlAgent agent;

    /**
     * コンストラクタインジェクションによりSqlAgentを注入
     * @param agent SqlAgent
     */
    public ItemRepository(SqlAgent agent) {
        this.agent = agent;
    }

    // ★ 既存の findAll() を発展させ、キーワードを引数に取る findItems メソッドを追加
    public List<Item> findItems(String keyword) {
        // Repository層で直接uroborosqlを実行する
        return agent.query("item/select_item_all")
                .param("keyword", keyword) // ★ keywordをバインド
                .collect(Item.class);
    }
    
    /**
     * 全てのモノのリストを取得する
     * SQLファイル: sql/select_item_all.sql
     * @return Itemオブジェクトのリスト
     */
    public List<Item> findAll() {
        // SQLファイル名と、マッピング先のエンティティクラスを指定して実行
        return agent.query("item/select_item_all")
                    .collect(Item.class);
    }

    /**
     * 新しいモノをデータベースに登録する
     * @param item 登録するItemオブジェクト
     * @return 登録されたレコード数 (通常は 1)
     */
    public Item save(Item item) {
        // executeUpdateWithKeys() を使うことで、
        // SQLで RETURNING句で返されたIDなどを取得できます。
        Long generatedId = agent.query("item/insert_item")
                // item.name と item.quantity の値をバインド
                .param("item", item)
                // .returnKeys() // RETURNING句の結果を返してもらう
                // .to(Long.class) // 結果を Long型 (ID) にマッピング
                .one(Long.class);

        // 取得したIDをItemオブジェクトにセットし直す
        item.setItemId(generatedId);
        return item;
    }
    
    /**
     * IDを指定してモノ（Item）を削除する
     * @param id 削除対象のID
     * @return 削除された行数 (通常は 1)
     */
    public int deleteById(long id) {
        // uroborosqlで、itemフォルダ内の delete.sql を実行する
        return agent.update("item/delete_item")
                .param("id", id) // SQL内の /*id*/ に値をバインド
                .count(); // DELETE, UPDATE, INSERT の場合は executeUpdate を使用
    }

    // ★ 更新メソッド
    /**
     * モノ（Item）を更新する
     * @param item 更新するItemオブジェクト
     * @return 更新されたレコード数 (通常は 1)
     */
    public int update(Item item) {
        // 戻り値は更新されたレコード数 (1件)
        return agent.update("item/update_item")
            .param("itemNm", item.getItemNm())
            .param("itemQty", item.getItemQty())
            .param("storLoc", item.getStorLoc())
            .param("itemId", item.getItemId())
            .count();
    }
}
