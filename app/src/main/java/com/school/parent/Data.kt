package com.school.parent

enum class AttStatus(val label: String, val emoji: String) {
    PRESENT("حاضر", "✅"), ABSENT("غائب", "❌"), LATE("متأخر", "⏰")
}

data class AttendanceRecord(val date: String, val status: AttStatus, val note: String = "")
data class HomeworkItem(val subject: String, val title: String, val due: String, val done: Boolean)
data class ExamItem(val subject: String, val type: String, val date: String, val time: String)
data class GradeItem(val subject: String, val title: String, val score: Int, val max: Int)
data class NoteItem(val from: String, val text: String, val date: String, val positive: Boolean)

data class StudentData(
    val name: String,
    val grade: String,
    val attendance: List<AttendanceRecord>,
    val homework: List<HomeworkItem>,
    val exams: List<ExamItem>,
    val grades: List<GradeItem>,
    val notes: List<NoteItem>,
)

// بيانات تجريبية فقط — استبدلها لاحقًا ببيانات حقيقية من الخادم
object SampleData {
    const val SCHOOL = "مدرسة النور الأهلية"

    val students = listOf(
        StudentData(
            name = "يوسف",
            grade = "الصف الخامس / أ",
            attendance = listOf(
                AttendanceRecord("الأربعاء 7 أكتوبر", AttStatus.PRESENT),
                AttendanceRecord("الثلاثاء 6 أكتوبر", AttStatus.PRESENT),
                AttendanceRecord("الإثنين 5 أكتوبر", AttStatus.LATE, "تأخر 15 دقيقة"),
                AttendanceRecord("الأحد 4 أكتوبر", AttStatus.PRESENT),
                AttendanceRecord("الخميس 1 أكتوبر", AttStatus.ABSENT, "غياب بعذر (مرض)"),
                AttendanceRecord("الأربعاء 30 سبتمبر", AttStatus.PRESENT),
                AttendanceRecord("الثلاثاء 29 سبتمبر", AttStatus.PRESENT),
            ),
            homework = listOf(
                HomeworkItem("الرياضيات", "حل تمارين صفحة 45", "الخميس 8 أكتوبر", false),
                HomeworkItem("اللغة العربية", "كتابة موضوع تعبير عن الأمانة", "الأحد 11 أكتوبر", false),
                HomeworkItem("العلوم", "تقرير عن دورة الماء", "الأربعاء 7 أكتوبر", true),
                HomeworkItem("اللغة الإنجليزية", "حفظ كلمات الوحدة الثالثة", "الثلاثاء 6 أكتوبر", true),
            ),
            exams = listOf(
                ExamItem("الرياضيات", "اختبار قصير", "الأحد 18 أكتوبر", "8:00 ص"),
                ExamItem("العلوم", "اختبار شهري", "الإثنين 19 أكتوبر", "9:30 ص"),
                ExamItem("اللغة العربية", "اختبار شهري", "الثلاثاء 20 أكتوبر", "8:00 ص"),
                ExamItem("اللغة الإنجليزية", "اختبار شفهي", "الأربعاء 21 أكتوبر", "10:00 ص"),
            ),
            grades = listOf(
                GradeItem("الرياضيات", "اختبار قصير 1", 18, 20),
                GradeItem("اللغة العربية", "اختبار قصير 1", 16, 20),
                GradeItem("العلوم", "اختبار قصير 1", 19, 20),
                GradeItem("اللغة الإنجليزية", "اختبار قصير 1", 14, 20),
                GradeItem("التربية الإسلامية", "اختبار قصير 1", 20, 20),
            ),
            notes = listOf(
                NoteItem("معلم الرياضيات - أ. خالد", "مشاركة ممتازة في الحصة وحلّ المسائل بسرعة.", "6 أكتوبر", true),
                NoteItem("معلمة الإنجليزي - أ. نورة", "يحتاج إلى مراجعة المفردات في المنزل.", "5 أكتوبر", false),
                NoteItem("مشرف الصف", "تأخر صباحي يوم الإثنين، نرجو الحرص على الحضور مبكرًا.", "5 أكتوبر", false),
            ),
        ),
        StudentData(
            name = "سارة",
            grade = "الصف الثاني / ب",
            attendance = listOf(
                AttendanceRecord("الأربعاء 7 أكتوبر", AttStatus.PRESENT),
                AttendanceRecord("الثلاثاء 6 أكتوبر", AttStatus.PRESENT),
                AttendanceRecord("الإثنين 5 أكتوبر", AttStatus.PRESENT),
                AttendanceRecord("الأحد 4 أكتوبر", AttStatus.ABSENT, "غياب بدون عذر"),
                AttendanceRecord("الخميس 1 أكتوبر", AttStatus.PRESENT),
            ),
            homework = listOf(
                HomeworkItem("الرياضيات", "ورقة عمل الجمع والطرح", "الخميس 8 أكتوبر", false),
                HomeworkItem("اللغة العربية", "كتابة حرف (ج) ثلاث مرات", "الأربعاء 7 أكتوبر", true),
            ),
            exams = listOf(
                ExamItem("اللغة العربية", "اختبار قراءة", "الأحد 18 أكتوبر", "9:00 ص"),
                ExamItem("الرياضيات", "اختبار قصير", "الثلاثاء 20 أكتوبر", "9:00 ص"),
            ),
            grades = listOf(
                GradeItem("الرياضيات", "اختبار قصير 1", 10, 10),
                GradeItem("اللغة العربية", "اختبار قصير 1", 9, 10),
                GradeItem("العلوم", "اختبار قصير 1", 8, 10),
            ),
            notes = listOf(
                NoteItem("معلمة الصف - أ. هند", "سارة مجتهدة ومنظمة، استمري على هذا المستوى.", "6 أكتوبر", true),
            ),
        ),
    )
}
