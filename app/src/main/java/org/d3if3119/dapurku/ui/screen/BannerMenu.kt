package org.d3if3119.dapurku.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun BannerMenu(modifier: Modifier = Modifier) {
//    val recipeList = listOf(
//        RecipeData(
//            name = "Soto Semarang",
//            imageUrl = "https://th.bing.com/th/id/OIP.5Z4g_PXU6vC4n1bLEKHK8gHaEW?rs=1&pid=ImgDetMain",
//        ),
//        RecipeData(
//            name = "Sate Madura",
//            imageUrl = "https://caramembuat.id/wp-content/uploads/2022/07/Cara-Membuat-Sate-Ayam-Madura.jpg",
//        ),
//        RecipeData(
//            name = "Kue Putu",
//            imageUrl = "https://www.tiktak.id/wp-content/uploads/2022/07/Resep-Kue-Putu-Jajan-Tradisional-yang-Masuk-50-Kue-Terbaik-Dunia.jpg",
//        )
//    )
//    val pagerState = rememberPagerState(pageCount = { recipeList.size })
//    HorizontalPager(
//        state = pagerState,
//        pageSize = PageSize.Fill,
//        modifier = modifier
//    ) { page ->
//        Box(
//            contentAlignment = Alignment.BottomStart
//        ) {
//            AsyncImage(
//                model = recipeList[page].imageUrl,
//                contentDescription = null,
//                modifier = Modifier
//                    .aspectRatio(2f)
//                    .clip(RoundedCornerShape(8.dp)),
//                contentScale = ContentScale.Crop
//            )
//            Box(
//                modifier = Modifier
//                    .padding(8.dp)
//                    .clip(CircleShape)
//                    .background(MaterialTheme.colorScheme.tertiaryContainer)
//            ) {
//                Text(
//                    text = recipeList[page].name,
//                    modifier = Modifier
//                        .padding(horizontal = 8.dp, vertical = 4.dp)
//                )
//            }
//        }
//    }
//}