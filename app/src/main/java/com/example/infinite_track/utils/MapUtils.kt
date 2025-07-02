package com.example.infinite_track.utils

import android.util.Log
import com.example.infinite_track.domain.model.attendance.Location
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
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
     * Function to add markers and radius circle to the map
     */
    fun addMarkersAndRadius(
        mapView: MapView,
        targetLocation: Location,
        onMarkerClick: (Location) -> Unit
    ) {
        try {
            val annotationApi = mapView.annotations
            val pointAnnotationManager = annotationApi.createPointAnnotationManager()
            val circleAnnotationManager = annotationApi.createCircleAnnotationManager()

            // Clear existing annotations
            pointAnnotationManager.deleteAll()
            circleAnnotationManager.deleteAll()

            // Create point for target location
            val targetPoint = Point.fromLngLat(targetLocation.longitude, targetLocation.latitude)

            // Add circle annotation for geofence radius (langsung dari API tanpa konversi)
            // Gunakan radius langsung dari response API
            val radiusFromAPI = targetLocation.radius.toDouble()

            Log.d(
                "MapUtils",
                "Adding circle with radius directly from API: ${targetLocation.radius} (no conversion)"
            )

            val circleAnnotationOptions = CircleAnnotationOptions()
                .withPoint(targetPoint)
                .withCircleRadius(radiusFromAPI)
                .withCircleColor("#4285F4")
                .withCircleOpacity(0.3)
                .withCircleStrokeColor("#1976D2")
                .withCircleStrokeWidth(2.0)
                .withCircleStrokeOpacity(0.8)

            val circleAnnotation = circleAnnotationManager.create(circleAnnotationOptions)

            // Add point annotation for office marker dengan text yang jelas
            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(targetPoint)
                .withTextField("🏢 ${targetLocation.description}")
                .withTextSize(14.0)
                .withTextColor("#FFFFFF")
                .withTextHaloColor("#000000")
                .withTextHaloWidth(2.0)
                .withIconSize(1.5)
                .withIconColor("#FF0000")

            val pointAnnotation = pointAnnotationManager.create(pointAnnotationOptions)

            // Set click listener for marker
            pointAnnotationManager.addClickListener { annotation ->
                if (annotation.id == pointAnnotation.id) {
                    onMarkerClick(targetLocation)
                    true
                } else {
                    false
                }
            }

            Log.d(
                "MapUtils",
                "Marker and circle added successfully at ${targetLocation.latitude}, ${targetLocation.longitude}"
            )
            Log.d("MapUtils", "Target location data: $targetLocation")

        } catch (e: Exception) {
            // Log error or handle gracefully
            Log.e("MapUtils", "Error adding markers and radius", e)
        }
    }
}
