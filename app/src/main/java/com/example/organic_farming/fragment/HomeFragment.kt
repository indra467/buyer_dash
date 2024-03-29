package com.example.organic_farming.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.organic_farming.R
import com.example.organic_farming.adapter.CategoryAdapter
import com.example.organic_farming.adapter.ProductAdapter
import com.example.organic_farming.databinding.FragmentHomeBinding
import com.example.organic_farming.model.AddProductModel
import com.example.organic_farming.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        getCategory()
        getSliderImage()
        getProducts()
        return binding.root
    }

    private fun getSliderImage() {
        Firebase.firestore.collection("slider").document("item")
            .get().addOnSuccessListener {
                Glide.with(requireContext()).load(it.get("img")).into(binding.sliderImage)
            }
    }

    private fun getProducts(){
    val list = ArrayList<AddProductModel>()
    Firebase.firestore.collection("products")
        .get().addOnSuccessListener {
            list.clear()
            for(doc in it.documents){
                val data = doc.toObject(AddProductModel::class.java)
                list.add(data!!)
            }
         binding.productRecycler.adapter = ProductAdapter(requireContext(), list)
        }
}
private fun getCategory(){
    val list = ArrayList<CategoryModel>()
    Firebase.firestore.collection("categories")
        .get().addOnSuccessListener {
            list.clear()
            for(doc in it.documents){
                val data = doc.toObject(CategoryModel::class.java)
                list.add(data!!)
            }
            binding.categoryRecycler.adapter = CategoryAdapter(requireContext(), list)
        }
}
}