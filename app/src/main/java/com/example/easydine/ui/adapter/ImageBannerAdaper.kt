package com.example.easydine.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.easydine.databinding.ItemBannerBinding
import com.example.easydine.data.model.ImageBanner
import com.bumptech.glide.Glide

class ImageBannerAdapter(
    private var banners: List<ImageBanner>
) : RecyclerView.Adapter<ImageBannerAdapter.ImageBannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageBannerViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageBannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageBannerViewHolder, position: Int) {
        val banner = banners[position]
        holder.bind(banner)
    }

    override fun getItemCount(): Int {
        return banners.size
    }

    // Hàm cập nhật dữ liệu banner
    fun setData(newBanners: List<ImageBanner>) {
        banners = newBanners
        notifyDataSetChanged()
    }

    class ImageBannerViewHolder(private val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(banner: ImageBanner) {
            // Load hình ảnh bằng Glide
            Glide.with(binding.root.context)
                .load(banner.image)
                .into(binding.imageView) // Giả sử bạn có ImageView trong layout `item_banner.xml`

            // Nếu có TextView cho title, bạn có thể gán dữ liệu ở đây
//            binding.titleTextView?.text = banner.title // Nếu TextView là tùy chọn
        }
    }
}
