package com.github.calo001.hazel.providers

import android.app.Activity
import android.content.Context

class RewardAdHelper(
    private val activity: Activity,
    private val context: Context,
) {
    private val AD_ID = "testx9dtjwj8hp"
    //private var rewardAd: RewardAd? = null

    fun loadRewardAd(onSuccess: () -> Unit) {
//        if (rewardAd == null) {
//            rewardAd = RewardAd(context, AD_ID)
//        }
//        val listener: RewardAdLoadListener = object : RewardAdLoadListener() {
//            override fun onRewardedLoaded() {
//                // Rewarded ad loaded successfully.
//                rewardAdShow(onSuccess)
//            }
//
//            override fun onRewardAdFailedToLoad(errorCode: Int) {
//                // Failed to load the rewarded ad.
//            }
//
//        }
//        rewardAd?.loadAd(AdParam.Builder().build(),listener)
    }

    /**
     * Display a rewarded ad.
     */
    private fun rewardAdShow(onSuccess: () -> Unit) {
//        if (rewardAd!!.isLoaded) {
//            rewardAd!!.show(activity, object : RewardAdStatusListener() {
//                override fun onRewardAdOpened() {
//                    // Rewarded ad opened.
//
//                }
//                override fun onRewardAdFailedToShow(errorCode: Int) {
//                    // Failed to display the rewarded ad.
//                }
//                override fun onRewardAdClosed() {
//                    // Rewarded ad closed.
//                }
//                override fun onRewarded(reward: Reward) {
//                    // Provide a reward when reward conditions are met.
//                    onSuccess()
//                }
//            })
//        }
    }
}