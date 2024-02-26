package br.com.nicolas.appmultipartrequest.data.repository

import br.com.nicolas.appmultipartrequest.data.response.Turism
import br.com.nicolas.appmultipartrequest.data.service.TurismService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class TurismRepositoryImpl(
    private val service: TurismService,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : TurismRepository {

    override suspend fun createTurism(image: MultipartBody.Part): Flow<Turism> =
        flow {

            val bodyParams = mutableMapOf<String, RequestBody>()
            bodyParams["title"] = "some title".toRequestBody("text".toMediaTypeOrNull())
            bodyParams["description"] =
                "some description".toRequestBody("text".toMediaTypeOrNull())
            emit(service.createTurism(image, bodyParams))
        }.flowOn(defaultDispatcher)
}