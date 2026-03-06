package com.deallens.data.network

import com.deallens.data.model.BestOfferResponse
import com.deallens.data.model.ProductItem
import com.deallens.data.model.ScanMatchItem
import com.deallens.data.model.ScanMatchRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DealLensApi {
    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") q: String,
        @Query("limit") limit: Int = 20
    ): List<ProductItem>

    @GET("products/{productId}/best-offer")
    suspend fun getBestOffer(
        @Path("productId") productId: String
    ): BestOfferResponse

    @POST("scans/match")
    suspend fun matchScan(
        @Body request: ScanMatchRequest
    ): List<ScanMatchItem>
}
