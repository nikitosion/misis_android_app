package com.example.misisapp.ui.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.misisapp.R
import com.google.android.material.divider.MaterialDivider
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LessonScheduleAdapter(private val lessons: List<LessonScheduleItem>, private val onChildClick: (lessonScheduleItem: LessonScheduleItem, nextLessons: Array<LessonScheduleItem>) -> Unit) : RecyclerView.Adapter<LessonScheduleAdapter.LessonViewHolder>() {

    class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lessonType: TextView = itemView.findViewById(R.id.lesson_type)
        val subjectName: TextView = itemView.findViewById(R.id.subject_name)
        val auditorium: TextView = itemView.findViewById(R.id.auditorium)
        val lessonTime: TextView = itemView.findViewById(R.id.lesson_time)
        val lessonNumber: TextView = itemView.findViewById(R.id.lesson_number)
        val divider: MaterialDivider = itemView.findViewById(R.id.lessons_divider)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lesson_schedule_item, parent, false)
        return LessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessons[position]
        var next_lesson = lessons[position]
        if (position + 1 < lessons.size) {
            next_lesson = lessons[position + 1]
        }
        holder.lessonType.text = lesson.lessonType
        holder.subjectName.text = lesson.subject
        holder.auditorium.text = lesson.auditorium
        val dateHours = SimpleDateFormat("HH").format(lesson.date)
        val dateMinutes = SimpleDateFormat("mm").format(lesson.date)
        holder.lessonNumber.text = when(dateHours.toString() + dateMinutes.toString()) {
            "0900" -> "1"
            "1050" -> "2"
            "1240" -> "3"
            "1430" -> "4"
            "1620" -> "5"
            "1800" -> "6"
            "1935" -> "7"
            else -> "##"
        }
        val time = LocalTime.parse("$dateHours:$dateMinutes", DateTimeFormatter.ofPattern("HH:mm"))
        val updatedTime = time.plusHours(1).plusMinutes(35)
        val updatedTimeFormatted = updatedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        holder.lessonTime.text = "$dateHours:$dateMinutes - $updatedTimeFormatted"
        if (position == lessons.size - 1) {
            holder.divider.dividerColor = 0xFFFFFF
        }

        holder.itemView.setOnClickListener {
            onChildClick(lesson,
                lessons.slice(position..lessons.size-1).toTypedArray()
            )
        }
    }


    override fun getItemCount(): Int = lessons.size
}