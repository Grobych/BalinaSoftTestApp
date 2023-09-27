package com.globa.balinasofttestapp.network.internal

import com.globa.balinasofttestapp.network.api.model.NetworkResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

internal class NetworkResultCallAdapter(
    private val resultType: Type
) : CallAdapter<Type, Call<NetworkResponse<Type>>> {

    override fun responseType(): Type = resultType

    override fun adapt(call: Call<Type>): Call<NetworkResponse<Type>> {
        return NetworkResultCall(call)
    }
}