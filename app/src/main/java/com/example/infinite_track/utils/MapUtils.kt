package com.example.infinite_track.utils

import android.util.Log
import com.example.infinite_track.R
import com.example.infinite_track.domain.model.attendance.Location
import com.example.infinite_track.domain.model.wfa.WfaRecommendation
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager

/**
 * Utility object for map-related operations with custom icons
 */
object MapUtils {

    // Store references for click handling
    private val locationAnnotations = mutableMapOf<String, Location>()
    private val wfaAnnotations = mutableMapOf<String, WfaRecommendation>()

    fun updateMapAnnotations(
        mapView: MapView,
        wfoLocation: Location? = null,
        wfhLocation: Location? = null,
        wfaRecommendations: List<WfaRecommendation> = emptyList(),
        selectedWfaLocation: WfaRecommendation? = null,
        currentUserLocation: Point? = null,
        onMarkerClick: (Location) -> Unit = {},
        onWfaMarkerClick: (WfaRecommendation) -> Unit = {}
    ) {
        try {
            val context = mapView.context
            val annotationApi = mapView.annotations
            val pointAnnotationManager = annotationApi.createPointAnnotationManager()
            val circleAnnotationManager = annotationApi.createCircleAnnotationManager()

            // 1. Load all icons into map style first
            mapView.mapboxMap.getStyle { style ->
                // Remove old images to prevent duplication
                style.removeStyleImage("office-marker")
                style.removeStyleImage("home-marker")
                style.removeStyleImage("user-marker")
                style.removeStyleImage("wfa-marker")
                style.removeStyleImage("wfa-selected-marker")

                // Add new icons as Bitmaps using existing drawable resources
                style.addImage(
                    "office-marker",
                    context.bitmapFromVector(R.drawable.ic_marker_office)
                )
                style.addImage("home-marker", context.bitmapFromVector(R.drawable.ic_marker_home))
                style.addImage(
                    "user-marker",
                    context.bitmapFromVector(R.drawable.ic_maps)
                )
                style.addImage("wfa-marker", context.bitmapFromVector(R.drawable.ic_wfh_icon))
                style.addImage(
                    "wfa-selected-marker",
                    context.bitmapFromVector(R.drawable.ic_location)
                )

                // 2. Clear old annotations
                pointAnnotationManager.deleteAll()
                circleAnnotationManager.deleteAll()
                locationAnnotations.clear()
                wfaAnnotations.clear()

                // 3. Draw WFO annotations
                wfoLocation?.let { location ->
                    val point = Point.fromLngLat(location.longitude, location.latitude)
                    circleAnnotationManager.create(
                        createCircleOptions(
                            point,
                            location.radius,
                            "#1976D2"
                        )
                    )
                    val annotation =
                        pointAnnotationManager.create(createPointOptions(point, "office-marker"))
                    locationAnnotations[annotation.id] = location
                    Log.d("MapUtils", "WFO marker added: ${location.description}")
                }

                // 4. Draw WFH annotations
                wfhLocation?.let { location ->
                    val point = Point.fromLngLat(location.longitude, location.latitude)
                    circleAnnotationManager.create(
                        createCircleOptions(
                            point,
                            location.radius,
                            "#4CAF50"
                        )
                    )
                    val annotation =
                        pointAnnotationManager.create(createPointOptions(point, "home-marker"))
                    locationAnnotations[annotation.id] = location
                    Log.d("MapUtils", "WFH marker added: ${location.description}")
                }

                // 5. Draw WFA annotations
                wfaRecommendations.forEach { recommendation ->
                    val point = Point.fromLngLat(recommendation.longitude, recommendation.latitude)
                    val isSelected = recommendation.name == selectedWfaLocation?.name
                    val iconId = if (isSelected) "wfa-selected-marker" else "wfa-marker"
                    val annotation =
                        pointAnnotationManager.create(createPointOptions(point, iconId))
                    wfaAnnotations[annotation.id] = recommendation
                    Log.d(
                        "MapUtils",
                        "WFA marker added: ${recommendation.name} (selected: $isSelected)"
                    )
                }

                // 6. Draw user location
                currentUserLocation?.let { userPoint ->
                    pointAnnotationManager.create(createPointOptions(userPoint, "user-marker"))
                    Log.d("MapUtils", "User location marker added")
                }

                // 7. Set up click listeners (fix API usage)
                pointAnnotationManager.addClickListener { annotation ->
                    locationAnnotations[annotation.id]?.let {
                        onMarkerClick(it)
                        Log.d("MapUtils", "Location marker clicked: ${it.description}")
                        return@addClickListener true
                    }
                    wfaAnnotations[annotation.id]?.let {
                        onWfaMarkerClick(it)
                        Log.d("MapUtils", "WFA marker clicked: ${it.name}")
                        return@addClickListener true
                    }
                    false
                }

                Log.d(
                    "MapUtils",
                    "Map annotations updated successfully - WFO: ${wfoLocation != null}, WFH: ${wfhLocation != null}, WFA: ${wfaRecommendations.size}"
                )
            }
        } catch (e: Exception) {
            Log.e("MapUtils", "Error updating map annotations", e)
        }
    }

    // Helper function to create circle options
    private fun createCircleOptions(point: Point, radius: Int, color: String) =
        CircleAnnotationOptions()
            .withPoint(point)
            .withCircleRadius(radius.toDouble())
            .withCircleColor(color)
            .withCircleOpacity(0.2)
            .withCircleStrokeColor(color)
            .withCircleStrokeWidth(1.0)

    // Helper function to create point options
    private fun createPointOptions(point: Point, iconId: String) = PointAnnotationOptions()
        .withPoint(point)
        .withIconImage(iconId)
        .withIconSize(1.0)

}
