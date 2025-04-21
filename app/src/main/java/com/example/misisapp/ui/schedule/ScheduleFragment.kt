package com.example.misisapp.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.misisapp.R
import com.example.misisapp.databinding.FragmentScheduleBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var dayAdapter: DayScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView = root.findViewById(R.id.day_schedule_list)

        /*val textView: TextView = binding.textSchedule
        scheduleViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exampleData = createExampleData()

        dayAdapter = DayScheduleAdapter(exampleData) { currentLesson, nextLessons ->
            val action =
                ScheduleFragmentDirections.actionNavigationScheduleToNavgiationLessonDetails(
                    currentLesson,
                    nextLessons
                )

            view.findNavController().navigate(action)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = dayAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun createExampleData(): List<DayScheduleItem> {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru", "RU"))

        return listOf(
            DayScheduleItem(
                date = dateFormat.parse("01.01.2024 00:00")!!,
                weekday = "Понедельник",
                lessons = listOf(
                    LessonScheduleItem(
                        date = dateFormat.parse("01.01.2024 10:50")!!,
                        subject = "Теоретические основы проектирования и оптимизации алгоритмов обработки больших данных",
                        teacher = "Иванов И.И.",
                        groups = listOf("Группа 101"),
                        lessonType = "Лекция",
                        auditorium = "Ауд. 305",
                        address = "Корпус 1, ул. Математики, 1",
                        link = "https://example.com/math",
                        comment = "Прочитать главы 1-3"
                    ),
                    LessonScheduleItem(
                        date = dateFormat.parse("01.01.2024 12:40")!!,
                        subject = "Моделирование, анализ и оптимизация распределённых вычислительных систем",
                        teacher = "Петров П.П.",
                        groups = listOf("Группа 102"),
                        lessonType = "Практическое занятие",
                        auditorium = "Ауд. 202",
                        address = "Корпус 2, ул. Науки, 2",
                        link = "https://example.com/physics",
                        comment = "Подготовить лабораторную работу"
                    ),
                    LessonScheduleItem(
                        date = dateFormat.parse("01.01.2024 14:30")!!,
                        subject = "Методы и алгоритмы интеллектуальной обработки многомерных данных в реальном времени",
                        teacher = "Смирнова С.С.",
                        groups = listOf("Группа 103"),
                        lessonType = "Семинар",
                        auditorium = "Ауд. 104",
                        address = "Корпус 1, ул. Историков, 4",
                        link = "https://example.com/history",
                        comment = "Прочитать параграф 5"
                    )
                )
            ),
            DayScheduleItem(
                date = dateFormat.parse("02.01.2024 00:00")!!,
                weekday = "Вторник",
                lessons = listOf(
                    LessonScheduleItem(
                        date = dateFormat.parse("02.01.2024 09:00")!!,
                        subject = "Практические аспекты применения квантовых вычислений в задачах криптографии и моделирования",
                        teacher = "Захаров А.А.",
                        groups = listOf("Группа 101", "Группа 102"),
                        lessonType = "Лекция",
                        auditorium = "Ауд. 301",
                        address = "Корпус 2, ул. Биологии, 3",
                        link = "https://example.com/biology",
                        comment = "Прочитать главу 2"
                    ),
                    LessonScheduleItem(
                        date = dateFormat.parse("02.01.2024 10:50")!!,
                        subject = "Комплексная безопасность информационных систем и киберфизических устройств",
                        teacher = "Сидорова Н.Н.",
                        groups = listOf("Группа 103"),
                        lessonType = "Практическое занятие",
                        auditorium = "Лаб. 12",
                        address = "Корпус 3, ул. Химиков, 7",
                        link = "https://example.com/chemistry",
                        comment = "Принести реактивы для эксперимента"
                    ),
                    LessonScheduleItem(
                        date = dateFormat.parse("02.01.2024 12:40")!!,
                        subject = "Философия",
                        teacher = "Кузнецов А.А.",
                        groups = listOf("Группа 101"),
                        lessonType = "Семинар",
                        auditorium = "Ауд. 203",
                        address = "Корпус 1, ул. Логики, 9",
                        link = "https://example.com/philosophy",
                        comment = "Подготовить вопросы для обсуждения"
                    ),
                    LessonScheduleItem(
                        date = dateFormat.parse("02.01.2024 14:30")!!,
                        subject = "Экономика",
                        teacher = "Иванова Е.Е.",
                        groups = listOf("Группа 102"),
                        lessonType = "Лекция",
                        auditorium = "Ауд. 405",
                        address = "Корпус 4, ул. Финансов, 6",
                        link = "https://example.com/economy",
                        comment = "Изучить тему «Макроэкономика»"
                    )
                )
            ),
            DayScheduleItem(
                date = dateFormat.parse("03.01.2024 00:00")!!,
                weekday = "Среда",
                lessons = listOf(
                    LessonScheduleItem(
                        date = dateFormat.parse("03.01.2024 12:40")!!,
                        subject = "Программирование",
                        teacher = "Смирнов А.А.",
                        groups = listOf("Группа 101", "Группа 103"),
                        lessonType = "Лекция",
                        auditorium = "Ауд. 302",
                        address = "Корпус 1, ул. Программирования, 2",
                        link = "https://example.com/programming",
                        comment = "Прочитать главы 1-5"
                    ),
                    LessonScheduleItem(
                        date = dateFormat.parse("03.01.2024 14:30")!!,
                        subject = "Линейная алгебра",
                        teacher = "Петров В.В.",
                        groups = listOf("Группа 102"),
                        lessonType = "Практическое занятие",
                        auditorium = "Ауд. 106",
                        address = "Корпус 2, ул. Математики, 8",
                        link = "https://example.com/algebra",
                        comment = "Решить задачи из учебника"
                    ),
                    LessonScheduleItem(
                        date = dateFormat.parse("03.01.2024 16:20")!!,
                        subject = "Архитектурные подходы и технологии разработки программного обеспечения корпоративного уровня",
                        teacher = "Алексеева Т.Т.",
                        groups = listOf("Группа 103"),
                        lessonType = "Семинар",
                        auditorium = "Ауд. 105",
                        address = "Корпус 1, ул. Литературы, 10",
                        link = "https://example.com/literature",
                        comment = "Обсудить произведения Пушкина"
                    )
                )
            ),
            DayScheduleItem(
                date = dateFormat.parse("04.01.2024 00:00")!!,
                weekday = "Четверг",
                lessons = listOf(
                    LessonScheduleItem(
                        date = dateFormat.parse("04.01.2024 09:00")!!,
                        subject = "Математика",
                        teacher = "Иванова И.И.",
                        groups = listOf("Группа 101"),
                        lessonType = "Лекция",
                        auditorium = "Ауд. 303",
                        address = "Корпус 1, ул. Математики, 1",
                        link = "https://example.com/math",
                        comment = "Изучить теорему о производной"
                    ),
                    LessonScheduleItem(
                        date = dateFormat.parse("04.01.2024 10:50")!!,
                        subject = "Физика",
                        teacher = "Петров В.В.",
                        groups = listOf("Группа 102"),
                        lessonType = "Лабораторная работа",
                        auditorium = "Лаб. 13",
                        address = "Корпус 2, ул. Науки, 6",
                        link = "https://example.com/physics-lab",
                        comment = "Пройти эксперименты по механике"
                    ),
                    LessonScheduleItem(
                        date = dateFormat.parse("04.01.2024 12:40")!!,
                        subject = "Инженерия данных: методы структурирования, хранения и интеграции разнородной информации",
                        teacher = "Смирнов С.С.",
                        groups = listOf("Группа 101"),
                        lessonType = "Семинар",
                        auditorium = "Ауд. 201",
                        address = "Корпус 3, ул. ИТ, 9",
                        link = "https://example.com/informatics",
                        comment = "Решить задания по программированию"
                    )
                )
            ),
            DayScheduleItem(
                date = dateFormat.parse("05.01.2024 00:00")!!,
                weekday = "Пятница",
                lessons = listOf(
                    LessonScheduleItem(
                        date = dateFormat.parse("05.01.2024 12:40")!!,
                        subject = "Практические аспекты применения квантовых вычислений в задачах криптографии и моделирования",
                        teacher = "Сидорова Н.Н.",
                        groups = listOf("Группа 102"),
                        lessonType = "Лекция",
                        auditorium = "Ауд. 404",
                        address = "Корпус 3, ул. Химиков, 2",
                        link = "https://example.com/chemistry",
                        comment = "Обсудить реакции кислот"
                    ),
                    LessonScheduleItem(
                        date = dateFormat.parse("05.01.2024 14:30")!!,
                        subject = "Экономика",
                        teacher = "Иванова М.М.",
                        groups = listOf("Группа 103"),
                        lessonType = "Семинар",
                        auditorium = "Ауд. 206",
                        address = "Корпус 2, ул. Экономики, 5",
                        link = "https://example.com/economy-seminar",
                        comment = "Подготовить доклад на тему «Бюджет»"
                    ),
                    LessonScheduleItem(
                        date = dateFormat.parse("05.01.2024 16:20")!!,
                        subject = "Практические аспекты применения квантовых вычислений в задачах криптографии и моделирования",
                        teacher = "Смирнов В.В.",
                        groups = listOf("Группа 101"),
                        lessonType = "Лабораторная работа",
                        auditorium = "Лаб. 22",
                        address = "Корпус 3, ул. Историков, 3",
                        link = "https://example.com/history-lab",
                        comment = "Принести источники для исследования"
                    )
                )
            ),
            DayScheduleItem(
                date = dateFormat.parse("06.01.2024 00:00")!!,
                weekday = "Суббота",
                lessons = listOf(
                    LessonScheduleItem(
                        date = dateFormat.parse("06.01.2024 10:50")!!,
                        subject = "Технологии виртуальной и дополненной реальности в разработке интерактивных приложений",
                        teacher = "Захарова О.О.",
                        groups = listOf("Группа 101"),
                        lessonType = "Семинар",
                        auditorium = "Ауд. 207",
                        address = "Корпус 3, ул. Биологии, 4",
                        link = "https://example.com/biology-seminar",
                        comment = "Обсудить тему клеточной структуры"
                    ),
                    LessonScheduleItem(
                        date = dateFormat.parse("06.01.2024 12:40")!!,
                        subject = "Инновационные методы машинного обучения в анализе естественного языка и компьютерного зрения",
                        teacher = "Кузнецова А.А.",
                        groups = listOf("Группа 102"),
                        lessonType = "Лекция",
                        auditorium = "Ауд. 305",
                        address = "Корпус 1, ул. Литературы, 6",
                        link = "https://example.com/literature-lecture",
                        comment = "Прочитать произведения Чехова"
                    ),
                    LessonScheduleItem(
                        date = dateFormat.parse("06.01.2024 14:30")!!,
                        subject = "Физика",
                        teacher = "Сидоров С.С.",
                        groups = listOf("Группа 103"),
                        lessonType = "Лабораторная работа",
                        auditorium = "Лаб. 17",
                        address = "Корпус 2, ул. Физики, 8",
                        link = "https://example.com/physics-lab",
                        comment = "Решить задачи по электродинамике"
                    ),
                    LessonScheduleItem(
                        date = dateFormat.parse("06.01.2024 16:20")!!,
                        subject = "Программирование",
                        teacher = "Иванова Л.Л.",
                        groups = listOf("Группа 101"),
                        lessonType = "Семинар",
                        auditorium = "Ауд. 303",
                        address = "Корпус 1, ул. Программирования, 5",
                        link = "https://example.com/programming-seminar",
                        comment = "Подготовить отчет по проекту"
                    )
                )
            )
        )
    }
}