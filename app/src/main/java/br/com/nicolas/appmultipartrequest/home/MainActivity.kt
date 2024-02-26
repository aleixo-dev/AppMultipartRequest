package br.com.nicolas.appmultipartrequest.home

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import br.com.nicolas.appmultipartrequest.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding
    private var imageUri: Uri? = null

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageUri = uri
                setupVisibilityImageView()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        render()
        setupListeners()
    }

    private fun render() {
        viewModel.state.observe(this) { state ->
            when (state) {
                HomeViewState.Loading -> setupProgressBarLoading(true)

                HomeViewState.Error -> {
                    setupProgressBarLoading(false)
                    setupSnackBar("Something went wrong, try again")
                }

                is HomeViewState.Success -> {
                    setupProgressBarLoading(false)
                    setupSnackBar("Success to upload image!")
                }
            }
        }
    }

    private fun setupProgressBarLoading(hasLoading: Boolean = false) {
        binding.includeLayoutProgressBar.apply {
            root.isVisible = hasLoading
        }
        setupClickableButton(!hasLoading)
    }

    private fun setupClickableButton(hasUploadedImage: Boolean) = binding.apply {
        buttonUpload.isClickable = hasUploadedImage
        iconRemoveImage.isClickable = hasUploadedImage
    }

    private fun setupListeners() {
        with(binding) {
            iconPhoto.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            buttonUpload.setOnClickListener {
                if (ifInputHasText()) {
                    setupSnackBar("You need to fill in all the fields")
                    return@setOnClickListener
                }
                imageUri?.let {
                    setEvent(event = HomeViewEvent.UploadImage(File(getRealPathFromURI(it))))
                } ?: setupSnackBar("You need to load a image")
            }
        }
    }

    private fun setupVisibilityImageView() = binding.apply {
        iconPhoto.isVisible = false
        imageView.run {
            isVisible = true
            setImageURI(imageUri)
        }
        removeImageFile()
    }

    private fun removeImageFile() = binding.apply {
        iconRemoveImage.run {
            isVisible = true
            setOnClickListener {
                isVisible = false
                imageUri = null
                imageView.isVisible = false
                iconPhoto.isVisible = true
            }
        }
    }

    private fun setupSnackBar(message: String) {
        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).apply {
            setAction("Ok") { dismiss() }
        }
        snackBar.show()
    }

    private fun ifInputHasText(): Boolean {
        binding.apply {
            return inputTitle.text.isNullOrBlank() || inputDescription.text.isNullOrBlank()
        }
    }

    private fun setEvent(event: HomeViewEvent) {
        viewModel.interact(event)
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val contentResolver: ContentResolver = applicationContext.contentResolver
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val columnIndex = it.getColumnIndex(filePathColumn[0])
            return it.getString(columnIndex)
        }
        return ""
    }
}