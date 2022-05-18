package com.github.calo001.hazel.providers

import android.app.Activity
import android.content.Context

class RewardAdHelper(private val activity: Activity, private val context: Context) {
//    private var mInterstitialAd: InterstitialAd? = null
//    private var adRequest: AdRequest = AdRequest.Builder().build()

    fun loadRewardAd(onDismissed: () -> Unit) {
//        InterstitialAd.load(context, "ca-app-pub-3940256099942544/1033173712", adRequest,
//            object : InterstitialAdLoadCallback() {
//                override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                    setupInterstitialAd(interstitialAd, onDismissed)
//                    if (mInterstitialAd != null) {
//                        mInterstitialAd?.show(activity)
//                    } else {
//                        Logger.d("The interstitial ad wasn't ready yet.")
//                    }
//                    Logger.i("onAdLoaded")
//                }
//
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    // Handle the error
//                    Logger.i(loadAdError.message)
//                    mInterstitialAd = null
//                }
//            }
//        )
    }

//   private fun setupInterstitialAd(interstitialAd: InterstitialAd, onDismissed: () -> Unit) {
//        mInterstitialAd = interstitialAd
//        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
//            override fun onAdDismissedFullScreenContent() {
//                Logger.i("Ad was dismissed.")
//                onDismissed()
//            }
//
//            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
//                Logger.i("Ad failed to show.")
//            }
//
//            override fun onAdShowedFullScreenContent() {
//                Logger.i("Ad showed fullscreen content.")
//                mInterstitialAd = null
//            }
//        }
//    }
}