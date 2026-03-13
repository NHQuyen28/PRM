package com.example.prm.data.repository

import com.example.prm.data.remote.RetrofitClient
import com.example.prm.data.remote.api.AddressApi
import com.example.prm.data.remote.dto.AddressResponse
import com.example.prm.data.remote.dto.CreateAddressRequest
import com.example.prm.utils.ResultState

class AddressRepository {

    private val api = RetrofitClient.createService(AddressApi::class.java)

    suspend fun createAddress(request: CreateAddressRequest): ResultState<Unit> {

        return try {

            val response = api.createAddress(request)

            if (response.isSuccessful) {
                ResultState.Success(Unit)
            } else {
                ResultState.Error("Create address failed")
            }

        } catch (e: Exception) {

            ResultState.Error(e.message ?: "Unknown error")

        }

    }

    suspend fun getAddresses(): ResultState<List<AddressResponse>> {

        return try {

            val response = api.getAddresses()

            if (response.isSuccessful) {
                ResultState.Success(response.body()?.data ?: emptyList())
            } else {
                ResultState.Error("Cannot load address")
            }

        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }

    }

    suspend fun setDefaultAddress(id: String): ResultState<Unit> {

        return try {

            val response = api.setDefaultAddress(id)

            if (response.isSuccessful) {
                ResultState.Success(Unit)
            } else {
                ResultState.Error("Set default address failed")
            }

        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun deleteAddress(id: String): ResultState<Unit> {

        return try {

            val response = api.deleteAddress(id)

            if (response.isSuccessful) {
                ResultState.Success(Unit)
            } else {
                ResultState.Error("Delete address failed")
            }

        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun updateAddress(request: CreateAddressRequest): ResultState<Unit> {

        return try {

            val response = api.updateAddress(request)

            if (response.isSuccessful) {
                ResultState.Success(Unit)
            } else {
                ResultState.Error("Update address failed")
            }

        } catch (e: Exception) {

            ResultState.Error(e.message ?: "Unknown error")

        }
    }

}