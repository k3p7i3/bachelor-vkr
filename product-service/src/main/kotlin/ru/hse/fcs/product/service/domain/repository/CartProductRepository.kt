package ru.hse.fcs.product.service.domain.repository

import org.springframework.data.repository.CrudRepository
import ru.hse.fcs.product.service.domain.model.CartProduct
import java.util.UUID

interface CartProductRepository : CrudRepository<CartProduct, UUID> {

    fun findAllByUserId(userId: UUID): List<CartProduct>
}