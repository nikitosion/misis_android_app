package com.example.misisapp.presentation.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.misisapp.R
import com.example.misisapp.databinding.FragmentScheduleBinding
import com.example.misisapp.domain.model.LessonItem
import com.example.misisapp.presentation.ui.schedule.adapter.ScheduleAdapter
import com.example.misisapp.presentation.viewmodel.ScheduleViewModel
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val scheduleViewModel: ScheduleViewModel by viewModels()
    private lateinit var skeleton: Skeleton
    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        requireActivity().supportFragmentManager
            .setFragmentResultListener(
                "date_selected",
                this
            ) { _, bundle ->

                val day = bundle.getInt("day")
                val month = bundle.getInt("month")
                val year = bundle.getInt("year")

                val calendar = Calendar.getInstance()
                calendar.set(year, month, day)
                scheduleViewModel.loadSchedule(calendar.time)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSkeletonLoading()
        setupFragmentResultListener()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener(
            "details_result",
            viewLifecycleOwner
        ) { _, bundle ->

            val day = bundle.getInt("day")
            val month = bundle.getInt("month")
            val year = bundle.getInt("year")

            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            scheduleViewModel.loadSchedule(calendar.time)
        }
    }

    private fun setupRecyclerView() {
        scheduleAdapter = ScheduleAdapter { lesson ->
            navigateToLessonDetails(lesson)
        }
        binding.scheduleList.apply {
            adapter = this@ScheduleFragment.scheduleAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {

        scheduleViewModel.schedule.observe(viewLifecycleOwner) { scheduleItems ->
            scheduleAdapter.submitList(scheduleItems)
            binding.scheduleList.isNestedScrollingEnabled = true;
            binding.scheduleList.post {
                skeleton.showOriginal()
            }
        }

        scheduleViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) skeleton.showSkeleton() else skeleton.showOriginal()
        }

        scheduleViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToLessonDetails(lesson: LessonItem) {
        val bundle = Bundle().apply {
            putString("lesson_id", lesson.id)
            putLong("date", lesson.date.time)
            putString("subject", lesson.subject)
            putString("teacher", lesson.teacher)
            putString("groups", lesson.groups.toString())
            putString("lesson_type", lesson.lessonType)
            putString("auditorium", lesson.auditorium)
            putString("address", lesson.address)
            putString("link", lesson.link)
            putString("comment", lesson.comment)
            putLong("start_time", lesson.startTime.time)
            putLong("end_time", lesson.endTime.time)
        }

        findNavController().navigate(
            R.id.action_navigation_schedule_to_navgiation_lesson_details,
            bundle
        )
    }

    private fun setupSkeletonLoading() {
        skeleton = binding.scheduleList.applySkeleton(
            R.layout.day_item_skeleton,
            itemCount = 3
        )
        binding.scheduleList.isNestedScrollingEnabled = false;
    }
}