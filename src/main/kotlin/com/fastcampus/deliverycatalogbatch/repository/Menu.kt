package com.fastcampus.deliverycatalogbatch.repository

import com.fastcampus.deliverycatalogbatch.domain.MenuStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
@Table(name = "menus", catalog = "food_delivery")
class Menu(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menuId", nullable = false)
    var menuId: Long = 0,

    @Column(name = "storeId", nullable = false)
    val storeId: Long,

    @Column(name = "menuName", nullable = false)
    var menuName: String,

    @Column(name = "menuMainImage", nullable = false)
    var menuMainImageUrl: String,

    @Column(name = "price", nullable = false)
    var price: BigDecimal,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    val menuStatus: MenuStatus = MenuStatus.READY,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "isDeleted", nullable = false)
    val isDeleted: Boolean = false,

    @Column(name = "createdAt", nullable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "updatedAt", nullable = false)
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "createdBy", nullable = false)
    var createdBy: String,

    @Column(name = "updatedBy", nullable = false)
    var updatedBy: String,
)