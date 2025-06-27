package com.example.misisapp.presentation.ui.lessondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.misisapp.R
import com.example.misisapp.databinding.FragmentLessonDetailsBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class LessonDetailsFragment : Fragment() {
    private var _binding: FragmentLessonDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments ?: return

        val date = Date(args.getLong("date"))
        val dateFormatter = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        binding.lessonDate.text = dateFormatter.format(date)

        val startTime = Date(args.getLong("start_time"))
        val endTime = Date(args.getLong("end_time"))
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.lessonTime.text = "${timeFormatter.format(startTime)} - ${timeFormatter.format(endTime)}"

        binding.subjectName.text = args.getString("subject")
        binding.teacherName.text = args.getString("teacher")
        binding.lessonGroups.text = args.getString("groups")
        binding.lessonType.text = args.getString("lesson_type")
        binding.lessonAuditorium.text = args.getString("auditorium")
        binding.lessonAddress.text = args.getString("address")
        binding.lessonLink.text = args.getString("link")
        binding.lessonComment.text = args.getString("comment")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                sendSelectedDateResult()
                findNavController().navigate(R.id.action_navgiation_lesson_details_to_navigation_schedule)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sendSelectedDateResult() {
        val args = requireArguments()
        val dateMs = args.getLong("date")
        val calendar = Calendar.getInstance().apply { timeInMillis = dateMs }
        val result = Bundle().apply {
            putInt("day", calendar.get(Calendar.DAY_OF_MONTH))
            putInt("month", calendar.get(Calendar.MONTH))
            putInt("year", calendar.get(Calendar.YEAR))
        }
        parentFragmentManager.setFragmentResult("details_result", result)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}