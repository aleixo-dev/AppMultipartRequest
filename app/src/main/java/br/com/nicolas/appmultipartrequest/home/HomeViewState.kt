package br.com.nicolas.appmultipartrequest.home

import br.com.nicolas.appmultipartrequest.data.response.Turism

sealed class HomeViewState {

    data object Loading : HomeViewState()
    data class Success(val turism: Turism) : HomeViewState()
    data object Error : HomeViewState()

}