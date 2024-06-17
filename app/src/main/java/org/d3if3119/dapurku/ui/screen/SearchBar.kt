package org.d3if3119.dapurku.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onSearchAction: (String) -> Unit,
    filterList: List<String>,
) {
    val localFilterList by remember { mutableStateOf(filterList) }
    val selectedFilterList by remember { mutableStateOf(listOf("")) }
    var isShowingOptions by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = value,
                onValueChange = onSearchAction,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Search,
                        contentDescription = "Cari resep"
                    )
                },
                placeholder = {
                    Text(text = "Cari resep..")
                },
                shape = RoundedCornerShape(8.dp)
            )
            IconButton(
                onClick = { isShowingOptions = !isShowingOptions },
                content = {
                    Icon(imageVector = Icons.Default.FilterAlt, contentDescription = "Filter")
                }
            )
        }
        AnimatedVisibility(
            visible = isShowingOptions,
            content = {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(localFilterList) {
                        FilterChip(
                            selected = selectedFilterList.contains(it),
                            onClick = {
                                if (!selectedFilterList.contains(it)) {
                                    selectedFilterList.toMutableList().add(it)
                                } else selectedFilterList.toMutableList().remove(it)
                            },
                            label = { Text(text = it) },
                            shape = CircleShape
                        )
                    }
                }
            }
        )
    }
}