package com.kotlinpractice.smarttaskmanager.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AddTagBottomSheetContent(
    onAddTag:(String)-> Unit
){
    Column(
        modifier = Modifier.padding(16.dp)
    ){
        var tagName by remember { mutableStateOf("") }
        OutlinedTextField(
            value = tagName,
            onValueChange = {tagName = it},
            label = { Text("Tag Name") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 1
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                if(!tagName.isEmpty()){
                    onAddTag(tagName)
                }
            },
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
            Text("Add Tag")
        }
    }
}