package com.kotlinpractice.smarttaskmanager.ui.managetags

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTagsRoute(
    viewModel: ManageTagsViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val tags by viewModel.tags.collectAsStateWithLifecycle()

    ManageTagsScreen(
        tags = tags,
        onAddTag = viewModel::addTag,
        onDeleteTag = viewModel::deleteTag,
        onBackClick = {onBack()}
    )
}