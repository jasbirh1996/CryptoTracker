package com.android.cryptotracker.presentation.coin_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cryptotracker.common.Resource
import com.android.cryptotracker.domain.use_case.get_coins.GetCoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(val getCoinUseCase: GetCoinUseCase ) : ViewModel(){
    private var _state = mutableStateOf(CoinListState())
    val state : State<CoinListState> = _state

    init {
        getCoins()
    }

   private fun getCoins(){
        getCoinUseCase().onEach { result ->
            when(result){
                is Resource.Success ->{
                    _state.value = CoinListState(coinList = result.data ?: emptyList())
                }
                is Resource.Loading ->{
                    _state.value = CoinListState(isLoading = true)
                }
                is Resource.Error ->{
                    _state.value = CoinListState(error = result.message ?: "An unexpected error occur.")
                }
            }
        }.launchIn(viewModelScope)
    }

}