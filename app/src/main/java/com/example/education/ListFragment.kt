package com.example.education

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.education.data.search.Coins
import com.example.education.databinding.FragmentListBinding
import retrofit2.HttpException

class ListFragment : Fragment() {

    private var binding: FragmentListBinding? = null
    private var adapter: CustomAdapter? = null
    private var viewModel: ListViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = defaultViewModelProviderFactory.create(ListViewModel::class.java)
    }

    private fun initView() {
        binding?.button?.setOnClickListener {
            onButtonClicked()
            binding?.button?.visibility = View.GONE
        }
    }

    private fun onButtonClicked() {
        binding?.progressBar?.visibility = View.VISIBLE
        useMerge()
        sendRequest()
    }

    private fun sendRequest() {
        viewModel?.sendRequest()?.subscribe({ setList(it.coins) }, { errorHandle(it) })
    }

    private fun useMerge() {
        viewModel?.merge()?.subscribe({
            Log.d("ADV_MERGE", it.coins.toString())
        }, {
            errorHandle(it)
            Log.e("ADV_MERGE", it.localizedMessage ?: it.toString())
        })
    }

    private fun setList(list: ArrayList<Coins>) = with(binding) {
        adapter = CustomAdapter(list)
        binding?.progressBar?.visibility = View.GONE
        binding?.rwList?.adapter = adapter
        binding?.rwList?.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun errorHandle(e: Throwable) = with(binding) {
        binding?.progressBar?.visibility = View.GONE
        binding?.rwList?.visibility = View.GONE
        binding?.error?.visibility = View.VISIBLE
        if (e is HttpException) {
            binding?.error?.text =
                "Status code: " + e.code() + e.response()?.errorBody().toString()
        } else {
            binding?.error?.text = "Retrofit error" + e.localizedMessage
        }
    }

    companion object {
        fun newInstance() = ListFragment()
    }
}
