package com.example.education

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.education.data.ApiInterface
import com.example.education.data.Crypto
import com.example.education.data.RetrofitClient
import com.example.education.databinding.FragmentListBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: CustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.rwList.layoutManager = layoutManager
        initView()
        return binding.root
    }

    private fun initView() = with(binding) {
        button.setOnClickListener {
            onButtonClicked()
            button.visibility = View.GONE
        }
    }

    private fun onButtonClicked() = with(binding) {

        progressBar.visibility = View.VISIBLE
        val api: ApiInterface = RetrofitClient.getClient()
        api.getCryptos("usd", "market_cap_desc", 100, 1, false)
            .subscribeOn(Schedulers.io())
            .map { it ->
                it.sortedBy { it.name }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ setList(it) }, { errorHandle(it) })
    }

    private fun setList(list: List<Crypto>) = with(binding) {
        progressBar.visibility = View.GONE
        adapter = CustomAdapter(list)
        rwList.adapter = adapter
        rwList.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun errorHandle(e: Throwable) = with(binding) {
        progressBar.visibility = View.GONE
        error.visibility = View.VISIBLE
        if (e is HttpException) {
            error.text =
                "Status code: " + e.code() + e.response()?.errorBody().toString()
        } else {
            error.text = "Retrofit error" + e.localizedMessage
        }
    }

    companion object {
        fun newInstance() = ListFragment()
    }
}
