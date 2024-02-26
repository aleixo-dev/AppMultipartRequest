package br.com.nicolas.appmultipartrequest.data.service

import br.com.nicolas.appmultipartrequest.data.response.Turism
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface TurismService {

    @Multipart
    @POST("turism")
    suspend fun createTurism(
        @Part image: MultipartBody.Part,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>
    ): Turism

}