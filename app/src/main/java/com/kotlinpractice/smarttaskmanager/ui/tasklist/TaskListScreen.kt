package com.kotlinpractice.smarttaskmanager.ui.tasklist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlinpractice.smarttaskmanager.domain.model.Task
import com.kotlinpractice.smarttaskmanager.uistate.TaskListUiState

@Composable
fun TaskListScreen(
    uiState: TaskListUiState,
    onAddTaskClick: () -> Unit,
    onDeleteTask:(id:Long)-> Unit,
    onUpdateTask:(id: Long)-> Unit,
    onComplete:(task: Task)-> Unit,
    searchText: String,
    onSearch:(query:String)-> Unit,
    filterItem: FilterType,
    onFilter:(filterItem:FilterType)-> Unit,
    sortType: SortType,
    onSort:(sortType: SortType)-> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize().padding(top = 32.dp)
    ) {

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column (modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally){
                Text("Tasks", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    SearchField(searchText,onSearch, Modifier.weight(0.60f))
                    Spacer(modifier = Modifier.width(10.dp))
                    FilterDropdown(filterItem,onFilter, Modifier.weight(0.30f))
                    SortIcon(sortType,onSort)
                }
                LazyColumn {
                    items(
                        items = uiState.tasks,
                        key = { it.id }
                    ) { task ->
                        Card (modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (task.isCompleted) Color.Green else Color.LightGray
                            )) {
                            Row (modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically)
                            {
                                Text(
                                    text = task.title,
                                    modifier = Modifier.padding(16.dp)
                                )
                                Spacer(modifier = Modifier.weight(1f)) //pushes everything after it to the end
                                IconButton(
                                    onClick = {onUpdateTask(task.id)}
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Delete"
                                    )
                                }
                                IconButton(
                                    onClick = {onComplete(task.copy(isCompleted = !task.isCompleted))}
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Complete"
                                    )
                                }
                                IconButton(
                                    onClick = {onDeleteTask(task.id)}
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = onAddTaskClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+")
        }
    }
}

@Composable
fun SearchField(searchText: String, onSearch:(str:String)-> Unit,modifier: Modifier){
    OutlinedTextField(
        value = searchText,
        onValueChange = {
            onSearch(it)
        },
        label = { Text("Search") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        singleLine = true,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdown(selected: FilterType,onFilter:(selectedItem:FilterType)-> Unit,modifier: Modifier){
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ){
        OutlinedTextField(
            value = selected.name,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("All") },
                onClick = {
                    onFilter(FilterType.ALL)
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("Completed") },
                onClick = {
                    onFilter(FilterType.COMPLETED)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Pending") },
                onClick = {
                    onFilter(FilterType.PENDING)
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun SortIcon(sortType: SortType,onSort: (SortType) -> Unit){
    IconButton(
        onClick = {
            if(sortType == SortType.ASC) onSort(SortType.DESC) else onSort(SortType.ASC)
        }
    ) {
        Icon(
            imageVector = if(sortType == SortType.ASC) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = "Sort"
        )
    }
}

