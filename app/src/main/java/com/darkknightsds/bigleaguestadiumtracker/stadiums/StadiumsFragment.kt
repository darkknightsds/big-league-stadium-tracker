package com.darkknightsds.bigleaguestadiumtracker.stadiums

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darkknightsds.bigleaguestadiumtracker.R


class StadiumsFragment : Fragment() {

    companion object {
        fun newInstance() = StadiumsFragment()
    }

    private lateinit var viewModel: StadiumsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stadiums_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StadiumsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
