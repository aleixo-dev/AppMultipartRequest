package br.com.nicolas.appmultipartrequest.data.repository

import br.com.nicolas.appmultipartrequest.data.response.Turism
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface TurismRepository {

    suspend fun createTurism(image: MultipartBody.Part): Flow<Turism>

}