# Badmini - E-Commerce App Preview Guide

## 📱 Available Previews

Tất cả các screens đều có `@Preview` composables để bạn có thể xem trực tiếp trong Android Studio.

### Cách xem Preview:

1. Mở file screen (\*.kt)
2. Click vào **"Preview"** button bên cạnh `@Preview` annotation
3. Hoặc nhấn `Ctrl+Shift+P` (Windows) / `Cmd+Shift+P` (Mac)

---

## 🎨 Available Screens with Previews

### ✅ LoginScreen

**File:** `ui/screens/login/LoginScreen.kt`

- Bottom sheet animation (login form)
- Email/Password inputs
- Sign in button → navigates to Home
- Sign up link → navigates to Register
- **Preview:** `LoginScreenPreview()`

### ✅ RegisterScreen

**File:** `ui/screens/register/RegisterScreen.kt`

- Bottom sheet with 4 input fields (Full Name, Email, Password, Confirm Password)
- Create Account button → navigates to Home
- Sign in link → navigates to Login
- **Preview:** `RegisterScreenPreview()`

### ✅ HomeScreen

**File:** `ui/screens/home/HomeScreen.kt`

- Purple header with app logo "Badmini" + cart icon
- Banner carousel (3 banners with discounts)
- Search bar
- Categories horizontal scroll
- Featured products grid
- View All Products button
- **Preview:** `HomeScreenPreview()`

### ✅ ProductListScreen

**File:** `ui/screens/products/ProductListScreen.kt`

- Header with back button + search field
- Category filter chips (All, Electronics, Fashion, Home & Garden, Sports)
- Products in 2-column grid layout
- Price display with discount strikethrough
- Add button
- **Preview:** `ProductListScreenPreview()`

### ✅ ProductDetailScreen

**File:** `ui/screens/product_detail/ProductDetailScreen.kt`

- Image carousel with 3 product images
- Product name, rating (stars + count), price
- Full description
- Variants section (Color, Size options)
- Add-ons section with checkboxes (Extended Warranty, Screen Protector, Premium Packaging)
- Reviews summary
- Bottom action bar with quantity selector + Add to Cart
- **Preview:** `ProductDetailScreenPreview()`

### ✅ CartScreen

**File:** `ui/screens/cart/CartScreen.kt`

- Header with back button
- List of cart items with images, quantities, prices
- Delete button for each item
- Voucher code input (test code: "SAVE10" for 10% discount)
- Subtotal, Discount, Total calculation
- Proceed to Checkout button
- Empty cart state with "Continue Shopping"
- **Preview:** `CartScreenPreview()`

### ✅ CheckoutScreen

**File:** `ui/screens/checkout/CheckoutScreen.kt`

- Header with back button
- Shipping address form (Full Name, Email, Phone, Address, City, Zip Code)
- Order summary section (Subtotal, Shipping, Discount, Tax, Total)
- Place Order button
- Order success screen with checkmark icon
- **Preview:** `CheckoutScreenPreview()` + `OrderSuccessScreenPreview()`

---

## 🧪 Testing with Mock Data

### Login / Register

```
Email: any@email.com
Password: anything
→ Will navigate to Home screen
```

### Voucher Code

```
Code: SAVE10
Result: 10% discount applied to cart total
```

### Mock Data

- **8 products** with names, prices, images (placeholder)
- **4 categories** (Electronics, Fashion, Home & Garden, Sports)
- **3 banners** with discount percentages
- **Cart items** pre-loaded with 2 items

---

## 🎯 Navigation Flow

```
Login/Register
    ↓ (login successful)
Home (main page)
    ↓ (click product)
Product Detail (view options, add variants/addons)
    ↓ (click Add to Cart)
Home (back to shopping)
    ↓ (click cart icon)
Cart (review items, apply voucher)
    ↓ (click Proceed to Checkout)
Checkout (enter shipping address)
    ↓ (click Place Order)
Order Success ✓
    ↓ (click Continue Shopping)
Home (loop)
```

---

## 🛠️ Technology Stack

- **UI Framework:** Jetpack Compose + Material3
- **Navigation:** Navigation Compose
- **State Management:** ViewModel + StateFlow
- **Image Loading:** Coil
- **HTTP Client:** Retrofit + OkHttp (configured, mock data used)
- **Theme:** Custom purple color scheme (#635BFF)

---

## 💡 Quick Tips

1. **All Previews are Interactive:**
   - Scroll items
   - Click buttons (some navigations won't work in preview)
   - See animations

2. **Color Theme:**
   - Primary: Purple (#635BFF)
   - Background: White
   - Accents: Gold/Orange for ratings

3. **Responsive Design:**
   - All screens use Compose layout system
   - Works on all phone sizes

4. **Mock vs Real API:**
   - Currently using `MockDataProvider`
   - To switch to real API: Update `ProductRepository` to call `ProductApi` instead of mock
   - All DTOs and API interfaces already defined in `data/remote/api/` and `data/remote/dto/`

---

## 📋 Files Structure

```
ui/
├── screens/
│   ├── login/
│   │   ├── LoginScreen.kt ⭐ (has preview)
│   │   ├── LoginViewModel.kt
│   │   └── LoginUiState.kt
│   ├── register/
│   │   ├── RegisterScreen.kt ⭐ (has preview)
│   │   ├── RegisterViewModel.kt
│   │   └── RegisterUiState.kt
│   ├── home/
│   │   ├── HomeScreen.kt ⭐ (has preview)
│   │   ├── HomeViewModel.kt
│   │   └── HomeUiState.kt
│   ├── products/
│   │   ├── ProductListScreen.kt ⭐ (has preview)
│   │   ├── ProductListViewModel.kt
│   │   └── ProductListUiState.kt
│   ├── product_detail/
│   │   ├── ProductDetailScreen.kt ⭐ (has preview)
│   │   ├── ProductDetailViewModel.kt
│   │   └── ProductDetailUiState.kt
│   ├── cart/
│   │   ├── CartScreen.kt ⭐ (has preview)
│   │   ├── CartViewModel.kt
│   │   └── CartUiState.kt
│   └── checkout/
│       ├── CheckoutScreen.kt ⭐ (has previews)
│       ├── CheckoutViewModel.kt
│       └── CheckoutUiState.kt
├── navigation/
│   └── AppNavigation.kt
└── theme/
    ├── Color.kt
    └── Theme.kt

data/
├── remote/
│   ├── api/
│   │   ├── ProductApi.kt
│   │   └── AuthApi.kt
│   ├── dto/
│   │   ├── HomeResponse.kt
│   │   ├── Product.kt
│   │   ├── CartResponse.kt
│   │   └── (10+ more DTOs)
│   └── RetrofitClient.kt
└── repository/
    ├── ProductRepository.kt (with mock data)
    ├── AuthRepository.kt (with mock data)
    └── MockDataProvider.kt
```

---

## ✨ Features Implemented

✅ Authentication (Login/Register with mock data)
✅ Home screen with banners, categories, featured products
✅ Product listing with filtering and search
✅ Product detail with variants and add-ons
✅ Shopping cart with item management
✅ Voucher code system (test: "SAVE10")
✅ Checkout with shipping form
✅ Order success confirmation
✅ All screens have Preview for quick testing
✅ Proper navigation flow
✅ Mock data for instant testing

---

## 🚀 Ready for Integration

When backend is ready:

1. Update `ProductRepository` to call API instead of `MockDataProvider`
2. Configure real API base URL in `RetrofitClient.kt`
3. Same code structure, just swap data source!

---

**Enjoy exploring Badmini! 🎉**
