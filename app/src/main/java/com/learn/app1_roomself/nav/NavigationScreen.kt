package com.learn.app1_roomself.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.learn.app1_roomself.models.Student
import com.learn.app1_roomself.ui.EditStudent
import com.learn.app1_roomself.ui.StudentsScreen

data object StudentsScreen : NavKey
data class EditStudent(val student: Student) : NavKey

@Composable
fun NavigationScreen(modifier: Modifier) {
    val backStack = remember {
        mutableStateListOf<NavKey>(StudentsScreen)
    }
    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<StudentsScreen> {
                StudentsScreen(
                    modifier = modifier,
                    onStudentClick = { clickedStudent ->
                        backStack.add(EditStudent(clickedStudent))
                    })
            }
            entry<EditStudent> {
                EditStudent(
                    student = it.student,
                    onCancelClick = {
                        backStack.removeLastOrNull()
                    })
            }
        }
    )
}