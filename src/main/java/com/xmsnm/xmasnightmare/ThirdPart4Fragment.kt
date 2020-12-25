package com.xmsnm.xmasnightmare

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import android.widget.Toast
import com.xmsnm.xmasnightmare.R
import com.xmsnm.xmasnightmare.ThirdPart4FragmentDirections
import com.xmsnm.xmasnightmare.ml.SavedModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteOrder
import java.nio.ByteBuffer as ByteBuffer1
import kotlin.math.truncate

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ThirdPart4Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third_part4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.capture_button_santa_4).setOnClickListener {
            dispatchTakePictureIntent()
        }

        view.findViewById<Button>(R.id.next_button_8).setOnClickListener {
            findNavController().navigate(R.id.action_ThirdPart4Fragment_to_ThirdPart5Fragment)
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
            val myToast = Toast.makeText(context, e.message, Toast.LENGTH_SHORT)
            myToast.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            var imgAsBitmap = data?.extras?.get("data") as Bitmap
            val w = imgAsBitmap.width
            val h = imgAsBitmap.height
            val squareSize = Math.min(w, h)
            imgAsBitmap = Bitmap.createBitmap(
                imgAsBitmap,
                (w - squareSize) / 2,
                (h - squareSize) / 2,
                squareSize,
                squareSize
            )
            val accuracy = tftest(imgAsBitmap)
            val accuracyInInteger = truncate(accuracy[4] * 100) //changed to the corresponding element
            val accuracytoInt = accuracyInInteger.toInt()

            // Changes required to the corresponding element
            if (accuracy[4] > 0.8) {
//              changes dialogue
                val nextDialogue = getString(R.string.santa_d8)
                view?.findViewById<TextView>(R.id.santa_d7)?.text = nextDialogue
//              changes hp visibility
                view?.findViewById<TextView>(R.id.santa_hp_3_a)?.visibility = INVISIBLE
//              changes image
                view?.findViewById<ImageView>(R.id.santa_image_1_a)
                    ?.setImageBitmap(imgAsBitmap)
//              change take photo button to invisible
                view?.findViewById<Button>(R.id.capture_button_santa_4)?.visibility = INVISIBLE
//              change defeated text to visible
                view?.findViewById<Button>(R.id.next_button_8)?.visibility = VISIBLE
//              change next round button to visible
//                view?.findViewById<TextView>(R.id.defeated_text)?.visibility = VISIBLE
//              change text for punch accuracy
                val accuracyText = getString(R.string.accuracy_kamehameha, accuracytoInt)
                view?.findViewById<TextView>(R.id.accuracy_kamehameha)?.text = accuracyText
                view?.findViewById<TextView>(R.id.accuracy_kamehameha)?.visibility = VISIBLE
            } else {
                val action =
                    ThirdPart4FragmentDirections.actionThirdPart4FragmentToGameoverFragment(
                        accuracytoInt
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun tftest(bmp: Bitmap): FloatArray {
        val imageProcessor: ImageProcessor =
            ImageProcessor.Builder()
                .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
                .build()

//        var tImage = TensorImage(DataType.FLOAT32)
        var tImage = TensorImage(DataType.UINT8)
        tImage.load(bmp)
        tImage = imageProcessor.process(tImage)
//        var bb = tImage.buffer
//        bb.position(0)
//        var imgData = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
//        imgData.order(ByteOrder.nativeOrder())
//        for (i in 0 until 224*224*3) {
//            imgData.putFloat(bb.getFloat() / 255.0f)
//        }
        val imgData2 = convert(tImage.bitmap)
        val model = SavedModel.newInstance(requireContext())

// Creates inputs for reference.
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
//        inputFeature0.loadBuffer(tImage.buffer)
        inputFeature0.loadBuffer(imgData2)

// Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        println(outputFeature0.floatArray)

// Releases model resources if no longer used.
        model.close()

// Return ouput
        return outputFeature0.floatArray
    }

    private fun convert(bp: Bitmap): ByteBuffer1 {
        val h = 224
        val w = 224
        var imgData = ByteBuffer1.allocateDirect(4 * w * h * 3)
        imgData.order(ByteOrder.nativeOrder())

        var intValues = IntArray(w * h)
        bp.getPixels(intValues, 0, w, 0, 0, w, h)

        var pixel = 0
        for (i in 0 until w) {
            for (j in 0 until h) {
                val intVal = intValues[pixel++]

                imgData.putFloat(((intVal shr 16) and 0xFF) / 255.0f)
                imgData.putFloat(((intVal shr 8) and 0xFF) / 255.0f)
                imgData.putFloat((intVal and 0xFF) / 255.0f)
            }
        }

        return imgData
    }
}