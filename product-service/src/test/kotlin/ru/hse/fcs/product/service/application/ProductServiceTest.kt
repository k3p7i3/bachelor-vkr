package ru.hse.fcs.product.service.application

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ru.hse.fcs.product.service.domain.model.CartProduct
import ru.hse.fcs.product.service.domain.model.FavoriteProduct
import ru.hse.fcs.product.service.domain.model.Product
import ru.hse.fcs.product.service.domain.repository.CartProductRepository
import ru.hse.fcs.product.service.domain.repository.FavoriteProductRepository
import ru.hse.fcs.product.service.domain.repository.ProductRepository
import java.util.Optional
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {

    @Mock
    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var cartProductRepository: CartProductRepository

    @Mock
    private lateinit var favoriteProductRepository: FavoriteProductRepository

    @InjectMocks
    private lateinit var productService: ProductService

    private val userId = UUID.randomUUID()
    private val productId = UUID.randomUUID()
    private val cartId = UUID.randomUUID()
    private val favoriteId = UUID.randomUUID()
    private val testProduct = Product(
        id = productId,
        externalId = "externalId",
        skuId = "skuId",
        title = "Test Product",
        productUrl = "productUrl",
        imageUrl = "productImageUrl"
    )
    private val testCartProduct = CartProduct(
        id = cartId,
        productId = productId,
        userId = userId,
        count = 5
    )
    private val testFavoriteProduct = FavoriteProduct(
        id = favoriteId,
        productId = productId,
        userId = userId
    )

    @Test
    fun `getProduct should return product when exists`() {
        whenever(productRepository.findById(productId))
            .thenReturn(Optional.of(testProduct))
        val result = productService.getProduct(productId)
        assertEquals(testProduct, result)
    }

    @Test
    fun `getProduct should throw exception when product not found`() {
        whenever(productRepository.findById(productId))
            .thenReturn(Optional.empty())
        assertFailsWith<IllegalStateException> {
            productService.getProduct(productId)
        }
    }

    @Test
    fun `getProducts should return list of products`() {
        val productIds = listOf(productId)
        whenever(productRepository.findAllById(productIds))
            .thenReturn(listOf(testProduct))
        val result = productService.getProducts(productIds)
        assertEquals(1, result.size)
        assertEquals(testProduct, result[0])
    }

    @Test
    fun `getCartProducts should return cart products with products`() {
        whenever(cartProductRepository.findAllByUserId(userId))
            .thenReturn(listOf(testCartProduct))
        whenever(productRepository.findAllById(listOf(productId)))
            .thenReturn(listOf(testProduct))
        val result = productService.getCartProducts(userId)
        assertEquals(1, result.size)
        assertEquals(testCartProduct, result[0].first)
        assertEquals(testProduct, result[0].second)
    }

    @Test
    fun `addProductToCart with product should save both product and cart item`() {
        whenever(productRepository.save(testProduct))
            .thenReturn(testProduct)
        whenever(cartProductRepository.save(any<CartProduct>()))
            .thenReturn(testCartProduct)
        val result = productService.addProductToCart(testProduct, userId, 1)
        assertEquals(testCartProduct, result.first)
        assertEquals(testProduct, result.second)
    }

    @Test
    fun `addProductToCart with productId should save cart item`() {
        whenever(productRepository.existsById(productId))
            .thenReturn(true)
        whenever(cartProductRepository.save(any<CartProduct>()))
            .thenReturn(testCartProduct)
        val result = productService.addProductToCart(productId, userId, 1)
        assertEquals(testCartProduct, result)
    }

    @Test
    fun `addProductToCart with productId should throw when product not found`() {
        whenever(productRepository.existsById(productId))
            .thenReturn(false)
        assertFailsWith<IllegalArgumentException> {
            productService.addProductToCart(productId, userId, 1)
        }
    }

    @Test
    fun `updateCartProductCount should update count`() {
        val updatedCount = 2L
        val updatedCart = testCartProduct.copy(count = updatedCount)
        whenever(cartProductRepository.findById(cartId))
            .thenReturn(Optional.of(testCartProduct))
        whenever(cartProductRepository.save(any<CartProduct>()))
            .thenReturn(updatedCart)
        val result = productService.updateCartProductCount(cartId, updatedCount)
        assertEquals(updatedCount, result.count)
    }

    @Test
    fun `updateCartProductCount should throw when cart not found`() {
        whenever(cartProductRepository.findById(cartId)).thenReturn(Optional.empty())

        assertFailsWith<IllegalArgumentException> {
            productService.updateCartProductCount(cartId, 1)
        }
    }

    @Test
    fun `deleteCartProduct should delete cart item`() {
        productService.deleteCartProduct(cartId)
        verify(cartProductRepository).deleteById(cartId)
    }

    @Test
    fun `getFavoriteProducts should return favorites with products`() {
        whenever(favoriteProductRepository.findAllByUserId(userId))
            .thenReturn(listOf(testFavoriteProduct))
        whenever(productRepository.findAllById(listOf(productId)))
            .thenReturn(listOf(testProduct))

        val result = productService.getFavoriteProducts(userId)
        assertEquals(1, result.size)
        assertEquals(testFavoriteProduct, result[0].first)
        assertEquals(testProduct, result[0].second)
    }

    @Test
    fun `addProductToFavorites with product should save both product and favorite`() {
        whenever(productRepository.save(testProduct))
            .thenReturn(testProduct)
        whenever(favoriteProductRepository.save(any<FavoriteProduct>()))
            .thenReturn(testFavoriteProduct)

        val result = productService.addProductToFavorites(testProduct, userId)
        assertEquals(testFavoriteProduct, result.first)
        assertEquals(testProduct, result.second)
    }

    @Test
    fun `addProductToFavorites with productId should save favorite`() {
        whenever(productRepository.existsById(productId))
            .thenReturn(true)
        whenever(favoriteProductRepository.save(any<FavoriteProduct>()))
            .thenReturn(testFavoriteProduct)

        val result = productService.addProductToFavorites(productId, userId)
        assertEquals(testFavoriteProduct, result)
    }

    @Test
    fun `addProductToFavorites with productId should throw when product not found`() {
        whenever(productRepository.existsById(productId))
            .thenReturn(false)

        assertFailsWith<IllegalArgumentException> {
            productService.addProductToFavorites(productId, userId)
        }
    }

    @Test
    fun `deleteProductFromFavorites should delete favorite`() {
        productService.deleteProductFromFavorites(favoriteId)

        verify(favoriteProductRepository).deleteById(favoriteId)
    }

    @Test
    fun `updateOrSaveProduct should save custom product`() {
        val customProduct = testProduct.copy(isCustom = true)
        whenever(productRepository.save(customProduct))
            .thenReturn(customProduct)

        val result = productService.updateOrSaveProduct(customProduct)
        assertEquals(customProduct, result)
    }

    @Test
    fun `updateOrSaveImportedProduct should update existing product`() {
        val product = testProduct.copy(id = null, title = "New title")
        val skuId = product.skuId!!
        whenever(productRepository.findBySkuId(skuId))
            .thenReturn(Optional.of(testProduct))
        whenever(productRepository.save(any<Product>()))
            .thenReturn(product)

        val result = productService.updateOrSaveImportedProduct(product)
        assertEquals(product, result)
    }

    @Test
    fun `updateOrSaveImportedProduct should throw when skuId is null`() {
        val productWithoutSku = testProduct.copy(id = null, skuId = null)

        assertFailsWith<IllegalArgumentException> {
            productService.updateOrSaveImportedProduct(productWithoutSku)
        }
    }
}