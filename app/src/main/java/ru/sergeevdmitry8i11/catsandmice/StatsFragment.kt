package ru.sergeevdmitry8i11.catsandmice

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import ru.sergeevdmitry8i11.catsandmice.placeholder.PlaceholderContent

class StatsFragment : Fragment() {
    private val statsList = mutableListOf<GameStats>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stats_list, container, false)
        val db = DbHelper(requireContext())
        statsList.addAll(db.getLastTenGames())

        view.findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MyItemRecyclerViewAdapter(statsList)
        }
        return view
    }
}