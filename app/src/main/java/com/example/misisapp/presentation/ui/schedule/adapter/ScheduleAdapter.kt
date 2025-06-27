package com.example.misisapp.presentation.ui.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.misisapp.databinding.DayItemBinding
import com.example.misisapp.domain.model.DayScheduleItem
import com.example.misisapp.domain.model.LessonItem
import java.text.SimpleDateFormat
import java.util.Locale

class ScheduleAdapter(
    private val onLessonClick: (LessonItem) -> Unit
) : RecyclerView.Adapter<ScheduleAdapter.DayViewHolder>() {

    private var items = emptyList<DayScheduleItem>()

    fun submitList(newItems: List<DayScheduleItem>) {
        val diffCallback = ScheduleDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        stateRestorationPolicy = StateRestorationPolicy.ALLOW
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = DayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class DayViewHolder(private val binding: DayItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dayItem: DayScheduleItem) {
            val dateFormatter = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
            binding.date.text = "${dateFormatter.format(dayItem.date)}, ${dayItem.weekday}"

            val lessonsAdapter = LessonAdapter(onLessonClick)

            binding.lessonsList.apply {
                adapter = lessonsAdapter
                layoutManager = LinearLayoutManager(binding.root.context)
            }

            lessonsAdapter.submitList(dayItem.lessons)
        }
    }

    private class ScheduleDiffCallback(
        private val oldList: List<DayScheduleItem>,
        private val newList: List<DayScheduleItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].date == newList[newItemPosition].date
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }
}