package cobo.chat.data.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime

@Entity
@DynamicInsert
data class ChatBotChat(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,

    @Column(length = 20)
    val studentId: String,

    @Column(length = 5000)
    val question: String,

    @Column(length = 5000)
    val answer: String,

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val createdAt: LocalDateTime?,
)
