package com.deallens.data.model

data class ProductItem(
    val id: String,
    val gtin: String? = null,
    val brand: String? = null,
    val name: String,
    val normalized_name: String? = null,
    val variant: String? = null,
    val size_value: Double? = null,
    val size_unit: String? = null,
    val image_url: String? = null,
    val source: String? = null
)

data class BestOfferResponse(
    val product_id: String,
    val product_name: String,
    val retailer: String,
    val title: String,
    val currency: String,
    val price: Double,
    val shipping_price: Double? = null,
    val total_price: Double? = null,
    val product_url: String? = null
)

data class ScanMatchRequest(
    val raw_ocr_text: String? = null,
    val detections: List<ScannedItem>
)

data class ScannedItem(
    val detected_name: String,
    val detected_price: Double? = null,
    val brand_hint: String? = null,
    val size_hint: String? = null,
    val gtin_hint: String? = null
)

data class ScanMatchItem(
    val detected_name: String,
    val detected_price: Double? = null,
    val matched_product_id: String? = null,
    val matched_product_name: String? = null,
    val best_offer: BestOfferResponse? = null,
    val match_confidence: Double = 0.0
)
