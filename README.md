# 🛒 iSpan E-Commerce Project 

![Vue.js](https://img.shields.io/badge/Vue%203-35495E?style=for-the-badge&logo=vuedotjs&logoColor=4FC08D)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![SQL Server](https://img.shields.io/badge/SQL_Server-CC292B?style=for-the-badge&logo=microsoft-sql-server&logoColor=white)

這是一個全端電子商務平台專案，前端採用 **Vue 3 (Composition API)** 與 **Vite**，後端採用 **Java Spring Boot 3** 建構，並搭配 MS SQL Server 作為資料庫。

本 README 主要展示與記錄 **商城模組 (Shop)** 的核心開發細節與技術亮點。

---

## ✨ 負責模組：商城系統 (Shop) 全端開發

本專案中，我獨立負責 **商城模組 (Shop)** 的全端開發。包含後端 API 設計與資料庫操作，以及前端前台購物體驗、後台商品庫存管理的完整實作。

### 🛠️ 技術選型與框架 (Tech Stack)

#### 前端 (Front-End)
- **核心框架**: Vue 3 (Composition API), Vite
- **狀態管理**: Pinia (集中管理購物車與商品庫存狀態)
- **路由管理**: Vue Router
- **UI 函式庫**: PrimeVue 4, Bootstrap 5, SweetAlert2
- **網路請求**: Axios(封裝 API 請求，提升維護性)

- **其他套件**: 
  - `sweetalert2`: 用於優化的彈跳視窗與提示。
  - `leaflet`: 用於地圖功能呈現。
  - `tw-city-selector`: 台灣縣市/鄉鎮區下拉選單。

### 後端 (Back-End)
- **核心框架**: Java 17, Spring Boot 3.5.10
- **資料存取**: Spring Data JPA, Hibernate
- **資料庫**: MS SQL Server (JDBC)


---

## 💻 核心功能與開發特色 (Methods & Features)

1. **購物與訂單系統**
   - 購物車功能
   - 金流串接 (ezpay)
   - 自動化信件通知
     - **訂單確認信**: 成立訂單但尚未付款時寄送，包含去識別化的地址與訂單明細。
     - **付款成功信**: 透過 ezpay 完成刷卡/繳費後寄出確認信。

2. **後台管理平台**
   - 商品管理、訂單管理。

### 1. 順暢的購物體驗與全域狀態同步 (前台商城)
- **集中式狀態管理 (Pinia)**: 購物車最注重資料的一致性。我透過 `stores/carts.js` 與 `stores/productsDepot.js` 集中控管商品與購物車資訊。使用者在瀏覽商品 (`ShopStore.vue`)、查看明細 (`productsDetail.vue`) 與打開購物車 (`ShopCart.vue`) 時，數量與金額皆能 **即時、無延遲地雙向同步**。
- **API 邏輯解耦 (Separation of Concerns)**: 將購物車相關的 API 請求獨立封裝於 `api/cart.js`，讓視圖層 (View) 的 Vue 檔專注於畫面渲染與使用者互動，大幅提升專案結構的可讀性與後續維護彈性。
- **使用者體驗優化 (UX)**: 結合定制的 `OrderTOAST.vue` 實作非侵入式微互動 (Micro-interactions)，當商品加入購物車或產生異動時，會給予流暢適中的吐司通知回饋。

### 2. 流暢的結帳與訂單處理 (交易流程)
- **一條龍動線設計**: 從進入結帳頁面 (`Checkout.vue`) 到最終付款結果的展示 (`PaymentResult.vue`)，實作了流暢的引導與購物服務。
- **訂單狀態精準暫存**: 考慮到交易中斷與跳轉的情況，透過 Pinia (`stores/orderDepot.js`) 穩妥掌握結帳過程中的暫存狀態，同時為使用者提供清楚的歷史訂單查詢介面 (`UserInfoOrders.vue`)。

### 3. 高內聚的後台管理介面 (商家後台)
- **頁籤化 (Tab) 與元件化設計**: 後台的商品管理功能非常繁雜。為了避免單一頁面變成難以維護的「千行程式碼」，我將介面依據**功能職責**拆分為多個獨立的子元件：
  - `TabIncreaseProducts.vue` (新增商品)
  - `TabStock.vue` / `tabStockCRUD.vue` (庫存盤點與庫存異動)
  - `TabUpdateProducts.vue` (商品資訊更新)
- **管理清單整合**: 實作 `BackEndProductsList.vue` 與 `BackEndproductsOrders.vue`，讓商家能以最具結構性且直覺的方式，管理上架商品以及檢視賣場訂單。

### 4. 進階的開發輔助工具
- **前端自定義日誌系統**: 特別撰寫了 `utils/shopLogger.js` 工具來封裝前端的 `console.log`。藉此在開發階段能詳細追蹤 API 的生命週期與狀態變化；而在專案打包部署 (Production) 時又能輕鬆關閉，不僅保障前端效能，更避免了敏感資料外洩的資安風險。

---

## 📂 負責的檔案結構一覽

```text
front-end/
├── src/
│   ├── api/
│   │   └── cart.js                  # 購物車 API 請求層封裝
│   ├── component/
│   │   ├── common/banner.vue        # 頁面橫幅共用元件
│   │   ├── OrderTOAST.vue           # 訂單操作專屬提示元件
│   │   ├── StoreCard.vue            # 商城商品卡片元件
│   │   ├── TabIncreaseProducts.vue  # [後台] 新增商品功能頁籤
│   │   ├── TabStock.vue             # [後台] 庫存總覽功能頁籤
│   │   ├── TabStockCRUD.vue         # [後台] 庫存異動功能頁籤
│   │   └── TabUpdateProducts.vue    # [後台] 更新商品功能頁籤
│   ├── stores/
│   │   ├── carts.js                 # 購物車 Pinia Store
│   │   ├── orderDepot.js            # 訂單資料 Pinia Store
│   │   └── productsDepot.js         # 商品庫存 Pinia Store
│   ├── utils/
│   │   └── shopLogger.js            # 開發環境專用日誌工具
│   └── views/
│       ├── BackEndProductsList.vue  # [後台] 商家商品列表管理頁
│       ├── BackEndproductsOrders.vue# [後台] 商家訂單清單管理頁
│       ├── Checkout.vue             # [前台] 結帳作業頁面
│       ├── PaymentResult.vue        # [前台] 付款結果與回饋頁面
│       ├── ProductsDetail.vue       # [前台] 商品單獨詳細資訊頁
│       ├── ShopCart.vue             # [前台] 購物車統整頁面
│       ├── ShopStore.vue            # [前台] 商城主頁 (首頁)
│       └── UserInfoOrders.vue       # [前台] 會員歷史訂單查詢頁
│
back-end/
└── src/...                          # Shop 模組全功能對應之 Entity, Controller, Service, Repository
```

---

## 🚀 本地開發與啟動指南

### 前置作業
- 環境需求：請確保電腦已安裝 **Node.js** (建議 v20 以上)、**Java 17** 以及 **MS SQL Server**。

### 1. 啟動後端服務 (Spring Boot)
開啟終端機，切換至 `back-end` 目錄：
```bash
cd back-end
# 使用 Maven Wrapper 啟動 (Windows)
mvnw.cmd spring-boot:run
```

### 2. 啟動前端服務 (Vue + Vite)
開啟終端機，切換至 `front-end` 目錄：
```bash
cd front-end
# 安裝相依套件並啟動本地伺服器
npm install
npm run dev
```
啟動後，請依循終端機提示的 Localhost 網址 (`http://localhost:5173/`) 進行預覽。
