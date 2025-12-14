// ItemService.java
package com.dwswapp.monokanriapp;

import jp.co.future.uroborosql.SqlAgent;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ItemService {
    
    private final SqlAgent agent;
    private final ItemRepository repository;

    public ItemService(SqlAgent agent, ItemRepository repository) {
        this.agent = agent;
        this.repository = repository;
    }

    public Item create(Item item) { // ★ メソッド名を変更
        return agent.required(() -> {
            return repository.save(item);
        });
    }

    public List<Item> findAll() { // ★ メソッド名を変更
        return repository.findAll();
    }
    
    // 2. 検索 (READ) : 既存のfindAll()を検索対応版に置き換え
    public List<Item> findItems(String keyword) { 
        // Serviceの責務: 単純にRepositoryに処理を委譲（トランザクションは不要）
        return repository.findItems(keyword);
    }

    public int deleteById(long id) { // ★ メソッド名を変更
        return agent.required(() -> {
            return repository.deleteById(id);
        });
    }

    // ★ 更新 (UPDATE)
    public Item update(Item item) {
        // Serviceの責務: トランザクション管理
        return agent.required(() -> {
            repository.update(item);
            // 更新後、更新されたアイテムをそのまま返す (またはDBから再取得するが、今回はそのまま返す)
            return item;
        });
    }
}
