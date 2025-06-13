
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.gedoise.domain.usecase.ClearDataUseCase
import com.upsaclay.gedoise.domain.usecase.ListenDataUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val listenDataUseCase: ListenDataUseCase,
    private val clearDataUseCase:  ClearDataUseCase
): ViewModel() {
    fun startListening() {
        viewModelScope.launch {
            authenticationRepository.authenticated.collectLatest {
                if (it) {
                    listenDataUseCase.start()
                } else {
                    listenDataUseCase.stop()
                    delay(2000)
                    clearDataUseCase()
                }
            }
        }
    }
}