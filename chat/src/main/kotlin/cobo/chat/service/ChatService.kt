package cobo.chat.service

import cobo.chat.config.response.CoBoResponseDto
import cobo.chat.config.response.CoBoResponseStatus
import cobo.chat.data.dto.prof.ProfPostReq
import cobo.chat.data.dto.student.StudentGetElementRes
import cobo.chat.data.dto.student.StudentPostReq
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication

interface ChatService {
    fun studentPost(
        studentPostReq: StudentPostReq,
        authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>

    fun studentGet(authentication: Authentication): ResponseEntity<CoBoResponseDto<List<StudentGetElementRes>>>
    fun profPost(profPostReq: ProfPostReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>
}