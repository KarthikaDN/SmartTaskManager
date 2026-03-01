package com.kotlinpractice.smarttaskmanager.ui.addedittask

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.kotlinpractice.smarttaskmanager.domain.model.Category
import com.kotlinpractice.smarttaskmanager.ui.components.SmartTaskTopAppBar
import com.kotlinpractice.smarttaskmanager.uistate.AddEditTaskUiState
import com.kotlinpractice.smarttaskmanager.util.enums.Priority
import com.kotlinpractice.smarttaskmanager.util.enums.ScreenName
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditTaskScreen(
    state: AddEditTaskUiState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategorySelected: (Long) -> Unit,
    onTagToggle: (Long) -> Unit,
    onDueDateSelected: (Long) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    onPrioritySelected:(Priority)-> Unit
) {
    Scaffold(
        topBar = {
            SmartTaskTopAppBar(
                title = if (state.taskId == null)
                    "Add Task"
                else
                    "Edit Task",
                showBackButton = true,
                onBackClick = onBackClick,
                screenName = ScreenName.ADD_EDIT_TASK
            )
        },
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            SectionCard(
                title = "Task Details",
                accentColor = MaterialTheme.colorScheme.primary
            ){
                // Title
                OutlinedTextField(
                    value = state.title,
                    onValueChange = onTitleChange,
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Description
                OutlinedTextField(
                    value = state.description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                // Category Dropdown
                CategoryDropdown(
                    categories = state.categories,
                    selectedCategoryId = state.selectedCategoryId,
                    onCategorySelected = onCategorySelected
                )
            }

            // Tags
            // 🟩 Tags Section
            SectionCard(
                title = "Tags",
                accentColor = MaterialTheme.colorScheme.secondary
            ){

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    state.tags.forEach { tag ->
                        FilterChip(
                            selected = state.selectedTagIds.contains(tag.id),
                            onClick = { onTagToggle(tag.id) },
                            label = { Text(tag.name) }
                        )
                    }
                }
            }


            // 🟨 Priority Section
            SectionCard(
                title = "Priority",
                accentColor = MaterialTheme.colorScheme.tertiary
            ){
                PriorityRadioButtons(
                    selectedPriority = state.priority,
                    onPrioritySelected = onPrioritySelected
                )
            }

            // Due Date (simple version)
            SectionCard(
                title = "Due Date",
                accentColor = MaterialTheme.colorScheme.primary
            ) {
                DueDatePickerField(
                    selectedDate = state.dueDate,
                    onDateSelected = onDueDateSelected
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50), // Material Green 500
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                ),
            ) {
                Text("Save Task")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = state.error ?:"",
                color = Color.Red
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    categories: List<Category>,
    selectedCategoryId: Long?,
    onCategorySelected: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedCategory =
        categories.find { it.id == selectedCategoryId }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedCategory?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        onCategorySelected(category.id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PriorityRadioButtons(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
){
    val prioritiesList:List<Priority> = listOf(
        Priority.LOW,
        Priority.MEDIUM,
        Priority.HIGH
    )

    Row (
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        prioritiesList.forEach { priority ->
            Row (modifier = Modifier
                .selectable(
                    selected = selectedPriority == priority,
                    onClick = { onPrioritySelected(priority) },
                    role = Role.RadioButton
            ),
                verticalAlignment = Alignment.CenterVertically){
                RadioButton(
                    selected = selectedPriority == priority,
                    onClick = {onPrioritySelected(priority)}
                )
                Text(priority.name)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DueDatePickerField(
    selectedDate: Long?,
    onDateSelected: (Long) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    val formatter = remember {
        java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
    }

    val formattedDate = selectedDate?.let {
        val instant = java.time.Instant.ofEpochMilli(it)
        val zoned = instant.atZone(java.time.ZoneId.systemDefault())
        formatter.format(zoned)
    } ?: "Select due date"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true }
    ) {
        OutlinedTextField(
            value = formattedDate,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text("Due Date") },
            modifier = Modifier.fillMaxWidth()
        )
    }

    // 🔵 Date Picker
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    showDatePicker = false
                    showTimePicker = true
                }) {
                    Text("Next")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // 🟣 Time Picker
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                Button(onClick = {
                    showTimePicker = false

                    val selectedDateMillis =
                        datePickerState.selectedDateMillis?.let { dateMillis ->

                            val localDate = Instant.ofEpochMilli(dateMillis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()

                            val localDateTime = localDate.atTime(
                                timePickerState.hour,
                                timePickerState.minute
                            )

                            localDateTime
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()
                        }

                    selectedDateMillis?.let { onDateSelected(it) }
                }) {
                    Text("Confirm")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}

@Composable
fun SectionCard(
    title: String,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Accent header
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = accentColor
            )

            content()
        }
    }
}