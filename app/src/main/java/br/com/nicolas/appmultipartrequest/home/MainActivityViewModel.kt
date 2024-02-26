package br.com.nicolas.appmultipartrequest.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.nicolas.appmultipartrequest.data.repository.TurismRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MainActivityViewModel(
    private val turismRepository: TurismRepository
) : ViewModel() {

    private val _state = MutableLiveData<HomeViewState>()
    val state: LiveData<HomeViewState> get() = _state

    fun interact(event: HomeViewEvent) {
        when (event) {
            is HomeViewEvent.UploadImage -> {
                uploadImage(event.file)
            }
        }
    }

    private fun uploadImage(file: File) = viewModelScope.launch {

        turismRepository.createTurism(createFormDataToMultipart(file))
            .onStart {
                setState(HomeViewState.Loading)
            }
            .catch {
                setState(HomeViewState.Error)
            }
            .collect { turismResponse ->
                setState(HomeViewState.Success(turismResponse))
            }
    }

    private fun createFormDataToMultipart(file: File) =
        MultipartBody.Part.createFormData("image", file.name, file.asRequestBody())

    private fun setState(newState: HomeViewState) {
        _state.value = newState
    }
}