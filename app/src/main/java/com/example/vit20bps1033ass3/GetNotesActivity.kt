package com.example.vit20bps1033ass3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.vit20bps1033ass3.Database.NoteDatabase
import com.example.vit20bps1033ass3.model.NoteViewModel
import com.example.vit20bps1033ass3.model.NoteViewModelFactory
import com.example.vit20bps1033ass3.model.Notes
import com.example.vit20bps1033ass3.repository.NoteRepository

import java.text.SimpleDateFormat
import java.util.*

class GetNotesActivity : ComponentActivity() {
    private lateinit var viewModel: NoteViewModel
    private lateinit var db: NoteDatabase
    private lateinit var repo: NoteRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val name = remember { mutableStateOf("") }
            val details = remember { mutableStateOf("") }
            db = NoteDatabase.getDatabase(this)
            repo = NoteRepository(db.notesDao())
            viewModel =
                ViewModelProvider(this, NoteViewModelFactory(repo))[NoteViewModel::class.java]
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                val noteType = intent.getStringExtra("noteType")
                OutlinedTextField(value = name.value, onValueChange = {
                    name.value = it
                }, label = { Text(text = "Note Title",color = Color.White, fontSize = 16.sp) } , colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Black ,
                    cursorColor = Color.White,
                    textColor = Color.White
                ),modifier = Modifier.height(80.dp).fillMaxWidth().padding(all = 10.dp))
                OutlinedTextField(value = details.value, onValueChange = {
                    details.value = it
                }, label = { Text(text = "Note Description",color = Color.White) } , colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Black,
                    cursorColor = Color.White,
                    textColor = Color.White
                ), modifier = Modifier.height(280.dp).padding(all = 10.dp).fillMaxWidth())
                Row(modifier = Modifier.padding(top = 180.dp)) {
                    Button(
                        onClick = {
                            val noteTitle = name.value
                            val noteDescription = details.value
                            if (noteType.equals("Edit")) {
                                name.value = intent.getStringExtra("noteTitle")!!
                                details.value = intent.getStringExtra("noteDescription")!!
                                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                                    val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                                    val currDate: String = sdf.format(Date())
                                    val updateNote = Notes(0, noteTitle, noteDescription, currDate)
                                    viewModel.updataNote(updateNote)
                                }
                            } else {
                                Log.i("TAG","SUBMIT HUA")
                                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                                    val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                                    val currDate: String = sdf.format(Date())
                                    val addNote = Notes(0, noteTitle, noteDescription, currDate)
                                    viewModel.insertNote(addNote)

                                }
                            }
                            val intent = Intent(this@GetNotesActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(text = "SUBMIT", color = Color.White)
                    }

                }
            }
        }
    }

}
