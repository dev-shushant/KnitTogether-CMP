package dev.shushant.permission.data

actual interface ImageGallery {
    actual suspend fun getImages(): List<Image>

    companion object {
        fun invoke(): ImageGallery = ImageGalleryImpl()
    }
}