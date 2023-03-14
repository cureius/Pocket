package com.cureius.pocket.feature_pot.domain.util

import com.cureius.pocket.R

object IconDictionary{
    private val save = R.drawable.save
    private val wallet = R.drawable.wallet
    private val emi = R.drawable.coins
    private val invest = R.drawable.shop
    private val add = R.drawable.add

    private val food = R.drawable.food
    private val entertainment = R.drawable.food
    private val travel = R.drawable.travel
    private val house = R.drawable.home
    private val fuel = R.drawable.fuel
    private val health = R.drawable.health
    private val shopping = R.drawable.shopping
    private val grocery = R.drawable.shop

    val allIcons: Map<String, Int> = mapOf(
        "save" to save,
        "wallet" to wallet,
        "emi" to emi,
        "invest" to invest,
        "add" to add,
        "food" to food,
        "entertainment" to entertainment,
        "travel" to travel,
        "house" to house,
        "fuel" to fuel,
        "health" to health,
        "shopping" to shopping,
        "grocery" to grocery,
    )
}
