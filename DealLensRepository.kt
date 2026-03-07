package com.dealz.data.repository

import com.dealz.data.model.BestOfferResponse
import com.dealz.data.model.ProductItem
import com.dealz.data.model.ScanMatchItem
import com.dealz.data.model.ScanMatchRequest
import com.dealz.data.model.ScannedItem
import com.dealz.data.network.ApiClient

class DealZRepository {
    private val demoProducts = listOf(
        ProductItem(
            id = "demo-nutella",
            gtin = "3017620429484",
            brand = "Nutella",
            name = "Nutella 750g",
            normalized_name = "nutella 750g",
            size_value = 750.0,
            size_unit = "g",
            source = "offline-demo"
        ),
        ProductItem(
            id = "demo-milka",
            brand = "Milka",
            name = "Milka Alpenmilch 100g",
            normalized_name = "milka alpenmilch 100g",
            size_value = 100.0,
            size_unit = "g",
            source = "offline-demo"
        ),
        ProductItem(
            id = "demo-cola",
            brand = "Coca-Cola",
            name = "Coca-Cola 1.5L",
            normalized_name = "coca cola 1 5l",
            size_value = 1.5,
            size_unit = "l",
            source = "offline-demo"
        )
    )

    private val demoOffers = mapOf(
        "demo-nutella" to BestOfferResponse(
            product_id = "demo-nutella",
            product_name = "Nutella 750g",
            retailer = "Amazon",
            title = "Nutella 750g",
            currency = "EUR",
            price = 4.29,
            shipping_price = 0.0,
            total_price = 4.29,
            product_url = null
        ),
        "demo-milka" to BestOfferResponse(
            product_id = "demo-milka",
            product_name = "Milka Alpenmilch 100g",
            retailer = "Idealo",
            title = "Milka Alpenmilch 100g",
            currency = "EUR",
            price = 0.89,
            shipping_price = 0.0,
            total_price = 0.89,
            product_url = null
        ),
        "demo-cola" to BestOfferResponse(
            product_id = "demo-cola",
            product_name = "Coca-Cola 1.5L",
            retailer = "Rewe",
            title = "Coca-Cola 1.5L",
            currency = "EUR",
            price = 1.79,
            shipping_price = 0.0,
            total_price = 1.79,
            product_url = null
        )
    )

    private val demoScanData = listOf(
        ScannedItem(detected_name = "Nutella 750g", detected_price = 5.49, size_hint = "750g"),
        ScannedItem(detected_name = "Milka Alpenmilch 100g", detected_price = 0.99, size_hint = "100g"),
        ScannedItem(detected_name = "Coca Cola 1.5L", detected_price = 1.79, size_hint = "1.5L")
    )

    suspend fun searchProducts(query: String): List<ProductItem> {
        return runCatching { ApiClient.api.searchProducts(query) }
            .getOrElse {
                val q = query.trim().lowercase()
                demoProducts.filter {
                    listOfNotNull(it.name, it.brand, it.gtin, it.normalized_name)
                        .any { value -> value.lowercase().contains(q) }
                }
            }
    }

    suspend fun getBestOffer(productId: String): BestOfferResponse {
        return runCatching { ApiClient.api.getBestOffer(productId) }
            .getOrElse {
                demoOffers[productId]
                    ?: error("Kein Angebot für dieses Produkt gefunden.")
            }
    }

    suspend fun submitDemoScan(): List<ScanMatchItem> {
        val request = ScanMatchRequest(
            raw_ocr_text = "Nutella 750g 5,49€\nMilka Alpenmilch 100g 0,99€\nCoca Cola 1.5L 1,79€",
            detections = demoScanData
        )

        return runCatching { ApiClient.api.matchScan(request) }
            .getOrElse {
                demoScanData.map { item ->
                    val match = demoProducts.firstOrNull { product ->
                        item.detected_name.lowercase().contains(product.name.substringBefore(" ").lowercase()) ||
                            product.name.lowercase().contains(item.detected_name.substringBefore(" ").lowercase())
                    }
                    val offer = match?.let { demoOffers[it.id] }
                    ScanMatchItem(
                        detected_name = item.detected_name,
                        detected_price = item.detected_price,
                        matched_product_id = match?.id,
                        matched_product_name = match?.name,
                        best_offer = offer,
                        match_confidence = if (match != null) 0.91 else 0.0
                    )
                }
            }
    }
}
