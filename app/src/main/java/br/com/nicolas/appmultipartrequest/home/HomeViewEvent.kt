package br.com.nicolas.appmultipartrequest.home

import java.io.File

sealed class HomeViewEvent {

    data class UploadImage(val file: File) : HomeViewEvent()
}