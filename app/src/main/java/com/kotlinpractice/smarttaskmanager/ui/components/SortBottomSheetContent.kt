package com.kotlinpractice.smarttaskmanager.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotlinpractice.smarttaskmanager.ui.tasklist.SortOption
import com.kotlinpractice.smarttaskmanager.util.enums.SortType
import com.kotlinpractice.smarttaskmanager.util.enums.displayText

@Composable
fun SortBottomSheetContent(
    currentSort: SortOption,
    onSortSelected: (SortType) -> Unit,
    onToggleDirection: () -> Unit,
    onToggleShowCompletedTasks:()-> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text(
            text = "Sort by",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(16.dp))

        SortType.entries.forEach { type ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSortSelected(type) }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(type.displayText())

                if (currentSort.type == type) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleDirection() }
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ascending")

            Switch(
                checked = currentSort.isAscending,
                onCheckedChange = { onToggleDirection() }
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleShowCompletedTasks() }
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show Completed Tasks")

            Switch(
                checked = currentSort.showCompletedTasks,
                onCheckedChange = { onToggleShowCompletedTasks() }
            )
        }
    }
}