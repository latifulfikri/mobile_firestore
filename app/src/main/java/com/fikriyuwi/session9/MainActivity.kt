@file:OptIn(ExperimentalMaterial3Api::class)

package com.fikriyuwi.session9

import android.content.ContentValues.TAG
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fikriyuwi.session9.ui.theme.Session9Theme
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

data class Student(
    val student_id: String,
    val name: String,
    val gender: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddDataPage()
        }
    }
}

@Composable
fun Title(text: String)
{
    Text(text = text, fontSize = 30.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun makeSpace()
{
    Spacer(modifier = Modifier.padding(0.dp,20.dp,0.dp,0.dp))
}

fun AddData(student_id:String, name: String, gender: String)
{
    val db = Firebase.firestore;
    val stud = hashMapOf(
        "student_id" to student_id,
        "name" to name,
        "gender" to gender
    )
    db.collection("student")
        .add(stud)
        .addOnSuccessListener { documentReference ->
            Log.d(TAG,"DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e->
            Log.w(TAG,"Error adding document", e)
        }
}

@Composable
fun Form() {
    var isValid by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Title(text = "Add Data")
        makeSpace()
        var name by remember { mutableStateOf("") }
        var student_id by remember { mutableStateOf("") }
        val options = listOf("Male", "Female")
        var expanded by remember { mutableStateOf(false) }
        var gender by remember { mutableStateOf(options[0]) }
        TextField(
            value = student_id,
            onValueChange = { newText ->
                student_id = newText
            },
            label = { Text(text = "Student ID") }
        )
        makeSpace()
        TextField(
            value = name,
            onValueChange = { newText ->
                name = newText
            },
            label = { Text(text = "Name") }
        )
        makeSpace()
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                // The `menuAnchor` modifier must be passed to the text field for correctness.
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = gender,
                onValueChange = {},
                label = { Text("Label") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            gender = selectionOption
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
        makeSpace()
        Button(
            onClick = {
                AddData(student_id,name,gender)
            }
        ) {
            Text(text = "Submit")
        }
    }
}

@Composable
fun AddDataPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Form()
    }
}