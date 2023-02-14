package com.example.education.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.education.SearchAdapter
import com.example.education.adapter.CryptoAdapter
import com.example.education.clicks
import com.example.education.data.Crypto
import com.example.education.data.SearchResponse
import com.example.education.databinding.FragmentFlowBinding
import com.example.education.ext.collectLatestFlow
import com.example.education.ext.collectLatestSharedFlow
import com.example.education.utils.Result
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FlowFragment : Fragment() {

    private var binding: FragmentFlowBinding? = null
    private val viewModel: FlowViewModel by viewModels()
    private var adapter: CryptoAdapter? = null
    private var adapterSearch: SearchAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //how to proceed screen rotation
        binding = FragmentFlowBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.startRequestForOneCrypto()
        subscribe()
        initView()
        setupUI()
    }

    private fun initView() = with(binding) {
        this?.let {
            stateBtn.clicks().onEach {
                viewModel.incrementFlow()
                viewModel.sharedFlow()
            }.launchIn(lifecycleScope)

            var counts = 0
            coldFlowBtn.clicks().onEach {
                val answer = ++counts * 2 + 15
                clicks.text = "($counts)"
                result.text = answer.toString()
                showToast(counts)
            }.launchIn(lifecycleScope)
        }
    }

    private fun setupUI() {
        adapter = CryptoAdapter(arrayListOf())
        binding?.rwList?.adapter = adapter
        adapterSearch = SearchAdapter(arrayListOf())
        binding?.cryptoInfo?.adapter = adapterSearch
    }

    private fun subscribe() {
        binding?.let { b ->
            collectLatestFlow(viewModel.stateFlow) {
                b.stateCount.text = it.toString()
            }
            collectLatestSharedFlow(viewModel.sharedFlow) {
                b.countDownShared.text = it.toString()
            }

            collectLatestFlow(viewModel.errorMessage) {
                if (it.isNotEmpty()) b.result.text = it
            }
            collectLatestFlow(viewModel.cryptos) {
                when (it) {
                    is Result.Loading<*> -> b.progressBar.visibility = View.VISIBLE
                    is Result.Error -> handleError(it.msg)
                    is Result.Success<*> -> {
                        renderList(it.data as List<Crypto>)
                        b.progressBar.visibility = View.GONE
                    }
                }
            }
            collectLatestFlow(viewModel.crypto) {
                when (it) {
                    is Result.Loading<*> -> b.progressBar.visibility = View.VISIBLE
                    is Result.Error ->  handleError(it.msg)
                    is Result.Success<*> -> {
                        renderCrypto(listOf(it.data as SearchResponse))
                        b.progressBar.visibility = View.GONE
                    }
                }

            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderList(users: List<Crypto>) {
        adapter?.update(users)
        adapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderCrypto(users: List<SearchResponse>) {
        if (users.none { (it.coins.isEmpty()) }) {
            adapterSearch?.update(users)
            adapterSearch?.notifyDataSetChanged()
        } else {
            binding?.error?.text = "Error, the list is empty. \nCheck the request"
        }
    }

    private fun showToast(counts: Int) {
        if (counts == 5) {
            Toast.makeText(
                requireContext(),
                "Heeeey! You have clicked $counts times. Maybe you should relax, man!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun handleError(error: String) {
        binding?.let { b ->
            b.error.text = error
            b.error.visibility = View.VISIBLE
            b.progressBar.visibility = View.GONE
        }
    }

}