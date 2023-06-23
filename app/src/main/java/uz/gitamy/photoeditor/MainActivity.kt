package uz.gitamy.photoeditor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF
import android.media.MediaScannerConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import uz.gitamy.photoeditor.databinding.ActivityMainBinding
import uz.gitamy.photoeditor.databinding.ContainerViewBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.math.atan

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var addViewData: ViewData? = null
//    private var addTextData: ViewData? = null
    private var lastSelectView: ContainerViewBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.addGlasses.setOnClickListener {
            addViewData = ViewData.EmojiData(R.drawable.glasses, 60.px, 30.px)
        }
        binding.addText.setOnClickListener {
            binding.edittx.visibility = View.VISIBLE
            addViewData = ViewData.TextData(binding.edittx.text.toString())
        }
//        binding.saveBtn.setOnClickListener {
//            saveImageToGallery()
//        }



        binding.editor.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                when (addViewData) {
                    is ViewData.EmojiData -> {
                        addView(event.x, event.y)
                    }

                    is ViewData.TextData -> {
                        addView(event.x, event.y)
                    }

                    null -> {
                        binding.edittx.visibility = View.GONE
                        unSelect()
                    }
                }
                addViewData = null
            }
            return@setOnTouchListener true
        }


    }




    private fun selectView(view: ContainerViewBinding) {
        if (lastSelectView != view) unSelect()
        lastSelectView = view
        lastSelectView!!.apply {
            this.viewContainer.isSelected = true
            this.buttonCancel.visibility = View.VISIBLE
        }
    }

    private fun unSelect() {
        lastSelectView?.let {
            it.viewContainer.isSelected = false
            it.buttonCancel.visibility = View.GONE
        }
    }

    private fun addView(targetX: Float, targetY: Float) {
        val _view: View = when (addViewData!!) {
            is ViewData.EmojiData -> {
                ImageView(this).apply {
                    setImageResource((addViewData as ViewData.EmojiData).imageResID)
                }
            }

            is ViewData.TextData -> {
                TextView(this).apply {
                    setText(binding.edittx.text.toString())
                }
            }
        }

        val containerBinding = ContainerViewBinding.inflate(layoutInflater, binding.editor, false)

        containerBinding.root.x = targetX - 50.px
        containerBinding.root.y = targetY - 30.px

        containerBinding.viewContainer.addView(_view)
        binding.editor.addView(containerBinding.root, 100.px, 60.px)
        selectView(containerBinding)

        containerBinding.buttonCancel.setOnClickListener {
            binding.editor.removeView(containerBinding.root)
        }

        var lastPoint = PointF()
        var firstPoint = PointF()
        var secondPoint : PointF?= null
        var oldLength = 0.0

        containerBinding.root.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    selectView(containerBinding)
                    lastPoint = PointF(event.x, event.y)
                    firstPoint = PointF(event.x, event.y)
                }

                MotionEvent.ACTION_MOVE -> {

                    var alpha: Float
                    var betta: Float = (0.0).toFloat()
                    var gamma: Float

                    if (event.pointerCount == 1) {
                        val distanceX = event.x - lastPoint.x
                        val distanceY = event.y - lastPoint.y

                        containerBinding.root.x += distanceX
                        containerBinding.root.y += distanceY
                    } else {
                        val index0 = event.findPointerIndex(0)
                        val index1 = event.findPointerIndex(1)

                        val x0 = event.getX(index0)
                        val y0 = event.getY(index0)

                        val x1 = event.getX(index1)
                        val y1 = event.getY(index1)

                        if (secondPoint == null) {
                            secondPoint = PointF(x1, y1)
                            oldLength = lineLength(firstPoint, secondPoint!!)


                            betta =
                                atan((secondPoint!!.y - firstPoint.y) / (firstPoint.x - secondPoint!!.x))
                        }

                        val newLength = lineLength(PointF(x0, y0), PointF(x1, y1))
                        val k = newLength / oldLength
                        gamma = atan((y1 - y0) / (x0 - x1))

                        alpha = betta - gamma


                        if (containerBinding.root.scaleX * k > 0.1) {
                            containerBinding.root.scaleX *= k.toFloat()
                            containerBinding.root.scaleY *= k.toFloat()
                        }

                        //rotation
                        containerBinding.root.rotation += alpha
                    }
                }
            }
            return@setOnTouchListener true

//            binding.editor.draw()
        }
    }

//    fun saveImageToGallery() {
//        val context: Context = applicationContext
//        val frameLayout = binding.editor
//        val bitmap =
//            Bitmap.createBitmap(frameLayout.width, frameLayout.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        frameLayout.draw(canvas)
//        val filename = "${UUID.randomUUID()}.jpg"
//        val saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//
//        if (!saveDir.exists()) {
//            saveDir.mkdirs()
//        }
//
//        val file = File(saveDir, filename)
//
//        try {
//            val outputStream: OutputStream = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//            outputStream.flush()
//            outputStream.close()
//
//            // Tell the media scanner to scan the new image, so it will be visible in the gallery
//            MediaScannerConnection.scanFile(
//                context,
//                arrayOf(file.absolutePath),
//                null,
//                null
//            )
//
//            Toast.makeText(context, "saved!!!", Toast.LENGTH_SHORT).show()
//        } catch (e: Exception) {
//            Toast.makeText(context, "not saved!!!", Toast.LENGTH_SHORT).show()
//            e.printStackTrace()
//        }
//    }

}