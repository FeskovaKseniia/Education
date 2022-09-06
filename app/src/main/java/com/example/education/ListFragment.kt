package com.example.education

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.education.data.SearchResult
import com.example.education.data.search.SearchResponse
import com.example.education.databinding.FragmentListBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.HttpException

class ListFragment : Fragment() {

    private var binding: FragmentListBinding? = null
    private var adapter: CustomAdapter? = null
    private var viewModel: ListViewModel? = null
    private var compositeDisposable = CompositeDisposable()

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

    private fun initView() = with(binding) {
        this?.let {
            button.setOnClickListener {
                onButtonClicked()
                it.visibility = View.GONE
            }
            timerBtn.setOnClickListener {
                viewModel?.let { viewModel ->
                    compositeDisposable.add(viewModel.timerSubject.subscribe(
                        { time -> timer.text = time },
                        { error -> errorHandle(error) }
                    ))
                    viewModel.startTimer()
                }
            }
            unsubscribeBtn.setOnClickListener {
                compositeDisposable.dispose()
            }
            errorBtn.setOnClickListener {
                viewModel?.startRequestWithError()?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe { result ->
                        when (result) {
                            is SearchResult.Error -> Toast.makeText(
                                context,
                                result.throwable?.localizedMessage,
                                Toast.LENGTH_LONG
                            ).show()
                            is SearchResult.Success -> Toast.makeText(
                                context,
                                result.name,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }

    private fun onButtonClicked() {
        binding?.progressBar?.visibility = View.VISIBLE
        mergeTwoCryptos("ethereum", "bitcoin")
        getCryptosInfo()
    }

    private fun getCryptosInfo() {
        viewModel?.getCryptoInfo()?.subscribe({ setCryptosInfo(it) }, { errorHandle(it) })
    }

    private fun mergeTwoCryptos(firstCrypto: String, secondCrypto: String) {
        viewModel?.mergeTwoSearchResponses(firstCrypto, secondCrypto)?.subscribe({
            Log.d("ADV_MERGE", it.coins.toString())
        }, {
            errorHandle(it)
            Log.e("ADV_MERGE", it.localizedMessage ?: it.toString())
        })
    }

    private fun setCryptosInfo(listInfo: List<SearchResponse>) = with(binding) {
        adapter = CustomAdapter(listInfo)
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
