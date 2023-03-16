package me.shakhriyor.mytaxihackaton.presentation.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.location.LocationServices
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.logo.logo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.shakhriyor.mytaxihackaton.common.LocationPermissionHelper
import me.shakhriyor.mytaxihackaton.R
import me.shakhriyor.mytaxihackaton.common.Constants.DEVICE_THEME
import me.shakhriyor.mytaxihackaton.common.Resource
import me.shakhriyor.mytaxihackaton.common.SharedPref
import me.shakhriyor.mytaxihackaton.common.service.DefaultLocationClient
import me.shakhriyor.mytaxihackaton.common.service.LocationClient
import me.shakhriyor.mytaxihackaton.data.model.CurrentLocation
import me.shakhriyor.mytaxihackaton.databinding.FragmentMainBinding
import me.shakhriyor.mytaxihackaton.presentation.viewmodels.MainViewModel

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    private var zoom = 12.0

    private val viewModel: MainViewModel by viewModels()

    private lateinit var locationClient:LocationClient

    private lateinit var locationPermissionHelper: LocationPermissionHelper

    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        binding.mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        binding.mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        binding.mapView.gestures.focalPoint = binding.mapView.getMapboxMap().pixelForCoordinate(it)
    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationClient = DefaultLocationClient(
            requireContext(),
            LocationServices.getFusedLocationProviderClient(requireContext())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getScreenSize()

        with(binding) {
            btnDrawerMenu.setOnClickListener {  }
            btnNotification.setOnClickListener {  }
            btnLightning.setOnClickListener {  }

        }


        locationClient.getLocation()
                .catch { e -> e.printStackTrace() }
                .onEach { location ->
                    updateCamera(CurrentLocation(latitude = location.latitude, longitude = location.longitude))
                }
                .launchIn(lifecycleScope)



        onMapReady()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentLocation.collect {result->
                    when(result) {
                        is Resource.LOADING -> {
                        }
                        is Resource.SUCCESS -> {
                            Log.d("@@@", "onViewCreated: succes1")
                            if (result.data.isNotEmpty()) {
                                Log.d("@@@", "onViewCreated: succes2")
                                updateCamera(result.data[0])
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Check Permission",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                        is Resource.ERROR -> {
                        }
                        else -> {}
                    }
                }
            }
        }

        binding.btnMyLocation.setOnClickListener {
            viewModel.getCurrentLocation()
        }

        binding.btnPlus.setOnClickListener {
            if (zoom < 18) {
                zoom++
                zoomControl(zoom)
            }

        }

        binding.btnMinus.setOnClickListener {
            if (zoom>0) {
                zoom--
                zoomControl(zoom)
            }
        }

    }



    private fun getScreenSize() {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels
    }


    private fun updateCamera(currentLocation: CurrentLocation) {
        zoom = 12.0
        val mapAnimationOptions = MapAnimationOptions.Builder().duration(2500L).build()
        binding.mapView.camera.easeTo(
            CameraOptions.Builder()
                .center(Point.fromLngLat(currentLocation.longitude!!, currentLocation.latitude!!))
                .zoom(12.0)
                .padding(EdgeInsets(0.0,
                    0.0, 0.0, 0.0))
                .build(),
            mapAnimationOptions
        )
        initLocationComponent()
    }

    private fun zoomControl(zoom: Double) {
        val mapAnimationOptions = MapAnimationOptions.Builder().duration(1500L).build()
        binding.mapView.camera.easeTo(
            CameraOptions.Builder()
                .zoom(zoom)
                .padding(EdgeInsets(0.0,
                    0.0, 0.0, 0.0))
                .build(),
            mapAnimationOptions

        )
    }

    private fun onMapReady() {
        binding.mapView.logo.updateSettings {
            enabled = false
        }
        binding.mapView.attribution.updateSettings {
            enabled = false
        }
        binding.mapView.compass.updateSettings {
            enabled = false
        }
        if (SharedPref(requireContext()).getBoolean(DEVICE_THEME)) {
            binding.mapView.getMapboxMap().loadStyleUri(
                Style.MAPBOX_STREETS
            ) {
                initLocationComponent()
                setupGesturesListener()
            }
        } else {
            binding.mapView.getMapboxMap().loadStyleUri(
                Style.DARK
            ) {
                initLocationComponent()
                setupGesturesListener()
            }
        }


    }

    private fun setupGesturesListener() {
        binding.mapView.gestures.addOnMoveListener(onMoveListener)
    }

    private fun initLocationComponent() {

        val locationComponentPlugin = binding.mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true

            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_car,
                ),
                shadowImage = AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_car
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(1.0)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            )
        }
//        locationComponentPlugin.addOnIndicatorPositionChangedListener(
//            onIndicatorPositionChangedListener
//        )
//        locationComponentPlugin.addOnIndicatorBearingChangedListener(
//            onIndicatorBearingChangedListener
//        )
    }

    private fun onCameraTrackingDismissed() {
        binding.mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        binding.mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        binding.mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        binding.mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        binding.mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }



//    override fun onDestroy() {
//        super.onDestroy()
//        binding.mapView.onDestroy()
//    }

}

