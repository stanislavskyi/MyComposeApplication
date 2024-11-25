package com.hfad.mycomposeapplication.data.mapper

import com.hfad.mycomposeapplication.domain.entity.Friend

class Mapper {
    fun mapDomainToUi(domain: Friend) = com.hfad.mycomposeapplication.ui.screens.account.Friend(
        name = domain.name,
        subscription = domain.subscription
    )
}