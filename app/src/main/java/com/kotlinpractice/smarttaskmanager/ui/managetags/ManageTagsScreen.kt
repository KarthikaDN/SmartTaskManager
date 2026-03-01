package com.kotlinpractice.smarttaskmanager.ui.managetags

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlinpractice.smarttaskmanager.domain.model.Tag
import com.kotlinpractice.smarttaskmanager.ui.components.AddTagBottomSheetContent
import com.kotlinpractice.smarttaskmanager.ui.components.SmartTaskTopAppBar
import com.kotlinpractice.smarttaskmanager.ui.components.SortBottomSheetContent
import com.kotlinpractice.smarttaskmanager.util.enums.ScreenName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTagsScreen(
    tags: List<Tag>,
    onAddTag: (String) -> Unit,
    onDeleteTag: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    var showAddTagBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {
            SmartTaskTopAppBar(
                title = "Manage Tags",
                showBackButton = true,
                onBackClick = onBackClick,
                screenName = ScreenName.MANAGE_TAGS
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {

            Text(
                text = "Available Tags",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                tags.forEach { tag ->

                    InputChip(
                        selected = false,
                        onClick = {},
                        label = { Text(tag.name) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Delete",
                                modifier = Modifier
                                    .clickable { onDeleteTag(tag.id) }
                            )
                        }
                    )
                }

                SuggestionChip(
                    onClick = { showAddTagBottomSheet = true },
                    label = { Text("Add") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }

    if (showAddTagBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddTagBottomSheet = false },
            sheetState = sheetState
        ) {
            AddTagBottomSheetContent(
                onAddTag = {
                    onAddTag(it)
                    showAddTagBottomSheet = false
                }
            )
        }
    }
}