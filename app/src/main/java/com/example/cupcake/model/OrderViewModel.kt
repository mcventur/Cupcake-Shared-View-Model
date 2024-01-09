package com.example.cupcake.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Representa el ViewModel con los datos que necesitan cada una de las pantallas
 */
class OrderViewModel : ViewModel() {
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _price = MutableLiveData<Double>()
    //Ejecutamos una transformación de LiveData con Transformations.map()
    val price: LiveData<String> = Transformations.map(_price){
        NumberFormat.getCurrencyInstance().format(it)
    }

    val dateOptions = getPickupOptions()

    init{
        resetOrder()
    }

    fun setQuantity(numberCupcackes: Int) {
        _quantity.value = numberCupcackes
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    fun setDate(pickupDate: String) {
        Log.d(TAG, "Actualizando a fecha: $pickupDate")
        _date.value = pickupDate
        updatePrice()
    }

    fun hasNoFlavorSet(): Boolean = _flavor.value.isNullOrEmpty()

    private fun getPickupOptions(): List<String>{
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()

        repeat(4){
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }

        return options
    }

    companion object{
        private const val TAG = "OrderViewModel"
        private const val PRICE_PER_CUPCAKE = 2.00
        private const val PRICE_FORM_SAME_DAY_PICKUP = 3.00
    }

    fun resetOrder(){
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    private fun updatePrice(){
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        if(dateOptions[0] == date.value){
            calculatedPrice += PRICE_FORM_SAME_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }

}