package com.example.misisapp.ui.schedule

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.misisapp.R
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

class DayScheduleAdapter(private var days: List<DayScheduleItem>, private val onChildClick: (currentLesson: LessonScheduleItem, nextLessons: Array<LessonScheduleItem>) -> Unit) : RecyclerView.Adapter<DayScheduleAdapter.DayViewHolder>() {

    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayAndWeekDay: TextView = itemView.findViewById(R.id.day_schedule_date)
        val lessonsRecyclerView: RecyclerView = itemView.findViewById(R.id.lessons_list)
        val scheduleDayView: LinearLayout = itemView.findViewById(R.id.day_schedule)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_schedule_item, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]
        val localDate = day.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val dayOfWeek = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("ru", "RU"))
        val month = localDate.month.getDisplayName(TextStyle.FULL, Locale("ru", "RU"))
        holder.dayAndWeekDay.text = "${localDate.dayOfMonth} $month, $dayOfWeek"

        val layoutParams = holder.scheduleDayView.layoutParams as ViewGroup.MarginLayoutParams

        if (position == days.size - 1) {
            layoutParams.bottomMargin = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                15f, holder.itemView.context.resources.displayMetrics
            ).toInt()
        }

        holder.scheduleDayView.layoutParams = layoutParams

        holder.lessonsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = LessonScheduleAdapter(day.lessons, onChildClick)
        }
    }

    fun updateData(newItems: List<DayScheduleItem>) {
        days = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = days.size
}