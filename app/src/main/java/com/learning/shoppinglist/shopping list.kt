package com.learning.shoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import kotlin.math.truncate

data class ShoppingItem(
    val id: Int, var name: String, var quantity: Int, var isEditing: Boolean = false
)

@Composable
fun shoppinglistapp() {
    var sitems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = true }, modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(50.dp)
        ) {
            items(sitems) { item ->
                if (item.isEditing) {
                    shoppingitemeditor(item = item, oneditcomplete = { editname, editqty ->
                        sitems = sitems.map {
                            if (it.id == item.id) {
                                it.copy(name = editname,
                                    quantity = editqty,
                                    isEditing = false
                                )
                            } else it
                        }
                    })
                } else {
                    ListItem(item = item, onEditClick = {
                        sitems = sitems.map { if (it.id == item.id) it.copy(isEditing = true) else it }
                    }, onDeleteClick = {
                        sitems = sitems - item
                    })
                }
            }
        }
    }
    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false }, confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    if (itemName.isNotBlank() && itemQuantity.isNotBlank()) {
                        val newItem = ShoppingItem(
                            id = sitems.size + 1,
                            name = itemName,
                            quantity = itemQuantity.toInt()
                        )
                        sitems += newItem
                        showDialog = false
                        itemName = ""
                        itemQuantity = ""
                    }
                }) {
                    Text(text = "Add")
                }
                Button(onClick = { showDialog = false }) {
                    Text(text = "Cancel")
                }
            }
        }, title = { Text(text = "Add New Item") }, text = {
            Column {
                OutlinedTextField(value = itemName,
                    onValueChange = { itemName = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = { Text("Item Name") })
                OutlinedTextField(value = itemQuantity,
                    onValueChange = { itemQuantity = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = { Text("Quantity") })
            }
        })
    }
}

@Composable
fun ListItem(
    item: ShoppingItem, onEditClick: () -> Unit, onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0x8DC3D4F4)), shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.name, modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = "qty: ${item.quantity}",
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically)
        )
        Row(modifier = Modifier.padding(7.dp)) {
            IconButton(onClick = { onEditClick() } ,
                modifier = Modifier.background(Color(0x33ff7588ab),
                        shape = RoundedCornerShape(20))
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(3.dp))
            IconButton(onClick = { onDeleteClick() },
                    modifier = Modifier.background(Color(0x33ff7588ab),
                    shape = RoundedCornerShape(20))
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }

        }
    }
}

@Composable
fun shoppingitemeditor(item: ShoppingItem, oneditcomplete: (String, Int) -> Unit) {
    var editname by remember { mutableStateOf(item.name) }
    var editqty by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            BasicTextField(
                value = editname,
                onValueChange = { editname = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editqty,
                onValueChange = { editqty = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }
        Button(onClick = {
            isEditing = false
            oneditcomplete(editname, editqty.toInt() ?: 1)
        }) {
            Text(text = "Save")
        }
    }

}