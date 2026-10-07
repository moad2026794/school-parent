@file:OptIn(ExperimentalMaterial3Api::class)

package com.school.parent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun App() {
    var loggedIn by rememberSaveable { mutableStateOf(false) }
    if (loggedIn) Dashboard { loggedIn = false } else LoginScreen { loggedIn = true }
}

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    var phone by rememberSaveable { mutableStateOf("") }
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("🏫", fontSize = 56.sp)
        Text(SampleData.SCHOOL, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("بوابة ولي الأمر", color = MaterialTheme.colorScheme.outline)
        Spacer(Modifier.height(24.dp))
        OutlinedTextField(
            value = phone, onValueChange = { phone = it },
            label = { Text("رقم الجوال") }, singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))
        Button(onClick = onLogin, modifier = Modifier.fillMaxWidth()) { Text("دخول") }
        Spacer(Modifier.height(12.dp))
        Text("نسخة تجريبية: اضغط دخول بأي رقم", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
fun Dashboard(onLogout: () -> Unit) {
    var studentIdx by rememberSaveable { mutableIntStateOf(0) }
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val s = SampleData.students[studentIdx]
    val tabs = listOf("الرئيسية", "الحضور", "الواجبات", "الاختبارات", "الدرجات", "الملاحظات")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(SampleData.SCHOOL) },
                actions = { TextButton(onClick = onLogout) { Text("خروج") } },
            )
        }
    ) { pad ->
        Column(Modifier.padding(pad)) {
            Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SampleData.students.forEachIndexed { i, st ->
                    FilterChip(selected = i == studentIdx, onClick = { studentIdx = i }, label = { Text(st.name) })
                }
            }
            ScrollableTabRow(selectedTabIndex = tab, edgePadding = 8.dp) {
                tabs.forEachIndexed { i, t -> Tab(selected = tab == i, onClick = { tab = i }, text = { Text(t) }) }
            }
            when (tab) {
                0 -> HomeScreen(s) { tab = it }
                1 -> AttendanceScreen(s)
                2 -> HomeworkScreen(s)
                3 -> ExamsScreen(s)
                4 -> GradesScreen(s)
                else -> NotesScreen(s)
            }
        }
    }
}

@Composable
fun ListScreen(content: LazyListScope.() -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = content,
    )
}

@Composable
fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(14.dp), content = content) }
}

@Composable
fun SummaryCard(title: String, value: String, sub: String, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(14.dp)) {
            Text(title, color = MaterialTheme.colorScheme.outline)
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(sub, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

fun gradeColor(p: Float): Color = when {
    p >= 0.85f -> Color(0xFF2E7D32)
    p >= 0.70f -> Color(0xFFF9A825)
    else -> Color(0xFFC62828)
}

@Composable
fun HomeScreen(s: StudentData, onGo: (Int) -> Unit) {
    val absent = s.attendance.count { it.status == AttStatus.ABSENT }
    val late = s.attendance.count { it.status == AttStatus.LATE }
    val rate = (s.attendance.size - absent) * 100 / s.attendance.size
    val pending = s.homework.filter { !it.done }
    val avg = s.grades.sumOf { it.score } * 100 / s.grades.sumOf { it.max }
    val exam = s.exams.first()
    ListScreen {
        item {
            Text("${s.name} • ${s.grade}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        item { SummaryCard("نسبة الحضور", "$rate%", "غياب: $absent • تأخر: $late") { onGo(1) } }
        item {
            SummaryCard("واجبات لم تُسلَّم", "${pending.size}",
                pending.firstOrNull()?.let { "الأقرب: ${it.subject} - ${it.due}" } ?: "لا يوجد") { onGo(2) }
        }
        item { SummaryCard("الاختبار القادم", exam.subject, "${exam.date} • ${exam.time}") { onGo(3) } }
        item { SummaryCard("معدل الدرجات", "$avg%", "${s.grades.size} مواد") { onGo(4) } }
        item { SummaryCard("آخر ملاحظة", s.notes.first().from, s.notes.first().text) { onGo(5) } }
    }
}

@Composable
fun AttendanceScreen(s: StudentData) {
    val absent = s.attendance.count { it.status == AttStatus.ABSENT }
    val late = s.attendance.count { it.status == AttStatus.LATE }
    val present = s.attendance.size - absent
    ListScreen {
        item {
            InfoCard {
                Text("ملخص الفترة", fontWeight = FontWeight.Bold)
                Text("حضور: $present  •  غياب: $absent  •  تأخر: $late")
            }
        }
        items(s.attendance) { r ->
            InfoCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(r.status.emoji, fontSize = 22.sp)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(r.date, fontWeight = FontWeight.Bold)
                        Text(r.status.label + if (r.note.isNotEmpty()) " - ${r.note}" else "")
                    }
                }
            }
        }
    }
}

@Composable
fun HomeworkScreen(s: StudentData) {
    ListScreen {
        items(s.homework) { h ->
            InfoCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(if (h.done) "✅" else "⬜", fontSize = 22.sp)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("${h.subject}: ${h.title}", fontWeight = FontWeight.Bold)
                        Text("موعد التسليم: ${h.due}", style = MaterialTheme.typography.bodySmall)
                        Text(if (h.done) "تم التسليم" else "لم يُسلَّم بعد",
                            color = if (h.done) Color(0xFF2E7D32) else Color(0xFFC62828))
                    }
                }
            }
        }
    }
}

@Composable
fun ExamsScreen(s: StudentData) {
    ListScreen {
        items(s.exams) { e ->
            InfoCard {
                Text("📝 ${e.subject}", fontWeight = FontWeight.Bold)
                Text(e.type)
                Text("${e.date} • ${e.time}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun GradesScreen(s: StudentData) {
    val avg = s.grades.sumOf { it.score } * 100 / s.grades.sumOf { it.max }
    ListScreen {
        item {
            InfoCard {
                Text("الفصل الدراسي الأول", fontWeight = FontWeight.Bold)
                Text("المعدل العام: $avg%")
            }
        }
        items(s.grades) { g ->
            val p = g.score.toFloat() / g.max
            InfoCard {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(g.subject, fontWeight = FontWeight.Bold)
                        Text(g.title, style = MaterialTheme.typography.bodySmall)
                    }
                    Text("${g.score} / ${g.max}", fontWeight = FontWeight.Bold, color = gradeColor(p))
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(progress = { p }, modifier = Modifier.fillMaxWidth(), color = gradeColor(p))
            }
        }
    }
}

@Composable
fun NotesScreen(s: StudentData) {
    ListScreen {
        items(s.notes) { n ->
            InfoCard {
                Row {
                    Text(if (n.positive) "👍" else "⚠️", fontSize = 22.sp)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(n.from, fontWeight = FontWeight.Bold)
                        Text(n.text)
                        Text(n.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    }
                }
            }
        }
    }
}
