package com.example.demo.seeder;

import com.example.demo.Feedback.entity.FeedbackStatus;
import com.example.demo.Feedback.entity.FeedbackTypes;
import com.example.demo.Feedback.repository.FeedbackStatusRepository;
import com.example.demo.Feedback.repository.FeedbackTypesRepository;
import com.example.demo.admin.Admin;
import com.example.demo.admin.AdminPosition;
import com.example.demo.admin.AdminRepository;
import com.example.demo.store.entity.Category;
import com.example.demo.store.entity.StoresInfo;
import com.example.demo.store.repository.CategoryRepository;
import com.example.demo.store.repository.StoreInfoRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.MapSearch.service.GeocodingService;
import com.example.demo.shop.entity.Products;
import com.example.demo.shop.entity.Stock;
import com.example.demo.shop.repository.ProductsRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StoreInfoRepository storeInfoRepository;
    private final AdminRepository adminRepository;
    private final FeedbackStatusRepository feedbackStatusRepository;
    private final FeedbackTypesRepository feedbackTypesRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeocodingService geocodingService;
    private final ProductsRepository productsRepository;

    @Value("${app.seeder.enabled:false}")
    private boolean seederEnabled;

    public DatabaseSeeder(
            UserRepository userRepository,
            StoreInfoRepository storeInfoRepository,
            AdminRepository adminRepository,
            FeedbackStatusRepository feedbackStatusRepository,
            FeedbackTypesRepository feedbackTypesRepository,
            CategoryRepository categoryRepository,
            PasswordEncoder passwordEncoder,
            GeocodingService geocodingService,
            ProductsRepository productsRepository) {
        this.userRepository = userRepository;
        this.storeInfoRepository = storeInfoRepository;
        this.adminRepository = adminRepository;
        this.feedbackStatusRepository = feedbackStatusRepository;
        this.feedbackTypesRepository = feedbackTypesRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.geocodingService = geocodingService;
        this.productsRepository = productsRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (!seederEnabled) {
            System.out.println("Data Seeder is disabled.");
            return;
        }

        System.out.println("Starting to seed database...");

        seedFeedbackStatus();
        seedFeedbackTypes();
        seedCategories();
        seedAdmins();
        seedUsersAndStores(); // 產生User與Stores
        seedProducts(); // 來自原本的 DataInitializer

        System.out.println("Database seeding completed.");
    }

    private void seedFeedbackStatus() {
        if (feedbackStatusRepository.count() == 0) {
            List<String> statuses = Arrays.asList("待處理", "已處理", "待致電", "追蹤中");
            for (String statusName : statuses) {
                FeedbackStatus status = new FeedbackStatus();
                status.setStatusName(statusName);
                feedbackStatusRepository.save(status);
            }
            System.out.println("Seeded 4 FeedbackStatuses.");
        }
    }

    private void seedFeedbackTypes() {
        if (feedbackTypesRepository.count() == 0) {
            List<String> types = Arrays.asList("意見回饋", "餐廳訂位", "商品訂單", "問題通報", "其他");
            for (String typeName : types) {
                FeedbackTypes type = new FeedbackTypes();
                type.setTypeName(typeName);
                feedbackTypesRepository.save(type);
            }
            System.out.println("Seeded 5 FeedbackTypes.");
        }
    }

    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            List<String> categories = Arrays.asList("提供插座", "寵物友善", "無障礙設施", "有停車場", "素食餐點");
            for (String catName : categories) {
                Category category = new Category();
                category.setCategoryName(catName);
                categoryRepository.save(category);
            }
            System.out.println("Seeded 5 Categories.");
        }
    }

    private void seedAdmins() {
        if (adminRepository.count() == 0) {
            String defaultPassword = passwordEncoder.encode("1qaz@WSX");
            
            // {account, name, email, positionName}
            String[][] adminData = {
                    {"sa", "測試總管理員", "sa@admin.com", "SUPER_ADMIN"},
                    {"hr", "測試人事", "hr@admin.com", "HUMAN_RESOURCE"},
                    {"cs", "測試客服", "cs@admin.com", "CUSTOMER_SERVICE"},
                    {"sh", "測試電商", "sh@admin.com", "SHOP_MANAGER"}
            };

            for (String[] data : adminData) {
                AdminPosition position = AdminPosition.valueOf(data[3]);
                Admin admin = Admin.builder()
                        .account(data[0])
                        .password(defaultPassword)
                        .name(data[1])
                        .position(position)
                        .email(data[2])
                        .enabled(true)
                        .build();
                adminRepository.save(admin);
            }
            System.out.println("Seeded 4 Admins.");
        }
    }

    private void seedUsersAndStores() {
        if (userRepository.count() == 0) {
            Faker faker = new Faker(Locale.TAIWAN);
            String defaultPassword = passwordEncoder.encode("1qaz@WSX");

            // 預設的使用者名稱與信箱列表
            String[][] userData = {
                    {"Liam", "liam@test.com"},
                    {"Noah", "noah@test.com"},
                    {"Oliver", "oliver@test.com"},
                    {"Elijah", "elijah@test.com"},
                    {"James", "james@test.com"},
                    {"William", "william@test.com"},
                    {"Benjamin", "benjamin@test.com"},
                    {"Lucas", "lucas@test.com"},
                    {"Henry", "henry@test.com"},
                    {"Alexander", "alexander@test.com"},
                    {"Mason", "mason@test.com"},
                    {"Michael", "michael@test.com"},
                    {"Ethan", "ethan@test.com"},
                    {"Daniel", "daniel@test.com"},
                    {"Jacob", "jacob@test.com"},
                    {"Logan", "logan@test.com"},
                    {"Jackson", "jackson@test.com"},
                    {"Levi", "levi@test.com"},
                    {"Sebastian", "sebastian@test.com"},
                    {"Mateo", "mateo@test.com"},       // 第20筆 (含) 之前是 Store User
                    {"Jack", "jack@test.com"},         // 第21筆起是 Normal User
                    {"Owen", "owen@test.com"},
                    {"Theodore", "theodore@test.com"},
                    {"Aiden", "aiden@test.com"},
                    {"Samuel", "samuel@test.com"},
                    {"Joseph", "joseph@test.com"},
                    {"John", "john@test.com"},
                    {"David", "david@test.com"},
                    {"Wyatt", "wyatt@test.com"},
                    {"Matthew", "matthew@test.com"}
            };

            List<User> storeUsers = new ArrayList<>();
            List<User> normalUsers = new ArrayList<>();

            // 1. 產生 30 筆 User，前 20 筆 isStore=true，後 10 筆 isStore=false
            for (int i = 0; i < userData.length; i++) {
                boolean isStore = i < 20;
                User user = User.builder()
                        .name(userData[i][0])
                        .email(userData[i][1])
                        .password(defaultPassword)
                        .isStore(isStore)
                        .enabled(true)
                        .twoFactorEnabled(false)
                        .build();

                if (isStore) {
                    storeUsers.add(user);
                } else {
                    normalUsers.add(user);
                }
            }

            // 儲存所有 Users
            storeUsers = userRepository.saveAll(storeUsers);
            userRepository.saveAll(normalUsers);
            System.out.println("Seeded 30 Users (20 Store Users, 10 Normal Users).");

            // 2. 針對 20 名 Store User 產生 StoresInfo
            String[][] storeData = {
                    {"日初壽司屋", "新北市中和區中山路三段122號", "02-2721-5508"},
                    {"山海樓私房料理", "新北市三重區重新路二段21號", "04-2326-8801"},
                    {"老王滷肉飯", "桃園市桃園區中正路47號", "02-2556-0931"},
                    {"森林早午餐", "新北市淡水區中山路8號", "0978-351-206"},
                    {"古早味排骨飯", "高雄市前金區中華三路21號", "04-2231-5094"},
                    {"川味麻婆食堂", "南投縣草屯鎮太平路二段294號", "04-2228-3157"},
                    {"港都海產店", "桃園市龜山區復興一路8號", "07-551-3372"},
                    {"巷弄義麵屋", "台南市永康區中正南路358號", "0972-604-773"},
                    {"藍海景觀餐廳", "屏東縣屏東市民生路295號", "03-978-6612"},
                    {"金色三麥餐酒館", "南投縣埔里鎮中正路399號", "0937-115-624"},
                    {"米香台菜餐廳", "台中市西屯區台灣大道四段1038號", "02-2395-8210"},
                    {"香辣川味館", "台北市士林區中正路115號", "03-422-5631"},
                    {"海港鮮味餐廳", "台北市信義區松壽路12號", "07-332-9184"},
                    {"阿婆鹹酥雞", "雲林縣虎尾鎮林森路一段491號", "0916-875-320"},
                    {"暖心鍋物", "新北市新莊區中正路425號", "0933-642-851"},
                    {"富士山丼飯", "高雄市鳳山區中山路149號", "0914-992-385"},
                    {"小時光甜點店", "金門縣金城鎮民生路45號", "0975-210-943"},
                    {"滿福餃子館", "台中市北區三民路三段161號", "0955-481-903"},
                    {"龍門客棧川菜", "花蓮縣花蓮市中山路295號", "02-2388-1723"},
                    {"金月韓式料理", "台南市安平區華平路711號", "02-2735-6219"}
            };

            List<Category> allCategories = categoryRepository.findAll();
            for (int i = 0; i < 20; i++) {
                User storeUser = storeUsers.get(i);
                StoresInfo store = new StoresInfo();
                store.setUser(storeUser);
                store.setStoreName(storeData[i][0]);
                store.setAddress(storeData[i][1]);
                store.setStorePhone(storeData[i][2]);
                store.setDescription(faker.lorem().paragraph());
                
                // Image Logic: store0.jpg ~ store5.jpg
                store.setCoverImage("store" + (i % 6) + ".jpg");

                // 使用 GeocodingService 將地址轉換成經緯度
                BigDecimal[] coords = geocodingService.geocode(storeData[i][1]);
                if (coords != null && coords.length == 2) {
                    store.setLatitude(coords[0]);
                    store.setLongitude(coords[1]);
                } else {
                    // 若獲取失敗則預設給一個經緯度
                    store.setLatitude(BigDecimal.valueOf(22.0 + Math.random() * 3.0));
                    store.setLongitude(BigDecimal.valueOf(120.0 + Math.random() * 2.0));
                }
                
                // 隨機關聯 1~3 個 Categories
                int numCategories = faker.number().numberBetween(1, 4);
                List<Category> randomCategories = new ArrayList<>();
                for (int c = 0; c < numCategories; c++) {
                    Category randomCat = allCategories.get(faker.number().numberBetween(0, allCategories.size()));
                    if (!randomCategories.contains(randomCat)) {
                        randomCategories.add(randomCat);
                    }
                }
                store.setCategories(randomCategories);

                storeInfoRepository.save(store);
            }
            System.out.println("Seeded 20 Stores for the 20 Store Users.");
        }
    }

    private void seedProducts() {
        if (productsRepository.count() > 0) {
            return;
        }

        System.out.println("Starting to seed products...");

        saveProduct("限量小農鮮蔬盒", "生鮮", new BigDecimal("499"),
            "嚴選在地小農友善耕作蔬菜，新鮮現採直送。無毒栽培降低農藥殘留風險，吃得更安心。支持在地農業同時減少長途運輸碳排放。", 50, "/productPictures/VeggieBox.jpg");

        saveProduct("【季節限定】柿柿如意鮮果禮盒", "生鮮", new BigDecimal("799"),
            "精選當季新鮮柿子，由在地農民用心栽種。自然熟成風味香甜，無過度人工催熟處理。產地直送縮短運輸流程，保留最佳鮮度。", 30, "/productPictures/persimmon.jpg");

        saveProduct("田野直送鮮蔬箱", "生鮮", new BigDecimal("529"),
            "嚴選在地小農每日現採蔬菜，新鮮直送到家，保留最自然的風味與營養。採用友善耕作方式種植，減少農藥與化學肥料使用。", 40, "/productPictures/Veggie.jpg");

        saveProduct("旬採鮮果禮盒", "生鮮", new BigDecimal("649"),
            "精選當季成熟水果，由小農自然栽培，無過度催熟與人工加工。果實在最佳熟度採收，保留最純粹甜味與香氣。", 35, "/productPictures/fruit.jpg");

        saveProduct("永恆工藝．循環餐具組【流光金】", "日常用品", new BigDecimal("680"),
            "霧面金質感設計，兼具時尚與耐用性，外食或露營都適用。採用可重複使用材質製作，大幅減少一次性餐具浪費。", 60, "/productPictures/gold.jpg");

        saveProduct("永恆工藝．循環餐具組【月光銀】", "日常用品", new BigDecimal("580"),
            "俐落極簡外型搭配高耐用金屬材質，長期使用不易損耗。適合上班族、學生與外食族隨身攜帶。", 60, "/productPictures/silver.jpg");

        saveProduct("植萃癒合．三效美體護理組(三合一)", "日常用品", new BigDecimal("1880"),
            "結合乳液、精華油與面霜三步驟完整保養。植物來源成分溫和親膚，同時降低對環境負擔。深層滋養與鎖水保濕一次完成。", 25, "/productPictures/cream.jpg");

        saveProduct("【天然原萃】手作無添加果糖(減糖)", "食品", new BigDecimal("250"),
            "僅使用純天然水果與零卡替代糖，不含人工色素與防腐劑。口感自然清甜，適合大人小孩安心享用。採用可回收包裝。", 80, "/productPictures/candy.jpg");

        saveProduct("【大地秘境】產地直送純淨辛香料", "食品", new BigDecimal("480"),
            "嚴選自永續農法認證在地小農，不噴灑化學農藥。100%純粹，絕不添加防腐劑、人工色素、味精。採用自然風乾與低溫研磨技術。", 70, "/productPictures/spices.jpg");

        saveProduct("純淨補水．Tritan 永續運動瓶", "日常用品", new BigDecimal("450"),
            "輕量防漏運動水杯(800ml)，醫療級 Tritan™ 材質，不含雙酚 A (BPA Free)。減少瓶裝水依賴，即便損壞也可回收處理。", 45, "/productPictures/bottle.jpg");

        saveProduct("植感定型．天然蠟豆造型髮蠟", "日常用品", new BigDecimal("420"),
            "採用植物蠟與天然油脂配方，好清洗、不殘留，不造成頭皮負擔。不排放難分解化學物質，讓造型也很有環保意識。", 55, "/productPictures/hairWax.jpg");

        saveProduct("愛地球環保摺疊紙袋", "日常用品", new BigDecimal("80"),
            "採用環保再生紙材製成，可自然分解，減少塑膠袋對環境造成的負擔。袋身厚實耐用，承重力佳，購物、外出、送禮皆適用。", 200, "/productPictures/bag.jpg");

        System.out.println("Seeded 12 Products.");
    }

    private void saveProduct(String name, String category, BigDecimal price, String description, int stockQty, String image) {
        Products p = new Products();
        p.setProductName(name);
        p.setCategory(category);
        p.setPrice(price);
        p.setProductDescription(description);
        p.setImage(image);

        Stock stock = new Stock();
        stock.setAvailableQuantity(stockQty);
        stock.setProduct(p);
        p.setStock(stock);

        // 先存取得 productId，再設定商品編號
        Products saved = productsRepository.save(p);
        saved.setProductCode(String.format("PRD-%04d", saved.getProductId()));
        productsRepository.save(saved);
    }
}
