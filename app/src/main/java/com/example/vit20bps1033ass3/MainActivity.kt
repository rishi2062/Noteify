package com.example.vit20bps1033ass3


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.semantics.SemanticsActions.OnClick
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.vit20bps1033ass3.Database.NoteDatabase
import com.example.vit20bps1033ass3.model.NoteViewModel
import com.example.vit20bps1033ass3.model.NoteViewModelFactory
import com.example.vit20bps1033ass3.model.Notes
import com.example.vit20bps1033ass3.repository.NoteRepository


class MainActivity : ComponentActivity() {
    private lateinit var viewModel : NoteViewModel
    private lateinit var db : NoteDatabase
    private lateinit var repo : NoteRepository
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            db = NoteDatabase.getDatabase(this)
            repo = NoteRepository(db.notesDao())
            viewModel = ViewModelProvider(this, NoteViewModelFactory(repo))[NoteViewModel::class.java]
//            var notes = remember{ listOf<Notes>() }
//            viewModel.getNotes.observe(this@MainActivity, Observer { list ->
//                list?.let {
//                    notes = list
//                }
//            })
            val notes by viewModel.getNotes.observeAsState()
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                LazyColumn(modifier = Modifier.padding(all = 4.dp)){
                    notes?.let{items ->
                        items(items){
                            Card(modifier = Modifier
                                .padding(horizontal = 3.dp, vertical = 4.dp)
                                .fillMaxWidth(),elevation = 4.dp, backgroundColor = Color.LightGray,
                                shape = RoundedCornerShape(corner = CornerSize(10.dp)), onClick = {
                                    val intent = Intent(this@MainActivity,GetNotesActivity::class.java)
                                    intent.putExtra("noteType","Edit")
                                    intent.putExtra("noteTitle",it.title)
                                    intent.putExtra("noteDescription",it.description)
                                    intent.putExtra("noteId",it.id)
                                    startActivity(intent)
                                    finish()
                                }) {
                                Row(modifier = Modifier.align(Alignment.Start)) {
                                Column(modifier = Modifier.padding(all = 5.dp)) {
                                    Text(
                                        text = it.title,
                                        style = TextStyle(color = Color.Black, fontSize = 20.sp)
                                    )
                                    Text(
                                        text = it.timeStamp,
                                        style = TextStyle(color = Color.Black, fontSize = 13.sp),
                                        modifier = Modifier.padding(top = 5.dp)
                                    )

                                }
                            }
                                Row(modifier = Modifier.align(Alignment.End).padding(top=15.dp,start = 300.dp)){
                                    Button(
                                        onClick = {
                                            viewModel.deleteNote(it)
                                        },
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                                    )
                                    {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                                            contentDescription = "Image"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    FloatingActionButton(
                        modifier = Modifier.align(alignment = Alignment.BottomEnd),
                        onClick = {
                            val intent = Intent(
                                this@MainActivity,
                                GetNotesActivity::class.java
                            )
                            startActivity(intent)
                            finish()
                        },
                        backgroundColor = Color.DarkGray,
                        contentColor = Color.White,
                        shape = CircleShape
                    ) {
                        Icon(Icons.Filled.Add, "")
                    }
                }
            }
        }
    }
}
