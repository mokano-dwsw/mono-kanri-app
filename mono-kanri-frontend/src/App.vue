<script setup>
import { ref, onMounted } from 'vue';
import apiClient from './services/api.js'; // jsのapiClientをインポート

// アイテムのリストを保持するためのリアクティブな変数
const items = ref([]); 
const searchKeyword = ref('');
const loading = ref(false);
const error = ref(null);

// ★★★ フォーム用の状態変数 ★★★
const newItem = ref({
  itemNm: '', // モノの名前
  itemQty: 1, // 将来のために残しますが、今回は固定値でもOKです
  storLoc: '' // 保管場所
});

// ★★★ 検索用の状態変数 ★★★
// 検索ボタンを押すための処理（テンプレートから呼び出される）
const applySearch = () => {
  fetchItems();
};

// ★★★ データ取得ロジック ★★★
const fetchItems = async () => {
  loading.value = true;
  error.value = null;
  try {
    // パスは常に '/items' 固定でOK！
    // 第二引数に params を渡すと、axiosが勝手に ?keyword=... を付けてくれます
    const response = await apiClient.get('/items', {
      params: {
        keyword: searchKeyword.value || undefined // キーワードが空ならパラメータを送らない
      }
    });
    // 取得したデータをitems変数に格納
    items.value = response.data; 
    console.log("アイテム一覧を取得しました:", items.value);
  } catch (err) {
    console.error("アイテム一覧の取得に失敗しました:", err);
    error.value = 'データ取得エラーが発生しました。バックエンドが起動しているか確認してください。';
  } finally {
    loading.value = false;
  }
};

// ★★★ 新規登録ロジック ★★★
const addItem = async () => {
    // itemNmとstorLocが空でないかチェック
    if (!newItem.value.itemNm || !newItem.value.storLoc) {
        alert("モノの名前と場所を入力してください。");
        return;
    }

    try {
        // POSTリクエストを実行
        // Spring Bootのコントローラが期待するJSON形式で送信
        await apiClient.post('/items', newItem.value);
        
        // フォームをクリア
        newItem.value.itemNm = '';
        newItem.value.itemQty = 1;
        newItem.value.storLoc = '';
        
        // 登録後、一覧を再取得して表示を更新
        await fetchItems(); 

    } catch (error) {
        console.error("アイテム登録に失敗しました:", error);
        alert('アイテム登録に失敗しました。詳細をコンソールで確認してください。');
    }
};

// 削除ロジック
const deleteItem = async (itemId, itemNm) => {
    // 削除確認ダイアログを表示
    if (!confirm(`アイテムID: ${itemId} (${itemNm}) を本当に削除しますか？`)) {
        return; // キャンセルの場合は処理を終了
    }

    try {
        // DELETEリクエストを実行 (URLにitemIdを含める)
        await apiClient.delete(`/items/${itemId}`);
        
        // 削除後、一覧を再取得して表示を更新
        await fetchItems(); 

    } catch (error) {
        console.error(`アイテムID: ${itemId} の削除に失敗しました:`, error);
        alert('アイテムの削除に失敗しました。バックエンドが稼働しているか確認してください。');
    }
};

// ★★★ 更新用の状態変数とメソッド ★★★

// 編集中のアイテムIDを保持 (IDがセットされると編集モードON)
const editingItemId = ref(null);

// 編集フォームの入力値を保持するためのオブジェクト
const editedItem = ref({
    itemId: null,
    itemNm: '',
    itemQty: 0,
    storLoc: ''
});

// 編集モードに入る
const startEdit = (item) => {
    editingItemId.value = item.itemId;
    // 現在のアイテムの値を editedItem にコピー
    editedItem.value = { ...item };
};

// 編集をキャンセルする
const cancelEdit = () => {
    editingItemId.value = null;
    editedItem.value = { itemId: null, itemNm: '', itemQty: 0, storLoc: '' };
};

// 更新を実行する (PUT APIの呼び出し)
const saveEdit = async () => {
    if (!editedItem.value.itemNm || !editedItem.value.storLoc) {
        alert("モノの名前と場所を入力してください。");
        return;
    }
    const itemId = editedItem.value.itemId;

    try {
        // PUTリクエストを実行。URLにはID、Bodyには更新後のデータを送信
        await apiClient.put(`/items/${itemId}`, editedItem.value);
        
        // 編集モードを終了し、一覧を再取得して表示を更新
        cancelEdit();
        await fetchItems(); 
    } catch (error) {
        console.error(`アイテムID: ${itemId} の更新に失敗しました:`, error);
        alert('アイテムの更新に失敗しました。');
    }
};

// コンポーネントが画面に表示された後に、一度だけデータ取得を実行
onMounted(fetchItems);
</script>

<template>
  <div class="container">
    <h1>モノ管理リスト</h1>

    <div class="search-form">
      <h2>アイテム検索</h2>
      <form @submit.prevent="applySearch">
        <label for="search">キーワード:</label>
        <input type="text" id="search" v-model="searchKeyword" placeholder="モノ名で検索">
        <button type="submit">検索</button>
        <button type="button" @click="searchKeyword = ''; applySearch();">リセット</button>
      </form>
      <hr>
    </div>

    <div class="registration-form">
      <h2>アイテム新規登録</h2>
      <form @submit.prevent="addItem">
        
        <label for="itemNm">モノの名前:</label>
        <input type="text" id="itemNm" v-model="newItem.itemNm" required>
        
        <label for="itemQty">数量:</label>
        <input type="text" id="itemQty" class="qty-input" v-model="newItem.itemQty" required>
        
        <label for="storLoc">保管場所:</label>
        <input type="text" id="storLoc" v-model="newItem.storLoc" required>
        
        <button type="submit">登録</button>
      </form>
      <hr>
    </div>
    
    <div v-if="items.length > 0">
      <h2>登録済みアイテム ({{ items.length }} 件)</h2>
      <ul>
        <li v-for="item in items" :key="item.itemId">
          <template v-if="editingItemId === item.itemId">
            <div class="edit-mode">
              モノ名: <input type="text" v-model="editedItem.itemNm">
              数量: <input type="text" class="qty-input" v-model="editedItem.itemQty">
              場所: <input type="text" v-model="editedItem.storLoc">
              <button @click="saveEdit()">保存</button>
              <button @click="cancelEdit()">キャンセル</button>
            </div>
          </template>

          <template v-else>
            <span @dblclick="startEdit(item)">
              ID: {{ item.itemId }} |
              モノ名: **{{ item.itemNm }}** |
              数量: {{ item.itemQty }} |
              場所: {{ item.storLoc }}
            </span>
            <button @click="startEdit(item)" class="edit-button">編集</button>
            <button @click="deleteItem(item.itemId, item.itemNm)" class="delete-button">削除</button>
          </template> 
        </li>
      </ul>
    </div>
    <div v-else>
      <p>アイテムがまだ登録されていません。</p>
    </div>
  </div>
</template>

<style scoped>
  .qty-input {
    width: 60px;
    margin-right: 10px;
    text-align: right; 
  }
</style>
