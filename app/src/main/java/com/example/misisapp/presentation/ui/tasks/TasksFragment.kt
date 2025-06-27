package com.example.misisapp.presentation.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.misisapp.databinding.FragmentGroupBinding

class TasksFragment : Fragment() {

    private var _binding: FragmentGroupBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*val organizationsViewModel =
            ViewModelProvider(this).get(GroupViewModel::class.java)*/

        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textOrganizations
        organizationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}