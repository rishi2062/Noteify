package com.example.vit20bps1033ass3


import android.Manifest
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.vit20bps1033ass3.Database.NoteDatabase
import com.example.vit20bps1033ass3.model.NoteViewModel
import com.example.vit20bps1033ass3.model.NoteViewModelFactory
import com.example.vit20bps1033ass3.model.Notes
import com.example.vit20bps1033ass3.repository.NoteRepository
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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
            var name = remember { mutableStateOf("") }
            var details = remember { mutableStateOf("") }
            db = NoteDatabase.getDatabase(this)
            repo = NoteRepository(db.notesDao())
            viewModel =
                ViewModelProvider(this, NoteViewModelFactory(repo))[NoteViewModel::class.java]

            //LOCATION
            val isTagged = false
            val locationLiveData = LocationLiveData(application)
            fun getLocationLiveData() = locationLiveData
            fun startLocationUpdates() {
                locationLiveData.startLocationUpdates()
            }

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == 1
            ) {
                val isReal = true
                LaunchedEffect(Unit) {
                    while (isReal) {
                        locationLiveData.startLocationUpdates()
                        delay(15000)
                    }
                }

            } else {
                // EMPTY CONDITION
            }



            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9F9F9))
            ) {
                val location by getLocationLiveData().observeAsState()

                Row(modifier = Modifier
                    .padding(top = 30.dp, start = 30.dp)
                    .fillMaxWidth()
                    .align(alignment = Alignment.Start)) {
                    Icon(Icons.Filled.ArrowBack, "", Modifier.clickable {
                        val intent = Intent(
                            this@GetNotesActivity,
                            MainActivity::class.java
                        )
                        startActivity(intent)
                        finish()
                    })
                }


                TextField(
                    value = name.value,
                    onValueChange = {
                        name.value = it

                    },
                    textStyle = TextStyle.Default.copy(
                        fontSize = 32.sp,
                        fontFamily = lato,
                        fontWeight = FontWeight.Bold
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        cursorColor = Color(0xFF383838),
                        backgroundColor = Color(0xFFF9F9F9),
                        textColor = Color(0xFF383838)
                    ),
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 0.dp)
                )

                // NOTE CONTENT
                TextField(
                    value = details.value,
                    onValueChange = {
                        details.value = it
                    },
                    textStyle = TextStyle.Default.copy(
                        fontSize = 15.sp,
                        fontFamily = lato,
                        fontWeight = FontWeight.Bold
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        cursorColor = Color(0xFF383838),
                        backgroundColor = Color(0xFFE4DCCF),
                        textColor = Color(0xFF383838)
                    ),
                    modifier = Modifier
                        .border(width = 0.dp, color = Color.Transparent)
                        .height(480.dp)
                        .padding(all = 30.dp)
                        .fillMaxWidth()
                )



                // SUBMIT NOTE

                val noteType = intent.getStringExtra("noteType")
                if (noteType.equals("Edit")) {
                    name.value = intent.getStringExtra("noteTitle")!!
                    details.value = intent.getStringExtra("noteDescription")!!

                }
                Row(modifier = Modifier.padding(top = 10.dp, start = 30.dp, end = 30.dp)) {
                    Button(
                        onClick = {
                            val noteTitle = name.value
                            val noteDescription = details.value

                            if (noteType.equals("Edit")) {
                                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                                    val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                                    val currDate: String = sdf.format(Date())
                                    val updateNote = Notes(0, noteTitle, noteDescription, currDate)
                                    viewModel.updataNote(updateNote)
                                }
                            } else {
                                Log.i("TAG123456", "SUBMIT HUA")
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

                // GEOTAG FEATURE

                Box(modifier = Modifier
                    .fillMaxSize()
                    .align(alignment = Alignment.Start)) {
                    Column() {
                        var locationdetail by remember {
                            mutableStateOf("")}

                        Text(
                            modifier = Modifier.padding(horizontal = 30.dp, vertical = 30.dp),
                            text =  locationdetail,
                            fontFamily = lato,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier
                                .padding(bottom = 30.dp, start = 30.dp, end = 30.dp)
                                .align(alignment = Alignment.Start)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                                Image(painter = painterResource(id = R.drawable.geotagwb),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable {


                                        locationdetail =
                                            "At:  " +
                                                    location?.latitude?.let {
                                                        location?.longitude?.let { it1 ->
                                                            getCompleteAddressString(
                                                                it.toDouble(), it1.toDouble()
                                                            )
                                                        }
                                                    }

                                    })
                        }
                    }
                }
            }
        }
    }

    private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String? {
        var strAdd = ""
        var res = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                var city = returnedAddress.locality
                var state = returnedAddress.adminArea
                var country = returnedAddress.countryCode
                res = "$city, $state, $country"
                strAdd = strReturnedAddress.toString()
                Log.w("My Current loction address", strReturnedAddress.toString())
            } else {
                Log.w("My Current loction address", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("My Current loction address", "Canont get Address!")
        }
        return res
    }
}

