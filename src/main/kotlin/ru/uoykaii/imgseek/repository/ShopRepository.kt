package ru.uoykaii.imgseek.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.uoykaii.imgseek.entity.Shop

@Repository
interface ShopRepository : CrudRepository<Shop, Long> {
}