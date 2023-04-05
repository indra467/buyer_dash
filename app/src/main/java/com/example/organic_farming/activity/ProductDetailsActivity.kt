package com.example.organic_farming.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.organic_farming.MainActivity
import com.example.organic_farming.R
import com.example.organic_farming.databinding.ActivityProductDetailsBinding
import com.example.organic_farming.roomdb.AppDatabase
import com.example.organic_farming.roomdb.ProductDao
import com.example.organic_farming.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)

        getProductDetails(intent.getStringExtra("id"))
        setContentView(binding.root)
    }

    private fun getProductDetails(proId: String?) {
Firebase.firestore.collection("products")
    .document(proId!!).get().addOnSuccessListener {
        val list = it.get("productImage") as ArrayList<String>
        val name = it.getString("productName")
            val productMrp = it.getString("productMrp")

        binding.textView7.text = name
        binding.textView8.text = productMrp
        binding.textView9.text = it.getString("productDescription")

        val slideList = ArrayList<SlideModel>()
        for(data in list){
            slideList.add(SlideModel(data, ScaleTypes.CENTER_CROP))
        }

        cartAction(proId, name, productMrp, it.getString("productCoverImg"))

        binding.imageSlider.setImageList(slideList)
    }.addOnFailureListener {
Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
    }
    }

    private fun cartAction(proId: String, name: String?, productMrp: String?, coverImg: String?) {
val productDao = AppDatabase.getInstance(this).productDao()

        if(productDao.isExit(proId)!= null){
            binding.textView10.text = "Go to Cart"
        }else{
            binding.textView10.text = "Add to Cart"
        }
        binding.textView10.setOnClickListener {
            if(productDao.isExit(proId)!= null){
                openCart()
            }else{
              addToCart(productDao, proId, name, productMrp, coverImg)
            }
        }
    }

    private fun addToCart(productDao: ProductDao, proId: String, name: String?, productMrp: String?, coverImg: String?) {
val data = ProductModel(proId, name, coverImg, productMrp)
        lifecycleScope.launch(Dispatchers.IO){
            productDao.insertProduct(data)
            binding.textView10.text = "Go to Cart"
        }
    }

    private fun openCart() {
        val preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", true)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }


}