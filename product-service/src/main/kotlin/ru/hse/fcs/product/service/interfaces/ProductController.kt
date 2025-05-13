package ru.hse.fcs.product.service.interfaces

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.hse.fcs.product.service.application.ProductService
import ru.hse.fcs.product.service.interfaces.dto.*
import ru.hse.fcs.product.service.interfaces.dto.converter.ProductDtoToProductConverter
import ru.hse.fcs.product.service.interfaces.dto.converter.ProductToProductDtoConverter
import java.util.UUID

@RestController
@RequestMapping("/api/product")
class ProductController(
    private val productService: ProductService,
    private val toProductDtoConverter: ProductToProductDtoConverter,
    private val fromProductDtoConverter: ProductDtoToProductConverter
) {

    @PostMapping("/products")
    fun getProducts(
        @RequestBody productIds: List<UUID>
    ): ResponseEntity<List<ProductDto>> {
        val products = productService.getProducts(productIds = productIds)
            .map { toProductDtoConverter.convert(it) }
        return ResponseEntity.ok(products)
    }

    @GetMapping("/cart")
    fun getCart(
        @RequestParam userId: UUID
    ): ResponseEntity<UserCart> {
        val userCart = productService.getCartProducts(userId)
        val result = userCart.map { (cartItem, product) ->
            UserCart.CartProduct(
                id = cartItem.id!!,
                userId = userId,
                product = toProductDtoConverter.convert(product),
                count = cartItem.count,
                createdAt = cartItem.createdAt!!
            )
        }
        return ResponseEntity.ok(
            UserCart(
                userId = userId,
                products = result
            )
        )
    }

    @PostMapping("/cart/add")
    fun addProductToCart(
        @RequestBody body: AddProductToCartRequest
    ): ResponseEntity<UserCart.CartProduct> {
        if (body.productId == null && body.product == null) {
            return ResponseEntity.badRequest().build()
        }

        body.productId?.let {
            val cartProduct = productService.addProductToCart(
                productId = body.productId!!,
                userId = body.userId,
                count = body.count
            )
            val product = productService.getProduct(body.productId!!)

            return ResponseEntity.ok(
                UserCart.CartProduct(
                    id = cartProduct.id!!,
                    userId = cartProduct.userId,
                    count = cartProduct.count,
                    createdAt = cartProduct.createdAt!!,
                    product = toProductDtoConverter.convert(product)
                )
            )
        }

        val (cart, product) = productService.addProductToCart(
            product = fromProductDtoConverter.convert(body.product!!),
            userId = body.userId,
            count = body.count
        )
        return ResponseEntity.ok(
            UserCart.CartProduct(
                id = cart.id!!,
                userId = cart.userId,
                count = cart.count,
                createdAt = cart.createdAt!!,
                product = toProductDtoConverter.convert(product)
            )
        )
    }

    @PostMapping("/cart/add/list")
    fun addProductsToCart(
        @RequestBody body: AddProductsToCartRequest
    ): ResponseEntity<List<UserCart.CartProduct>> {
        val userCarts = mutableListOf<UserCart.CartProduct>();

        body.products.forEach {
            val (cart, product) = productService.addProductToCart(
                product = fromProductDtoConverter.convert(it.product),
                userId = body.userId,
                count = it.count
            )

            userCarts.add(
                UserCart.CartProduct(
                    id = cart.id!!,
                    userId = cart.userId,
                    count = cart.count,
                    createdAt = cart.createdAt!!,
                    product = toProductDtoConverter.convert(product)
                )
            )
        }
        return ResponseEntity.ok(userCarts)
    }

    @PostMapping("/cart/edit-count")
    fun editCartProductCount(
        @RequestBody body: EditCartProductCountRequest
    ): ResponseEntity<UserCart.CartProduct> {
        val cart = productService.updateCartProductCount(
            cartId = body.cartProductId,
            count = body.count
        )
        return ResponseEntity.ok(
            UserCart.CartProduct(
                id = cart.id!!,
                userId = cart.userId,
                count = cart.count,
                createdAt = cart.createdAt!!
            )
        )
    }

    @PostMapping("/cart/remove")
    fun removeCartProduct(
        @RequestBody body: DeleteRequest
    ): ResponseEntity<UUID> {
        productService.deleteCartProduct(body.id)
        return ResponseEntity.ok(body.id)
    }

    @GetMapping("/favorites")
    fun getFavorites(
        @RequestParam userId: UUID
    ): ResponseEntity<UserFavorites> {
        val userFavorites = productService.getFavoriteProducts(userId)
        val result = userFavorites.map { (like, product) ->
            UserFavorites.FavoriteProduct(
                id = like.id!!,
                product = toProductDtoConverter.convert(product),
                createdAt = like.createdAt!!
            )
        }
        return ResponseEntity.ok(
            UserFavorites(
                userId = userId,
                products = result
            )
        )
    }

    @PostMapping("/favorites/add")
    fun addProductToFavorites(
        @RequestBody body: AddProductToFavoritesRequest
    ): ResponseEntity<UserFavorites.FavoriteProduct> {
        if (body.productId == null && body.product == null) {
            return ResponseEntity.badRequest().build()
        }

        body.productId?.let {
            val like = productService.addProductToFavorites(
                productId = body.productId!!,
                userId = body.userId
            )
            val product = productService.getProduct(body.productId!!)

            return ResponseEntity.ok(
                UserFavorites.FavoriteProduct(
                    id = like.id!!,
                    userId = like.userId,
                    createdAt = like.createdAt!!,
                    product = toProductDtoConverter.convert(product)
                )
            )
        }

        val (like, product) = productService.addProductToFavorites(
            product = fromProductDtoConverter.convert(body.product!!),
            userId = body.userId
        )
        return ResponseEntity.ok(
            UserFavorites.FavoriteProduct(
                id = like.id!!,
                userId = like.userId,
                createdAt = like.createdAt!!,
                product = toProductDtoConverter.convert(product)
            )
        )
    }

    @PostMapping("/favorites/remove")
    fun removeProductFromFavorites(
        @RequestBody body: DeleteRequest
    ): ResponseEntity<UUID> {
        productService.deleteProductFromFavorites(body.id)
        return ResponseEntity.ok(body.id)
    }
}