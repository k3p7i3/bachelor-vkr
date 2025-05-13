package ru.hse.fcs.product.service.domain.repository

import org.springframework.data.repository.CrudRepository
import ru.hse.fcs.product.service.domain.model.FavoriteProduct
import java.util.UUID

interface FavoriteProductRepository : CrudRepository<FavoriteProduct, UUID> {

    fun findAllByUserId(userId: UUID): List<FavoriteProduct>
}