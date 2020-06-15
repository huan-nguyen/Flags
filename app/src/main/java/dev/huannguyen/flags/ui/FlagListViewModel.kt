package dev.huannguyen.flags.ui

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.huannguyen.flags.R
import dev.huannguyen.flags.data.DataResponse
import dev.huannguyen.flags.data.FlagDataModel
import dev.huannguyen.flags.data.FlagRepo
import dev.huannguyen.flags.data.FlagRepoImpl
import dev.huannguyen.flags.di.NetworkComponent
import dev.huannguyen.flags.domain.Flag
import kotlinx.coroutines.launch

class FlagListViewModel(private val repo: FlagRepo) : ViewModel() {

    private val flagData: MutableLiveData<UiState> = MutableLiveData()
    val flags: LiveData<UiState> = flagData

    fun getFlags() = viewModelScope.launch {
        // Start with in progress state before any result comes back
        flagData.value = UiState.InProgress

        when (val response = repo.getFlags()) {
            is DataResponse.Success -> {
                flagData.value = UiState.Success(data = response.data.map { it.toFlagModel() })
            }

            is DataResponse.Failure -> {
                flagData.value = UiState.Failure(message = R.string.flag_list_error_message)
            }
        }
    }
}

/**
 * Trivial example of data mapping here. However in reality this mapping would perhaps
 * be more useful when some certain format of data needs to be generated to display to users.
 * For example convert a date in .NET format to a nice understandable String.
 *
 * This place would be a great place for such data mapping/transformation.
 */
private fun FlagDataModel.toFlagModel() = Flag(
    country = country,
    capital = capital,
    population = population,
    url = url,
    language = language,
    currency = currency,
    timeZone = timeZone
)

@Suppress("UNCHECKED_CAST")
class FlagListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FlagListViewModel(FlagRepoImpl(NetworkComponent.webServices)) as T
    }
}

sealed class UiState {
    data class Success(val data: List<Flag>) : UiState()
    data class Failure(@StringRes val message: Int) : UiState()
    object InProgress : UiState()
}
