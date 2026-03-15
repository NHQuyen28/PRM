package com.example.prm.ui.screens.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items as lazyRowItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.prm.data.remote.dto.Category
import com.example.prm.data.remote.dto.Product
import com.example.prm.ui.theme.PurpleJobsly
import androidx.compose.ui.text.TextStyle

@Composable
fun ProductListScreen(
    navController: NavHostController,
    initialSearch: String? = null,
    initialCategoryId: String? = null,
    viewModel: ProductListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFilters by remember { mutableStateOf(false) }
    var sortBy by remember { mutableStateOf("popularity") } // popularity, price_low, price_high, newest

    LaunchedEffect(initialSearch, initialCategoryId) {
        initialSearch?.let { viewModel.onSearchChange(it) }
        initialCategoryId?.let { viewModel.onCategorySelect(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // Header with Back & Filter
            ProductHeaderModern(
                navController = navController,
                searchQuery = uiState.searchQuery,
                onSearchChange = { viewModel.onSearchChange(it) },
                onFilterClick = { showFilters = !showFilters }
            )

            // Category Filter Chips
            if (uiState.categories.isNotEmpty()) {
                ModernCategoryFilter(
                    categories = uiState.categories,
                    selectedCategoryId = uiState.selectedCategoryId,
                    onCategorySelect = {
                        viewModel.onCategorySelect(if (it == uiState.selectedCategoryId) null else it)
                    }
                )
            }

            // Sort & View Options
            SortBar(sortBy) { sortBy = it }

            // Products Grid
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = PurpleJobsly,
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }

                uiState.products.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "😢 No Products Found",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Try adjusting filters or search term",
                                fontSize = 13.sp,
                                color = Color(0xFF999),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.products) { product ->
                            ModernProductGridCard(
                                product = product,
                                onProductClick = {
                                    navController.navigate("product_detail/${product.id}")
                                },
                                onAddClick = { }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================== HEADER ====================

@Composable
private fun ProductHeaderModern(
    navController: NavHostController,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(PurpleJobsly, Color(0xFF7C3AED))
                )
            )
            .padding(16.dp)
    ) {
        // Back Button & Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { navController.popBackStack() }
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Shop Products",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // Filter Button
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Filter",
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onFilterClick() }
            )
        }

        // Search Field
        TextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Racket, shoes, grips...", fontSize = 13.sp) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = Color(0xFF999),
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}

// ==================== CATEGORY FILTER ====================

@Composable
private fun ModernCategoryFilter(
    categories: List<Category>,
    selectedCategoryId: String?,
    onCategorySelect: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategoryId == null,
                onClick = { onCategorySelect("all") },
                label = { Text("All", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PurpleJobsly,
                    selectedLabelColor = Color.White
                )
            )
        }

        lazyRowItems(categories.take(5)) { category ->
            FilterChip(
                selected = selectedCategoryId == category.id,
                onClick = { onCategorySelect(category.id) },
                label = { 
                    Text(
                        category.name, 
                        fontSize = 12.sp, 
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    ) 
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PurpleJobsly,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

// ==================== SORT BAR ====================

@Composable
private fun SortBar(sortBy: String, onSortChange: (String) -> Unit) {
    var showSortMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Sort Button
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { showSortMenu = !showSortMenu }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(Icons.Filled.SwapVert, contentDescription = null, tint = PurpleJobsly, modifier = Modifier.size(18.dp))
            Text(
                when (sortBy) {
                    "price_low" -> "Price: Low to High"
                    "price_high" -> "Price: High to Low"
                    "newest" -> "Newest"
                    else -> "Popular"
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = PurpleJobsly
            )
        }

        // View Toggle (Grid/List)
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(2) { 
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(6.dp)
                        )
                )
            }
        }

        // Sort Menu
        if (showSortMenu) {
            DropdownMenu(
                expanded = showSortMenu,
                onDismissRequest = { showSortMenu = false }
            ) {
                listOf(
                    "popularity" to "Popular",
                    "newest" to "Newest",
                    "price_low" to "Price: Low to High",
                    "price_high" to "Price: High to Low"
                ).forEach { (value, label) ->
                    DropdownMenuItem(
                        text = { Text(label, fontSize = 12.sp) },
                        onClick = {
                            onSortChange(value)
                            showSortMenu = false
                        }
                    )
                }
            }
        }
    }
}

// ==================== PRODUCT GRID CARD ====================

@Composable
private fun ModernProductGridCard(
    product: Product,
    onProductClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onProductClick() }
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(14.dp)),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFFF5F5F5))
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(Color(0xFFFF6B6B), shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .padding(8.dp)
                ) {
                    Text(
                        "-20%",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Wishlist
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "Add to Wishlist",
                    tint = Color(0xFFFF6B6B).copy(alpha = 0.3f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                        .clickable { }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    product.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("⭐ 4.8", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text("(128)", fontSize = 10.sp, color = Color(0xFF999))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Price
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "₩${String.format("%,d", product.price.toInt())}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PurpleJobsly
                    )
                    Text(
                        "₩${String.format("%,d", (product.price * 1.25).toInt())}",
                        fontSize = 12.sp,
                        color = Color(0xFF999),
                        textDecoration = TextDecoration.LineThrough
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))


            }
        }
    }
}