package com.example.vit20bps1033ass3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.vit20bps1033ass3.Database.ToDoDatabase
import com.example.vit20bps1033ass3.model.ToDo
import com.example.vit20bps1033ass3.model.ToDoViewModel
import com.example.vit20bps1033ass3.model.ToDoViewModelFactory
import com.example.vit20bps1033ass3.repository.ToDoRepository


class ToDoMainActivity : ComponentActivity() {
    private lateinit var viewModel: ToDoViewModel
    private lateinit var db: ToDoDatabase
    private lateinit var repo: ToDoRepository

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lato = FontFamily(
            Font(R.font.latobold, FontWeight.Bold),
            Font(R.font.latoregular, FontWeight.Normal)
        )
        setContent {
            Log.i("Tagqwer", "Entered")
            db = ToDoDatabase.getDatabase(this)
            repo = ToDoRepository(db.todoDao())
            viewModel =
                ViewModelProvider(this, ToDoViewModelFactory(repo))[ToDoViewModel::class.java]
            val list by viewModel.getToDo.observeAsState()
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9F9F9))
                    .padding(top = 30.dp, start = 30.dp, end = 30.dp, bottom = 30.dp)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 18.dp),
                    text = "Noteify-ToDo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Left,
                    fontFamily = lato,
                    color = Color(0xff383838)
                )
                LazyColumn() {
                    list?.let { items ->
                        items(items) {
                            Card(
                                modifier = Modifier
                                    .padding(bottom = 23.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        val intent =
                                            Intent(
                                                this@ToDoMainActivity,
                                                GetToDo::class.java
                                            )
                                        intent.putExtra("ToDoType", "Edit")
                                        intent.putExtra("listTitle", it.title)
                                        intent.putExtra("dueDate", it.dueDate)
                                        intent.putExtra("listId", it.id)
                                        startActivity(intent)
                                        finish()
                                    },
                                elevation = 4.dp, backgroundColor = Color(0xFFE4DCCF),
                                shape = RoundedCornerShape(corner = CornerSize(12.dp))
                            ) {
                                Row(modifier = Modifier.align(Alignment.Start)) {
                                    Column() {
                                        Text(
                                            text = it.title,
                                            style = TextStyle(
                                                color = Color(0xff383838),
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = Modifier.padding(top = 15.dp, start = 10.dp),
                                            fontFamily = lato
                                        )
                                        Text(
                                            text = it.timeStamp,
                                            style = TextStyle(
                                                color = Color(0xff6F6F6F),
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = Modifier.padding(
                                                top = 25.dp,
                                                start = 10.dp,
                                                bottom = 15.dp
                                            ), fontFamily = lato
                                        )
                                        Text(
                                            text = "Due Date : " + it.dueDate,
                                            style = TextStyle(
                                                color = Color(0xff6F6F6F),
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = Modifier.padding(
                                                top = 25.dp,
                                                start = 10.dp,
                                                bottom = 15.dp
                                            ), fontFamily = lato
                                        )

                                    }
                                }

                                Row(modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(top = 55.dp, bottom = 30.dp, start = 262.dp)
                                    .clickable {
                                        viewModel.updateFinishedTodo(it)
                                        val updateTodo =
                                            ToDo(it.id, it.title, it.timeStamp, it.dueDate, 1)
                                        viewModel.updateTodo(updateTodo)
                                    }) {
                                    Icon(Icons.Filled.CheckCircle, "hello")
                                }
                            }
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {

                //Removed Old floating button (its commented at the bottom)
                AddNew()
                FloatingActionButton(
                    modifier = Modifier.align(alignment = Alignment.BottomStart),
                    onClick = {
                        val intent = Intent(
                            this@ToDoMainActivity,
                            FinishedToDo::class.java
                        )
                        startActivity(intent)

                    },
                    backgroundColor = Color.White,
                    contentColor = Color(0xFF7D9D9C),
                    shape = CircleShape
                ) {
                    Icon(Icons.Filled.List, "hello")
                }
            }
        }
    }


    @Composable
    fun AddNew() {
        Box(modifier = Modifier.fillMaxSize()) {
            val openDialog = remember {
                mutableStateOf(false)
            }

            FloatingActionButton(
                modifier = Modifier.align(alignment = Alignment.BottomEnd),
                onClick = {
                    openDialog.value = true
                },
                backgroundColor = Color.White,
                contentColor = Color(0xFF7D9D9C),
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, "", Modifier.clickable {
                    val intent = Intent(
                        this@ToDoMainActivity,
                        GetToDo::class.java
                    )
                    startActivity(intent)
                    finish()
                })
            }

//            if (openDialog.value) {
//                Dialog(onDismissRequest = { openDialog.value = false },
//                ) {
//                    Card(modifier = Modifier
//                        .background(Color(0xffF9F9F9)), shape = RoundedCornerShape(corner = CornerSize(12.dp))) {
//                        Column(modifier = Modifier.padding(23.dp)) {
//
//                            Row(modifier = Modifier.padding(bottom = 10.dp)) {
//
//                                Image(painter = painterResource(id = R.drawable.checklist), contentDescription = null,
//                                    modifier = Modifier
//                                        .size(24.dp)
//                                        .padding(end = 7.dp)
//                                        .clickable {
//                                            //Reminder Activity Link
//                                        })
//                                Text(text = "Make a Reminder", color = Color(0xFF7D9D9C))
//                            }
//                            Row(modifier = Modifier.padding(bottom = 10.dp)) {
//
//                                Image(painter = painterResource(id = R.drawable.note), contentDescription = null,
//                                    modifier = Modifier
//                                        .size(24.dp)
//                                        .padding(end = 7.dp)
//                                        .clickable {
//                                            val intent = Intent(
//                                                this@MainActivity,
//                                                GetNotesActivity::class.java
//                                            )
//                                            startActivity(intent)
//                                            finish()
//                                        })
//
//                                Text(text = "note", color = Color(0xFF7D9D9C))
//                            }
//
//                            Row(modifier = Modifier.padding(bottom = 10.dp)) {
//
//                                Image(painter = painterResource(id = R.drawable.cross), contentDescription = null,
//                                    modifier = Modifier
//                                        .size(24.dp)
//                                        .padding(end = 7.dp)
//                                        .clickable {
//                                            openDialog.value = false
//                                        })
//
//                                Text(text = "close", color = Color(0xFF7D9D9C))
//                            }
//
//                        }
//                    }
//
//                }
////                AlertDialog(
////                    onDismissRequest = { openDialog.value = false},
////
////                    title = {
////                        Text(text = "Hello")
////                    },
////                    text = { Text(text = "Do lala?") },
////
////                    confirmButton = {
////                        Button(onClick = {
////                            openDialog.value = false
////                        }) {
////                            Text(text = "YES")
////                        }
////                    },
////
////                    dismissButton = {
////                        Button(onClick = {
////                            openDialog.value = false
////                        }) {
////                            Text(text = "NO")
////                        }
////                    })
//            }
        }
    }
}

//Box(modifier = Modifier.fillMaxSize()) {
//
//
//    FloatingActionButton(
//        modifier = Modifier.align(alignment = Alignment.BottomEnd),
//        onClick = {
//            val intent = Intent(
//                this@MainActivity,
//                GetNotesActivity::class.java
//            )
//            startActivity(intent)
//            finish()
//
//
//        },
//        backgroundColor = Color.White,
//        contentColor = Color(0xFF7D9D9C),
//        shape = CircleShape
//    ) {
//        Icon(Icons.Filled.Add, "")
//    }
//}
