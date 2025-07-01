package com.example.infinite_track.domain.model.profile

data class faq(
    val question: String,
    val answer: String
)
data class FAQCategory(val title: String, val faqs: List<faq>)

val faqsList = listOf(
    FAQCategory(
    title = "Attendance Related Questions",
            faqs = listOf(
                faq(question = "How do I  mark my attendance? ",
                    answer = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",),
                faq(question = "What if I am late for work?",
                    answer = "If you arrive late, the app will still allow you to check In. However, your attendance record will be marked as late. ",)
            ),
    ),
    FAQCategory(
    title = "System related questions",
            faqs = listOf(
                faq(question = "Lorep Ipsum?",
                    answer = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"),
                faq("Loremmm? ipsum!" , "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua")
            ),
        ),
    FAQCategory(
        title = "Support related questions",
        faqs = listOf(
            faq(question = "Lorep Ipsum?",
                answer = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"),
            faq("Loremmm? ipsum!" , "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua")
        ),
    )
)

