package ru.hse.fcs.product.service.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.hse.fcs.product.service.domain.model.CartProduct
import ru.hse.fcs.product.service.domain.model.FavoriteProduct
import ru.hse.fcs.product.service.domain.model.Product
import ru.hse.fcs.product.service.domain.repository.CartProductRepository
import ru.hse.fcs.product.service.domain.repository.FavoriteProductRepository
import ru.hse.fcs.product.service.domain.repository.ProductRepository
import java.util.*

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val cartProductRepository: CartProductRepository,
    private val favoriteProductRepository: FavoriteProductRepository
) {

    fun getProduct(productId: UUID): Product {
        val product = productRepository.findById(productId)
        if (product.isEmpty) {
            throw IllegalStateException("Product with id=$productId does not exist")
        }
        return product.get()
    }

    fun getProducts(productIds: List<UUID>): List<Product> {
        val products = productRepository.findAllById(productIds).toList()
        return products
    }

    fun getCartProducts(userId: UUID): List<Pair<CartProduct, Product>> {
        val cartProducts = cartProductRepository
            .findAllByUserId(userId)
            .sortedByDescending { it.createdAt }
        val products = productRepository.findAllById(
            cartProducts.map { it.productId }
        )
        val result = cartProducts.map { cart ->
            cart to products.first { it.id == cart.productId }
        }
        return result
    }

    @Transactional
    fun addProductToCart(
        product: Product,
        userId: UUID,
        count: Long
    ): Pair<CartProduct, Product> {
        val savedProduct = updateOrSaveProduct(product)
        val cartObject = cartProductRepository.save(
            CartProduct(
                productId = product.id!!,
                userId = userId,
                count = count
            )
        )
        return cartObject to savedProduct
    }

    @Transactional
    fun addProductToCart(
        productId: UUID,
        userId: UUID,
        count: Long
    ): CartProduct {
        if (!productRepository.existsById(productId)) {
            throw IllegalArgumentException("Product with id=$productId does not exist")
        }

        val cartObject = cartProductRepository.save(
            CartProduct(
                productId = productId,
                userId = userId,
                count = count
            )
        )
        return cartObject
    }

    fun updateCartProductCount(
        cartId: UUID,
        count: Long
    ): CartProduct {
        val cartProduct = cartProductRepository.findById(cartId)

        if (cartProduct.isPresent) {
            return cartProductRepository.save(
                cartProduct.get().apply {
                    this.count = count
                }
            )
        } else {
            throw IllegalArgumentException("Cart Product with id=$cartId does not exist")
        }
    }

    @Transactional
    fun deleteCartProduct(cartId: UUID) {
        // TODO delete product too
        cartProductRepository.deleteById(cartId)
    }

    fun getFavoriteProducts(userId: UUID): List<Pair<FavoriteProduct, Product>> {
        val likes = favoriteProductRepository
            .findAllByUserId(userId)
            .sortedByDescending { it.createdAt }
        val products = productRepository.findAllById(
            likes.map { it.productId }
        )
        val result = likes.map { like ->
            like to products.first { it.id == like.productId }
        }
        return result
    }


    @Transactional
    fun addProductToFavorites(
        product: Product,
        userId: UUID
    ) : Pair<FavoriteProduct, Product> {
        val savedProduct = updateOrSaveProduct(product)
        val like = favoriteProductRepository.save(
            FavoriteProduct(
                productId = product.id!!,
                userId = userId
            )
        )
        return like to savedProduct
    }

    @Transactional
    fun addProductToFavorites(
        productId: UUID,
        userId: UUID,
    ): FavoriteProduct {
        if (!productRepository.existsById(productId)) {
            throw IllegalArgumentException("Product with id=$productId does not exist")
        }
        val like = favoriteProductRepository.save(
            FavoriteProduct(
                productId = productId,
                userId = userId
            )
        )
        return like
    }

    fun deleteProductFromFavorites(favoriteId: UUID) {
        // TODO delete product too
        favoriteProductRepository.deleteById(favoriteId)
    }

    fun updateOrSaveProduct(product: Product): Product =
        when (product.isCustom) {
            true -> productRepository.save(product)
            else -> updateOrSaveImportedProduct(product)
        }

    fun updateOrSaveImportedProduct(product: Product): Product {
        product.id?.let {
            return productRepository.save(product)
        }

        if (product.skuId == null) {
            throw IllegalArgumentException(
                "skuId field must not be null for product exported via extension"
            )
        }

        val foundProduct = productRepository.findBySkuId(product.skuId!!)
        return when (foundProduct.isPresent) {
            true ->
                productRepository.save(
                    product.apply {
                        setId(foundProduct.get().id!!)
                    }
                )

            else -> productRepository.save(product)
        }
    }
}