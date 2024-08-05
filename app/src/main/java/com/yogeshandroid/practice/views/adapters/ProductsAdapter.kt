package com.yogeshandroid.practice.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.squareup.picasso.Picasso
import com.yogeshandroid.practice.databinding.RvProductsBinding
import com.yogeshandroid.practice.databinding.RvUserBinding
import com.yogeshandroid.practice.model.Product
import com.yogeshandroid.practice.model.ProductResponse

class ProductsAdapter(var myData: List<Product>, val context: Context) :
    RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    class ProductViewHolder(val rvProductsBinding: RvProductsBinding) :
        ViewHolder(rvProductsBinding.root)

    fun filterList(searchList: MutableList<Product>) {
        myData = searchList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            RvProductsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val single = myData[position]

        Picasso.get().load(single.images[0]).into(holder.rvProductsBinding.img)
        holder.rvProductsBinding.titleTv.text = single.title
        holder.rvProductsBinding.brandTv.text = single.brand
        holder.rvProductsBinding.categoryTv.text = single.category
        holder.rvProductsBinding.idTv.text = single.id.toString()
    }

    override fun getItemCount() = myData.size
}