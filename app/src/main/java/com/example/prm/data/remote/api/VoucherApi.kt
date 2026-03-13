package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.ApiResponse
import com.example.prm.data.remote.dto.CreateVoucherReq
import com.example.prm.data.remote.dto.UpdateVoucherReq
import com.example.prm.data.remote.dto.VoucherListResp
import com.example.prm.data.remote.dto.VoucherResp
import retrofit2.Response
import retrofit2.http.*

interface VoucherApi {
    @GET("Voucher/all")
    suspend fun getAllVouchers(
        @Query("Page") page: Int = 1,
        @Query("PageSize") pageSize: Int = 50
    ): Response<ApiResponse<VoucherListResp>>

    @GET("Voucher/{id}")
    suspend fun getVoucherById(
        @Path("id") id: String
    ): Response<ApiResponse<VoucherResp>>

    @POST("Voucher")
    suspend fun createVoucher(
        @Body request: CreateVoucherReq
    ): Response<ApiResponse<VoucherResp>>

    @PUT("Voucher")
    suspend fun updateVoucher(
        @Body request: UpdateVoucherReq
    ): Response<ApiResponse<VoucherResp>>

    @DELETE("Voucher/{id}")
    suspend fun deleteVoucher(
        @Path("id") id: String
    ): Response<ApiResponse<Boolean>>
}