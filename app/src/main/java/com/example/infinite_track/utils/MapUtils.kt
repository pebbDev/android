package com.example.infinite_track.utils

import android.content.Context
import android.util.Log
import com.example.infinite_track.R
import com.example.infinite_track.domain.model.attendance.Location
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager

/**
 * Utility object for map-related operations
 */
object MapUtils {

    /**
     * Unified function to update all map annotations with custom icons
     * This replaces all previous marker functions with a smarter approach
     */
    fun updateMapAnnotations(
        mapView: MapView,
        wfoLocation: Location? = null,
        wfhLocation: Location? = null,
        currentUserLocation: Point? = null,
        onMarkerClick: (Location) -> Unit = {}
    ) {
        try {
            val context = mapView.context
            val annotationApi = mapView.annotations
            val pointAnnotationManager = annotationApi.createPointAnnotationManager()
            val circleAnnotationManager = annotationApi.createCircleAnnotationManager()

            // Clear all existing annotations before redrawing
            pointAnnotationManager.deleteAll()
            circleAnnotationManager.deleteAll()

            // Add WFO location if available
            wfoLocation?.let { location ->
                val wfoPoint = Point.fromLngLat(location.longitude, location.latitude)

                // Add circle annotation for WFO geofence radius
                val circleOptions = CircleAnnotationOptions()
                    .withPoint(wfoPoint)
                    .withCircleRadius(location.radius.toDouble())
                    .withCircleColor("#1976D2") // Blue color for office
                    .withCircleOpacity(0.2)
                    .withCircleStrokeColor("#0D47A1")
                    .withCircleStrokeWidth(2.0)
                    .withCircleStrokeOpacity(0.6)

                circleAnnotationManager.create(circleOptions)

                // Add point annotation for WFO marker with custom icon
                val wfoIcon = context.bitmapFromVector(R.drawable.ic_marker_office)
                val pointOptions = PointAnnotationOptions()
                    .withPoint(wfoPoint)
                    .withTextField("🏢 ${location.description}")
                    .withTextSize(12.0)
                    .withTextColor("#FFFFFF")
                    .withTextHaloColor("#1976D2")
                    .withTextHaloWidth(2.0)
                    .withIconSize(1.2)
                    .withIconImage(wfoIcon)

                val wfoAnnotation = pointAnnotationManager.create(pointOptions)

                // Set click listener for WFO marker
                pointAnnotationManager.addClickListener { annotation ->
                    if (annotation.id == wfoAnnotation.id) {
                        onMarkerClick(location)
                        true
                    } else {
                        false
                    }
                }

                Log.d("MapUtils", "WFO marker added at ${location.latitude}, ${location.longitude}")
            }

            // Add WFH location if available
            wfhLocation?.let { location ->
                val wfhPoint = Point.fromLngLat(location.longitude, location.latitude)

                // Add circle annotation for WFH geofence radius
                val circleOptions = CircleAnnotationOptions()
                    .withPoint(wfhPoint)
                    .withCircleRadius(location.radius.toDouble())
                    .withCircleColor("#4CAF50") // Green color for home
                    .withCircleOpacity(0.2)
                    .withCircleStrokeColor("#2E7D32")
                    .withCircleStrokeWidth(2.0)
                    .withCircleStrokeOpacity(0.6)

                circleAnnotationManager.create(circleOptions)

                // Add point annotation for WFH marker with custom icon
                val wfhIcon = context.bitmapFromVector(R.drawable.ic_marker_home)
                val pointOptions = PointAnnotationOptions()
                    .withPoint(wfhPoint)
                    .withTextField("🏠 ${location.description}")
                    .withTextSize(12.0)
                    .withTextColor("#FFFFFF")
                    .withTextHaloColor("#4CAF50")
                    .withTextHaloWidth(2.0)
                    .withIconSize(1.2)
                    .withIconImage(wfhIcon)

                val wfhAnnotation = pointAnnotationManager.create(pointOptions)

                // Set click listener for WFH marker
                pointAnnotationManager.addClickListener { annotation ->
                    if (annotation.id == wfhAnnotation.id) {
                        onMarkerClick(location)
                        true
                    } else {
                        false
                    }
                }

                Log.d("MapUtils", "WFH marker added at ${location.latitude}, ${location.longitude}")
            }

            // Add current user location if available
            currentUserLocation?.let { userPoint ->
                val pointOptions = PointAnnotationOptions()
                    .withPoint(userPoint)
                    .withTextField("📍 Your Location")
                    .withTextSize(10.0)
                    .withTextColor("#FFFFFF")
                    .withTextHaloColor("#2196F3")
                    .withTextHaloWidth(2.0)
                    .withIconSize(0.8)
                    .withIconColor("#2196F3")

                pointAnnotationManager.create(pointOptions)

                Log.d("MapUtils", "User location marker added at ${userPoint.latitude()}, ${userPoint.longitude()}")
            }

            Log.d("MapUtils", "All map annotations updated successfully")

        } catch (e: Exception) {
            Log.e("MapUtils", "Error updating map annotations", e)
        }
    }

    /**
     * Function to fit map bounds to show all locations
     */
    fun fitMapToBounds(mapView: MapView, locations: List<Location>) {
        try {
            if (locations.isEmpty()) return

            val mapboxMap = mapView.mapboxMap

            // Calculate bounds from all locations
            val latitudes = locations.map { it.latitude }
            val longitudes = locations.map { it.longitude }

            val minLat = latitudes.minOrNull() ?: return
            val maxLat = latitudes.maxOrNull() ?: return
            val minLng = longitudes.minOrNull() ?: return
            val maxLng = longitudes.maxOrNull() ?: return

            // Add padding to bounds
            val latPadding = (maxLat - minLat) * 0.2
            val lngPadding = (maxLng - minLng) * 0.2

            val paddedMinLat = minLat - latPadding
            val paddedMaxLat = maxLat + latPadding
            val paddedMinLng = minLng - lngPadding
            val paddedMaxLng = maxLng + lngPadding

            // Calculate center point
            val centerLat = (paddedMinLat + paddedMaxLat) / 2
            val centerLng = (paddedMinLng + paddedMaxLng) / 2

            // Calculate appropriate zoom level
            val latDiff = paddedMaxLat - paddedMinLat
            val lngDiff = paddedMaxLng - paddedMinLng
            val maxDiff = maxOf(latDiff, lngDiff)

            val zoom = when {
                maxDiff > 10.0 -> 4.0
                maxDiff > 5.0 -> 6.0
                maxDiff > 2.0 -> 8.0
                maxDiff > 1.0 -> 10.0
                maxDiff > 0.5 -> 12.0
                maxDiff > 0.1 -> 14.0
                else -> 15.0
            }

            // Animate to bounds
            val centerPoint = Point.fromLngLat(centerLng, centerLat)
            mapboxMap.flyTo(
                CameraOptions.Builder()
                    .center(centerPoint)
                    .zoom(zoom)
                    .pitch(0.0)
                    .bearing(0.0)
                    .build(),
                MapAnimationOptions.Builder()
                    .duration(1500L)
                    .build()
            )

            Log.d("MapUtils", "Map bounds fitted for ${locations.size} locations with zoom level $zoom")

        } catch (e: Exception) {
            Log.e("MapUtils", "Error fitting map bounds", e)
        }
    }
}
