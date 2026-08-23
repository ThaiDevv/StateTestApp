# 📱 State Test App – State Android tồn tại được bao lâu?

> Đề tài: So sánh 5 cơ chế lưu state trong Android Compose  
> Môn: [Tên môn học]  
> Nhóm: [Tên nhóm]

---

## 🗂️ Phân công thành viên

| Thành viên | Cơ chế | File cần implement |
|:---:|---|---|
| **TV1** | `remember` | `screens/RememberScreen.kt` |
| **TV2** | `rememberSaveable` | `screens/RememberSaveableScreen.kt` |
| **TV3** | `ViewModel` | `screens/ViewModelScreen.kt` + hiểu `viewmodel/CounterViewModel.kt` |
| **TV4** | `SavedStateHandle` | `screens/SavedStateScreen.kt` + hiểu `viewmodel/SavedStateViewModel.kt` |
| **TV5** | `DataStore` + Tích hợp | `screens/DataStoreScreen.kt` + merge code + báo cáo |

---

## 🚀 Hướng dẫn clone và chạy app

```bash
# 1. Clone repo về máy
git clone https://github.com/[username]/StateTestApp.git

# 2. Mở Android Studio → Open → chọn thư mục StateTestApp

# 3. Đợi Gradle sync xong (~2-5 phút lần đầu)

# 4. Chạy app trên emulator hoặc thiết bị thật
```

---

## 📂 Cấu trúc project

```
StateTestApp/
├── app/src/main/java/com/nhom/statetestapp/
│   ├── MainActivity.kt              ✅ TV5 (DONE)
│   ├── navigation/
│   │   └── AppNavigation.kt         ✅ TV5 (DONE)
│   ├── screens/
│   │   ├── HomeScreen.kt            ✅ TV5 (DONE)
│   │   ├── PlaceholderScreen.kt     ✅ TV5 (DONE – helper)
│   │   ├── RememberScreen.kt        ⏳ TV1 implement
│   │   ├── RememberSaveableScreen.kt⏳ TV2 implement
│   │   ├── ViewModelScreen.kt       ⏳ TV3 implement
│   │   ├── SavedStateScreen.kt      ⏳ TV4 implement
│   │   └── DataStoreScreen.kt       ⏳ TV5 implement
│   ├── viewmodel/
│   │   ├── CounterViewModel.kt      ✅ TV5 (DONE – TV3 đọc & dùng)
│   │   └── SavedStateViewModel.kt   ✅ TV5 (DONE – TV4 đọc & dùng)
│   └── data/
│       └── DataStoreManager.kt      ✅ TV5 (DONE)
└── ...
```

---

## 📋 Workflow cho mỗi thành viên

### Bước 1: Clone repo và tạo branch riêng
```bash
git checkout -b feature/tv1-remember    # TV1
git checkout -b feature/tv2-saveable    # TV2
git checkout -b feature/tv3-viewmodel   # TV3
git checkout -b feature/tv4-savedstate  # TV4
git checkout -b feature/tv5-datastore   # TV5
```

### Bước 2: Implement file của mình
- Mở file screen của mình (xem bảng phân công ở trên)
- Đọc kỹ comment `TODO` trong file
- Implement UI theo hướng dẫn trong comment
- Xoá `PlaceholderScreen(...)` ở cuối file sau khi implement xong

### Bước 3: Commit và push
```bash
git add .
git commit -m "[TV1] Implement RememberScreen với logging đầy đủ"
git push origin feature/tv1-remember
```

### Bước 4: Tạo Pull Request lên `main`
- Vào GitHub → New Pull Request
- TV5 review và merge

---

## 🔧 Yêu cầu môi trường

- Android Studio **Ladybug** (2024.2.x) hoặc mới hơn
- JDK 11+
- Android SDK 35
- Min Android: API 26 (Android 8.0)

---

## 🧪 Cách test

### Filter Logcat
Trong Android Studio, mở Logcat và filter:
```
TAG: STATE_TEST
```

### Các thao tác test
1. **Recomposition**: Nhập text hoặc bấm nút
2. **Xoay màn hình**: `Ctrl+F11` trong emulator (hoặc xoay thiết bị)
3. **Navigate đi & quay lại**: Bấm nút Back
4. **Tạo lại Activity**: Developer Options → Don't keep activities
5. **Kill process**: `adb shell am kill com.nhom.statetestapp`

---

## 📸 Quy tắc chụp ảnh minh chứng

Mỗi screenshot BẮT BUỘC có:
- [x] Full màn hình (không crop)
- [x] Code đang mở trong Android Studio
- [x] AVD/emulator hiển thị kết quả
- [x] Logcat với filter `STATE_TEST`
- [x] File `chuki_nhom.txt` mở ở góc trên phải

---

## 📌 Tài liệu tham khảo

- [implementation_plan.md](implementation_plan.md) – Hướng dẫn chi tiết từng task
- [Android Compose State](https://developer.android.com/develop/ui/compose/state)
- [ViewModel Overview](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [DataStore Guide](https://developer.android.com/topic/libraries/architecture/datastore)
- [SavedStateHandle](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate)

---

## 👥 Thành viên nhóm

| # | Họ tên | MSSV | Phụ trách |
|---|--------|------|-----------|
| 1 | [Họ tên] | [MSSV] | remember |
| 2 | [Họ tên] | [MSSV] | rememberSaveable |
| 3 | [Họ tên] | [MSSV] | ViewModel |
| 4 | [Họ tên] | [MSSV] | SavedStateHandle |
| 5 | [Họ tên] | [MSSV] | DataStore + Tích hợp |
