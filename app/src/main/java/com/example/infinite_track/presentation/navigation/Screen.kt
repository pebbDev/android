package com.example.infinite_track.presentation.navigation

sealed class  Screen(val route: String) {
    data object Home : Screen("home")
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Contact : Screen("contact")
    data object Profile : Screen("profile")
    data object Attendance : Screen("attendance")
//    data object Leave : Screen("leave")
    data object FAQ : Screen("faq")
    data object ContactUs : Screen("contactus")
    data object EditProfile : Screen("editProfile")

    data object History : Screen("history")
    data object TimeOffReq : Screen("timeOffReq")

    // Parent navigation graphs
    data object HistoryFlow : Screen("history_flow")
    data object ProfileFlow : Screen("profile_flow")

    //Time Off
    data object MyLeave : Screen("myLeave")
    data object TimeOffRequest : Screen("timeOffRequest")

    //See More
    data object DetailListTimeOff : Screen("home/detailsTimeOff")
    data object DetailMyAttendance : Screen("home/listMyAttendance")
    data object PaySlip : Screen("profile/PaySlip")
    data object MyDocument : Screen("profile/MyDocument")

}