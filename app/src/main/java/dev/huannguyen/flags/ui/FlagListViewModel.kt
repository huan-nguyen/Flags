package dev.huannguyen.flags.ui

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import dev.huannguyen.flags.R
import dev.huannguyen.flags.data.DataResponse
import dev.huannguyen.flags.data.FlagRepo
import dev.huannguyen.flags.data.source.local.LocalFlagData
import dev.huannguyen.flags.di.ServiceLocator
import dev.huannguyen.flags.domain.Flag
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart

class FlagListViewModel(private val repo: FlagRepo) : ViewModel() {
    private val fetchEvents = BroadcastChannel<Unit>(1)

    val flags: LiveData<UiState> = createLiveData()

    fun fetch() {
        fetchEvents.offer(Unit)
    }

    private fun createLiveData(): LiveData<UiState> {
        val flagsData = repo.flags().map { it.toUiState() }
        val fetchResults = fetchEvents.asFlow()
            .flatMapConcat { repo.fetch() }
            .map { it.toUiState() }

        return merge(flagsData, fetchResults)
            .distinctUntilChanged()
            .onStart { emit(UiState.InProgress) }
            .asLiveData()
    }
}

private fun DataResponse<List<LocalFlagData>>.toUiState() = when (this) {
    is DataResponse.Success -> UiState.Success(data = data.map { it.toFlagModel() })
    is DataResponse.Failure -> UiState.Failure(message = R.string.flag_list_error_message)
    is DataResponse.Fetching -> UiState.InProgress
}

private fun List<LocalFlagData>.toUiState(): UiState = UiState.Success(data = map { it.toFlagModel() })

/**
 * Trivial example of data mapping here. However in reality this mapping would perhaps
 * be more useful when some certain format of data needs to be generated to display to users.
 * For example convert a date in .NET format to a nice understandable String.
 *
 * This place would be a great place for such data mapping/transformation.
 */
private fun LocalFlagData.toFlagModel() = Flag(
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
        return FlagListViewModel(ServiceLocator.flagRepo) as T
    }
}

sealed class UiState {
    data class Success(val data: List<Flag>) : UiState()
    data class Failure(@StringRes val message: Int) : UiState()
    object InProgress : UiState()
}
