package cobo.chat.presentation

import cobo.chat.config.response.CoBoResponse
import cobo.chat.config.response.CoBoResponseDto
import cobo.chat.config.response.CoBoResponseStatus
import cobo.chat.data.dto.student.StudentGetRes
import cobo.chat.data.dto.student.StudentPostReq
import cobo.chat.data.enum.ChatStateEnum
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime


@RestController
@RequestMapping("/api/student")
class StudentPresentation(

){

    @PostMapping
    @Operation(summary = "학생이 교수에게 질문 작성")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "작성 성공")
    )
    fun post(
        @RequestBody studentPostReq: StudentPostReq,
        @Parameter(hidden = true) authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>{
        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    @GetMapping
    @Operation(summary = "학생이 교수에게 한 질문 조회 API")
    @ApiResponses(
        ApiResponse(responseCode = "403", description = "인증 실패", )
    )
    fun getStudent(
        @Parameter(hidden = true) authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<List<StudentGetRes>>> {
        return CoBoResponse(
            listOf(StudentGetRes(
                comment = "Hello1", localDateTime = LocalDateTime.now(), chatStateEnum = ChatStateEnum.COMPLETE
            ), StudentGetRes(
                comment = "Hello2", localDateTime = LocalDateTime.now(), chatStateEnum = ChatStateEnum.CONFIRMATION
            ))
            ,CoBoResponseStatus.SUCCESS
        ).getResponseEntity()
    }
}