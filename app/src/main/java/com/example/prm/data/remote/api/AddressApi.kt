package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.AddressListResponse
import com.example.prm.data.remote.dto.CreateAddressRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AddressApi {

    @POST("Address")
    suspend fun createAddress(
        @Body request: CreateAddressRequest
    ): Response<Unit>

    @GET("Address")
    suspend fun getAddresses(): Response<AddressListResponse>

    @PATCH("Address/{id}/set-default")
    suspend fun setDefaultAddress(
        @Path("id") id: String
    ): Response<Unit>

    @DELETE("Address/{id}")
    suspend fun deleteAddress(
        @Path("id") id: String
    ): Response<Unit>

    @PUT("Address")
    suspend fun updateAddress(
        @Body request: CreateAddressRequest
    ): Response<Unit>
}