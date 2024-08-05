package cobo.writing.service

import cobo.writing.config.response.CoBoResponseDto
import cobo.writing.config.response.CoBoResponseStatus
import cobo.writing.data.dto.assignment.AssignmentGetListRes
import cobo.writing.data.dto.assignment.AssignmentPostReq
import org.springframework.http.ResponseEntity

interface AssignmentService {
    fun post(assignmentPostReq: AssignmentPostReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>
    fun getList(): ResponseEntity<CoBoResponseDto<AssignmentGetListRes>>
}