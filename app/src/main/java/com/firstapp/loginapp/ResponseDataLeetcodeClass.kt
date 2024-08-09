package com.firstapp.loginapp

data class ResponseDataLeetcodeClass(
    val contributionPoint: Int,
    val easySolved: Int,
    val hardSolved: Int,
    val mediumSolved: Int,
    val ranking: Int,
    val reputation: Int,
    val submissionCalendar: SubmissionCalendar,
    val totalEasy: Int,
    val totalHard: Int,
    val totalMedium: Int,
    val totalQuestions: Int,
    val totalSolved: Int,
    val totalSubmissions: List<TotalSubmission>
)