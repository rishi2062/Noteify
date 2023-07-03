package com.example.vit20bps1033ass3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.vit20bps1033ass3.model.ToDoViewModel
import com.example.vit20bps1033ass3.model.ToDoViewModelFactory
import com.example.vit20bps1033ass3.repository.ToDoRepository


class FinishedToDo : ComponentActivity() {
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
            val list by viewModel.getFinishedToDo.observeAsState()
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9F9F9))
                    .padding(top = 30.dp, start = 30.dp, end = 30.dp, bottom = 30.dp)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 18.dp),
                    text = "Finished Task",
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
                            }
                        }
                    }
                }
            }
        }
    }
}