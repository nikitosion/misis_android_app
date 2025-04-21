package com.example.misisapp.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.misisapp.databinding.FragmentLessonDetailsBinding
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class LessonDetails : Fragment() {
    private var _binding: FragmentLessonDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLessonDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    private fun curNextLessonTime(timestamp: Long): String {
        val dateHoursMinutes = SimpleDateFormat("HH:mm").format(timestamp)
        val lessonNumber = when (dateHoursMinutes) {
            "09:00" -> "1"
            "10:50" -> "2"
            "12:40" -> "3"
            "14:30" -> "4"
            "16:20" -> "5"
            "18:00" -> "6"
            "19:35" -> "7"
            else -> "##"
        }
        val time = LocalTime.parse(dateHoursMinutes, DateTimeFormatter.ofPattern("HH:mm"))
        val nextLessonTime = time.plusHours(1).plusMinutes(35)
        val nextLessonTimeFormatted = nextLessonTime.format(DateTimeFormatter.ofPattern("HH:mm"))

        return "$dateHoursMinutes - $nextLessonTimeFormatted ($lessonNumber пара)"
    }

    private fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))

        val formattedDate = dateFormat.format(date)

        return formattedDate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receivedArgs: LessonDetailsArgs by navArgs()

        val subjectTextView = binding.subjectName
        val dateTextView = binding.lessonDate
        val timeTextView = binding.lessonTime
        val groupsTextView = binding.lessonGroups
        val lessonTypeTextView = binding.lessonType
        val auditoriumTextView = binding.lessonAuditorium
        val addressTextView = binding.lessonAddress
        val linkTextView = binding.lessonLink
        val commentTextView = binding.lessonComment
        val nextLessonInfoLinearLayout = binding.nextLessonInfo
        val nextLessonSubjectTextView = binding.nextSubjectName
        val nextLessonTimeTextView = binding.nextLessonTime


        timeTextView.text = curNextLessonTime(receivedArgs.currentLesson.date.time)
        dateTextView.text = formatDate(receivedArgs.currentLesson.date.time)
        subjectTextView.text = receivedArgs.currentLesson.subject
        groupsTextView.text = receivedArgs.currentLesson.groups[0]
        lessonTypeTextView.text = receivedArgs.currentLesson.lessonType
        auditoriumTextView.text = receivedArgs.currentLesson.auditorium
        addressTextView.text = receivedArgs.currentLesson.address
        linkTextView.text = receivedArgs.currentLesson.link
        commentTextView.text = receivedArgs.currentLesson.comment

        if (receivedArgs.currentLesson.date.time == receivedArgs.nextLessons[0].date.time) {
            nextLessonInfoLinearLayout.visibility = View.GONE
        } else {
            nextLessonSubjectTextView.text = receivedArgs.nextLessons[0].subject
            nextLessonTimeTextView.text = curNextLessonTime(receivedArgs.nextLessons[0].date.time)
            nextLessonInfoLinearLayout.setOnClickListener { _ ->
                val newNextLessons: Array<LessonScheduleItem>
                if (receivedArgs.nextLessons.isEmpty()) {
                    newNextLessons = emptyArray()
                } else {
                    newNextLessons = receivedArgs.nextLessons.sliceArray(1..<receivedArgs.nextLessons.size)
                }
                /*val action =
                    ScheduleFragmentDirections.actionNavigationScheduleToNavgiationLessonDetails(
                        receivedArgs.nextLessons[0],
                        newNextLessons)
                view.findNavController().navigate(action)*/
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}