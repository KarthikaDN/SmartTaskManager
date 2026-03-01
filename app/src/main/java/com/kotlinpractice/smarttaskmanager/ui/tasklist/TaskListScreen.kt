package com.kotlinpractice.smarttaskmanager.ui.tasklist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlinpractice.smarttaskmanager.domain.model.Category
import com.kotlinpractice.smarttaskmanager.domain.model.Task
import com.kotlinpractice.smarttaskmanager.ui.components.SmartTaskTopAppBar
import com.kotlinpractice.smarttaskmanager.ui.components.SortBottomSheetContent
import com.kotlinpractice.smarttaskmanager.uistate.TaskListUiState
import com.kotlinpractice.smarttaskmanager.util.enums.Priority
import com.kotlinpractice.smarttaskmanager.util.enums.ScreenName
import com.kotlinpractice.smarttaskmanager.util.enums.SectionType
import com.kotlinpractice.smarttaskmanager.util.enums.SortType
import com.kotlinpractice.smarttaskmanager.util.enums.backgroundColor
import com.kotlinpractice.smarttaskmanager.util.enums.displayText
import com.kotlinpractice.smarttaskmanager.util.enums.textColor
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    taskListUiState: TaskListUiState,
    onCategorySelected: (Long?) -> Unit,
    listState: LazyListState,
    onTaskClick: (Long) -> Unit,
    onToggleComplete: (Long, Boolean) -> Unit,
    onDeleteTask: (Long) -> Unit,
    onAddTaskClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onSortSelected: (SortType) -> Unit,
    onToggleDirection: () -> Unit,
    onToggleShowCompletedTasks:()-> Unit,
    onSectionExpanded:(SectionType)-> Unit,
    onManageTagsClicked:()-> Unit,
    modifier: Modifier = Modifier
) {
    var showSortSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            SmartTaskTopAppBar(
                onSortIconClicked = {
                    showSortSheet = true
                },
                screenName = ScreenName.TASK_LIST,
                onManageTagsClick = onManageTagsClicked
            )
        },
        floatingActionButton = {

            val isAtTop by remember {
                derivedStateOf { !listState.canScrollBackward }
            }

            ExtendedFloatingActionButton(
                expanded = isAtTop,
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
                categories = taskListUiState.categories,
                selectedCategoryId = taskListUiState.selectedCategoryId,
                onCategorySelected = onCategorySelected
            )

            if (taskListUiState.tasksSection.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    taskListUiState.tasksSection.forEach { section ->
                        when (section) {

                            is TaskSection.Header -> {
                                stickyHeader(key = "header_${section.title}") {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().animateItem()
                                    ) {
                                        SectionHeader(section.title,
                                            section.count,
                                            isExpanded = taskListUiState.expandedSections.contains(section.title),
                                            onSectionExpanded = {
                                                onSectionExpanded(section.title)
                                            }
                                        )
                                    }
                                }
                            }

                            is TaskSection.Item -> {
                                item(
                                    key = "task_${section.task.id}"
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().animateItem()
                                    ) {
                                        SwipeableTaskItem(
                                            task = section.task,
                                            onDelete = onDeleteTask,
                                            onEdit = onTaskClick
                                        ) {
                                            TaskItem(
                                                task = section.task,
                                                onClick = { onTaskClick(section.task.id) },
                                                onToggleComplete = { onToggleComplete(section.task.id,!section.task.isCompleted) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    //Sort Bottom Sheet
    if (showSortSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSortSheet = false },
            sheetState = sheetState
        ) {
            SortBottomSheetContent(
                currentSort = taskListUiState.currentSort,
                onSortSelected = {
                    onSortSelected(it)
                    showSortSheet = false   // optional auto-close
                },
                onToggleDirection = onToggleDirection,
                onToggleShowCompletedTasks = onToggleShowCompletedTasks
            )
        }
    }
}

@Composable
fun SectionHeader(title: SectionType,count: Int,
                  isExpanded: Boolean,
                  onSectionExpanded: () -> Unit
) {
    Surface(
        color = title.backgroundColor(),
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSectionExpanded() }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title.displayText(count),
                color = title.textColor(),
                fontSize = 16.sp
            )

            val rotation by animateFloatAsState(
                targetValue = if (isExpanded) 180f else 0f,
                label = ""
            )

            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null,
                modifier = Modifier.rotate(rotation)
            )
        }
    }
//    Surface(
//        color = title.backgroundColor(),
//        tonalElevation = 2.dp
//    ) {
//        Text(
//            text = title.displayText(count),
//            style = MaterialTheme.typography.titleLarge,
//            fontSize = 16.sp,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 12.dp),
//            color = title.textColor()
//        )
//    }
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
        Priority.LOW -> Color(0xFF4CAF50)
        Priority.MEDIUM -> MaterialTheme.colorScheme.tertiary
        Priority.HIGH -> MaterialTheme.colorScheme.error
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            // 🔹 Left accent strip (premium touch)
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(priorityColor)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {

                // ─── Top Row ───
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { onToggleComplete() }
                    )

                    Spacer(Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {

                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = if (task.isCompleted)
                                TextDecoration.LineThrough else null,
                            color = if (task.isCompleted)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurface
                        )

                        task.description?.let {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    task.dueDate?.let { due ->
                        val formatted = remember(due) {
                            val zoned = due.atZone(ZoneId.systemDefault())
                            DateTimeFormatter.ofPattern("dd MMM")
                                .format(zoned)
                        }

                        Text(
                            text = formatted,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (due.isOverdue())
                                MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // ─── Category + Tags ───
                Text(
                    text = task.category.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                if (task.tags.isNotEmpty()) {
                    Spacer(Modifier.height(6.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        task.tags.forEach { tag ->
                            AssistChip(
                                onClick = {},
                                label = { Text(tag.name) }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                // ─── Metadata (subtle & compact) ───
                val created = remember(task.createdAt) {
                    task.createdAt
                        .atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                }

                val updated = remember(task.updatedAt) {
                    task.updatedAt
                        .atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                }

                Text(
                    text = "Created $created • Updated $updated",
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