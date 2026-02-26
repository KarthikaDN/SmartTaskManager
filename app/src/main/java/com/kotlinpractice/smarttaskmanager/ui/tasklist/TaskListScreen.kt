package com.kotlinpractice.smarttaskmanager.ui.tasklist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.SwipeToDismissBoxValue.EndToStart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.kotlinpractice.smarttaskmanager.domain.model.Category
import com.kotlinpractice.smarttaskmanager.domain.model.Task
import com.kotlinpractice.smarttaskmanager.ui.components.SmartTaskTopAppBar
import com.kotlinpractice.smarttaskmanager.util.enums.Priority
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    tasks: List<Task>,
    categories: List<Category>,
    selectedCategoryId: Long?,
    onCategorySelected: (Long?) -> Unit,
    onTaskClick: (Long) -> Unit,
    onToggleComplete: (Long, Boolean) -> Unit,
    onDeleteTask: (Long) -> Unit,
    onAddTaskClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            SmartTaskTopAppBar()
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTaskClick,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Add Task") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            CategoryRow(
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                onCategorySelected = onCategorySelected
            )

            if (tasks.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tasks, key = { it.id }) { task ->
                        SwipeableTaskItem(
                            task = task,
                            onDelete = onDeleteTask,
                            onEdit = onTaskClick
                        ) {
                            Box(
                                modifier = Modifier.animateItem()
                            ) {
                                TaskItem(
                                    task = task,
                                    onClick = { onTaskClick(task.id) },
                                    onToggleComplete = { onToggleComplete(task.id,!task.isCompleted) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryRow(
    categories: List<Category>,
    selectedCategoryId: Long?,
    onCategorySelected: (Long?) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategoryId == null,
                onClick = { onCategorySelected(null) },
                label = { Text("All") }
            )
        }

        items(categories, key = { it.id }) { category ->
            FilterChip(
                selected = selectedCategoryId == category.id,
                onClick = { onCategorySelected(category.id) },
                label = { Text(category.name) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableTaskItem(
    task: Task,
    onDelete: (Long) -> Unit,
    onEdit: (Long) -> Unit,
    content: @Composable () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {

            SwipeToDismissBoxValue.StartToEnd -> {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onEdit(task.id)

                // Reset immediately so it doesn’t stack
                dismissState.snapTo(SwipeToDismissBoxValue.Settled)
            }

            SwipeToDismissBoxValue.EndToStart -> {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onDelete(task.id)
            }

            else -> Unit
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            val direction = dismissState.dismissDirection

            val color = when (direction) {
                SwipeToDismissBoxValue.StartToEnd ->
                    MaterialTheme.colorScheme.tertiaryContainer
                SwipeToDismissBoxValue.EndToStart ->
                    MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.surface
            }

            val icon = when (direction) {
                SwipeToDismissBoxValue.StartToEnd ->
                    Icons.Default.Edit
                SwipeToDismissBoxValue.EndToStart ->
                    Icons.Default.Delete
                else -> null
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = when (direction) {
                    SwipeToDismissBoxValue.StartToEnd ->
                        Alignment.CenterStart
                    SwipeToDismissBoxValue.EndToStart ->
                        Alignment.CenterEnd
                    else -> Alignment.Center
                }
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    ) {
        content()
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onClick: () -> Unit,
    onToggleComplete: () -> Unit
) {
    val priorityColor = when (task.priority) {
        Priority.LOW -> Color.Green
        Priority.MEDIUM -> MaterialTheme.colorScheme.tertiary
        Priority.HIGH -> Color.Red
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggleComplete() }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(priorityColor, CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = if (task.isCompleted)
                            TextDecoration.LineThrough else null,
                        color = if (task.isCompleted)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurface
                    )

                    task.description?.let {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = task.category.name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (task.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    task.tags.forEach { tag ->
                        AssistChip(
                            onClick = {},
                            label = { Text(tag.name) }
                        )
                    }
                }
            }
            //createdDate:
            val formattedDate = remember(task.createdAt) {
                val zoned = task.createdAt.atZone(ZoneId.systemDefault())
                val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
                formatter.format(zoned)
            }

            Text(
                text = "Created at: $formattedDate",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            //updated at:
            val formattedUpdatedDate = remember(task.updatedAt) {
                val zoned = task.updatedAt.atZone(ZoneId.systemDefault())
                val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
                formatter.format(zoned)
            }

            Text(
                text = "Updated at: $formattedUpdatedDate",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            //Due date
            task.dueDate?.let { dueInstant ->

                val formattedDate = remember(dueInstant) {
                    val zoned = dueInstant.atZone(ZoneId.systemDefault())
                    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
                    formatter.format(zoned)
                }

                Text(
                    text = "Due: $formattedDate",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No Tasks Yet",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap + to create your first task",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}