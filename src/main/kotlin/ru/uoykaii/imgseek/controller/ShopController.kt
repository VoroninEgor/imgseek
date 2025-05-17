package ru.uoykaii.imgseek.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.uoykaii.imgseek.entity.Shop
import ru.uoykaii.imgseek.repository.ShopRepository
import java.util.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/shop")
class ShopController(
    private val shopRepo: ShopRepository
) {

    @PostMapping
    fun create(
        @RequestBody request: CreateShopRequest
    ) {
        Shop(UUID.randomUUID(), request.name, true, request.callback)
            .run { shopRepo.save(this) }
    }

    @PutMapping("/{id}")
    fun put(
        @PathVariable("id") id: Long,
        @RequestBody request: CreateShopRequest
    ) {
        shopRepo.findById(id).getOrNull()?.let { shop ->
            shop.name = request.name
            shop.callback = request.callback
            shopRepo.save(shop)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable("id") id: Long
    ) {
        shopRepo.deleteById(id)
    }

    @GetMapping
    fun list(): ShopListResponse {
        val shops = shopRepo.findAll().map { ShopItem(it.id!!, it.uid, it.name, it.locked) }
        return ShopListResponse(shops)
    }

    @GetMapping("/{id}")
    fun get(
        @PathVariable("id") id: Long
    ): ShopItem? {
        return shopRepo.findById(id).getOrNull()?.let { ShopItem(it.id!!, it.uid, it.name, it.locked) }
    }

    @PutMapping("/images/source")
    fun configureImagesSource(
        @RequestBody request: ConfigureImagesSourceRequest
    ) {
        shopRepo.findById(request.shopId).getOrNull()?.let { shop ->
            shop.s3Url = request.s3Url
            shop.s3Key = request.s3Key
            shop.s3SecretKey = request.s3SecretKey
            shop.s3Bucket = request.s3Bucket
            shop.locked = false
            shopRepo.save(shop)
        }
    }

    @GetMapping("/{id}/images/source")
    fun imagesSource(
        @PathVariable("id") id: Long,
    ): ImageSourceResponse? {
        val shop = shopRepo.findById(id).getOrNull()

        return if (shop != null && !shop.locked) {
            ImageSourceResponse(
                url = shop.s3Url,
                key = shop.s3Key,
                secretKey = shop.s3SecretKey,
                bucketName = shop.s3Bucket,
                callback = shop.callback
            )
        } else {
            null
        }
    }

    data class ImageSourceResponse(
        val url: String?,
        val key: String?,
        val secretKey: String?,
        val bucketName: String?,
        val callback: String?
    )

    data class CreateShopRequest(
        val name: String,
        val callback: String
    )

    data class ShopListResponse(
        val list: List<ShopItem>
    )

    data class ShopItem(
        val id: Long,
        val uid: UUID,
        val name: String,
        val locked: Boolean
    )

    data class ConfigureImagesSourceRequest(
        val shopId: Long,
        val s3Url: String,
        val s3Key: String,
        val s3SecretKey: String,
        val s3Bucket: String
    )
}