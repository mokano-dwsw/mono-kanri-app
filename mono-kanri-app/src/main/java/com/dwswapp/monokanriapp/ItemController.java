package com.dwswapp.monokanriapp;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping; // ★ 追加
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;  // ★ 追加import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * モノ管理のためのREST APIコントローラー
 */
@RestController // REST APIのコントローラーであることを示す
@RequestMapping("/api/items") // このコントローラーのベースURLを設定
public class ItemController {

    // ItemServiceをDIする
    private final ItemService itemService;

    /**
     * コンストラクタインジェクションによりItemServiceを注入
     */
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // --- 1. 取得API (GET) ---

    /**
     * モノのリストを取得する
     * GET /api/items
     * @param keyword 検索キーワード（任意）
     * @return Itemオブジェクトのリスト
     */
    @GetMapping
    public List<Item> findItems(@RequestParam(required = false) String keyword) {
        return itemService.findItems(keyword);
    }

    // --- 2. 新規登録API (POST) ---

    /**
     * 新しいモノを登録する
     * POST /api/items
     * @param item フロントエンドから送られてきたItemデータ（itemNm, storLocのみ）
     * @return 登録されたItemオブジェクト
     */
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        // 登録日時と更新日時を現在時刻で設定
        LocalDateTime now = LocalDateTime.now();
        item.setRegDtm(now);
        item.setUpdDtm(now); // upd_dtm は NULL 許容ですが、ここでは登録時に設定します

        // リポジトリ層を呼び出し、データベースに登録
        Item savedItem = itemService.create(item);

        // 登録成功として、ステータスコード 201 Created と登録された Item を返す
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    /**
     * IDを指定してモノを削除する (DELETE /api/items/{id})
     * @param id 削除対象のID
     * @return 削除成功の場合、204 No Content
     */
    @DeleteMapping("/{id}") // HTTP DELETEメソッドを受け付ける
    public ResponseEntity<Void> deleteItem(@PathVariable("id") long id) {
        
        int deletedCount = itemService.deleteById(id);

        if (deletedCount == 0) {
            // IDが見つからなかった場合（削除対象なし）
            return ResponseEntity.notFound().build();
        }

        // 削除成功の場合、コンテンツなし (204 No Content) を返す
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Item item) {
        // パスから取得したIDをBodyのオブジェクトに設定し、Serviceに渡す
        item.setItemId(id);
        return itemService.update(item);
    }
}
