package ru.sergeevdmitry8i11.catsandmice

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

class StartFragment : Fragment() {
    private val viewModel: StartViewModel by viewModels()
    private val minValueSpeed = 50
    private val minValueSize = 1
    private val minValueCount = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false)

        val sbSpeed = view.findViewById<SeekBar>(R.id.sb_mouse_speed)
        val sbMouse = view.findViewById<SeekBar>(R.id.sb_mouse_count)
        val sbSize = view.findViewById<SeekBar>(R.id.sb_mouse_size)

        val tvSpeed = view.findViewById<TextView>(R.id.tv_mouse_speed)
        val tvMouse = view.findViewById<TextView>(R.id.tv_mouse_count)
        val tvSize = view.findViewById<TextView>(R.id.tv_mouse_size)

        sbSpeed.progress = viewModel.speed.value!! - minValueSpeed
        sbMouse.progress = viewModel.miceCount.value!! - minValueCount
        sbSize.progress = viewModel.mouseSize.value!! - minValueSize

        viewModel.speed.observe(viewLifecycleOwner, Observer { value ->
            tvSpeed.text = getString(R.string.tv_am_mouse_speed_value, value)
        })
        viewModel.miceCount.observe(viewLifecycleOwner, Observer { value ->
            tvMouse.text =  value.toString()
        })
        viewModel.mouseSize.observe(viewLifecycleOwner, Observer { value ->
            tvSize.text = value.toString()
        })


        sbMouse.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.updateMiceCount(progress + minValueCount)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        sbSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.updateSpeed(progress + minValueSpeed)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        sbSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.updateMouseSize(progress + minValueSize)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        view.findViewById<Button>(R.id.btn_start).setOnClickListener{
            //findNavController().navigate(R.id.action_startFragment_to_gameActivity)
            val intent = Intent(activity, GameActivity::class.java).apply{
                putExtra(GameActivity.MICE_COUNT, viewModel.miceCount.value)
                putExtra(GameActivity.SPEED, viewModel.speed.value)
                putExtra(GameActivity.SIZE, viewModel.mouseSize.value)

            }
            startActivity(intent)
        }
        view.findViewById<Button>(R.id.btn_stats).setOnClickListener{
            findNavController().navigate(R.id.action_startFragment_to_statsFragment)
        }
        return view
    }
}