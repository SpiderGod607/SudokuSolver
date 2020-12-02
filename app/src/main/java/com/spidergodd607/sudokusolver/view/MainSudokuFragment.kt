package com.spidergodd607.sudokusolver.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.spidergodd607.sudokusolver.R
import com.spidergodd607.sudokusolver.databinding.LayoutMainSudokuFragmentBinding
import com.spidergodd607.sudokusolver.game.Cell
import com.spidergodd607.sudokusolver.viewmodel.SudokuViewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainSudokuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainSudokuFragment : Fragment(), SudokuBoardView.OnTouchListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: LayoutMainSudokuFragmentBinding
    private lateinit var viewModel: SudokuViewModel
    private lateinit var mAdView:AdView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val dialog = context?.let {
            MaterialDialog(it)
                .title(R.string.your_title)
                .message(R.string.your_message)
                .positiveButton(R.string.agree)
        }

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_main_sudoku_fragment,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(SudokuViewModel::class.java)
        binding.sudokuViewModel = viewModel
        binding.lifecycleOwner = this
        binding.sudokuBoardView.registerListener(this)


        viewModel.sudokuGame.selectedCellLiveData.observe(viewLifecycleOwner, {
            updateSelectedCellUI(it)
        })

        viewModel.sudokuGame.cellsLiveData.observe(viewLifecycleOwner, {
            updateCells(it)
        })
        val buttons = listOf(
            binding.button1, binding.button2, binding.button3, binding.button4,
            binding.button5, binding.button6, binding.button7, binding.button8, binding.button9
        )
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.sudokuGame.handleInput(index + 1)
            }
        }

        viewModel.suc.observe(viewLifecycleOwner, {
            if (!it) {
                dialog?.show()
            }
        })


        return binding.root
    }

    private fun updateCells(cells: List<Cell>?) = cells?.let {

        binding.sudokuBoardView.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        binding.sudokuBoardView.updateSelectedCellUI(cell.first, cell.second)
    }

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Main_Sudoku_Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainSudokuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}