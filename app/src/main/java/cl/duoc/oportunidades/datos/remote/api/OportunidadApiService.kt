package cl.duoc.oportunidades.datos.remote.api

import cl.duoc.oportunidades.datos.remote.dto.OportunidadDto
import retrofit2.Response
import retrofit2.http.*

interface OportunidadApiService {

    @GET("api/oportunidades")
    suspend fun listarTodas(): Response<List<OportunidadDto>>

    @GET("api/oportunidades/{id}")
    suspend fun obtenerPorId(@Path("id") id: Long): Response<OportunidadDto>

    @POST("api/oportunidades")
    suspend fun crear(@Body oportunidad: OportunidadDto): Response<OportunidadDto>

    @PUT("api/oportunidades/{id}")
    suspend fun actualizar(@Path("id") id: Long, @Body oportunidad: OportunidadDto): Response<OportunidadDto>

    @DELETE("api/oportunidades/{id}")
    suspend fun eliminar(@Path("id") id: Long): Response<Void>
}
