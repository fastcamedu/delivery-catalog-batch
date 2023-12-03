package com.fastcampus.deliverycatalogbatch.domain

enum class MenuStatus(
    val description: String,
) {
    READY("준비중"),
    SALE("판매중"),
    SOLD_OUT("품절"),
    HOLD("일시 판매중지"),
}