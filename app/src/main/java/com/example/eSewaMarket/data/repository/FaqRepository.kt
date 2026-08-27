package com.example.eSewaMarket.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eSewaMarket.data.models.Faqs

class FaqRepository {

    private val _faqs = MutableLiveData<List<Faqs>>()

    val faqs: LiveData<List<Faqs>> = _faqs

    fun fetchFaqs(){
        _faqs.value = listOf(
            Faqs(
                id = 1,
                question = "How do I place an order?",
                answer = "Browse products, add your desired items to the cart, proceed to checkout, enter your shipping details, choose a payment method, and confirm your order."
            ),
            Faqs(
                id = 2,
                question = "Can I cancel my order?",
                answer = "Yes. You can cancel your order before it has been shipped. Go to My Orders, select the order, and tap Cancel Order if the option is available."
            ),
            Faqs(
                id = 3,
                question = "How can I track my order?",
                answer = "Open My Orders, select the order you want to track, and you'll see its current delivery status along with tracking information."
            ),
            Faqs(
                id = 4,
                question = "What payment methods do you accept?",
                answer = "We accept debit cards, credit cards, digital wallets, cash on delivery (where available), and other supported online payment methods."
            ),
            Faqs(
                id = 5,
                question = "How do I return a product?",
                answer = "Go to My Orders, select the delivered item, tap Return, choose a reason, and submit your request within the eligible return period."
            ),
            Faqs(
                id = 6,
                question = "How long does delivery take?",
                answer = "Delivery usually takes 2–7 business days, depending on your location and product availability."
            ),
            Faqs(
                id = 7,
                question = "Can I save products for later?",
                answer = "Yes. Tap the Favorite or Wishlist icon on any product to save it for future purchase."
            ),
            Faqs(
                id = 8,
                question = "I receive a damaged or incorrect item?",
                answer = "Report the issue through My Orders within the return period. Include photos of the item if requested, and we'll assist with a replacement or refund."
            ),
            Faqs(
                id = 9,
                question = "How do I contact customer support?",
                answer = "You can reach our support team through the Help & Support section in the app or via email and phone during business hours."
            ),
            Faqs(
                id = 10,
                question = "Is my payment information secure?",
                answer = "Yes. We use secure encryption and trusted payment gateways to protect your payment and personal information."
            )
        )
    }
}