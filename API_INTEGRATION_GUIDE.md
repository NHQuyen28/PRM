# 🔗 API Integration Guide - Login & Register

## ✅ What's Been Done

### 1. **Backend API Structure**
The backend (.NET) provides these endpoints:
- **POST** `/api/v1/auth/login` - Login with email & password
- **POST** `/api/v1/auth/register` - Register with fullName, email, password

### 2. **Frontend Updates Made**

#### A. DTOs (Data Transfer Objects)
- ✅ `AuthResponse.kt` - New DTO matching backend response
- ✅ `ApiResponse.kt` - Generic wrapper for all API responses
- ✅ `AuthRepository.kt` - Updated to call real API
- ✅ `SessionManager.kt` - Updated to store tokens & user info

#### B. ViewModels
- ✅ `LoginViewModel.kt` - Now calls AuthRepository and validates input
- ✅ `LoginUiState.kt` - Added `loginSuccess`, `successMessage`
- ✅ `RegisterViewModel.kt` - New file with validation logic
- ✅ `RegisterUiState.kt` - New file with form state

#### C. UI Integration
- ✅ `LoginScreen.kt` - Updated to use ViewModel and handle API responses
- ✅ `RegisterScreen.kt` - Updated to use ViewModel with form validation

---

## 🚀 How to Set Up

### Step 1: Update Backend URL
Edit [RetrofitClient.kt](../../PRM/app/src/main/java/com/example/prm/data/remote/RetrofitClient.kt#L10):

```kotlin
private const val BASE_URL = "http://YOUR_BACKEND_IP:5000/api/v1/"
```

**Options:**
- **Android Emulator:** `http://10.0.2.2:5000/api/v1/`
- **Physical Device:** `http://192.168.X.X:5000/api/v1/` (your machine's local IP)
- **Cloud:** `https://yourdomain.com/api/v1/`

### Step 2: Provide SessionManager to ViewModels (Optional but Recommended)

Currently, ViewModels accept `SessionManager` as an optional parameter for dependency injection. To use it properly:

```kotlin
// In your MainActivity or App initialization
val sessionManager = SessionManager(context)
val loginViewModel = LoginViewModel(sessionManager = sessionManager)
```

### Step 3: Add Internet Permission
Make sure `AndroidManifest.xml` has:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Step 4: Handle Token in API Calls
For future authenticated API calls, add an interceptor to include the token:

```kotlin
private val authInterceptor = Interceptor { chain ->
    val token = sessionManager.getAccessToken()
    val request = chain.request().newBuilder()
        .addHeader("Authorization", "Bearer $token")
        .build()
    chain.proceed(request)
}

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(authInterceptor)
    .addInterceptor(loggingInterceptor)
    .build()
```

---

## 📊 API Response Format

### Login Response (Success)
```json
{
  "success": true,
  "code": "Success",
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGc...",
    "refreshToken": "refresh_token_here",
    "expiresAt": "2026-02-25T12:34:56",
    "user": {
      "id": "12345",
      "email": "user@example.com",
      "fullName": "John Doe",
      "phone": "+1234567890",
      "avatarUrl": "https://...",
      "role": "Customer",
      "roleDisplay": "Customer",
      "status": "Active",
      "statusDisplay": "Active"
    }
  }
}
```

### Register Response (Success)
Same format as login response - user gets tokens immediately after registration.

### Error Response
```json
{
  "success": false,
  "code": "BadRequest",
  "message": "Email already exists or invalid request",
  "data": null
}
```

---

## 🔄 Data Flow

### Login Flow
1. User enters email & password → LoginScreen
2. Clicks "Sign in" button → LoginViewModel.login()
3. LoginViewModel validates input:
   - Email format check
   - Password length (min 6)
4. Calls AuthRepository.login()
5. AuthRepository makes HTTP POST to backend
6. Response is wrapped in ApiResponse<AuthResponse>
7. If success:
   - Save tokens using SessionManager
   - Update UI state with `loginSuccess = true`
   - Navigate to home screen
8. If error:
   - Show error message in UI
   - No navigation

### Register Flow
Same as login, but with:
- Additional fullName field
- Confirm password validation
- Calls AuthRepository.register()

---

## 🧪 Testing

### Test Login Credentials
Use any email from the backend (depends on your test data):
```
Email: test@example.com
Password: password123
```

### Error Scenarios
- **Invalid email format** → Shows validation error immediately
- **Password < 6 characters** → Shows validation error immediately
- **Passwords don't match (Register)** → Shows validation error
- **Network error** → Shows "Connection error"
- **Wrong credentials** → Shows error from backend API

---

## 🛠️ Troubleshooting

### Issue: "Connection refused" or "Network error"
✅ **Solution:**
- Check backend is running: `dotnet run` in ControllerLayer directory
- Verify correct BASE_URL in RetrofitClient
- Check firewall/network connectivity
- Try `http://10.0.2.2:5000/api/v1/` for emulator

### Issue: CORS error
✅ **Solution:**
- Backend already has CORS configured in Program.cs
- No changes needed on frontend

### Issue: 401 Unauthorized on subsequent requests
✅ **Solution:**
- Add the auth interceptor (see Step 4 above)
- Include token in Authorization header for protected endpoints

### Issue: Session not persisting between app launches
✅ **Solution:**
- SessionManager uses SharedPreferences (persistent)
- Check SessionManager.isLoggedIn() before showing login screen
- In MainActivity onCreate, check if already logged in

---

## 📝 File Changes Summary

| File | Status | Changes |
|------|--------|---------|
| AuthApi.kt | ✅ Updated | New response types |
| AuthRepository.kt | ✅ Updated | Calls real API instead of mock |
| SessionManager.kt | ✅ Updated | Stores full AuthResponse |
| LoginViewModel.kt | ✅ Updated | Integrates with repository |
| LoginUiState.kt | ✅ Updated | Success tracking |
| RegisterViewModel.kt | ✅ Created | New file |
| RegisterUiState.kt | ✅ Created | New file |
| AuthResponse.kt | ✅ Created | New DTO file |
| ApiResponse.kt | ✅ Created | New wrapper DTO |
| LoginScreen.kt | ✅ Updated | Uses ViewModel, LaunchedEffect |
| RegisterScreen.kt | ✅ Updated | Uses ViewModel, validation |
| RetrofitClient.kt | ⚠️ Needs Config | Set BASE_URL |

---

## 🎯 Next Steps

1. **Update BASE_URL** with your backend server
2. **Test login** with backend credentials
3. **Test register** with new account
4. **Verify tokens** are stored in SessionManager
5. **Add token to**  subsequent API calls (step 4)
6. **Test logout** functionality
7. **Test token refresh** when expired

---

## 📞 Need Help?

Check backend endpoints in:
- Backend: [AuthController.cs](../PRM-BE/PRM392_SE1828_BadmintonShop/ControllerLayer/Controllers/AuthController.cs)
- DTOs: [AuthRequest.cs](../PRM-BE/PRM392_SE1828_BadmintonShop/ApplicationLayer/DTOs/Request/Auths/AuthRequest.cs)

---

**Integration Status: ✅ Ready for Testing**
