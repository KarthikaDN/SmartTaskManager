package com.kotlinpractice.smarttaskmanager.ui.managetags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotlinpractice.smarttaskmanager.domain.model.Tag

@Composable
fun ManageTagsScreen(
    tags: List<Tag>,
    onAddTag: (String) -> Unit,
    onDeleteTag: (Long) -> Unit
) {
    var newTag by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {

        Row {
            OutlinedTextField(
                value = newTag,
                onValueChange = { newTag = it },
                label = { Text("New Tag") },
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    onAddTag(newTag)
                    newTag = ""
                }
            ) {
                Icon(Icons.Default.Add, null)
            }
        }

        LazyColumn {
            items(tags) { tag ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(tag.name)
                    IconButton(onClick = { onDeleteTag(tag.id) }) {
                        Icon(Icons.Default.Delete, null)
                    }
                }
            }
        }
    }
}