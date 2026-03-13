package com.example.prm.data.repository

import com.example.prm.data.remote.RetrofitClient
import com.example.prm.data.remote.api.OrderApi
import com.example.prm.data.remote.dto.CreateOrderRequest
import com.example.prm.data.remote.dto.OrderResponse
import com.example.prm.utils.ResultState
import com.example.prm.data.remote.dto.OrderListResponse
class OrderRepository {

    private val api = RetrofitClient.createService(OrderApi::class.java)

    suspend fun createOrder(request: CreateOrderRequest): ResultState<Unit> {

        return try {

            val response = api.createOrder(request)

            if (response.isSuccessful) {
                ResultState.Success(Unit)
            } else {
                ResultState.Error("Create order failed")
            }

        } catch (e: Exception) {

            ResultState.Error(e.message ?: "Unknown error")

        }

    }

    suspend fun getOrders(): ResultState<List<OrderResponse>> {

        return try {

            val response = api.getOrders(1,10)

            println("ORDER RESPONSE CODE: ${response.code()}")
            println("ORDER RESPONSE BODY: ${response.body()}")

            if (response.isSuccessful) {

                val orders = response.body()?.data?.orders ?: emptyList()

                println("ORDER LIST SIZE: ${orders.size}")

                ResultState.Success(orders)

            } else {

                println("ORDER ERROR: ${response.errorBody()?.string()}")

                ResultState.Error("Failed to load orders")

            }

        } catch (e: Exception) {

            e.printStackTrace()

            ResultState.Error(e.message ?: "Failed to load orders")

        }
    }

}