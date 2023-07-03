package com.example.vit20bps1033ass3


import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class GetToDo : ComponentActivity() {
    private lateinit var viewModel: ToDoViewModel
    private lateinit var db: ToDoDatabase
    private lateinit var repo: ToDoRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lato = FontFamily(
            Font(R.font.latobold, FontWeight.Bold),
            Font(R.font.latoregular, FontWeight.Normal)
        )
        setContent {
            var name by remember { mutableStateOf("") }
            var details by remember { mutableStateOf("") }
            db = ToDoDatabase.getDatabase(this)
            repo = ToDoRepository(db.todoDao())
            viewModel =
                ViewModelProvider(this, ToDoViewModelFactory(repo))[ToDoViewModel::class.java]



            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9F9F9))
            ) {

                TextField(
                    value = name,
                    onValueChange = { name = it },
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

                // Fetching the Local Context


                // Declaring integer values
                // for year, month and day
                val mYear: Int
                val mMonth: Int
                val mDay: Int

                // Initializing a Calendar
                val mCalendar = Calendar.getInstance()

                // Fetching current year, month and day
                mYear = mCalendar.get(Calendar.YEAR)
                mMonth = mCalendar.get(Calendar.MONTH)
                mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

                mCalendar.time = Date()

                // Declaring a string value to
                // store date in string format
                var mDate = remember { mutableStateOf("") }

                // Declaring DatePickerDialog and setting
                // initial values as current values (present year, month and day)
                val mDatePickerDialog = DatePickerDialog(
                    this@GetToDo,
                    { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                        mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
                    }, mYear, mMonth, mDay
                )

                // Creating a button that on
                // click displays/shows the DatePickerDialog
                Button(onClick = {
                    mDatePickerDialog.show()
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE4DCCF))) {
                    Text(text = "Open Date Picker", color = Color.White)
                }

                // Adding a space of 100dp height
                Spacer(modifier = Modifier.size(100.dp))

                // Displaying the mDate value in the Text
                Text(
                    text = "Selected Date: ${mDate.value}",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )


                val toDoType = intent.getStringExtra("ToDoType")
                if (toDoType.equals("Edit")) {
                    val initialName = intent.getStringExtra("listTitle") ?: ""
                    val dueDate = intent.getStringExtra("dueDate")
                    name = initialName
                    //  mDate = dueDate
                }

                Row(modifier = Modifier.padding(top = 30.dp, start = 30.dp, end = 30.dp)) {
                    Button(
                        onClick = {
                            val noteTitle = name
                            val dueDate = mDate.value
                            if (toDoType.equals("Edit")) {
                                if (noteTitle.isNotEmpty()) {
                                    val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                                    val currDate: String = sdf.format(Date())
                                    val updateTodo = ToDo(0, noteTitle, currDate, dueDate, 0)
                                    viewModel.updateTodo(updateTodo)
                                }
                            } else {
                                Log.i("TAG123456", "SUBMIT HUA")
                                if (noteTitle.isNotEmpty()) {
                                    val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                                    val currDate: String = sdf.format(Date())
                                    val addTodo = ToDo(0, noteTitle, currDate, dueDate, 0)
                                    viewModel.insertTodo(addTodo)

                                }
                            }
                            val intent = Intent(this@GetToDo, ToDoMainActivity::class.java)
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


            }
        }


    }


}