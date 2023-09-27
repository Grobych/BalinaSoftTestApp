package com.globa.balinasofttestapp.network.internal

import com.globa.balinasofttestapp.network.api.model.ErrorResource
import com.globa.balinasofttestapp.network.api.model.NetworkResponse
import com.google.gson.Gson
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

internal class NetworkResultCall<T : Any>(
    private val proxy: Call<T>
) : Call<NetworkResponse<T>> {
    override fun enqueue(callback: Callback<NetworkResponse<T>>) {
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val networkResult = handleApi { response }
                callback.onResponse(this@NetworkResultCall, Response.success(networkResult))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val networkResult = NetworkResponse.Exception<T>(t)
                callback.onResponse(this@NetworkResultCall, Response.success(networkResult))
            }
        })
    }

    override fun execute(): Response<NetworkResponse<T>> = throw NotImplementedError()
    override fun clone(): Call<NetworkResponse<T>> = NetworkResultCall(proxy.clone())
    override fun request(): Request = proxy.request()
    override fun timeout(): Timeout = proxy.timeout()
    override fun isExecuted(): Boolean = proxy.isExecuted
    override fun isCanceled(): Boolean = proxy.isCanceled
    override fun cancel() { proxy.cancel() }
}

fun <T : Any> handleApi(
    execute:  () -> Response<T>
): NetworkResponse<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkResponse.Success(body)
        } else {
            val message = Gson().fromJson(response.errorBody()!!.string(), ErrorResource::class.java)
            NetworkResponse.Error(code = response.code(), message = message.error)
        }
    } catch (e: HttpException) {
        NetworkResponse.Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        NetworkResponse.Exception(e)
    }
}