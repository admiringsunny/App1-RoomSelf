package com.learn.app1_roomself.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.learn.app1_roomself.dao.StudentDao
import com.learn.app1_roomself.db.StudentDB
import com.learn.app1_roomself.models.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Preview(showBackground = true)
@Composable
fun StudentScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val database = remember {
        StudentDB.getDatabase(context)
    }
    val dao = database.studentDao()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val students by dao.getAllStudents()
        .collectAsState(initial = emptyList())
    var showStudent by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Student Registration",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = dob,
            onValueChange = { dob = it },
            label = { Text("Enter dob") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (name.isBlank() || email.isBlank() || dob.isBlank()) {
                    Toast.makeText(context, "All fields are Required", Toast.LENGTH_SHORT).show()
                } else {
                    scope.launch {
                        val student = Student(name = name, email = email, dob = dob)
                        dao.insertStudent(student)
                        Toast.makeText(context, "Student saved successfully", Toast.LENGTH_SHORT)
                            .show()

                        // clear form
                        name = ""
                        email = ""
                        dob = ""
                    }
                }
            }
        ) { Text("Register") }
        Spacer(Modifier.height(20.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                showStudent = !showStudent
            }
        ) { Text(text = if (!showStudent) "Fetch Students" else "Hide Students") }
        if (showStudent) {
            Text(
                text = "Student Record (${students.size})",
                style = MaterialTheme.typography.titleLarge
            )
            if (students.isEmpty()) {
                Text("Student list is empty")
            } else {
                LazyColumn {
                    items(
                        items = students,
                        key = { it.id }
                    ) { student ->
                        StudentItem(student, scope, dao)
                    }
                }
            }
        }
    }
}

@Composable
fun StudentItem(student: Student = Student(0, "", "", ""), scope: CoroutineScope, dao: StudentDao) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID: ${student.id}",
                    style = MaterialTheme.typography.labelMedium
                )
                Row() {
                    IconButton(
                        onClick = {
                            scope.launch {
                                dao.deleteStudent(student)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Icon"
                        )
                    }
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Icon"
                        )
                    }
                }
            }
            Text("Name: ${student.name}")
            Text("Email: ${student.email}")
            Text("DOB: ${student.dob}")
        }
    }
}
