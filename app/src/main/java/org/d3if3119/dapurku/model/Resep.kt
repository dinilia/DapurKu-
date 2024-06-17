package org.d3if3119.dapurku.model

data class Resep(
    val recipe_id: Int,
    val user_email: String,
    val nama: String,
    val deskripsi: String,
    val kategori: String,
    val image_id: String,
    val delete_hash: String,
    val created_at: String
)

data class ResepCreate(
    val user_email: String,
    val nama: String,
    val deskripsi: String,
    val kategori: String = "",
    val image_id: String,
    val delete_hash: String
)
