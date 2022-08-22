package com.example.education

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.education.data.Crypto
import com.example.education.data.search.Coins
import com.example.education.databinding.FragmentListBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: CustomAdapter
    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.rwList.layoutManager = layoutManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = defaultViewModelProviderFactory.create(ListViewModel::class.java)
    }

    private fun initView() = with(binding) {
        button.setOnClickListener {
            onButtonClicked()
            button.visibility = View.GONE
        }
    }

    private fun onButtonClicked() = with(binding) {
        progressBar.visibility = View.VISIBLE
        viewModel.cryptoList.subscribe({ sendSearch(it) }, { errorHandle(it) })
        useMerge()
        sendRequest()
    }

    private fun sendRequest() {
        viewModel.sendRequest()
            .subscribeOn(Schedulers.io())
            .map { it ->
                it.sortedBy { it.name }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewModel.cryptoList.onNext(it)
            }, { errorHandle(it) })
    }

    private fun useMerge() {
        val firstObservable = viewModel.sendFirstRequest()
        val secondObservable = viewModel.sendSecndRequest()

        Observable.merge(
            secondObservable,
            firstObservable
        ) //get from first, then from second, depends on Schedulers?
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d("ADV_MERGE", it.toString())
                },
                {
                    errorHandle(it)
                    Log.e("ADV_MERGE", it.localizedMessage ?: it.toString())
                }
            )

        Observable.concat(
            secondObservable,
            firstObservable
        ) // add first at the end of second
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d("ADV_CONCAT", it.toString())
                },
                {
                    errorHandle(it)
                    Log.e("ADV_CONCAT", it.localizedMessage ?: it.toString())
                }
            )
    }

    private fun sendSearch(list: List<Crypto>) {
        if (list.isNotEmpty()) {
            list[0].name?.let {
                viewModel.search(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { coin -> setList(coin.coins) },
                        { error -> errorHandle(error) })
            }
        } else {
            viewModel.search("bitcoin")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { coin -> setList(coin.coins) },
                    { error -> errorHandle(error) })
        }
    }

    private fun setList(list: ArrayList<Coins>) = with(binding) {
        adapter = CustomAdapter(list)
        progressBar.visibility = View.GONE
        rwList.adapter = adapter
        rwList.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun errorHandle(e: Throwable) = with(binding) {
        progressBar.visibility = View.GONE
        rwList.visibility = View.GONE
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
