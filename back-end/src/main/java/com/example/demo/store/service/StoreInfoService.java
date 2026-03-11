package com.example.demo.store.service;

import com.example.demo.store.dto.StoreCreateUpdateDto;
import com.example.demo.store.entity.Category;
import com.example.demo.store.entity.StoresInfo;
import com.example.demo.store.repository.CategoryRepository;
import com.example.demo.store.repository.StoreInfoRepository;
import com.example.demo.user.UserRepository;
import com.example.demo.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class StoreInfoService extends StoreBaseService {

    private final CategoryRepository categoryRepository;

    // 圖片儲存路徑（從設定檔讀取，若無則使用預設值）
    @Value("${app.upload.store-profile-dir:front-end/public/pictures/StoreProfile}")
    private String uploadDir;

    // 手動寫建構子來調用 super(...)
    public StoreInfoService(UserRepository userRepository,
            StoreInfoRepository storeInfoRepository,
            CategoryRepository categoryRepository) {
        super(userRepository, storeInfoRepository); // 將依賴傳遞給父類別
        this.categoryRepository = categoryRepository;
    }

    // 根據 ID 取得店家資訊 (公開 API 使用)
    public StoresInfo getStoreById(Integer id) {
        return storeInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + id + " 的店家"));
    }

    // 更新店家資訊，預設輸入 null，表示不更新該欄位資料
    @Transactional
    public StoresInfo updateMyStoreInfo(StoreCreateUpdateDto dto) {

        // 取得目前登入使用者的店家
        StoresInfo store = getMyStore();

        // 變更名稱
        if (dto.getStoreName() != null) {
            store.setStoreName(dto.getStoreName());
        }

        // 變更簡介
        if (dto.getDescription() != null) {
            store.setDescription(dto.getDescription());
        }

        // 變更電話
        if (dto.getStorePhone() != null) {
            store.setStorePhone(dto.getStorePhone());
        }

        // 變更地址(待處理地圖座標API)
        if (dto.getAddress() != null) {
            store.setAddress(dto.getAddress());
        }

        // 處理圖片邏輯：若有上傳新圖，則優先執行上傳(會覆蓋舊圖)；若無新圖但要求刪除，則執行刪除
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            imageUpload(store, dto.getImageFile());
        } else if (Boolean.TRUE.equals(dto.getRemoveImage())) { // 寫這樣在上傳新圖後反悔取消時才不會刪除舊有圖片
            removeExistingImage(store);
        }

        // 處理標籤邏輯：先選取標籤，再加入或刪除
        if (Boolean.TRUE.equals(dto.getUpdateCategories())) {
            if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
                // 找出所有對應的標籤實體
                List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
                // 更新店家的標籤清單
                store.setCategories(categories);
            } else {
                // 若 updateCategories 為 true 且 categoryIds 為空或 null，則清空標籤
                store.setCategories(java.util.Collections.emptyList());
            }
        }
        return storeInfoRepository.save(store);
    }

    // 取得圖片上傳的路徑
    private Path getUploadPath() {
        Path rootPath = Paths.get("").toAbsolutePath();
        if (rootPath.toString().endsWith("back-end")) {
            return rootPath.getParent().resolve(uploadDir);
        } else {
            return rootPath.resolve(uploadDir);
        }
    }

    // 刪除圖片
    private void removeExistingImage(StoresInfo store) {
        if (store.getCoverImage() != null && !store.getCoverImage().isEmpty()) { // 確保有圖片才嘗試刪除
            Path oldFilePath = getUploadPath().resolve(store.getCoverImage());
            try {
                // 嘗試刪除檔案
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                // 拋出例外中斷執行
                throw new RuntimeException("無法刪除舊圖片，上傳終止: " + e.getMessage());
            }

            // 刪除成功(無報錯)執行
            store.setCoverImage(null);
        }
    }

    // 圖片上傳的執行
    private void imageUpload(StoresInfo store, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上傳的圖片檔案不得為空");
        }
        try {
            // 1. 確定儲存路徑
            Path uploadPath = getUploadPath();

            // 確保資料夾存在
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 2. 刪除原有圖片 (複用邏輯)
            removeExistingImage(store);

            // 3. 儲存新圖片
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 圖片檔案使用 storeId 和 timestamp 命名，確保唯一性
            String fileName = "store_" + store.getStoreId() + "_" + System.currentTimeMillis() + extension;
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            // 4. 更新 Entity 中的檔名
            store.setCoverImage(fileName);

        } catch (IOException e) {
            throw new RuntimeException("伺服器異常，上傳終止: " + e.getMessage());
        }
    }

}
