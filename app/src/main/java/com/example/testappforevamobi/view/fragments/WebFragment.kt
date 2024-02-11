package com.example.testappforevamobi.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.testappforevamobi.databinding.FragmentWebBinding
import com.example.testappforevamobi.model.constant.MAIN

class WebFragment : Fragment() {

    private var binding: FragmentWebBinding? = null

    private var webView: WebView? = null
    private var fileUploadCallback: ValueCallback<Array<Uri>>? = null
    private val FILE_CHOOSER_RESULT_CODE = 1
    private var customView: View? = null

    private var webViewList = mutableListOf<WebView>()

    private var Request: PermissionRequest? = null
    private var Res:Array<String>? = null

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Request?.grant(Res)
            }
        }

        // обработка перехода назад
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            clickBack()
        }

        if (savedInstanceState != null) {
            webView?.restoreState(savedInstanceState)
        } else {
            webView = createWebView() // создание нового WebView
            webView?.loadUrl("https://google.com")
        }

    }

    // функция создания WebView с правильными настройками
    private fun createWebView():WebView{
        var newWebView = WebView(requireContext())
        newWebView = setSettingsForWebView(newWebView)
        webViewList.add(newWebView)
        binding!!.idWebview.addView(newWebView)
        return newWebView
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setSettingsForWebView(webView: WebView):WebView {

        val webSettings: WebSettings = webView.settings

        webSettings.domStorageEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.databaseEnabled = true
        webSettings.databasePath = MAIN.applicationContext.getDir("webview_databases", 0).path
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true

        webSettings.javaScriptCanOpenWindowsAutomatically = true

        webSettings.mediaPlaybackRequiresUserGesture = false
        webSettings.loadsImagesAutomatically = true
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH) // улучшение производительности
        webSettings.allowContentAccess = true // Разрешает веб-содержимому доступ к локальным ресурсам
        webSettings.allowFileAccessFromFileURLs = true // Разрешает доступ к файлам из файловых URL
        webSettings.allowUniversalAccessFromFileURLs = true // Разрешает универсальный доступ к файлам из файловых URL.
        webSettings.mediaPlaybackRequiresUserGesture = false // мультимедийные элементы могут воспроизводиться автоматически
        webSettings.setEnableSmoothTransition(true) // устанавливает включение плавных переходов в WebView
        webSettings.pluginState = WebSettings.PluginState.ON // включает поддержку плагинов
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW // разрешает загрузку смешанного контента
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL // отображение страницы в соответствии с обычными правилами макета
        webSettings.loadWithOverviewMode = true // загрузка контента в соответствии с размерами экрана
        webSettings.useWideViewPort = true // правильное масштабирование
        webSettings.domStorageEnabled = true // разрешает использование DOM Storage для сохранения данных в локальном хранилище
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT // использовать кеш по умолчанию
        webSettings.databaseEnabled = true // разрешает использование базы данных для хранения данных
        webSettings.databasePath = requireContext().getDir("webview_databases", 0).path // устанавливает путь к базе данных для WebView
        webSettings.allowFileAccess = true // разрешает загрузку файлов из локального хранилища
        webSettings.mediaPlaybackRequiresUserGesture = false // разрешение воспроизведения видео/аудио по умолчанию
        webSettings.loadsImagesAutomatically = true // разрешает загрузку изображений по умолчанию

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                if (fileUploadCallback != null) {
                    fileUploadCallback!!.onReceiveValue(null)
                    fileUploadCallback = null
                }

                fileUploadCallback = filePathCallback

                val intent = fileChooserParams.createIntent()
                try {
                    startActivityForResult(intent, FILE_CHOOSER_RESULT_CODE)
                } catch (e: Exception) {
                    fileUploadCallback = null
                    return false
                }

                return true
            }

            @SuppressLint("ObsoleteSdkInt")
            override fun onPermissionRequest(request: PermissionRequest) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    val resources = request.resources

                    if (resources.contains(PermissionRequest.RESOURCE_VIDEO_CAPTURE) && resources.size == 1) {
                        if (resources[0] == PermissionRequest.RESOURCE_VIDEO_CAPTURE) {
                            if (ContextCompat.checkSelfPermission(
                                    requireContext(),
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                request.grant(resources)
                            } else {
                                Request = request
                                Res = resources
                                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        } else {
                            request.deny()
                            super.onPermissionRequest(request)
                        }
                    }
                }
            }


            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                super.onShowCustomView(view, callback)

                if (customView != null) {
                    callback.onCustomViewHidden()
                    return
                }

                customView = view
                customView?.let {
                    val decorView = MAIN.window.decorView as FrameLayout
                    decorView.addView(it, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                }

                webView.visibility = View.GONE
            }

            override fun onHideCustomView() {
                super.onHideCustomView()

                customView?.let {
                    val decorView = MAIN.window.decorView as FrameLayout
                    decorView.removeView(it)
                    customView = null
                }

                webView.visibility = View.VISIBLE
            }

        }
        return webView
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView?.saveState(outState)
    }

    // очистка биндинга при очистке view
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (fileUploadCallback != null) {
                val results = WebChromeClient.FileChooserParams.parseResult(resultCode, data)
                fileUploadCallback!!.onReceiveValue(results)
                fileUploadCallback = null
            }
        }

    }

    // функция перехода по нажатии на клавишу НАЗАД
    private fun clickBack(){
        if(webView?.canGoBack() == true){
            webView?.goBack()
        }else{
            MAIN.finishAffinity()
        }
    }

}