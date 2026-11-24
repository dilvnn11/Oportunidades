package cl.duoc.oportunidades.datos.remote.api

import cl.duoc.oportunidades.datos.remote.dto.MindicadorResponse
import retrofit2.Response
import retrofit2.http.GET

interface ExternalApiService {
    @GET("api")
    suspend fun obtenerIndicadores(): Response<MindicadorResponse>
}
