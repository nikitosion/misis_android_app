package com.example.misisapp.presentation.ui.schedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.misisapp.databinding.LessonScheduleItemBinding
import com.example.misisapp.domain.model.LessonItem
import java.text.SimpleDateFormat
import java.util.Locale

class LessonAdapter(
    private val onLessonClick: (LessonItem) -> Unit
) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    private var lessons = emptyList<LessonItem>()

    fun submitList(newLessons: List<LessonItem>) {
        lessons = newLessons
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LessonScheduleItemBinding.inflate(inflater, parent, false)
        return LessonViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(lessons[position])
    }


    override fun getItemCount(): Int = lessons.size

    inner class LessonViewHolder(
        itemView: View,
        private val binding: LessonScheduleItemBinding?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(lesson: LessonItem) {
            binding?.let {
                val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                it.lessonNumber.text = timeToPairNum(timeFormatter.format(lesson.startTime))
                it.subjectName.text = lesson.subject
                it.auditorium.text = lesson.auditorium
                it.lessonTime.text =
                    "${timeFormatter.format(lesson.startTime)} - ${timeFormatter.format(lesson.endTime)}"

                it.root.setOnClickListener {
                    onLessonClick(lesson)
                }
            }
        }
    }

    private fun timeToPairNum(time: String) : String {
        val schedule = mapOf(
            "9:00" to "1",
            "10:50" to "2",
            "12:40" to "3",
            "14:30" to "4",
            "16:20" to "5",
            "18:00" to "6",
            "19:35" to "7"
        )
        return schedule[time] ?: "ВР"
    }
}