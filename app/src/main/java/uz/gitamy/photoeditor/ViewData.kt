package uz.gitamy.photoeditor

sealed interface ViewData {

    data class EmojiData(
        val imageResID : Int,
        val defHeight: Int,
        val defWidth: Int
    ):ViewData

    data class TextData(
        val st:String,

    ):ViewData

}