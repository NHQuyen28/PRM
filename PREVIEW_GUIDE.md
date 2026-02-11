# 🎨 How to View Previews in Android Studio

## Quick Start

### Method 1: Click Preview Button (Easiest)

1. Open any screen file (e.g., `LoginScreen.kt`)
2. Look for the **`@Preview`** annotation near the bottom of the file
3. Click the **"Preview"** icon that appears to the left of the annotation
4. The preview will appear on the right side panel

### Method 2: Use Keyboard Shortcut

- **Windows/Linux:** `Ctrl + Shift + P`
- **Mac:** `Cmd + Shift + P`
- This opens the "Design" tab with previews

### Method 3: Enable Preview in Editor

1. Go to **View → Tool Windows → Preview**
2. The preview panel will open on the right
3. Navigate to your screen file
4. Preview updates automatically

---

## 📱 Screens to Preview

| Screen             | File                                               | Preview Function               |
| ------------------ | -------------------------------------------------- | ------------------------------ |
| **Login**          | `ui/screens/login/LoginScreen.kt`                  | `LoginScreenPreview()`         |
| **Register**       | `ui/screens/register/RegisterScreen.kt`            | `RegisterScreenPreview()`      |
| **Home**           | `ui/screens/home/HomeScreen.kt`                    | `HomeScreenPreview()`          |
| **Products**       | `ui/screens/products/ProductListScreen.kt`         | `ProductListScreenPreview()`   |
| **Product Detail** | `ui/screens/product_detail/ProductDetailScreen.kt` | `ProductDetailScreenPreview()` |
| **Cart**           | `ui/screens/cart/CartScreen.kt`                    | `CartScreenPreview()`          |
| **Checkout**       | `ui/screens/checkout/CheckoutScreen.kt`            | `CheckoutScreenPreview()`      |
| **Order Success**  | `ui/screens/checkout/CheckoutScreen.kt`            | `OrderSuccessScreenPreview()`  |

---

## 💡 Preview Features

✅ **Interactive Previews:**

- Scroll through lists and grids
- See animations (if defined in code)
- View different states

✅ **Multiple Device Sizes:**

- Click the device selector in preview panel
- Test on phone, tablet, foldable

✅ **Dark Mode:**

- Enable dark mode toggle in preview settings
- See how colors adapt

✅ **Orientation:**

- Switch between portrait and landscape

---

## 🎯 Tips for Better Previews

1. **Use `showSystemUi = true`** → Shows status bar and navigation bar

   ```kotlin
   @Preview(showBackground = true, showSystemUi = true)
   ```

2. **Use `showBackground = true`** → Adds white background (better visibility)

   ```kotlin
   @Preview(showBackground = true)
   ```

3. **Set device size:**

   ```kotlin
   @Preview(device = "spec:parent_focus")
   ```

4. **Set custom name for preview:**
   ```kotlin
   @Preview(name = "Login Screen - Light Mode")
   ```

---

## 🚀 Example: View LoginScreen Preview

1. Open `app/src/main/java/com/example/prm/ui/screens/login/LoginScreen.kt`
2. Scroll to the bottom (around line 250)
3. Find:
   ```kotlin
   @Preview(showBackground = true, showSystemUi = true)
   @Composable
   fun LoginScreenPreview() {
       LoginScreen(...)
   }
   ```
4. Click the **Preview** icon to the left of `@Preview`
5. The login screen will render on the right!

---

## 🖼️ What You'll See

### LoginScreen

- Purple "Badmini" logo at top
- Bottom sheet with email/password fields
- Sign in and Sign up buttons

### HomeScreen

- Purple header with "Badmini" title
- Carousel with product banners
- Horizontal category scroll
- Grid of featured products

### ProductDetailScreen

- Large product image with carousel
- Product name, rating, price
- Variants (color, size) section
- Add-ons with checkboxes
- Bottom action bar with quantity selector

### CartScreen

- List of items with remove buttons
- Subtotal and total calculation
- Voucher code input
- Proceed to Checkout button

### CheckoutScreen

- Shipping address form
- Order summary
- Calculated totals (subtotal + shipping - discount + tax)

---

## ⚠️ If Preview Doesn't Show

1. **Build the project first:**
   - `Build → Make Project`
   - Or `Ctrl + F9`

2. **Sync Gradle files:**
   - `File → Sync Now`
   - Or `Ctrl + Shift + O`

3. **Restart Android Studio:**
   - File → Restart IDE

4. **Check device requirements:**
   - Need API level 25+ to run previews
   - Check `build.gradle` if needed

---

## 🎭 Testing Different States

You can create multiple preview functions for different states:

```kotlin
@Preview(name = "Loading State")
@Composable
fun ProductListPreviewLoading() {
    ProductListScreen()
    // Will show loading indicators
}

@Preview(name = "Empty State")
@Composable
fun CartScreenPreviewEmpty() {
    // Cart with no items
}

@Preview(name = "With Error")
@Composable
fun ProductDetailPreviewError() {
    // Error message displayed
}
```

Currently, we have basic previews showing normal states. Feel free to add more!

---

## 📚 More Info

- [Jetpack Compose Preview Documentation](https://developer.android.com/jetpack/compose/tooling)
- [Android Studio Compose Preview Guide](https://developer.android.com/studio/preview)

---

**Happy previewing! 🎉**
