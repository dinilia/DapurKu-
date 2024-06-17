package org.d3if3119.dapurku.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.d3if3119.dapurku.R
import org.d3if3119.dapurku.network.ImageApi

@Composable
fun RecipeCard(
    modifier: Modifier = Modifier,
    name: String,
    imageUrl: String,
    deskripsi: String,
    onClick: () -> Unit
) {
    Card(modifier) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(ImageApi.getImageUrl(imageUrl))
                    .crossfade(true)
                    .build(),
                contentDescription = name,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.broken_image)
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Column {
                    Text(
                        text = name,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = deskripsi,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                IconButton(onClick = { onClick() }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete Icon")
                }
            }
        }
    }
}