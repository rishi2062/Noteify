package com.example.vit20bps1033ass3

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.LocationRequest
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import com.example.vit20bps1033ass3.Database.NoteDatabase
import com.example.vit20bps1033ass3.model.NoteViewModel
import com.example.vit20bps1033ass3.model.NoteViewModelFactory
import com.example.vit20bps1033ass3.model.Notes
import com.example.vit20bps1033ass3.repository.NoteRepository
import com.google.android.material.color.utilities.MaterialDynamicColors.background
import androidx.core.content.ContextCompat


import java.text.SimpleDateFormat
import java.util.*

class GetNotesActivity : ComponentActivity() {
    private lateinit var viewModel: NoteViewModel
    private lateinit var db: NoteDatabase
    private lateinit var repo: NoteRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lato = FontFamily(
            Font(R.font.latobold, FontWeight.Bold),
            Font(R.font.latoregular, FontWeight.Normal)
        )
        setContent {
            val name = remember { mutableStateOf("") }
            val details = remember { mutableStateOf("") }
            db = NoteDatabase.getDatabase(this)
            repo = NoteRepository(db.notesDao())
            viewModel =
                ViewModelProvider(this, NoteViewModelFactory(repo))[NoteViewModel::class.java]

            //LOCATION
            val isTaggged = false
            val locationLiveData = LocationLiveData(application)
            fun getLocationLiveData() = locationLiveData
            fun startLocationUpdates() {
                locationLiveData.startLocationUpdates()
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == 1) {
                val isReal = true
                locationLiveData.startLocationUpdates()

            } else {
                // from here
//                val requestSinglePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
//                        isGranted ->
//                    if (isGranted) {
//                        startLocationUpdates()
//                    } else {
//                        Toast.makeText(this, "GPS Unavailable", Toast.LENGTH_LONG).show()
//                    }
//                }
//                requestSinglePermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                // till here
            }



            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9F9F9))
            ) {
                val location by getLocationLiveData().observeAsState()


                val noteType = intent.getStringExtra("noteType")
                TextField(value = name.value, onValueChange = {
                    name.value = it
                    //title
                }, textStyle = TextStyle.Default.copy(fontSize = 32.sp, fontFamily = lato, fontWeight = FontWeight.Bold),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Color(0xFF383838),
                    backgroundColor = Color(0xFFF9F9F9),
                    textColor = Color(0xFF383838)
                ),modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 0.dp))
                TextField(value = details.value, onValueChange = {
                    details.value = it
                    //description
                }, textStyle = TextStyle.Default.copy(fontSize = 15.sp, fontFamily = lato, fontWeight = FontWeight.Bold),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Color(0xFF383838),
                    backgroundColor = Color(0xFFE4DCCF),
                    textColor = Color(0xFF383838)
                ), modifier = Modifier
                        .border(width = 0.dp, color = Color.Transparent)
                        .height(480.dp)
                        .padding(all = 30.dp)
                        .fillMaxWidth())



                Row(modifier = Modifier.padding(top = 30.dp, start = 30.dp, end = 30.dp)) {
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
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE4DCCF)),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = "SUBMIT", color = Color(0xFF383838))
                    }

                }

                Row(modifier = Modifier.align(Alignment.Start).padding(top = 12.dp, start = 30.dp, end = 30.dp)) {
                    Text(text = "Note taken at:", fontFamily = lato, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    // Make it so that geotag info appears here on clicking the geotag button

                    //  DISPLAY GEOTAG TEXT
//                    Text(
//                        text = location?.latitude.toString(),
//                        fontFamily = lato,
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Text(
//                        text = location?.longitude.toString(),
//                        fontFamily = lato,
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold
//                    )
                }

                Box(modifier = Modifier.fillMaxSize()) {

                    //Removed Old floating button (its commented at the bottom)
                    AddNew()
                }
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
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(end = 30.dp, bottom = 30.dp),
            onClick = {
                openDialog.value = true
            },
            backgroundColor = Color.White,
            contentColor = Color(0xFF7D9D9C),
            shape = CircleShape
        ) {
            Icon(Icons.Filled.Menu, "")
        }

        if (openDialog.value) {
            Dialog(
                onDismissRequest = { openDialog.value = false },
            ) {
                Card(
                    modifier = Modifier
                        .background(Color(0xffF9F9F9)),
                    shape = RoundedCornerShape(corner = CornerSize(12.dp))
                ) {
                    Column(modifier = Modifier.padding(23.dp)) {

                        Row(modifier = Modifier.padding(bottom = 10.dp)) {

                            Image(painter = painterResource(id = R.drawable.sharenb),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 7.dp)
                                    .clickable {
                                        //Reminder Activity Link
                                    })
                            Text(text = "share...", color = Color(0xFF7D9D9C))
                        }
                        Row(modifier = Modifier.padding(bottom = 10.dp)) {

                            Image(painter = painterResource(id = R.drawable.geotag),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 7.dp)
                                    .clickable {
                                        // GEO TAG
                                    })

                            Text(text = "geotag", color = Color(0xFF7D9D9C))
                        }

                        Row(modifier = Modifier.padding(bottom = 10.dp)) {

                            Image(painter = painterResource(id = R.drawable.cross),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 7.dp)
                                    .clickable {
                                        openDialog.value = false
                                    })

                            Text(text = "close", color = Color(0xFF7D9D9C))
                        }

                    }
                }

            }
        }}}

