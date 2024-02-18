package com.cureius.pocket.feature_pot.domain.util

import android.annotation.SuppressLint
import com.cureius.pocket.R
@SuppressLint("NonConstantResourceId")

object IconDictionary{
    private const val save = R.drawable.save
    private const val wallet = R.drawable.wallet
    private const val emi = R.drawable.coins
    private const val invest = R.drawable.shop
    private const val add = R.drawable.add

    private const val food = R.drawable.food
    private const val entertainment = R.drawable.food
    private const val travel = R.drawable.travel
    private const val house = R.drawable.home
    private const val fuel = R.drawable.fuel
    private const val health = R.drawable.health
    private const val shopping = R.drawable.shopping
    private const val grocery = R.drawable.shop
    private const val outGoingArrow = R.drawable.round_arrow_outward

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
        "outGoingArrow" to outGoingArrow,
    )
}
