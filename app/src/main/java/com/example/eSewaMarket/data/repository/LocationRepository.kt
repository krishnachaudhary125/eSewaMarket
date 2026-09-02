package com.example.eSewaMarket.data.repository

import com.example.eSewaMarket.data.api.ApiService
import com.example.eSewaMarket.data.models.DistrictResponse
import com.example.eSewaMarket.data.models.ProvinceResponse

class LocationRepository(
    private val apiService: ApiService
) {

    suspend fun getProvinces(): Result<List<ProvinceResponse>> {
        return try {
            Result.success(apiService.getProvinces())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDistricts(
        provinceId: Long
    ): Result<List<DistrictResponse>> {
        return try {
            Result.success(apiService.getDistricts(provinceId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}