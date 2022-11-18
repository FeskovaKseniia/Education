package com.example.education

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.education.data.search.SearchResponse
import com.example.education.databinding.FragmentListBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch
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
            viewModel?.loading?.doOnNext {
                if (it) {
                    binding?.progressBar?.visibility = View.VISIBLE
                } else {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
            viewModel?.error?.doOnNext {
                binding?.error?.text = it
                if (it.isNotEmpty()) {
                    binding?.error?.visibility = View.VISIBLE
                } else binding?.error?.visibility = View.GONE
            }
        }
    }

    private fun onButtonClicked() {
        getCryptosInfo()
    }

    private fun getCryptosInfo() {
        lifecycleScope.launch {
            try {
                binding?.progressBar?.visibility = View.VISIBLE
                val list = viewModel?.getCryptoInfo() ?: emptyList()
                setCryptosInfo(list)
            } catch (e: HttpException) {
                Log.d("COROUTINES", "getCryptos from fragment ${e.localizedMessage}")
            } finally {
                binding?.progressBar?.visibility = View.GONE
            }
        }
    }

    private fun setCryptosInfo(listInfo: List<SearchResponse>) = with(binding) {
        adapter = CustomAdapter(listInfo)
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
