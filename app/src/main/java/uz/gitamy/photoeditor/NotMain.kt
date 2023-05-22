//package uz.gita.musicplayer
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import uz.gita.musicplayer.databinding.ActivityMainBinding
//import uz.gita.musicplayer.utils.MyRotationGestureDetector
//
//
//class MainActivity : AppCompatActivity(), MyRotationGestureDetector.OnRotationGestureListener {
//
//    private lateinit var binding: ActivityMainBinding
//    private val mRotationDetector: MyRotationGestureDetector by lazy { MyRotationGestureDetector(this@MainActivity) }
//
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//       binding.imageView.setOnTouchListener { v, event ->
//           mRotationDetector.onTouchEvent(event)
//           true
//       }
//
//    }
//
//    override fun onRotation(rotationDetector: MyRotationGestureDetector?) {
//        binding.imageView.apply {
//            if (rotationDetector != null) {
//                rotation += rotationDetector.angle
//                val cS = scaleX
//                scaleX = rotationDetector.scale * cS
//                scaleY = rotationDetector.scale * cS
//            }
//        }
//    }
//}