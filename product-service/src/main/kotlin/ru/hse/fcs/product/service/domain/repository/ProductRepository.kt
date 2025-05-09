package ru.hse.fcs.product.service.domain.repository

import org.springframework.data.repository.CrudRepository
import ru.hse.fcs.product.service.domain.model.Product
import java.util.*

interface ProductRepository: CrudRepository<Product, UUID> {

    fun findBySkuId(skuId: String): Optional<Product>
}